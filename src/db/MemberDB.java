package db;

import java.util.HashMap;
import java.util.Map;

import model.Member;

public class MemberDB {
	private Member currMember;
	private Map<Integer, Member> members;
	private volatile static MemberDB instance;
	
	private MemberDB(){
		members = new HashMap<Integer, Member>();
	}
	
	public static MemberDB getInstance(){
		if(instance == null){
			synchronized(MemberDB.class){
				if(instance == null){
					instance = new MemberDB();
				}
			}
		}
		return instance;
	}
	
	public boolean addMember(Member member){
		if(member == null){
			return false;
		}else{
			members.put(member.getId(), member);
			return true;
		}
	}
	
	public void addMembers(Map<Integer, Member> members) {
		for(Member m : members.values()){
			this.members.put(m.getId(), m);
		}
	}	
	
	
	public boolean deleteMember(Member member){
		if(member == null){
			return false;
		}else{
			members.remove(member.getId());
			return true;
		}
	}
	
	public Member getCurrMember() {
		return currMember;
	}
	
	public void setCurrMember(Member currMember) {
		this.currMember = currMember;
	}
	
	public Map<Integer, Member> getMembers() {
		return members;
	}
	
	public void setMembers(Map<Integer, Member> members) {
		this.members = members;
	}	
	
	public int getIdForEmail(String email){
		for(Member m : members.values()){
			if(m.getEmail().equalsIgnoreCase(email)){
				return m.getId();
			}
		}
		return -1;
	}
	
	
}
