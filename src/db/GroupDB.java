package db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Group;
import model.Member;

public class GroupDB {
	private Map<Integer, Group> groups;
	private volatile static GroupDB instance;
	
	private GroupDB(){
		groups = new HashMap<Integer, Group>();
	}
	
	public static GroupDB getInstance(){
		if(instance == null){
			synchronized(GroupDB.class){
				if(instance == null){
					instance = new GroupDB();
				}
			}
		}
		return instance;
	}
	
	public void setGroups(Map<Integer, Group> groups){
		this.groups = groups;
	}
	
	public Map<Integer, Group> getGroups(){
		return groups;
	}
	
	public void addGroup(Group group){
		groups.put(group.getId(), group);
	}
	
	public List<Member> getMembersInGroup(int groupId){
		return groups.get(groupId).getMembers();
	}
}
