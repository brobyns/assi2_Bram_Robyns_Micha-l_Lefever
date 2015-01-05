package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Expense;
import model.Group;
import model.Member;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import db.GroupDB;
import db.MemberDB;

import android.util.Log;
import android.util.Xml;

public class XMLParser {
	private static final String ns = null;
	private MemberDB memberDB = MemberDB.getInstance();
	private GroupDB groupDB = GroupDB.getInstance();

	public Map<String, Map> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, ns, "database");
			Map map = new HashMap<String, List>();
			Map<Integer, Member> members = new HashMap<Integer, Member>();
			Map<Integer, Group> groups = new HashMap<Integer, Group>();
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				if (name.equals("members")) {
					members = readMembers(parser);
					Log.v("bram4", "size members: "+members.values().size()+"");
					memberDB.addMembers(members);
				}else if (name.equals("groups")) {
					groups = readGroups(parser);
					Log.v("bram4", "size groups: "+groups.values().size()+"");
					groupDB.setGroups(groups);
				}else{
					skip(parser);
				}
			}  
			map.put("members", members);
			map.put("groups", groups);
			return map;
		} finally {
			in.close();
		}
	}
	
	private Map<Integer, Group> readGroups(XmlPullParser parser) throws XmlPullParserException, IOException {
		Map<Integer, Group> groups = new HashMap<Integer, Group>();

		parser.require(XmlPullParser.START_TAG, ns, "groups");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("group")) {
				Group g = readGroup(parser);
				groups.put(g.getId(), g);
			} else {
				skip(parser);
			}
		}  
		return groups;
	}

	private Group readGroup(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "group");
		int id = -1;
		String groupname = null;
		List<Member> members = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("groupname")) {
				groupname = readStringValue(parser, "groupname");
			}else if (name.equals("members")) {
				members = new ArrayList<Member>(readGroupMembers(parser).values());
			} else {
				skip(parser);
			}	
		}
		return new Group(groupname, members);
	}
	
	private Map<Integer, Member> readMembers(XmlPullParser parser) throws XmlPullParserException, IOException {
		Map<Integer, Member> members = new HashMap<Integer,Member>();

		parser.require(XmlPullParser.START_TAG, ns, "members");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("member")) {
				Member m = readMember(parser);
				members.put(m.getId(), m);
			} else {
				skip(parser);
			}
		}  
		return members;
	}
	
	private Map<Integer, Member> readGroupMembers(XmlPullParser parser) throws XmlPullParserException, IOException {
		Map<Integer, Member> members = new HashMap<Integer,Member>();

		parser.require(XmlPullParser.START_TAG, ns, "members");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("member")) {
				Member m = readGroupMember(parser);
				members.put(m.getId(), m);
			} else {
				skip(parser);
			}
		}  
		return members;
	}
	
	private Member readMember(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "member");
		int id = -1;
		String firstname = null;
		String lastname = null;
		String email = null;	
		Map<Integer, Expense> expenses = new HashMap<Integer, Expense>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("firstname")) {
				firstname = readStringValue(parser, "firstname");
			} else if(name.equals("lastname")){
				lastname = readStringValue(parser, "lastname");
			}else if(name.equals("email")){
				email = readStringValue(parser, "email");
/*			}else if(name.equals("expenses")){
				expenses = readExpenses(parser, id);
*/			}else{
				skip(parser);
			}	
		}
		Log.v("bram", id+ " " + firstname +" "+ lastname + " " + email);
		return new Member(firstname, lastname, email);
	}
	
	private Member readGroupMember(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "member");
		int id = -1;
		String firstname = null;
		String lastname = null;
		String email = null;	
		Member m = null;
		Map<Integer, Expense> expenses = new HashMap<Integer, Expense>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(name.equals("email")){
				email = readStringValue(parser, "email");
				m = memberDB.getMembers().get(memberDB.getIdForEmail(email));
			}else if(name.equals("expenses")){
				expenses = readExpenses(parser, memberDB.getIdForEmail(email));
				m.setExpenses(expenses);
			}else{
				skip(parser);
			}	
		}
		return m;
	}
	
	private Map<Integer, Expense> readExpenses(XmlPullParser parser, int memberid) throws XmlPullParserException, IOException {
		Map<Integer, Expense> expenses = new HashMap<Integer, Expense>();

		parser.require(XmlPullParser.START_TAG, ns, "expenses");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("expense")) {
				Expense e  = readExpense(parser, memberid);
				expenses.put(e.getId(), e);
			} else {
				skip(parser);
			}
		}  
		return expenses;
	}
	
	private Expense readExpense(XmlPullParser parser, int memberid) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "expense");
		int id=-1, groupid = -1;
		double amount = 0;
		String date = null;
		String description = null;	
		Set<Integer> receivers = new HashSet<Integer>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("expenseid")) {
				id = readId(parser, "expenseid");
			} else if (name.equals("amount")) {
				amount = Double.parseDouble(readStringValue(parser, "amount"));
			} else if(name.equals("date")){
				date = readStringValue(parser, "date");
			}else if(name.equals("description")){
				description = readStringValue(parser, "description");
			}else if(name.equals("receivers")){
				receivers = readReceivers(parser);
			}else if(name.equals("groupid")){
				groupid = readId(parser, "groupid");
			}else{
				skip(parser);
			}	
		}
		return new Expense(id, memberid, amount, date, description, receivers, groupid);
	}
	
	

	private Set<Integer> readReceivers(XmlPullParser parser) throws XmlPullParserException, IOException {
		Set<Integer> receivers = new HashSet<Integer>();
		parser.require(XmlPullParser.START_TAG, ns, "receivers");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("email")) {
				receivers.add(memberDB.getIdForEmail(readStringValue(parser,"email")));
			} else {
				skip(parser);
			}
		}  
		return receivers;
	}
	
	// Processes title tags in the feed.
	private int readId(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, tag);
		int id = Integer.parseInt(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, tag);
		return id;
	}
	
	private String readStringValue(XmlPullParser parser, String tag) throws XmlPullParserException, IOException{
		parser.require(XmlPullParser.START_TAG, ns, tag);
		String value = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tag);
		return value;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
	public Map<String, Object> readSettings(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, ns, "settings");
			Map map = new HashMap<String, Object>();
			String currency = null;
			Member member = null;
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				if (name.equals("member")) {
					member = readMember(parser);
				}else if (name.equals("currency")) {
					currency = readStringValue(parser, "currency");
				}else{
					skip(parser);
				}
			}  
			map.put("member", member);
			map.put("currency", currency);
			return map;
		} finally {
			in.close();
		}
	}

}