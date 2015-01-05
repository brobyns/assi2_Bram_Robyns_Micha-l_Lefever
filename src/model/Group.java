package model;

import java.util.List;

public class Group {
	private static int counter = 0;
	private int id;
	private Admin admin;
	private String name;
	private List<Member> members;
	
	public Group(String groupname, List<Member> members){
		counter++;
		id = counter;
		setId(id);
		setName(groupname);
		setMembers(members);
	}
	
	public Group(int id, String name){
		setId(id);
		setName(name);
	}

	public Group(int id, String groupname, List<Member> members) {
		this(id, groupname);
		setMembers(members);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
}
