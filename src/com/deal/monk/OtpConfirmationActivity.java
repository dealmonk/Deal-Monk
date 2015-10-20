package com.deal.monk;

import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;

public class OtpConfirmationActivity extends Activity {
	
	EditText btn_otp ;
	Button regenerateotp ,proceed ;
	String userid, contact_no ;
	
	private DealMonkPreferences dealMonkPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_confirmation);
		
	/*	getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("OTP Confirmation");

		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.INVISIBLE);

		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		*/
		
		btn_otp = (EditText)findViewById(R.id.etotp);
		regenerateotp = (Button)findViewById(R.id.btn_regenerateotp);
		proceed = (Button)findViewById(R.id.btn_process);
		
		dealMonkPreferences = DealMonkPreferences.getInstance(OtpConfirmationActivity.this);
		userid =dealMonkPreferences.getString("MAIN_USER_ID");
		contact_no =dealMonkPreferences.getString("Contact_Number");
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String address = extras.getString("address");
			String message = extras.getString("message");
			
			StringTokenizer st = new StringTokenizer(message, ",\n");
	        String parsemsg = st.nextToken();
	        String msg = st.nextToken();	        
	        btn_otp.setText(msg);
	        
	        Log.d(parsemsg, msg);
		}
		
		regenerateotp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new RegenerateOTP().execute();
				
			}
		});
		
		proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new proceedwithOTP().execute();				
			}
		});		
	}
	
	//  Async task for regeneration of otp
	
	
	private class RegenerateOTP extends AsyncTask<String, String, String>{
		
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (pDialog == null) {
				pDialog = DealmonkProgressDialog.createProgressDialog(OtpConfirmationActivity.this);
				pDialog.show();
			} else {
				pDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("phone", contact_no);
				jsonObject.put("user_id", userid);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log.d("Sending parameters", jsonObject.toString());
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_REGENERATE_OTP,jsonObject.toString());
			Log.d("resposnse", response);
			return response;
		
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();

			}
			
			if (!TextUtils.isEmpty(result)) {
				
				try {
					JSONObject jsonObject = new JSONObject(result);				
					String message ;				
					if (jsonObject.getBoolean("success")) {
						message = jsonObject.getString("message");				
					}
					
					else {
						message = jsonObject.getString("message");	
					}

					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	private class proceedwithOTP extends AsyncTask<String, String, String>{
		
		ProgressDialog pDialog;
		String Otp_Message = btn_otp.getText().toString();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (pDialog == null) {
				pDialog = DealmonkProgressDialog.createProgressDialog(OtpConfirmationActivity.this);
				pDialog.show();
			} else {
				pDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("otp", Otp_Message);
				jsonObject.put("phone", contact_no);
				jsonObject.put("user_id", userid);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.d("Sending parameters", jsonObject.toString());
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_OTP_CHECK,jsonObject.toString());
			Log.d("response", response);
			return response;		
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			
			if (!TextUtils.isEmpty(result)) {
				
				try {
					JSONObject jsonObject = new JSONObject(result);				
					String message , mobile_number ;				
					if (jsonObject.getBoolean("success")) {
						message = jsonObject.getString("message");
						mobile_number= jsonObject.getString("contact");
						dealMonkPreferences.setString("Contact_Number", mobile_number);	
						Intent landingpage = new Intent(OtpConfirmationActivity.this,MainPage.class);
						landingpage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);						
						startActivity(landingpage);
					}					
					else {
						message = jsonObject.getString("message");
						Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				        	}
					
				             } catch (JSONException e) {
				    	         e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	
	}
