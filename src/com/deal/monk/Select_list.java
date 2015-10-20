package com.deal.monk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deal.monk.adapters.SelectList1;
import com.deal.monk.model.LiveDetailsModel;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.R;

public class Select_list extends Activity {

	
	private ListView mListView;
	com.deal.monk.utility.GPSTracker gps;
	double latitude ;
	double longitude;
	String currentdate,currenttime ;	
	String searchlocation , searchtime,searchcuisine ;
	Boolean isfromrighthererightnow = false ;
	String location ;
	String DialogMsg_Nodeal ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_list);
		
		mListView  = (ListView)findViewById(R.id.list);
		
		ArrayList<LiveDetailsModel> detailsModels = ((Select)getParent()).getLiveDetails();
		
		
		if (detailsModels.size() > 0) {
			
			SelectList1 adapter = new SelectList1(Select_list.this, detailsModels);
			mListView.setAdapter(adapter);
			
		} else {
			  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// Yes button clicked
						finish();
						break;					
						
					}
				}
			};
			
			if (((Select)getParent()).getIsFromRightHereRightNow() == true) {
				DialogMsg_Nodeal ="There are no live deals in your area at this moment!";
				
			}
			else {
				DialogMsg_Nodeal ="No matches found!";
				
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(Select_list.this);
			builder.setMessage(DialogMsg_Nodeal)
					.setIcon(R.drawable.ic_launcher).setTitle("Try After Some Time")
					.setPositiveButton("OK", dialogClickListener)
					.show();					
			}
		
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				LiveDetailsModel liveDetailsModel = (LiveDetailsModel)parent.getItemAtPosition(position);
				
				Intent pass = new Intent(Select_list.this, Deal.class);
				DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(Select_list.this);
				dealMonkPreferences.setString("restaurant_id_for_offers", liveDetailsModel.getId());
				dealMonkPreferences.setString("restaurant_latitude", liveDetailsModel.getLatitude());
				dealMonkPreferences.setString("restaurant_longitude", liveDetailsModel.getLongitude());
				
				startActivity(pass);					
			}
		});	

	}

}
