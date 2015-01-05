package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import model.Group;
import model.Member;
import model.Settings;
import model.Facade.Facade;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class StoreData extends AsyncTask<Void, Void, Void>{
	private static final String FILENAME="db.xml";
	private static final String SETTINGS = "settings.xml";
	private List<Group> groups;
	private List<Member> members;
	private Context context;

	public StoreData(Context context, List<Member> members, List<Group> groups){
		this.members = members;
		this.groups = groups;
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		XMLWriter xmlWriter = new XMLWriter();
		try {
			String output = xmlWriter.write(members, groups);
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(output.getBytes());
			fos.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveSettings(Settings settings){
		XMLWriter xmlWriter = new XMLWriter();
		try {
			String output = xmlWriter.writeSettings(settings.getCurrentMember(), settings.getCurrency());
			FileOutputStream fos = context.openFileOutput(SETTINGS, Context.MODE_PRIVATE);
			fos.write(output.getBytes());
			fos.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
