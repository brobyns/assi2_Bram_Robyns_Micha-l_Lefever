package org.khl.assignment2;

import service.FetchData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashScreenActivity extends Activity{
	private static int SPLASH_TIME_OUT = 3000;
	private FetchData fetchData;
	private ProgressBar progressBar;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        fetchData = new FetchData(this.getApplicationContext());
        fetchData.execute();
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	if(fetchData.areSettingsLoaded()){
            		Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            		startActivity(i);
            	}else{
            		Intent i = new Intent(SplashScreenActivity.this, SettingsActivity.class);
            		startActivity(i);
            	}
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}