package org.khl.assignment2;

import java.util.ArrayList;
import java.util.List;

import org.khl.assignment2.adapter.AddedMemberAdapter;

import service.FetchData;
import service.StoreData;
import service.Validator;
import model.Group;
import model.Member;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import model.observer.Observer;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class CreateGroupActivity extends ListActivity implements OnItemSelectedListener, Observer{
	private LinearLayout groupsLayout;
	private Facade facade;
	private Group group;
	private String groupName;
	private EditText groupNameField, emailField;
	private Button createBtn, inviteBtn, cancelBtn;
	private Spinner spinner;
	private List<Member> members, membersInvited = new ArrayList<Member>();
	private ListView addMembersList;
	private Member selectedMember;
	private AddedMemberAdapter addedMemberAdapt;
	private ArrayAdapter<Member> memberAdapt;
	private FetchData fetchData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		fetchData = new FetchData(this.getApplicationContext());
		String dbWriterType = (fetchData.checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		facade.getDBWriter().addObserver(this);
		members = new ArrayList<Member>(facade.getMembers().values());
		Bundle b = getIntent().getExtras();
		if(b != null){
			loadIntentData(b);
		}
		initializeComponents();
	}

	private void loadIntentData(Bundle b){
		int groupid = b.getInt(GroupDetailActivity.GROUP_ID);
		group = facade.getGroups().get(groupid);
		membersInvited = group.getMembers();
		for(Member m : membersInvited){
			members.remove(m);
		}
		groupName = group.getName();
		setTitle("Manage group");
	}

	private void initializeComponents(){
		createBtn = (Button)findViewById(R.id.createBtn);
		inviteBtn = (Button)findViewById(R.id.inviteBtn);
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		groupsLayout = (LinearLayout)findViewById(R.id.groupsLayout);
		groupNameField = (EditText)findViewById(R.id.groupName);
		if(groupName != null){
			groupNameField.setText(groupName);
			createBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					updateGroup();
				}
			});
		}
		emailField = (EditText)findViewById(R.id.email);
		spinner = (Spinner) findViewById(R.id.spinner);
		memberAdapt = new ArrayAdapter<Member>(this,  android.R.layout.simple_spinner_item, members);
		memberAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(memberAdapt);
		spinner.setOnItemSelectedListener(this); 
		addedMemberAdapt = new AddedMemberAdapter(this,membersInvited, memberAdapt, members, spinner);
		addMembersList = (ListView)findViewById(android.R.id.list);
		addMembersList.setAdapter(addedMemberAdapt);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		selectedMember = (Member)parent.getItemAtPosition(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void createGroup(View v){
		String groupNameText = groupNameField.getText().toString();
		if(Validator.isValidLength(groupNameText, 1, 25)){
			Group group = new Group(groupNameText, membersInvited);
			facade.createGroup(group);
			finish();
		}else{
			groupNameField.setError(getString(R.string.error_length));
		}
	}

	public void updateGroup(){
		String groupNameText = groupNameField.getText().toString();
		if(Validator.isValidLength(groupNameText, 1, 25)){
			facade.updateGroup(group.getId(), groupNameText, membersInvited);
			finish();
		}else{
			groupNameField.setError(getString(R.string.error_length));
		}
	}

	public void createMember(View v){
		Intent intent = new Intent(CreateGroupActivity.this, CreateMemberActivity.class);
		startActivity(intent);
	}

	public void invite(View v){
		if(selectedMember != null){
			membersInvited.add(selectedMember);
			members.remove(selectedMember);
			if(members.isEmpty()){
				selectedMember = null;
			}
			refreshData();
		}
	}

	private void refreshData(){
		addedMemberAdapt.notifyDataSetChanged();
		memberAdapt.notifyDataSetChanged();
		spinner.setAdapter(memberAdapt);
	}

	public void cancel(View v){
		finish();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		update();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.menu_groups){
			Intent intent = new Intent(CreateGroupActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.menu_create_group){
			return true;
		}else if(id == R.id.menu_invitations){
			return true;
		}else if (id == R.id.action_settings) {
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

	public void finish(){
		super.finish();
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

	@Override
	public void update() {
		memberAdapt = new ArrayAdapter<Member>(this,  android.R.layout.simple_spinner_item, members);
		memberAdapt.notifyDataSetChanged();
		spinner.setAdapter(memberAdapt);
	}
}