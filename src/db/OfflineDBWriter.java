package db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import model.Expense;
import model.Group;
import model.Member;
import model.Settings;
import model.observer.Observer;

public class OfflineDBWriter implements DBWriter {

	private MemberDB memberDB;
	private GroupDB groupDB;
	private Settings settings;
	private ArrayList<Observer> observers;

	public OfflineDBWriter() {
		observers = new ArrayList<Observer>();
		memberDB = MemberDB.getInstance();
		groupDB = GroupDB.getInstance();
		settings = Settings.getInstance();
	}

	@Override
	public void closeConnection() {
		// NOTHING
	}

	@Override
	public void writeMember(Member member) {
		memberDB.addMember(member);
		notifyObservers();
	}

	@Override
	public void writeGroup(Group group) {
		groupDB.addGroup(group);
		notifyObservers();
	}

	@Override
	public Map<Integer, Group> getGroups() {
		return groupDB.getGroups();
	}

	@Override
	public void writeExpense(Expense expense, List<Member> recipients) {
		int senderId = expense.getSender().getId();
		for (Member m : recipients) {
			expense.addRecipient(m.getId());
		}
		Member sender = memberDB.getMembers().get(senderId);
		Log.v("bram", "Sender of payment: " + sender.toString());
		for (int id : expense.getMembersPaidFor()) {
			Log.v("bram", "paid to: " + memberDB.getMembers().get(id));
		}
		sender.addExpense(expense);
		notifyObservers();
	}

	@Override
	public void updateGroup(int groupId, String groupName,
			List<Member> membersInvited) {
		groupDB.addGroup(new Group(groupId, groupName, membersInvited));
		notifyObservers();
	}

	@Override
	public void notifyObservers() {
		for (Observer o : observers) {
			o.update();
		}
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void clearDatabase() {
	}

	@Override
	public void writeMembers(List<Member> members) {
		for (Member m : members) {
			memberDB.addMember(m);
		}
	}

	@Override
	public void writeGroups(List<Group> groups) {
		for (Group g : groups) {
			groupDB.addGroup(g);
		}
	}

	@Override
	public void writeExpenses(List<Expense> expenses) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<Integer, Member> getMembers() {
		return memberDB.getMembers();
	}

	@Override
	public Member getMemberForId(int id) {
		return memberDB.getMembers().get(id);
	}
}
