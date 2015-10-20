package com.deal.monk;

import com.deal.monk.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Deal_screen extends Activity {

	Button btnCheckIn_Mydeal, btnCall_Mydeal, btnCancle_Mydeal;
	TextView NameOfRestaurent,Status,CouponCode,RestaurentArea,Time,Offer;
	
	String 	getrestaurantname,getstatus,getcouponcode,getrestaurantarea,
	getstarttime,getendtime,getoffername,getrestaurantid,getofferid,getcontact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_screen);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("My Deal");
		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		btnCheckIn_Mydeal = (Button) findViewById(R.id.btnCheckIn_Mydeal);
		btnCall_Mydeal = (Button) findViewById(R.id.btnCall_Mydeal);
		btnCancle_Mydeal = (Button) findViewById(R.id.btnCancle_Mydeal);
	//	String data = getIntent().getExtras().getString("dealname", "dealname");
		NameOfRestaurent = (TextView) findViewById(R.id.txtNameOfRestaurent_Mydeal);
		Status=(TextView) findViewById(R.id.txtStatus_Mydeal);
		CouponCode=(TextView) findViewById(R.id.txtCoupon_Mydeal);
		RestaurentArea=(TextView) findViewById(R.id.txtRestaurantLocation_Mydeal);
		Time=(TextView) findViewById(R.id.txtTime_Mydeal);
		Offer=(TextView) findViewById(R.id.txtOffer_Mydeal);
		
			
		
		// Get Intents From Deal tabs(Upcoming and all)
		
		Intent intent = getIntent();
		
		getrestaurantname = intent.getStringExtra("getrestaurantname");
		getstatus=intent.getStringExtra("getstatus");
		getcouponcode=intent.getStringExtra("getcouponcode");
		getrestaurantarea=intent.getStringExtra("getrestaurantarea");
		getstarttime=intent.getStringExtra("getstarttime");
		getendtime=intent.getStringExtra("getendtime");
		getoffername=intent.getStringExtra("getoffername");
		getrestaurantid=intent.getStringExtra("getrestaurantid");
		getofferid=intent.getStringExtra("getofferid");
		getcontact= intent.getStringExtra("getrestaurantcontact");

		
		NameOfRestaurent.setText(getrestaurantname);
		Status.setText(getstatus);
		CouponCode.setText(getcouponcode);
		RestaurentArea.setText(getrestaurantarea);
		Time.setText(getstarttime+" to "+getendtime);
		Offer.setText(getoffername);
		
		//check for Status 
		btnCancle_Mydeal.setEnabled(false);
		btnCancle_Mydeal.setClickable(false);
		
        if (getstatus.equalsIgnoreCase("Fulfilled") ) {
        	btnCall_Mydeal.setEnabled(false);
			btnCall_Mydeal.setClickable(false);
			btnCheckIn_Mydeal.setClickable(false);
			btnCheckIn_Mydeal.setEnabled(false);
		}
		else if (getstatus.equalsIgnoreCase("No Show")) {
			btnCall_Mydeal.setEnabled(false);
			btnCall_Mydeal.setClickable(false);
			btnCheckIn_Mydeal.setClickable(false);
			btnCheckIn_Mydeal.setEnabled(false);
			
		}
		else if (getstatus.equalsIgnoreCase("Confirmed")) {
			
			btnCall_Mydeal.setEnabled(true);
			btnCall_Mydeal.setClickable(true);
			btnCheckIn_Mydeal.setClickable(true);
			btnCheckIn_Mydeal.setEnabled(true);
			
			
		}
		
		
		btnCheckIn_Mydeal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent checkin = new Intent(Deal_screen.this,CheckIn.class);
				checkin.putExtra("Restaurant_Id", getrestaurantid );
				checkin.putExtra("OFFER_ID",getofferid);
				
			
				
				startActivity(checkin);

			}
		});

		btnCall_Mydeal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(Deal_screen.this);
				builder.setMessage("Would you like to make a Call ?").setCancelable(false).setPositiveButton("Call",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										Intent callintent = new Intent(Intent.ACTION_CALL);
										callintent.setData(Uri.parse("tel:" +0+ getcontact));
										startActivity(callintent);
										finish();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
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

		btnCancle_Mydeal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent back = new Intent(Deal_screen.this, MainPage.class);
				startActivity(back);

			}
		});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

}
