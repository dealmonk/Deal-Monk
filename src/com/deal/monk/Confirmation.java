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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;

public class Confirmation extends Activity {

	private TextView txtCounterDinner, txtTime_Confirm, txtCalender_Confirm,
			txtRestaurantName, txtRestaurantLocation, txtOfffertime, txtOffer,
			txtCalender ,StatusNote;
	private Button btnConfirm, btnCheckIn_Confirm,btnCall_Confirm, btnDirection_Confirm;
	private String MAIN_USER_ID, Offerid, Restaurant_Id, restautantLocation,
			restaurentName,restaurant_number, OfferValidity, Offer, BookingId, Covers,Status;
	
	private ImageView  btnMinus, btnPlus;
	
	private double latitude,longitude ;

	private int counter = 2;
	
	String Confirmation_msg ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);
		
		StatusNote = (TextView)findViewById(R.id.thankNote_confirm);

		// Tittle Bar Function
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("Confirmation");

		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// Check for user name is null or not
		DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(Confirmation.this);
		MAIN_USER_ID = dealMonkPreferences.getString("MAIN_USER_ID");
		Restaurant_Id = dealMonkPreferences.getString("restaurant_id_for_offers");
		String lati = dealMonkPreferences.getString("restaurant_latitude");
		String longi = dealMonkPreferences.getString("restaurant_longitude");
		
		latitude = Double.parseDouble(lati);
		longitude = Double.parseDouble(longi);
		

		// Call for user name is null or not
		UserIDCheck();

		txtCounterDinner = (TextView) findViewById(R.id.txtCounterDinner);
		// txtCalender_Confirm = (TextView)
		// findViewById(R.id.txtCalender_Confirm);
		btnMinus = (ImageView) findViewById(R.id.btnMinus);
		btnPlus = (ImageView) findViewById(R.id.btnPlus);
		btnConfirm = (Button) findViewById(R.id.btnConfirm_Confirm);
		btnCheckIn_Confirm = (Button) findViewById(R.id.btnCheckIn_Confirm);
		btnCall_Confirm = (Button) findViewById(R.id.btnCall_Confirm);
		btnDirection_Confirm = (Button) findViewById(R.id.btnDirection_Confirm);

		// textview reference
		txtRestaurantName = (TextView) findViewById(R.id.txtRestaurantName);
		txtRestaurantLocation = (TextView) findViewById(R.id.txtRestaurantLocation);
		txtOfffertime = (TextView) findViewById(R.id.txtOfffertime);
		txtOffer = (TextView) findViewById(R.id.txtOffer);
		txtCalender = (TextView) findViewById(R.id.txtCalender_Confirm);

		Intent intent = getIntent();
		if (intent != null) {
			restaurentName = intent.getStringExtra("Restautant_Name");
			Offerid = intent.getStringExtra("Offer_id");
			restautantLocation = intent.getStringExtra("Restautant_location");
			OfferValidity = intent.getStringExtra("Offer_Validity");
			Offer = intent.getStringExtra("Offer");
			restaurant_number =intent.getStringExtra("Restaurant_Number");
			

			// Current date with day of week ,month of year
			DateFormat date = new SimpleDateFormat("EEEE, dd MMMM yyyy",Locale.getDefault());
			date.setLenient(false);
			Date today = new Date();
			String currentdate = date.format(today);

			txtRestaurantName.setText(restaurentName);
			txtRestaurantLocation.setText(restautantLocation);
			txtCalender.setText(currentdate);
			txtOfffertime.setText("Offer Validity :" + OfferValidity);
			txtOffer.setText(Offer);
		}
		// Counter Buttons
		btnMinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (counter > 1) {
					counter = Integer.parseInt((String) txtCounterDinner
							.getText());
					counter = counter - 1;
					txtCounterDinner.setText("" + counter);
				}

			}
		});

		btnPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				counter = Integer.parseInt((String) txtCounterDinner.getText());
				counter++;
				txtCounterDinner.setText("" + counter);

			}
		});

		btnConfirm.setOnClickListener(new View.OnClickListener() {
		

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Confirmrequest().execute();}
		});

		btnCheckIn_Confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent checkin = new Intent(Confirmation.this, CheckIn.class);
				checkin.putExtra("Restaurant_Id", Restaurant_Id);
				checkin.putExtra("OFFER_ID", Offerid);
				checkin.putExtra("BOOKING_ID", BookingId);
				//checkin.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));				
				startActivity(checkin);

			}
		});
		
		
		btnDirection_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				  String urlAddress = "http://maps.google.com/maps?q="+ latitude +"," + longitude +"("+ restaurentName + ")&iwloc=A&hl=es";     
				  Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
				  startActivity(maps);
				
			}
		});
		
		btnCall_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation.this);
				builder.setMessage("Would you like to make a Call ?").setCancelable(false).setPositiveButton("Call",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										Intent callintent = new Intent(Intent.ACTION_CALL);
										callintent.setData(Uri.parse("tel:" + 0+restaurant_number));
										startActivity(callintent);
										finish();
									}
								})
						.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// Action for 'NO' Button
										dialog.cancel();
									}
								});

				// Creating dialog box
				AlertDialog alert = builder.create();
				// Setting the title manually
				alert.setTitle("Make A Call");
				alert.show();				
			}
		});

	}
	// -----------------Confirm Click AsyncTask For sending request

	private void UserIDCheck() {
		if (MAIN_USER_ID == null) {

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// Yes button clicked
						Intent login = new Intent(Confirmation.this,Login.class);
						login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);						
						startActivity(login);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						// No button clicked
						if (com.deal.monk.utility.Utility.getInstance(Confirmation.this).isConnectingToInternet()) {
							new Statuscheck().execute();

						} else {
							Toast.makeText(Confirmation.this, "Please check your Internet Connection.",Toast.LENGTH_LONG).show();
						}

						// Disable Button
						btnConfirm.setEnabled(false);
						btnMinus.setEnabled(false);
						btnPlus.setEnabled(false);
						btnCheckIn_Confirm.setEnabled(false);
						btnCall_Confirm.setEnabled(false);
						btnDirection_Confirm.setEnabled(false);

						// Disable Clickable
						btnConfirm.setClickable(false);
						btnMinus.setClickable(false);
						btnPlus.setClickable(false);
						btnCheckIn_Confirm.setClickable(false);
						btnCall_Confirm.setClickable(false);
						btnDirection_Confirm.setClickable(false);

						// Set Text color to Grey
						btnConfirm.setTextColor(Color.parseColor("#868686"));
						btnCheckIn_Confirm.setTextColor(Color.parseColor("#868686"));
						btnCall_Confirm.setTextColor(Color.parseColor("#868686"));
						btnDirection_Confirm.setTextColor(Color.parseColor("#868686"));

						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation.this);
			builder.setMessage("Please login first to check-in")
					.setIcon(R.drawable.ic_launcher).setTitle("Please Login")
					.setPositiveButton("OK", dialogClickListener)
					.setNegativeButton("Cancel", dialogClickListener).show();
		}

		else {
			// btnConfirm.setEnabled(true);
			// btnConfirm.setClickable(true);
		}
	}
	@Override
	protected void onResume() {
	super.onResume();
	new Statuscheck().execute();
	}
	public class Confirmrequest extends AsyncTask<String, String, String> {

		ProgressDialog pDialog;
		
		String Confirm_Tittle , Confirm_msg ;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(Confirmation.this);		
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";

			DateFormat date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			date.setLenient(false);
			Date today = new Date();
			String currentdate = date.format(today);
			
			DateFormat time = new SimpleDateFormat("kk:mm:ss",Locale.getDefault());
			time.setLenient(true);
			Date ctime = new Date();
			String currenttime = time.format(ctime);

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("booking_date", currentdate);
				jsonObject.put("booking_time", currenttime);
				jsonObject.put("covers", counter);
				jsonObject.put("offer_id", Offerid);
				jsonObject.put("restaurantId", Restaurant_Id);
				jsonObject.put("user_id", MAIN_USER_ID);
				
				} catch (JSONException e1) {
				e1.printStackTrace();
			}

			response = ut.makeHttpPostRequest(DealMonkConstants.URL_Confirmation_page_book_table,
					jsonObject.toString());
			Log.d("sending parameters :", jsonObject.toString());
			Log.w("Response msg for Booking confirmation :- ", response);

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
					String msg = jsonObject.getString("success");

					if (jsonObject.getBoolean("success")) {
						
						StatusNote.setText("Thank you for deal confirmation!");
						Confirm_msg = "CHECK- IN on reaching the restaurant to get your DEAL PASS."
										+ "\n"	+ "You can also Check-in from the Home page";
						Confirm_Tittle = "Cheers!" + "\n" + "Your Deal is Confirmed!";

						// Disable Button
						btnConfirm.setEnabled(false);
						btnMinus.setEnabled(false);
						btnPlus.setEnabled(false);
						btnCheckIn_Confirm.setEnabled(true);
						btnCall_Confirm.setEnabled(true);
						btnDirection_Confirm.setEnabled(true);

						// Disable Clickable
						btnConfirm.setClickable(false);
						btnMinus.setClickable(false);
						btnPlus.setClickable(false);
						btnCheckIn_Confirm.setClickable(true);
						btnCall_Confirm.setClickable(true);
						btnDirection_Confirm.setClickable(true);

						// Set Text color to Grey
						btnConfirm.setBackgroundColor(Color.parseColor("#d9d9d9"));				
						btnCheckIn_Confirm.setTextColor(Color.parseColor("#ffffff"));
						btnCall_Confirm.setTextColor(Color.parseColor("#ffffff"));
						btnDirection_Confirm.setTextColor(Color.parseColor("#ffffff"));

					} else {
						
						Confirm_msg = "Oops! something went wrong.";
				        Confirm_Tittle = "Please try again..";
						
						// Disable Button
						btnConfirm.setEnabled(true);
						btnMinus.setEnabled(true);
						btnPlus.setEnabled(true);
						btnCheckIn_Confirm.setEnabled(false);
						btnCall_Confirm.setEnabled(false);
						btnDirection_Confirm.setEnabled(false);

						// Disable Clickable
						btnConfirm.setClickable(true);
						btnMinus.setClickable(true);
						btnPlus.setClickable(true);
						btnCheckIn_Confirm.setClickable(false);
						btnCall_Confirm.setClickable(false);
						btnDirection_Confirm.setClickable(false);

						// Set Text color to Grey
						btnConfirm.setTextColor(Color.parseColor("#ffffff"));
						btnCheckIn_Confirm.setTextColor(Color.parseColor("#868686"));
						btnCall_Confirm.setTextColor(Color.parseColor("#868686"));
						btnDirection_Confirm.setTextColor(Color.parseColor("#868686"));
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(Confirmation.this,"Please try after some time.", Toast.LENGTH_LONG)
						.show();
			}
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						
						break;
					}

				}

			};

			AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation.this);
			builder.setMessage(Confirm_msg)
					.setIcon(R.drawable.ic_launcher)
					.setTitle(Confirm_Tittle)
					.setPositiveButton("OK", dialogClickListener).show();
				}

	}

	// ---------------Asynctask for status check

	public class Statuscheck extends AsyncTask<String, String, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(Confirmation.this);			
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("offer_id", Offerid);
				jsonObject.put("restaurantId", Restaurant_Id);
				jsonObject.put("user_id", MAIN_USER_ID);

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			Log.d("Status :", jsonObject.toString());
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_Confirmation_page_status_check,jsonObject.toString());
			Log.w("Response for Checkin :- ", response);

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
					String msg = jsonObject.getString("success");

					if (jsonObject.getBoolean("success")) {

						BookingId = jsonObject.getString("booking_id");
						Covers = jsonObject.getString("dinner");
						
						String Status_check = jsonObject.getString("status");
//  ==========================================================================================================================  //						
						
						if (Status_check.equalsIgnoreCase("Already booked")) {
							
						}
						
						else if (Status_check.equalsIgnoreCase("Fulfilled")) {
							StatusNote.setText("You have already checked in!");
							
						}
						else if (Status_check.equalsIgnoreCase("Confirmed")) {
							StatusNote.setText("You have already booked this offer!");
							
						}

						// iF Already Booked then , Disable plus ,minus and
						// confirm and
						// Enable Checkin, Call , Directions

						// Disable Button
						btnConfirm.setEnabled(false);
						btnMinus.setEnabled(false);
						btnPlus.setEnabled(false);
						btnCheckIn_Confirm.setEnabled(true);
						btnCall_Confirm.setEnabled(true);
						btnDirection_Confirm.setEnabled(true);

						// Disable Clickable
						btnConfirm.setClickable(false);
						btnMinus.setClickable(false);
						btnPlus.setClickable(false);
						btnCheckIn_Confirm.setClickable(true);
						btnCall_Confirm.setClickable(true);
						btnDirection_Confirm.setClickable(true);

						// Set Text color to Grey
						btnConfirm.setTextColor(Color.parseColor("#ffffff"));
						btnConfirm.setBackgroundColor(Color.parseColor("#d9d9d9"));
						btnCheckIn_Confirm.setTextColor(Color.parseColor("#ffffff"));
						btnCall_Confirm.setTextColor(Color.parseColor("#ffffff"));
						btnDirection_Confirm.setTextColor(Color.parseColor("#ffffff"));

					} else {

						// If Not Booked Enable plus,minus,confirm and Disable
						// Checkin , call and directions

						// Disable Button
						btnConfirm.setEnabled(true);
						btnMinus.setEnabled(true);
						btnPlus.setEnabled(true);
						btnCheckIn_Confirm.setEnabled(false);
						btnCall_Confirm.setEnabled(false);
						btnDirection_Confirm.setEnabled(false);

						// Disable Clickable
						btnConfirm.setClickable(true);
						btnMinus.setClickable(true);
						btnPlus.setClickable(true);
						btnCheckIn_Confirm.setClickable(false);
						btnCall_Confirm.setClickable(false);
						btnDirection_Confirm.setClickable(false);

						// Set Text color to Grey
						btnConfirm.setTextColor(Color.parseColor("#ffffff"));
						btnCheckIn_Confirm.setTextColor(Color.parseColor("#868686"));
						btnCall_Confirm.setTextColor(Color.parseColor("#868686"));
						btnDirection_Confirm.setTextColor(Color.parseColor("#868686"));

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(Confirmation.this,
						"Please try after some time.", Toast.LENGTH_LONG).show();
			}

		}

	}
}
