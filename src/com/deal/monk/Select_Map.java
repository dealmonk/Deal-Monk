package com.deal.monk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.deal.monk.model.LiveDetailsModel;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Select_Map extends Activity implements OnInfoWindowClickListener {

	protected String TAG = getClass().getSimpleName();
	private GoogleMap googleMap;
//	private com.tungsten.dealmonk.utility.GPSTracker gps;
	final int RQS_GooglePlayServices = 1;
	double latitude ;
	double longitude;	
	private String Restaurant_Id ="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select__map);		

		// check for google play service
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Select_Map.this);
		if (resultCode == ConnectionResult.SUCCESS) {
				
			// MAP INITILIZE
			initilizeMap();
			
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,RQS_GooglePlayServices).show();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * @param <liveDetailsModels>
	 * */
	private <liveDetailsModels> void initilizeMap() {

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}

		// LatLng latLng = new LatLng(lati, longi);
		GPSTracker gps = new com.deal.monk.utility.GPSTracker(Select_Map.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// Instantiates a new CircleOptions object and defines the center
			// and radius

			CircleOptions circleOptions = (new CircleOptions()
					.center(new LatLng(latitude, longitude)).radius(5000) // In	// meters
					// .fillColor(Color.GREEN)
					.fillColor(0x1141acdc)
			// .strokeColor(Color.GREEN));
					.strokeColor(0xFF41acdc)).strokeWidth(2);
			// Get back the mutable Circle, not sure what this means yet
			googleMap.addCircle(circleOptions);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(12).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
							
				ArrayList<LiveDetailsModel> liveDetailsModels = ((Select)getParent()).getLiveDetails();
				final Map<Marker, LiveDetailsModel> hashMap = new HashMap<>();
				
				for (int i = 0; i < liveDetailsModels.size(); i++) {
					
					MarkerOptions markerOptions = new MarkerOptions()
					.position(new LatLng(Double.parseDouble(liveDetailsModels.get(i).getLatitude()), 
							Double.parseDouble(liveDetailsModels.get(i).getLongitude())))
					.title(liveDetailsModels.get(i).getName())
					.snippet(liveDetailsModels.get(i).getLive_detail());

					Marker marker = googleMap.addMarker(markerOptions);
					hashMap.put(marker, liveDetailsModels.get(i));
					
					Restaurant_Id  =liveDetailsModels.get(i).getId();
					
					/*DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(Select_Map.this);
					dealMonkPreferences.setString("restaurant_latitude", liveDetailsModels.get(i).getLatitude());
					dealMonkPreferences.setString("restaurant_longitude", liveDetailsModels.get(i).getLongitude());*/
				}
				
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						
		//				LiveDetailsModel liveDetailsModel = (LiveDetailsModel)hashMap.get(marker);
						//String id = liveDetailsModel.getId();
						
					//	Log.d("id is ", id);
						
//						String id = marker.getTitle();				
						Intent pass = new Intent(Select_Map.this, Deal.class);
						DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(Select_Map.this);
						dealMonkPreferences.setString("restaurant_id_for_offers", Restaurant_Id);
				//		dealMonkPreferences.setString("restaurant_latitude", liveDetailsModel.getLatitude());
				//		dealMonkPreferences.setString("restaurant_longitude", liveDetailsModel.getLongitude());
						startActivity(pass);

					}
				});
			

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		
	}

	
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// check for google play service
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			initilizeMap();
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,RQS_GooglePlayServices).show();
		}

	}
	
	
}