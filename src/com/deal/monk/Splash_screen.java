package com.deal.monk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.R;

public class Splash_screen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
				DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(this);
		dealMonkPreferences.setString("email_alert","1");
		dealMonkPreferences.setString("sms_alert","0");
		String userid_dealmonk = dealMonkPreferences.getString("MAIN_USER_ID");
		String Contact_number = dealMonkPreferences.getString("Contact_Number");
		if (!TextUtils.isEmpty(userid_dealmonk) && (!TextUtils.isEmpty(Contact_number))) {
			
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {

						Intent intent = new Intent(Splash_screen.this,MainPage.class);
						startActivity(intent);
						finish();
					}
				}, SPLASH_TIME_OUT);
			}					    
		 else {

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					Intent intent = new Intent(Splash_screen.this, Login.class);
					startActivity(intent);
					finish();
				}
			}, SPLASH_TIME_OUT);
		}

	}

}
