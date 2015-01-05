package org.khl.assignment2;

import db.MemberDB;
import model.Member;
import model.Settings;
import model.Facade.Facade;
import model.Facade.FacadeImpl;
import service.FetchData;
import service.Validator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
	private Facade facade;
	private FetchData fetchData;
	private Spinner spinner;
	private EditText firstname, lastname, email;
	private String[] currencies = {"EUR", "GBP", "USD"};
	private String currency;
	private Context context;
	private Settings settings;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		context = this.getApplicationContext();
		fetchData = new FetchData(context);
		String dbWriterType = (fetchData.checkIfConnected()? "OnlineDBWriter": "OfflineDBWriter");
		facade = new FacadeImpl(dbWriterType);
		initializeComponents();
	}

	private void initializeComponents(){
		firstname = (EditText)findViewById(R.id.firstname);
		lastname = (EditText)findViewById(R.id.lastname);
		email = (EditText)findViewById(R.id.email);
		ArrayAdapter<String> currencyAdapt = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, currencies);
		currencyAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(currencyAdapt);
		spinner.setOnItemSelectedListener(this); 
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		currency = (String)parent.getItemAtPosition(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	public void save(View v){
		if(Validator.isValidLength(firstname.getText().toString(),2,20)){
			if(Validator.isValidLength(lastname.getText().toString(), 2, 25)){
				if(Validator.isValidEmailAddress(email.getText().toString())){
					Member member = new Member(firstname.getText().toString(),lastname.getText().toString(),
							email.getText().toString());
					settings = Settings.getInstance();
					if(settings.getCurrentMember() == null){
						facade.createMember(member);
					}
					settings.setCurrentMember(member);
					settings.setCurrency(currency);
					facade.writeSettings(context, settings);
					Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else{
					email.setError(getString(R.string.error_email));
				}
			}else{
				lastname.setError(getString(R.string.error_length));
			}
		}else{
			firstname.setError(getString(R.string.error_length));
		}
	}

	public void cancel(View v){
		finish();
	}

}