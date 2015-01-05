package org.khl.assignment2;

import java.util.ArrayList;

import model.Group;
import model.Member;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import service.FetchData;
import service.StoreData;
import service.Validator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateMemberActivity extends Activity{
	private Facade facade;
	private EditText firstnameText, lastnameText, emailText;
	private FetchData fetchData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_member);
		fetchData = new FetchData(this.getApplicationContext());
		String dbWriterType = (fetchData.checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		initializeComponents();
	}

	private void initializeComponents(){
		firstnameText = (EditText)findViewById(R.id.firstname);
		lastnameText = (EditText)findViewById(R.id.lastname);
		emailText = (EditText)findViewById(R.id.email);
	}

	public void create(View v){
		String firstname = firstnameText.getText().toString();
		String lastname = lastnameText.getText().toString();
		String email = emailText.getText().toString();
		if(Validator.isValidLength(firstname, 2, 20)){
			if(Validator.isValidLength(lastname, 2, 25)){
				if(Validator.isValidEmailAddress(email)){
					Member member = new Member(firstname, lastname , email);
					facade.createMember(member);
					finish();
				}else{
					emailText.setError(getString(R.string.error_email));
				}
			}else{
				lastnameText.setError(getString(R.string.error_length));
			}
		}else{
			firstnameText.setError(getString(R.string.error_length));
		}
	}
	
	@Override
	 public void onStop() {
		ArrayList<Group> groups = new ArrayList<Group>(facade.getGroups().values());
		ArrayList<Member> members = new ArrayList<Member>(facade.getMembers().values());
		StoreData storeData = new StoreData(this.getApplicationContext(), members, groups);
		Log.v("bram", "data stored");
		storeData.execute();
		super.onStop();
	 }

	public void cancel(View v){
		finish();
	}
}