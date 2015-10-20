package com.deal.monk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class OtpActivity extends Activity {
	
	EditText numberfield ;
	Button process ;
	private DealMonkPreferences dealMonkPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp);
		// Tittle Bar Function
			/*	getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
				getActionBar().setCustomView(R.layout.action_bar_tittle);
				TextView head = (TextView) findViewById(R.id.actionbar_textview);
				head.setText("Mobile Number Registration");

				Button back = (Button) findViewById(R.id.button1);
				back.setVisibility(View.INVISIBLE);

				back.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});*/
				
			numberfield = (EditText)findViewById(R.id.etnumberfield);
			process = (Button)findViewById(R.id.btnprocess);
			dealMonkPreferences = DealMonkPreferences.getInstance(OtpActivity.this);
			
			
			process.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					String mobileno = numberfield.getText().toString();
					 
				    if(mobileno.length() > 9) 
				    { 				    	
				    	dealMonkPreferences.setString("Contact_Number", mobileno);						
						new sendcontactnumber().execute();
				    	}
				    else {
				    	Toast.makeText(OtpActivity.this, "please enter 10 digits valid phone number", Toast.LENGTH_SHORT).show();
					}
				}
			});
	    }
	
	private class sendcontactnumber extends AsyncTask<String, String, String>{
		
		String mobile = numberfield.getText().toString();
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog == null) {
			       pDialog = DealmonkProgressDialog.createProgressDialog(OtpActivity.this);
			       pDialog.show();
			       } else {
			       pDialog.show();
			       }
		}

		@Override
		protected String doInBackground(String... arg0) {

			String userid =dealMonkPreferences.getString("MAIN_USER_ID");
			
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("phone", mobile);
				jsonObject.put("user_id", userid);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			Log.d("Sending parameters", jsonObject.toString());
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_REQUEST_OTP,jsonObject.toString());
			Log.d("response", response);
			return response;
		}	
		
	@Override
	protected void onPostExecute(String result) {
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
					Intent next = new Intent(OtpActivity.this,OtpConfirmationActivity.class);
                    startActivity(next);
				}
				
				else {
					message = jsonObject.getString("message");	
				}
				
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
  }
	
	
	
}

	

	

