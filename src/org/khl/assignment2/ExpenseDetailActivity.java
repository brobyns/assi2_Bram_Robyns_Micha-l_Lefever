package org.khl.assignment2;

import org.khl.assignment2.adapter.MemberDetailAdapter;

import db.DBWriter;
import model.Expense;
import model.Member;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import model.observer.Observer;
import service.FetchData;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpenseDetailActivity extends Activity implements Observer {
	
	private TextView member, amount, date, description;
	private ImageView image;
	private int expenseid;
	private int memberid;
	private int groupid;
	private FetchData fetchData;
	private Facade facade;
	private Expense expense;
	private MemberDetailAdapter memberDetailAdapt;
	private DBWriter dbWriter;
	public static final String EXPENSE_ID = "expenseid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_detail);
		Bundle b = getIntent().getExtras();
        expenseid = b.getInt(MemberDetailActivity.EXPENSE_ID);
        memberid = b.getInt(GroupDetailActivity.MEMBER_ID);
        groupid = b.getInt(MemberDetailActivity.GROUP_ID);
        fetchData = new FetchData(this.getApplicationContext());
        String dbWriterType = (fetchData.isConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		dbWriter = facade.getDBWriter();
		dbWriter.addObserver(this);
		initializeComponents();
	}

	private void initializeComponents(){
		expense = facade.getGroups().get(groupid).getMembers().get(memberid - 1).getExpenses().get(expenseid);
		String membername = facade.getGroups().get(groupid).getMembers().get(memberid - 1).getFirstName();
		//String membername = expense.getSender().getFirstName();
		member = (TextView)findViewById(R.id.member);
		member.setText("Member: " + membername);
		amount = (TextView)findViewById(R.id.amount);
		image = (ImageView)findViewById(R.id.imageView1);
		amount.setText("Amount: " + expense.getAmount());
		date = (TextView)findViewById(R.id.date);
		date.setText("Date: " + expense.getDate());
		description = (TextView)findViewById(R.id.description);
		if (expense.getPhoto() != null){
			image.setImageBitmap(expense.getPhoto());
		}
		if (!expense.getDescription().equals(null)){
			description.setText("Description: \n" + expense.getDescription());
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.member_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.menu_groups){
			Intent intent = new Intent(ExpenseDetailActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.menu_create_group){
			Intent intent = new Intent(ExpenseDetailActivity.this, CreateGroupActivity.class);
			startActivity(intent);
			return true;
		}else if(id == R.id.menu_invitations){
			return true;
		}else if (id == R.id.action_settings) {
			Intent intent = new Intent(ExpenseDetailActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
