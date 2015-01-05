package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Expense;
import model.Group;
import model.Member;
import model.Settings;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import model.observer.Observer;
import model.observer.Subject;

import org.xmlpull.v1.XmlPullParserException;

import db.GroupDB;
import db.MemberDB;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class FetchData extends AsyncTask<Void, Void, Void> implements Subject{

	private ConnectivityManager connectivityManager;
	private NetworkInfo netwerkInfo;
	private static final String FILENAME= "db.xml",SETTINGS = "settings.xml";
	private Context context;
	private boolean isConnected = false, settingsLoaded = false;
	private MemberDB memberDB = MemberDB.getInstance();
	private GroupDB groupDB = GroupDB.getInstance();
	private Settings settings = Settings.getInstance();
	private Facade facade;
	private List<Observer> observers;

	public FetchData(Context context) {
		this.context = context;
		observers = new ArrayList<Observer>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		String dbWriterType = (checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
	}

	@Override
	protected Void doInBackground(Void...params) {
		Map<Integer, Member> members = new HashMap<Integer, Member>();
		Map<Integer, Group> groups = new HashMap<Integer, Group>();
		if(loadSettings()){
			if(!isConnected) {		//OFFLINE
				getXMLData();
			} else {		  //ONLINE
				if(facade.checkIfNewDataAvailable()){			
					members = facade.getMembersOnline();
					memberDB.addMembers(members);
					groups = facade.getGroupsOnline();
					groupDB.setGroups(groups);
				}else{
					getXMLData();
					facade.clearDatabase();
					ArrayList<Member> membersList = new ArrayList<Member>(memberDB.getMembers().values());
					facade.writeSettings(context, settings);
					facade.writeMembers(membersList);
					facade.writeGroups(new ArrayList<Group>(groupDB.getGroups().values()));
					for(Member m : membersList){
						facade.writeExpenses(new ArrayList<Expense>(m.getExpenses().values()));
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		notifyObservers();
		connectivityManager = null;
		netwerkInfo = null;

	}

	public boolean checkIfConnected(){
		connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		netwerkInfo = connectivityManager.getActiveNetworkInfo();	

		if(netwerkInfo != null && netwerkInfo.isConnected()) {
			isConnected = true;
		}
		return isConnected;
	}

	public boolean isConnected(){
		return isConnected;
	}

	public boolean areSettingsLoaded(){
		return settingsLoaded;
	}

	@Override
	public void notifyObservers() {
		for(Observer o : observers){
			o.update();
		}
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	private void getXMLData(){
		XMLParser xmlParser = new XMLParser();
		InputStream fin = null;
		try {
			fin = context.openFileInput(FILENAME);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(fin != null){
				Map<String, Map> map = xmlParser.parse(fin);
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loadSettings(){
		XMLParser xmlParser = new XMLParser();
		FileInputStream fin = null;
		try {
			fin = context.openFileInput(SETTINGS);
		} catch (FileNotFoundException e1) {
			return false;
		}
		try {
			Map<String, Object> settingsMap = xmlParser.readSettings(fin);
			Member member = (Member)settingsMap.get("member");
			String currency = (String)settingsMap.get("currency");
			settings.setCurrentMember(member);
			settings.setCurrency(currency);
			facade.writeSettings(context, settings);
			settingsLoaded = true;
			memberDB.setCurrMember(member);
			facade.createMember(member);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return settingsLoaded;
	}

}