package model.Facade;

import java.util.List;
import java.util.Map;

import android.content.Context;
import db.DBWriter;
import model.Expense;
import model.Group;
import model.Member;
import model.Settings;

public interface Facade {

	Member getCurrentMember();
	Map<Integer,Member> getMembers();
	Map<Integer,Group> getGroups();
	Map<Integer,Member> getMembersOnline();
	List<Member> getMembersInGroup(int groupid);
	Map<Integer,Group> getGroupsOnline();
	void createGroup(Group group);
	void writeExpense(Expense expense, List<Member> recipients);
	Map<Integer, Double> getAmountsPaid(int id, Group group);
	Map<Integer, Double> getAmountsReceived(int id, Group group);
	void updateGroup(int groupId, String string, List<Member> membersInvited);
	DBWriter getDBWriter();
	void writeMembers(List<Member> members);
	void writeGroups(List<Group> groups);
	void writeExpenses(List<Expense> expenses);
	void clearDatabase();
	boolean checkIfNewDataAvailable();
	void createMember(Member member);
	void writeSettings(Context context, Settings settings);
	Member getMemberForId(int id);
	String getCurrency();
}
