package service;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import db.MemberDB;

import model.Expense;
import model.Group;
import model.Member;
import android.util.Log;
import android.util.Xml;

public class XMLWriter {
	private MemberDB memberDB = MemberDB.getInstance();

	public String write(List<Member> members, List<Group> groups) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		xmlSerializer.setOutput(writer);
		xmlSerializer.startDocument("UTF-8", true);
		xmlSerializer.startTag("", "database");
		writeMembers(xmlSerializer, members);
		writeGroups(xmlSerializer, groups);	
		xmlSerializer.endTag("", "database");
		// end DOCUMENT
		xmlSerializer.endDocument();
		Log.v("bram", writer.toString());
		return writer.toString();

	}
	
	private void writeMembers(XmlSerializer xmlSerializer, List<Member> members) throws IllegalArgumentException, IllegalStateException, IOException{
		xmlSerializer.startTag("", "members"); //MEMBERS
		for(Member member : members){
			xmlSerializer.startTag("", "member");
				xmlSerializer.startTag("", "memberid");
					xmlSerializer.text(member.getId()+"");
				xmlSerializer.endTag("", "memberid");
				xmlSerializer.startTag("", "firstname");
					xmlSerializer.text(member.getFirstName());
				xmlSerializer.endTag("", "firstname");
				xmlSerializer.startTag("", "lastname");
					xmlSerializer.text(member.getLastName());
				xmlSerializer.endTag("", "lastname");
				xmlSerializer.startTag("", "email");
					xmlSerializer.text(member.getEmail());
				xmlSerializer.endTag("", "email");
				xmlSerializer.startTag("", "expenses");
				for(Expense expense : member.getExpenses().values()){
					writeExpense(xmlSerializer, expense);
				}
				xmlSerializer.endTag("", "expenses");
			xmlSerializer.endTag("", "member");
		}
		xmlSerializer.endTag("", "members");
	}
	
	private void writeGroups(XmlSerializer xmlSerializer, List<Group> groups) throws IllegalArgumentException, IllegalStateException, IOException{	
		xmlSerializer.startTag("", "groups");
			for(Group group : groups){
				xmlSerializer.startTag("", "group");
					xmlSerializer.startTag("", "groupid");
						xmlSerializer.text(group.getId()+"");
					xmlSerializer.endTag("", "groupid");
					xmlSerializer.startTag("", "groupname");
						xmlSerializer.text(group.getName());
					xmlSerializer.endTag("", "groupname");
					writeMembers(xmlSerializer, group.getMembers());
				xmlSerializer.endTag("", "group");
			}
			xmlSerializer.endTag("", "groups");
		}
	
	private void writeExpense(XmlSerializer xmlSerializer, Expense expense) throws IllegalArgumentException, IllegalStateException, IOException{
		xmlSerializer.startTag("", "expense");
			xmlSerializer.startTag("", "expenseid");
				xmlSerializer.text(expense.getId()+"");
			xmlSerializer.endTag("", "expenseid");
			xmlSerializer.startTag("", "amount");
				xmlSerializer.text(expense.getAmount()+"");
			xmlSerializer.endTag("", "amount");
			xmlSerializer.startTag("", "date");
				xmlSerializer.text(expense.getDate());
			xmlSerializer.endTag("", "date");
			xmlSerializer.startTag("", "description");
				xmlSerializer.text(expense.getDescription());
			xmlSerializer.endTag("", "description");
			xmlSerializer.startTag("", "receivers");
			for(Integer id : expense.getMembersPaidFor()){
				xmlSerializer.startTag("", "email");
					xmlSerializer.text(memberDB.getMembers().get(id).getEmail());
				xmlSerializer.endTag("", "email");
			}
			xmlSerializer.endTag("", "receivers");
			xmlSerializer.startTag("", "groupid");
				xmlSerializer.text(expense.getGroupId()+"");
			xmlSerializer.endTag("", "groupid");
		xmlSerializer.endTag("", "expense");
	}
	
	public String writeSettings(Member member, String currency) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		xmlSerializer.setOutput(writer);
		xmlSerializer.startDocument("UTF-8", true);
		xmlSerializer.startTag("", "settings");
			xmlSerializer.startTag("", "member");
				xmlSerializer.startTag("", "memberid");
					xmlSerializer.text(member.getId()+"");
				xmlSerializer.endTag("", "memberid");
				xmlSerializer.startTag("", "firstname");
					xmlSerializer.text(member.getFirstName());
				xmlSerializer.endTag("", "firstname");
				xmlSerializer.startTag("", "lastname");
					xmlSerializer.text(member.getLastName());
				xmlSerializer.endTag("", "lastname");
				xmlSerializer.startTag("", "email");
					xmlSerializer.text(member.getEmail());
				xmlSerializer.endTag("", "email");
			xmlSerializer.endTag("", "member");
			xmlSerializer.startTag("", "currency");
				xmlSerializer.text(currency);
			xmlSerializer.endTag("", "currency");
		xmlSerializer.endTag("", "settings");
		xmlSerializer.endDocument();
		return writer.toString();

	}
}