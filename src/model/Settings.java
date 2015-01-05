package model;

public class Settings {
	public Member currentMember;
	private String currency;
	private volatile static Settings instance;
	
	private Settings(){}
	
	public static Settings getInstance(){
		if(instance == null){
			synchronized(Settings.class){
				if(instance == null){
					instance = new Settings();
				}
			}
		}
		return instance;
	}
	
	public Member getCurrentMember() {
		return currentMember;
	}
	
	public void setCurrentMember(Member currentMember) {
		this.currentMember = currentMember;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
}
