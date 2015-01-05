package model;

import java.util.List;

public class Admin extends Member{
	
	private List<Group> adminGroups;

	public Admin(int id, String firstName, String lastName, String email) {
		super(id,firstName, lastName, email);
	}

}
