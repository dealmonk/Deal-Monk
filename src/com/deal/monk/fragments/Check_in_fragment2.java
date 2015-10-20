package com.deal.monk.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.deal.monk.CheckIn;
import com.deal.monk.model.AutoCheckIn;
import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.R;

public class Check_in_fragment2 extends android.support.v4.app.Fragment {

	private Button btnautogps, btncallus;
	private ImageButton i1;
	private  AlertDialog alertDialog;	  
	private com.deal.monk.utility.GPSTracker gps;
    double latitude,longitude;
    private ArrayList<AutoCheckIn> autoCheckIns = new ArrayList<>();;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.check_in_fragment2, container,
				false);
		
		    btnautogps = (Button) view.findViewById(R.id.button1);
	        btncallus = (Button) view.findViewById(R.id.button2);
		    i1 = (ImageButton) view.findViewById(R.id.imageButton1);
		
		 gps = new com.deal.monk.utility.GPSTracker(getActivity());

			// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	latitude = gps.getLatitude();
	        	longitude = gps.getLongitude();
	        	
	        	// For status check
	    		new AutoGpsStatusCheck().execute();
	      
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        	btnautogps.setEnabled(false);				        
		        btnautogps.setClickable(false);
		        btnautogps.setTextColor(Color.parseColor("#969696"));
		        btnautogps.setOnClickListener(null);
	        }
		
		
	      

		i1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Check_in_Fragment s1 = new Check_in_Fragment();
				android.support.v4.app.FragmentManager fm = getFragmentManager();
				android.support.v4.app.FragmentTransaction ft = fm
						.beginTransaction();
				ft.replace(R.id.My_Container_3_ID, s1);
				ft.commit();

			}
		});
		
		
		
		
		
		// for button call 
		btncallus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Would you like to make a Call ?").setCancelable(false).setPositiveButton("Call",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										Intent callintent = new Intent(Intent.ACTION_CALL);
										callintent.setData(Uri.parse("tel:" +0+9899479344l));
										startActivity(callintent);
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
				alert.setTitle("Make A Call to Deal Monk");
				alert.show();
				
				
			}
		});
		
		btnautogps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			        if (autoCheckIns.size() > 1) {
						
			        	AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
				        builderSingle.setIcon(R.drawable.ic_launcher);
				        builderSingle.setTitle("Select Your Offer :");
				        
				        ArrayList<String> strings = new ArrayList<>();
				        
				        for (int i = 0; i < autoCheckIns.size(); i++) {
				        	
				        	AutoCheckIn autoCheckIn = autoCheckIns.get(i);
				        	if (autoCheckIn != null) {
				        		strings.add(autoCheckIn.getRestaurant_name());
							}
							
						}
				        
				        final CharSequence offers [] = strings.toArray(new CharSequence[strings.size()]);
				        
				        builderSingle.setSingleChoiceItems(offers, 0, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								Intent autochekin = new Intent (getActivity(),CheckIn.class);
								autochekin.putExtra("OFFER_ID", autoCheckIns.get(which).getOfferId());
								autochekin.putExtra("BOOKING_ID", autoCheckIns.get(which).getBookingId());
								autochekin.putExtra("Restaurant_Id", autoCheckIns.get(which).getRestaurantId());
								startActivity(autochekin);
							}
						});
				        
				        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							
								alertDialog.dismiss();
							}
						});
			        	
			        	alertDialog = builderSingle.create();
				        alertDialog.show();
			        	
					} else if (autoCheckIns.size() == 1) {
						Intent autochekin = new Intent (getActivity(),CheckIn.class);
						autochekin.putExtra("OFFER_ID", autoCheckIns.get(0).getOfferId());
						autochekin.putExtra("BOOKING_ID", autoCheckIns.get(0).getBookingId());
						autochekin.putExtra("Restaurant_Id", autoCheckIns.get(0).getRestaurantId());
						startActivity(autochekin);
					}
				}
				
				
				
			});
				
		return view;
	}
	
	public class AutoGpsStatusCheck extends AsyncTask<String, String, String>{
		
        ProgressDialog pDialog;
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(getActivity());			
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ServerUtility ut = ServerUtility.getInstance();
            String response = "";
            DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
            String MAIN_USER_ID = dealMonkPreferences.getString("MAIN_USER_ID");
            
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
				jsonObject.put("user_id", MAIN_USER_ID);								
				jsonObject.put( "date",currentdate);
				jsonObject.put( "time",currenttime);
				jsonObject.put( "latitude",latitude);
				jsonObject.put( "longitude",longitude);	
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
            
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_AUTOGPS_CHEKIN,jsonObject.toString());
			
			return response;
		}
		
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
		
		 if (!TextUtils.isEmpty(result)) {
         	try {
				
				    JSONObject jsonob = new JSONObject(result);
					
				    if (jsonob.getBoolean("success")) {	
					
				    	JSONArray jsonArray = jsonob.getJSONArray("data");
				    	
				    	for (int i = 0; i < 4; i++) {
							
				    		JSONObject jsonObject = jsonArray.getJSONObject(i); 
				    		if (jsonObject != null) {
				    			AutoCheckIn autoCheckIn = new AutoCheckIn();
						    	autoCheckIn.setBookingId(jsonObject.getString("booking_id"));
						    	autoCheckIn.setOfferId(jsonObject.getString("offer_id"));
						    	autoCheckIn.setRestaurant_name(jsonObject.getString("restaurant_name"));
						    	autoCheckIn.setRestaurantId(jsonObject.getString("restaurant_id"));

						    	autoCheckIns.add(autoCheckIn);
							}
				    		
						}
				    	
				    }
				    else {
				    	String message = jsonob.getString("data");
				    	btnautogps.setEnabled(false);				        
				        btnautogps.setClickable(false);
				        btnautogps.setTextColor(Color.parseColor("#969696"));
				        btnautogps.setOnClickListener(null);
				       
				        
				    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									dialog.dismiss();
									
									break;
								}

							}

						};
						
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setMessage(message)
								.setIcon(R.drawable.ic_launcher)
								.setTitle("Deal Monk")
								.setPositiveButton("OK", dialogClickListener)
								.show();				    }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
         }
	}
	



		
		
	}
	
	}


