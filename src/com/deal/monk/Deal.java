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
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.fragments.ContactFragment;
import com.deal.monk.fragments.OfferFragment;
import com.deal.monk.fragments.ReviewFragment;
import com.deal.monk.model.OfferListModel;
import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Deal extends FragmentActivity {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private ArrayList<OfferListModel> mOfferlistModels = new ArrayList<OfferListModel>();
	private ArrayList<OfferListModel> Restaurantlistmodel = new ArrayList<OfferListModel>();
	private TextView txtRestaurentName,RestaurantArea;
	private ImageView ImgDealoffer;
	private ImageLoader imageLoader ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("Deal");
		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		txtRestaurentName = (TextView)findViewById(R.id.txtRestaurentName_Deal);
		ImgDealoffer = (ImageView)findViewById(R.id.imgDealoffer_Dealoffer);
		RestaurantArea = (TextView)findViewById(R.id.txtrestaurantArea);
		imageLoader = DealMonkApplication.getImageLoader(this);
	
		// For Asyn Task
		
		if (com.deal.monk.utility.Utility.getInstance(this).isConnectingToInternet()) {
			
			new GetOfferDetails().execute();
			
		} else {
			Toast.makeText(this, "Please check your Internet Connection.", Toast.LENGTH_LONG).show();
		}
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private static TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}
		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);

			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);

			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);

		}

		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);

			mTabHost.getTabWidget().setBackgroundColor(
					Color.parseColor("#ff0048"));

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		public void onPageSelected(int position) {

			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		public void onPageScrollStateChanged(int state) {

		}
	}
	
	
	private class GetOfferDetails extends AsyncTask<String, String, String>{
		
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = DealmonkProgressDialog.createProgressDialog(Deal.this);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(Deal.this);
			String id = dealMonkPreferences.getString("restaurant_id_for_offers");
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
				jsonObject.put("restaurant_id", id);				
				jsonObject.put("date",currentdate);
				jsonObject.put("time", currenttime);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			response = ut.makeHttpPostRequest(DealMonkConstants.URL_Deals_OfferandDetails,jsonObject.toString());
			Log.w("Response msg Restaurant list view  :- ", response);

			return response;
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			

			ArrayList<OfferListModel> offerlistmodel = new ArrayList<OfferListModel>();
			ArrayList<OfferListModel> restaurantdetails = new ArrayList<OfferListModel>();
			
			if (!TextUtils.isEmpty(result)) {
				
				try {
					String TAG_Details = "restaurantOfferList";
					String TAG_LIVE_DETAILS = "restaurant_offer";
					String TAG_START_TIME = "start_time";
					String TAG_OFFER_ID = "offer_id";
					String TAG_END_TIME = "end_time";
					String TAG_OFFER_STATUS = "status";
					String TAG_RESTAURANT_AREA = "restaurant_area";					
					String TAG_RESTAURANT_NAME = "restaurant_name";
					String TAG_RESTAURENT_IMG= "restaurant_image";					
					String TAG_RESTAURANT_ADDRESS2 = "restaurant_address2";
					String TAG_RESTAURANT_ADDRESS1 = "restaurant_address1";
					String TAG_RESTAURANT_DETAILS = "restaurant_contact_detail";
					
					
					JSONObject jsonObject = new JSONObject(result);
					String restaurant_name = jsonObject.getString(TAG_RESTAURANT_NAME);
					String restaurant_address1 = jsonObject.getString(TAG_RESTAURANT_ADDRESS1);
					String restaurant_address2 = jsonObject.getString(TAG_RESTAURANT_ADDRESS2);
					String restaurentUrl = jsonObject.getString(TAG_RESTAURENT_IMG);
					String restaurant_details = jsonObject.getString(TAG_RESTAURANT_DETAILS);
					String restaurant_area = jsonObject.getString(TAG_RESTAURANT_AREA);
					
					
					
					
					OfferListModel restaurantcontact = new OfferListModel();
					restaurantcontact.setRestaurantName(restaurant_name);
					restaurantcontact.setRestaurantAddress1(restaurant_address1);
					restaurantcontact.setRestaurantAddress2(restaurant_address2);
					restaurantcontact.setRestaurantImage(restaurentUrl);
					restaurantcontact.setRestaurantContactDetails(restaurant_details);
					restaurantcontact.setRestaurentArea(restaurant_area);
					
					restaurantdetails.add(restaurantcontact);	
					setRestaurantdetails(restaurantdetails);
					
					txtRestaurentName.setText(restaurant_name);
					imageLoader.displayImage(restaurentUrl, ImgDealoffer);
					RestaurantArea.setText(restaurant_area);
		
					
					JSONArray jsonArraydetails = jsonObject.getJSONArray(TAG_Details);
				
					
					if(jsonArraydetails.length()>0)
					for (int i = 0; i < jsonArraydetails.length(); i++) {
						
						JSONObject jsonObjectRestaurentList = jsonArraydetails.getJSONObject(i);
						
						String live_detail = jsonObjectRestaurentList.getString(TAG_LIVE_DETAILS);
						String start_time=jsonObjectRestaurentList.getString(TAG_START_TIME);
						String id = jsonObjectRestaurentList.getString(TAG_OFFER_ID);
						String end_time =jsonObjectRestaurentList.getString(TAG_END_TIME);
						String offer_status = jsonObjectRestaurentList.getString(TAG_OFFER_STATUS);
						
						OfferListModel offerlist = new OfferListModel();
						offerlist.setRestaurantOffer(live_detail);
						offerlist.setStartTime(start_time);
						offerlist.setOfferId(id);
						offerlist.setStatus(offer_status);						
						offerlist.setEndTime(end_time);
						offerlist.setRestaurentArea(restaurant_area);
						offerlist.setRestaurantName(restaurant_name);
						offerlist.setRestaurantContactDetails(restaurant_details);
						
						
						offerlistmodel.add(offerlist);	
						setLiveDetails(offerlistmodel);
					}
				}
				
				catch (Exception e) {
						e.printStackTrace();
				}
				
			}			
			 else {
					Toast.makeText(Deal.this, "Please try after some time.", Toast.LENGTH_LONG).show();
				}
				
						
			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();

			mViewPager = (ViewPager) findViewById(R.id.pager);
			mTabsAdapter = new TabsAdapter(Deal.this, mTabHost, mViewPager);
			
			mTabsAdapter.addTab(mTabHost.newTabSpec("one").setIndicator(null,getResources().getDrawable(R.drawable.offer64w)),OfferFragment.class, null);
			mTabsAdapter.addTab(mTabHost.newTabSpec("two").setIndicator(null,getResources().getDrawable(R.drawable.review64w)),ReviewFragment.class, null);
			mTabsAdapter.addTab(mTabHost.newTabSpec("three").setIndicator(null,getResources().getDrawable(R.drawable.contact64w)),ContactFragment.class, null);
		}
	
	}
	
	
	
	public ArrayList<OfferListModel> getLiveDetails() {
		return mOfferlistModels;
	}

	private void setLiveDetails(ArrayList<OfferListModel> offerlistmodel) {
		this.mOfferlistModels = offerlistmodel;
	}

	// for reataurant details
	public ArrayList<OfferListModel> getRestaurantdetails() {
		return Restaurantlistmodel;
	}

	private void setRestaurantdetails(
			ArrayList<OfferListModel> restaurantdetails) {
		this.Restaurantlistmodel = restaurantdetails;
	}
	

}
