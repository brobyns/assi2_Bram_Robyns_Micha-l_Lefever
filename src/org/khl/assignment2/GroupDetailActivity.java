package org.khl.assignment2;

import java.util.ArrayList;
import java.util.List;

import org.khl.assignment2.adapter.GroupDetailAdapter;

import db.DBWriter;
import service.FetchData;
import service.SettlePaymentsDialog;
import service.StoreData;
import model.Group;
import model.Member;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import model.observer.Observer;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class GroupDetailActivity extends ListActivity implements OnItemClickListener, Observer{
	private TextView memberName;
	private Facade facade;
	private ListView listView;
	private GroupDetailAdapter detailAdapt;
	private FetchData fetchData;
	private int groupid;
	private DBWriter dbWriter;
	public static final String GROUP_ID = "groupId", MEMBER_ID="memberId";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_detail);
		Bundle b = getIntent().getExtras();
		groupid = b.getInt(MainActivity.GROUP_ID);
		fetchData = new FetchData(this.getApplicationContext());
		String dbWriterType = (fetchData.checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		dbWriter = facade.getDBWriter();
		dbWriter.addObserver(this);
		initializeComponents();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		update();
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
	
	private void initializeComponents(){
		memberName = (TextView)findViewById(R.id.memberName);
		listView = (ListView)findViewById(android.R.id.list);
		detailAdapt=new GroupDetailAdapter(this, facade.getGroups().get(groupid).getMembers(), facade, groupid);
		listView.setAdapter(detailAdapt);
		listView.setOnItemClickListener(this);
	}
	
	public void settlePayments(View v){
		boolean groupHasExpenses = false;
		for(Member m : facade.getGroups().get(groupid).getMembers()){
			if(!m.getExpensesForGroup(groupid).isEmpty()){
				groupHasExpenses = true;
			}
		}
		if(groupHasExpenses){
			SettlePaymentsDialog dialog = new SettlePaymentsDialog();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			dialog.show(ft, "dialog");
		}else{
			Toast.makeText(getApplicationContext(), getString(R.string.error_no_expenses), 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void manageGroup(View v){
		Intent intent = new Intent(GroupDetailActivity.this, CreateGroupActivity.class);
		intent.putExtra(GROUP_ID, groupid);
		startActivity(intent);
	}
	
	public void addExpense(View v){
		if(facade.getGroups().get(groupid).getMembers().size() > 1){
			Intent intent = new Intent(GroupDetailActivity.this, AddExpenseActivity.class);
			intent.putExtra(GROUP_ID, groupid);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), getString(R.string.error_count), 
					Toast.LENGTH_SHORT).show();
		}
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
			Intent intent = new Intent(GroupDetailActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.menu_create_group){
			Intent intent = new Intent(GroupDetailActivity.this, CreateGroupActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.menu_invitations){
			return true;
		}else if (id == R.id.action_settings) {
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Member member = (Member) parent.getItemAtPosition(pos);
		Intent intent = new Intent(GroupDetailActivity.this, MemberDetailActivity.class);
		intent.putExtra(MEMBER_ID, member.getId());
		intent.putExtra(GROUP_ID, groupid);
		startActivity(intent);
	}

	@Override
	public void update() {
		detailAdapt=new GroupDetailAdapter(this, facade.getGroups().get(groupid).getMembers(), facade, groupid);
		detailAdapt.notifyDataSetChanged();
		listView.setAdapter(detailAdapt);
	}
	
	protected void sendEmail() {
		Log.v("bram", "send email");
		Group group = facade.getGroups().get(groupid);
		List<Member> members = group.getMembers();
		String[] TO = {};
		for(int i=0; i<members.size(); i++){
			TO[i] = members.get(i).getEmail();
		}
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");


		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			Log.v("bram", "Finished sending email...");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(GroupDetailActivity.this, 
					getString(R.string.error_no_email_client), Toast.LENGTH_SHORT).show();
		}
	}

	
	
}