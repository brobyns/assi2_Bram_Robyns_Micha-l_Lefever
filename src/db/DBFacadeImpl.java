package db;

import java.util.List;
import java.util.Map;

import model.Expense;
import model.Group;
import model.Member;
import model.Settings;

public class DBFacadeImpl implements DBFacade {

	private DBWriter writer;
	
	public DBFacadeImpl (DBWriter writer) {
		this.writer = writer;
	}
	
	public DBWriter getWriter() {
		return writer;
	}

	@Override
	public void setWriter(DBWriter writer) {
		this.writer = writer;
	}

	@Override
	public void closeConnection() {
		writer.closeConnection();
	}

	@Override
	public Map<Integer, Member> getMembers() {
		return writer.getMembers();
	}

	@Override
	public void writeGroup(Group group){
		writer.writeGroup(group);
	}
	

	@Override
	public void updateGroup(int groupId, String groupName, List<Member> membersInvited) {
		writer.updateGroup(groupId, groupName, membersInvited);
	}

	@Override
	public Map<Integer, Group> getGroups() {
		return writer.getGroups();
	}
	@Override
	public Member getMemberForId(int id){
		return writer.getMemberForId(id);
	}

	public void writeExpense(Expense expense, List<Member> recipients){
		writer.writeExpense(expense, recipients);
	}

	@Override
	public void clearDatabase() {
		writer.clearDatabase();
	}

	public void writeMembers(List<Member> members){
		writer.writeMembers(members);
	}

	public void writeGroups(List<Group> groups){
		writer.writeGroups(groups);
	}

	public void writeExpenses(List<Expense> expenses){
		writer.writeExpenses(expenses);
	}

	@Override
	public void writeMember(Member member) {
		writer.writeMember(member);
	}
}
