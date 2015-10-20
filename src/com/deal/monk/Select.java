package com.deal.monk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.model.LiveDetailsModel;
import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;

@SuppressWarnings("deprecation")
public class Select extends TabActivity {
	
	double latitude ;
	double longitude;
	private com.deal.monk.utility.GPSTracker gps;
	private String searchLocation,SearchRestaurant,searchCuisine;
	private TabHost tabHost ;
	private ArrayList<LiveDetailsModel> mLiveDetailsModels = new ArrayList<LiveDetailsModel>();
	
	// This is used to check navigation level of the user (i.e. from Search Screen or from RightHereRightNow)
	private boolean isFromRightHereRightNow ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		
		Intent intent = getIntent();
		
		gps = new com.deal.monk.utility.GPSTracker(Select.this);
		
		if (intent !=null) {
			
			setIsFromRightHereRightNow(intent.getBooleanExtra("IsFromRightHereRightnow",false));
			
			if (isFromRightHereRightNow == false) {
			
				searchLocation = (intent.getStringExtra("SEARCH_LOCATION")); 
				SearchRestaurant = (intent.getStringExtra("SEARCH_REST_NAME"));
				searchCuisine = (intent.getStringExtra("SEARCH_CUISINE"));
				
				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();}
				else {
					longitude = 0;
    				latitude = 0;					
				}
				if (com.deal.monk.utility.Utility.getInstance(this).isConnectingToInternet()) {
					new GetRestaurentlist().execute();
				} else {
					
					Toast.makeText(this, "Please check your Internet Connection.", Toast.LENGTH_LONG).show();
				}
			} else {
				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();

					if (com.deal.monk.utility.Utility.getInstance(this).isConnectingToInternet()) {
						new GetRestaurentlist().execute();
					} else {
						
						Toast.makeText(this, "Please check your Internet Connection.", Toast.LENGTH_LONG).show();
					}
					
				} else {
					enableGPSForSearch();
				}
			}
		}

		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		Button back = (Button) findViewById(R.id.button1);
		// create the TabHost that will contain the Tabs
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		head.setText("Live Deals");
		back.setVisibility(View.VISIBLE);
		
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
					tabHost.getTabWidget().getChildAt(i)
							.setBackgroundColor(Color.parseColor("#424242"));
					}

				tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
						.setBackgroundColor(Color.parseColor("#ff0048"));
			}
		});
		
	}
	
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void enableGPSForSearch() {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			dialog.cancel();
            
        			if (getIsFromRightHereRightNow()) {
        				finish();
        			} else {
        				
        				/**
        				 * If user clicks Cancel for enabling GPS which navigating from search screen.
        				 */
        			//	longitude = 0;
        			//	latitude = 0;
        			}
            	
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}
	

	
	
	private class GetRestaurentlist extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			
			if (pDialog == null) {
			       pDialog = DealmonkProgressDialog.createProgressDialog(Select.this);
			       pDialog.show();
			       } else {
			       pDialog.show();
			       }
		//	pDialog = new ProgressDialog(Select.this);
		//	pDialog.setCancelable(false);
		//	pDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			
					
			ServerUtility ut = ServerUtility.getInstance();
//			boolean isFromRightHereRightNow = ((Select)getParent()).getflag();
			String response = "";
			
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			date.setLenient(false);
			Date today = new Date();
			String currentdate = date.format(today);
			
			DateFormat time = new SimpleDateFormat("kk:mm:ss",Locale.getDefault());
			time.setLenient(true);
			Date ctime = new Date();
			String currenttime = time.format(ctime);
			
			if (getIsFromRightHereRightNow()) {
				
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("latitude", latitude);
					jsonObject.put("longitude", longitude);
					jsonObject.put("date",currentdate);
					jsonObject.put("time", currenttime);
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				response = ut.makeHttpPostRequest(DealMonkConstants.URL_RighthereRightNowComplete,jsonObject.toString());
				Log.d("sending parameters are :", jsonObject.toString());
				
				Log.w("Response msg of RightHereRightNow :- ", response);
				
			} else {
				
				JSONObject jsonObject = new JSONObject();
				try {
					
					if (longitude == 0 && latitude == 0) {
					
						jsonObject.put("latitude", "");
						jsonObject.put("longitude", "");
						
					} else {
						
						jsonObject.put("latitude", Double.toString(latitude));
						jsonObject.put("longitude", Double.toString(longitude));
					}
					
					jsonObject.put("Date", currentdate);					
					jsonObject.put("current_time", currenttime);
					jsonObject.put("Cuisine", searchCuisine);
					jsonObject.put("Location", searchLocation);
					jsonObject.put("RestaurantName", SearchRestaurant);
					
										
					
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				
                Log.d("sending  parameters", jsonObject.toString());
				response = ut.makeHttpPostRequest(DealMonkConstants.URL_SearchComplete,jsonObject.toString());
				Log.w("Response msg", response);
			}

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
					
					String TAG_Details = "nameList";
					String TAG_ID = "restaurantId";
					String TAG_NAME = "restaurantname";
					String TAG_LOCATION = "location";
					String TAG_FOODTYPE = "cuisine";
					String TAG_DISTANCE = "distance";
					String TAG_LIVE_DETAILS = "offer";
					String TAG_RESTAURENT_IMG= "image";
					String TAG_LATITUDE = "latitude";
					String TAG_LONGITUDE = "longitude";
					
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArrayContacts = jsonObject.getJSONArray(TAG_Details);

					ArrayList<LiveDetailsModel> liveDetailsModels = new ArrayList<LiveDetailsModel>();
					boolean isDistanceAvailable = false;

					for (int i = 0; i < jsonArrayContacts.length(); i++) {
						JSONObject jsonObjectRestaurentList = jsonArrayContacts.getJSONObject(i);

						String id = jsonObjectRestaurentList.getString(TAG_ID);
						String name = jsonObjectRestaurentList.getString(TAG_NAME);
						String location = jsonObjectRestaurentList.getString(TAG_LOCATION);
						String distance = jsonObjectRestaurentList.getString(TAG_DISTANCE);
						String live_detail = jsonObjectRestaurentList.getString(TAG_LIVE_DETAILS);
						String restaurentUrl = jsonObjectRestaurentList.getString(TAG_RESTAURENT_IMG);
						String latitude = jsonObjectRestaurentList.getString(TAG_LATITUDE);
						String longitude = jsonObjectRestaurentList.getString(TAG_LONGITUDE);

						JSONArray jsonArray = jsonObjectRestaurentList.getJSONArray("cuisine");
						ArrayList<String> cousineTypesList = new ArrayList<String>();

						for (int j = 0; j < jsonArray.length(); j++) {
							if (j <= 2) {
								String cousineType = jsonArray.get(j).toString();
								cousineTypesList.add(cousineType);
							} else {
								break;
							}
						 }
						
						LiveDetailsModel liveDetailsModel = new LiveDetailsModel();
						liveDetailsModel.setId(id);
						liveDetailsModel.setName(name);
						liveDetailsModel.setLocation(location);
						liveDetailsModel.setDistance(distance);
						liveDetailsModel.setLive_detail(live_detail);
						liveDetailsModel.setRestaurentUrl(restaurentUrl);
						liveDetailsModel.setCuisine(cousineTypesList);
						liveDetailsModel.setLatitude(latitude);
						liveDetailsModel.setLongitude(longitude);
						
						if (distance.contains("km")) {
							isDistanceAvailable = true;
						}
						liveDetailsModels.add(liveDetailsModel);
					 }
					
         		
	// Sorting Accending order for distance	
					
					if (isDistanceAvailable) {
					LiveDetailsModel temporary;

	                    for (int c = 0; c < (liveDetailsModels.size() - 1); c++) {
	                        for (int d = 0; d < (liveDetailsModels.size() - c - 1); d++) {

	                      
	                            String firstComparedistanceIs[] =liveDetailsModels.get(d).getDistance().split("km");
	                            String secondComparedistanceIs[] =liveDetailsModels.get(d + 1).getDistance().split("km");

	                            if (Double.parseDouble(firstComparedistanceIs[0]) >
	                                    Double.parseDouble(secondComparedistanceIs[0])){
	                                temporary = liveDetailsModels.get(d);
	                                liveDetailsModels.set(d, liveDetailsModels.get(d + 1));
	                                liveDetailsModels.set(d + 1, temporary);
	                            }
	                        }
	                    }
					}					
					setLiveDetails(liveDetailsModels);
					}
				
				catch (Exception e) {
						e.printStackTrace();
				} 
				
			} else {
				Toast.makeText(Select.this, "Please try after some time.", Toast.LENGTH_LONG).show();
			}
			
			setTabs();			
		}
	}

	
	public ArrayList<LiveDetailsModel> getLiveDetails() {
		return mLiveDetailsModels;
	}

	private void setLiveDetails(ArrayList<LiveDetailsModel> liveDetailsModels) {
		this.mLiveDetailsModels = liveDetailsModels;
	}

	public void setIsFromRightHereRightNow(boolean rightHereRightNow) {
		this.isFromRightHereRightNow = rightHereRightNow;
	}

	public boolean getIsFromRightHereRightNow() {
		return isFromRightHereRightNow;
	}

	/**
	 * This function  is used to set tabs for the Selected function.
	 */
	public void setTabs() {

		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		tab1.setIndicator(null, getResources().getDrawable(R.drawable.list64w));

		Intent intentSelectList = new Intent(Select.this, Select_list.class);
		/*intentSelectList.putExtra("SEARCH_LOCATION", location1);
		intentSelectList.putExtra("SEARCH_TIME", time1);
		intentSelectList.putExtra("SEARCH_CUISINE", cuisine);*/
		tab1.setContent(intentSelectList);

		tab2.setIndicator(null,getResources().getDrawable(R.drawable.navigation64w));
		tab2.setContent(new Intent(Select.this, Select_Map.class));

		tabHost.setBackgroundColor(Color.parseColor("#424242"));

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#424242"));
			tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff0048"));
		}
		/** Add the tabs to the TabHost to display. */
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
}
