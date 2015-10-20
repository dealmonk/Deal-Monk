package com.deal.monk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.R;

public class CheckIn extends Activity {
	
	TextView txtRestoName,OfferTime,txtOffer,txtCoupon_code,txt_currentdate;
	private com.deal.monk.utility.GPSTracker gps;
	double latitude,longitude;
	Button btn_currentDate,btn_currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_chek_in);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("Deal Pass");
		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		
		 gps = new com.deal.monk.utility.GPSTracker(CheckIn.this);

 		// check if GPS enabled
 		if (gps.canGetLocation()) {
 			latitude = gps.getLatitude();
 			longitude = gps.getLongitude();

 		
 			
 		} else {
 			gps.showSettingsAlert();
 		
 		}
		
 		txtRestoName = (TextView)findViewById(R.id.RestoName_checkin);
		btn_currentDate=(Button) findViewById(R.id.btn_currentDate);
		btn_currentDate.setClickable(false);
		txtOffer = (TextView)findViewById(R.id.txtOffer);
		txtCoupon_code = (TextView)findViewById(R.id.Coupon_code);
		btn_currentTime=(Button) findViewById(R.id.btn_currentTime);
		btn_currentTime.setClickable(false);
		
		Intent intent = getIntent();
		if (intent != null) {
			
			String offerId = intent.getStringExtra("OFFER_ID");
			String restauRantId = intent.getStringExtra("Restaurant_Id");
			String bookingId = intent.getStringExtra("BOOKING_ID");	
			
		
			
			if (com.deal.monk.utility.Utility.getInstance(this).isConnectingToInternet()) {
				new CheckInProcess().execute(offerId,restauRantId,bookingId);
			} else {
				Toast.makeText(this, "Please check your Internet Connection.", Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	public class CheckInProcess extends AsyncTask<String, String, String>{
		ProgressDialog pDialog;
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(CheckIn.this);
			
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			ServerUtility ut = ServerUtility.getInstance();
            String response = "";
            DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(CheckIn.this);
            String MAIN_USER_ID = dealMonkPreferences.getString("MAIN_USER_ID");
            
            
            DateFormat time = new SimpleDateFormat("kk:mm:ss",Locale.getDefault());
			time.setLenient(true);
			Date ctime = new Date();
			String currenttime = time.format(ctime);
           
            
            JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("offer_id", params[0]);								
				jsonObject.put( "restaurantId",params[1]);
				jsonObject.put("booking_id",params[2] );
				jsonObject.put( "user_id",MAIN_USER_ID);
				jsonObject.put( "latitude",latitude);
				jsonObject.put( "longitude",longitude);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			Log.d("Sending Parameters :", jsonObject.toString());
            
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_Check_in,jsonObject.toString());
			Log.w("Response msg for Check In :- ", response);
            
			
			
			return response;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			
			
			DateFormat date = new SimpleDateFormat(" dd MMM yyyy",Locale.getDefault());
			date.setLenient(false);
			Date today = new Date();
			String currentdate = date.format(today);
			
			
                if (!TextUtils.isEmpty(result)) {
                	try {
                	String TAG_Details = "message";
					String TAG_RESTAURANT_NAME = "restaurantname";
					String TAG_OFFER = "offer";
					String TAG_COUPON = "coupon_number";
					String TAG_TIME = "time";
					
					    JSONObject jsonob = new JSONObject(result);
						
					    if (jsonob.getBoolean("success")) {	
							JSONObject jsonObject = jsonob.getJSONObject(TAG_Details);
							String restaurant_name = jsonObject.getString(TAG_RESTAURANT_NAME);
							String restaurant_offer = jsonObject.getString(TAG_OFFER);
							String coupon = jsonObject.getString(TAG_COUPON);
							String time = jsonObject.getString(TAG_TIME);
							
							txtRestoName.setText(restaurant_name);
							txtCoupon_code.setText(coupon);
			                btn_currentDate.setText(currentdate);
							txtOffer.setText(restaurant_offer);
						    btn_currentTime.setText(time);
							
					    }
					    else {
					    	String message = jsonob.getString("message");
					    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
									case DialogInterface.BUTTON_POSITIVE:
										dialog.dismiss();
										finish();
										break;
									}

								}

							};
							
							AlertDialog.Builder builder = new AlertDialog.Builder(
									CheckIn.this);
							builder.setMessage(message)
									.setIcon(R.drawable.ic_launcher)
									.setTitle("Deal Monk")
									.setPositiveButton("OK", dialogClickListener)
									.show();						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
                }
		}
		
	
	}


	
}
