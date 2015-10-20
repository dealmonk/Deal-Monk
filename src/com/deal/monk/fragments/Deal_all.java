package com.deal.monk.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.R.id;
import com.deal.monk.R.layout;
import com.deal.monk.adapters.Deal_items_upcoming_adapter;
import com.deal.monk.model.MyDealModel;
import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.Deal_screen;
import com.deal.monk.R;

public class Deal_all extends Fragment {
	ListView list;
	TextView hidetext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.deal_all, container, false);
		list = (ListView) rootView.findViewById(R.id.mydeallist_all);
		hidetext=(TextView) rootView.findViewById(R.id.hidetext);		
		new GetOfferDetails().execute();		
		return rootView;
	}

	public class GetOfferDetails extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(getActivity());
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
			String MAIN_USER_ID = dealMonkPreferences.getString("MAIN_USER_ID");
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			date.setLenient(false);
			Date today = new Date();
			String currentdate = date.format(today);
			
			ServerUtility ut = ServerUtility.getInstance();
			String response = "";
			JSONObject jsonObject = new JSONObject();						
			try {
				jsonObject.put("user_id", MAIN_USER_ID);
				jsonObject.put("date", currentdate);

			} catch (Exception e) {
			}
			response = ut.makeHttpPostRequest(DealMonkConstants.URL_All_deal,jsonObject.toString());
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

					String TAG_DETAILS = "nameList";
					String TAG_STATUS = "status";
					String TAG_OFFER_ENDTIME = "offerEndTime";
					String TAG_RESTAURENT_ID = "restaurantId";
					String TAG_OFFER_START_TIME = "offerStartTime";
					String TAG_OFFER_RESTAURENTAREA = "restaurantArea";
					String TAG_RESTAURENT_NAME = "restaurantName";
					String TAG_OFFER_RESTAURENTIMAGE = "restaurantImage";
					String TAG_OFFER_COUPONCODE = "couponCode";
					String TAG_OFFER_OFFERID = "offerID";
					String TAG_OFFER_CONTACT = "contact";
					String TAG_OFFER_BOOKINGDATE = "bookingDate";
					String TAG_OFFER_OFFERNAME = "offerName";

					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray(TAG_DETAILS);

					ArrayList<MyDealModel> myDealModels = new ArrayList<MyDealModel>();

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObjectUpcomingList = jsonArray.getJSONObject(i);

						String status = jsonObjectUpcomingList.getString(TAG_STATUS);
						String offerEndTime = jsonObjectUpcomingList.getString(TAG_OFFER_ENDTIME);
						String restaurentId = jsonObjectUpcomingList.getString(TAG_RESTAURENT_ID);
						String offerStartTime = jsonObjectUpcomingList.getString(TAG_OFFER_START_TIME);
						String restaurentArea = jsonObjectUpcomingList.getString(TAG_OFFER_RESTAURENTAREA);
						String restaurentName = jsonObjectUpcomingList.getString(TAG_RESTAURENT_NAME);
						String restaurentImage = jsonObjectUpcomingList.getString(TAG_OFFER_RESTAURENTIMAGE);
						String couponCode = jsonObjectUpcomingList.getString(TAG_OFFER_COUPONCODE);
						String offerId = jsonObjectUpcomingList.getString(TAG_OFFER_OFFERID);
						String contact = jsonObjectUpcomingList.getString(TAG_OFFER_CONTACT);
						String bookingDate = jsonObjectUpcomingList.getString(TAG_OFFER_BOOKINGDATE);
						String offerName = jsonObjectUpcomingList.getString(TAG_OFFER_OFFERNAME);

						MyDealModel myDealModel = new MyDealModel();

						myDealModel.setStatus(status);
						myDealModel.setOfferEndTime(offerEndTime);
						myDealModel.setRestaurenrId(restaurentId);
						myDealModel.setOfferStartTime(offerStartTime);
						myDealModel.setRestaurentArea(restaurentArea);
						myDealModel.setRestaurentName(restaurentName);
						myDealModel.setRestaurentImage(restaurentImage);
						myDealModel.setCouponCode(couponCode);
						myDealModel.setOfferId(offerId);
						myDealModel.setContact(contact);
						myDealModel.setBookingDate(bookingDate);
						myDealModel.setOfferName(offerName);

						myDealModels.add(myDealModel);
						
						if (myDealModels.size() > 0) {
							Deal_items_upcoming_adapter adapter = new Deal_items_upcoming_adapter(getActivity(), myDealModels);
							list.setAdapter(adapter);
							Log.d("value message", "value");
						}else{
							list.setVisibility(View.GONE);							
							hidetext.setVisibility(View.VISIBLE);
							hidetext.setText("No Deals Available");
							Log.d("no value message", "null value");
						}
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						MyDealModel dealModel = (MyDealModel) parent.getItemAtPosition(position);						
						Intent  next = new Intent(getActivity(),Deal_screen.class);
						next.putExtra("getrestaurantname", dealModel.getRestaurentName());
						next.putExtra("getstatus", dealModel.getStatus());
						next.putExtra("getcouponcode", dealModel.getCouponCode());
						next.putExtra("getrestaurantarea", dealModel.getRestaurentArea());
						next.putExtra("getstarttime", dealModel.getOfferStartTime());
						next.putExtra("getendtime", dealModel.getOfferEndTime());
						next.putExtra("getoffername", dealModel.getOfferName());
						next.putExtra("getrestaurantid", dealModel.getRestaurenrId());
						next.putExtra("getofferid", dealModel.getOfferId());
						next.putExtra("getrestaurantcontact", dealModel.getContact());
						
						startActivity(next);
						
					}
				});
			}
		}
	}
}
