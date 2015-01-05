package db;

import java.util.List;
import java.util.Map;

import model.Expense;
import model.Group;
import model.Member;
import model.Settings;

public interface DBFacade {
	DBWriter getWriter();
	void setWriter(DBWriter writer);
	Map<Integer, Member> getMembers();
	void writeGroup(Group group);
	void updateGroup(int groupId, String groupName, List<Member> membersInvited);
	Map<Integer, Group> getGroups();
	void writeExpense(Expense expense, List<Member> recipients);
	void writeMember(Member member);
	void writeMembers(List<Member> members);
	void writeGroups(List<Group> groups);
	void writeExpenses(List<Expense> expenses);
	void clearDatabase();
	void closeConnection();
	Member getMemberForId(int id);
}
