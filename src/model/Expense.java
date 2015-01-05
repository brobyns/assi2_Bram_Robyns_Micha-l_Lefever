package model;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;

public class Expense {
	private static int counter = 0; 
	private int id, groupId, senderId;
	private Member sender;
	private double amount;
	private String type, description;
	private String date;
	private Set<Integer> membersPaidFor = new HashSet<Integer>();
	private Bitmap photo;

	public Expense(Member sender, double amount, String date, String description, int groupId){
		counter++;
		id = counter;
		setSender(sender);
		setAmount(amount);
		setDate(date);
		setDescription(description);
		setGroupId(groupId);
	}
	
	public Expense(Member sender, double amount, String date, String description, int groupId, Bitmap photo){
		this(sender, amount, date, description, groupId);
		setPhoto(photo);
	}

	public Expense(int senderId, double amount, String date, String description, Set<Integer> membersPaidFor, int groupId){
		setSenderId(senderId);
		setAmount(amount);
		setDate(date);
		setDescription(description);
		setGroupId(groupId);
		setMembersPaidFor(membersPaidFor);
	}

	public Expense(int senderId, double amount, String date, String description, Set<Integer> membersPaidFor, int groupId, Bitmap photo){
		this(senderId, amount, date, description, membersPaidFor, groupId);
		setPhoto(photo);
	}
	
	public Expense(int id, int senderId, double amount, String date, String description, Set<Integer> membersPaidFor, int groupId, Bitmap photo) {
		this(senderId, amount, date, description, membersPaidFor, groupId);
		setId(id);
	}
	
	public Expense(int id, int senderId, double amount, String date, String description, Set<Integer> membersPaidFor, int groupId) {
		this(senderId, amount, date, description, membersPaidFor, groupId);
		setId(id);
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Member getSender() {
		return sender;
	}

	private void setSender(Member sender) {
		this.sender = sender;
	}
	
	private void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
	public int getSenderId(){
		return senderId;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Set<Integer> getMembersPaidFor() {
		return membersPaidFor;
	}
	public void setMembersPaidFor(Set<Integer> membersPaidFor) {
		this.membersPaidFor = membersPaidFor;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public void addRecipient(int memberid) {
		membersPaidFor.add(memberid);
	}

}
