package db;

import java.util.List;
import java.util.Map;

import model.Expense;
import model.Group;
import model.Member;
import model.Settings;
import model.observer.Subject;

public interface DBWriter extends Subject{	
	void writeMember(Member member);
	void writeMembers(List<Member> members);
	Map<Integer, Member> getMembers();
	Map<Integer, Group> getGroups();
	void writeGroup(Group group);
	void writeGroups(List<Group> groups);
	void writeExpense(Expense expense, List<Member> recipients);
	void writeExpenses(List<Expense> expenses);
	void updateGroup(int groupid, String groupName, List<Member> membersInvited);
	Member getMemberForId(int id);
	void clearDatabase();
	void closeConnection();
}