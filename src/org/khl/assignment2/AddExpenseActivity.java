package org.khl.assignment2;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.khl.assignment2.adapter.AddedMemberAdapter;
import org.khl.assignment2.adapter.SpinnerListener;

import service.FetchData;
import service.Validator;
import model.Expense;
import model.Member;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpenseActivity extends Activity implements OnItemSelectedListener {
	private Facade facade;
	private int groupid;
	private EditText description, amountText;
	private FetchData fetchData;
	private SpinnerListener spinnerListener;
	private Spinner spinnerSend, spinnerReceive;
	private ArrayAdapter<Member> senderAdapt, receiverAdapt;
	private AddedMemberAdapter addedMemberAdapt;
	private List<Member> members, receivers;
	private List<Member> recipients = new ArrayList<Member>();
	private ListView addMembersList;
	private ImageView imageview;
	private Member selectedMember;
	private Bitmap photo = null;
	private static final int CAM_REQUEST = 1313;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		Bundle b = getIntent().getExtras();
		groupid = b.getInt(GroupDetailActivity.GROUP_ID);
		fetchData = new FetchData(this.getApplicationContext());
		String dbWriterType = (fetchData.checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		members = new ArrayList<Member>(facade.getMembersInGroup(groupid));
		initializeComponents();
	}


	private void initializeComponents(){
		members = new ArrayList<Member>(facade.getGroups().get(groupid).getMembers());
//		members.remove(facade.getCurrentMember());
		receivers = members;
		description = (EditText)findViewById(R.id.description);
		amountText = (EditText)findViewById(R.id.amount);
		spinnerSend = (Spinner) findViewById(R.id.spinner_send);
		spinnerReceive = (Spinner) findViewById(R.id.spinner_receive);
		imageview = (ImageView) findViewById(R.id.imageView1);
		senderAdapt = new ArrayAdapter<Member>(this,  android.R.layout.simple_spinner_item, members);
		senderAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSend.setAdapter(senderAdapt);
		spinnerListener = new SpinnerListener(this);
		spinnerSend.setOnItemSelectedListener(spinnerListener);
		receiverAdapt = new ArrayAdapter<Member>(this,  android.R.layout.simple_spinner_item, receivers);
		receiverAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerReceive.setAdapter(receiverAdapt);
		spinnerReceive.setOnItemSelectedListener(this);
		addedMemberAdapt = new AddedMemberAdapter(this,recipients, receiverAdapt, receivers, spinnerSend, spinnerReceive);
		addMembersList = (ListView)findViewById(android.R.id.list);
		addMembersList.setAdapter(addedMemberAdapt);
	}

	public void addExpense(View v){
		if(!recipients.isEmpty()){
			if(Validator.isValidAmount(amountText.getText().toString())){
				Double amount = Double.parseDouble(amountText.getText().toString());
				Expense expense = new Expense(spinnerListener.getSender(), amount, getCurrentDateTime(), description.getText().toString(), groupid, photo);
				facade.writeExpense(expense, recipients);
				finish();
			}else{
				amountText.setError(getString(R.string.error_amount));
			}
		}else{
			Toast.makeText(getApplicationContext(), getString(R.string.error_no_recipient), 
					   Toast.LENGTH_SHORT).show();
		}
	}

	private String getCurrentDateTime(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return df.format(c.getTime());
	}

	public void cancel(View v){
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAM_REQUEST){
			photo = (Bitmap) data.getExtras().get("data");
			imageview.setImageBitmap(photo);
		}
	}

	public void invite(View v){
		if(selectedMember != null && spinnerListener.getSender().getId() != selectedMember.getId()){
			recipients.add(selectedMember);
			receivers.remove(selectedMember);
			if(receivers.isEmpty()){
				selectedMember = null;
			}
			refreshData();
		}
	}

	public void takePhoto(View v){
		Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(camera, CAM_REQUEST);
	}

	public static void saveToFile(String filename,Bitmap bmp) {
		try {
			FileOutputStream out = new FileOutputStream(filename);
			bmp.compress(CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch(Exception e) {}
	}

	private void refreshData(){
		addedMemberAdapt.notifyDataSetChanged();
		receiverAdapt.notifyDataSetChanged();
		spinnerReceive.setAdapter(receiverAdapt);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		selectedMember = (Member)parent.getItemAtPosition(pos);
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}