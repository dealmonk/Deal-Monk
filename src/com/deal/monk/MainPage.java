package com.deal.monk;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.adapters.NavDrawerListAdapter;
import com.deal.monk.fragments.Brownie_points;
import com.deal.monk.fragments.Invite_Friend;
import com.deal.monk.fragments.Profile;
import com.deal.monk.fragments.Settings_page;
import com.deal.monk.model.NavDrawerItem;
import com.deal.monk.utility.DealMonkPreferences;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.deal.monk.R;

public class MainPage extends AppCompatActivity implements OnClickListener,
	ConnectionCallbacks, OnConnectionFailedListener,
	ResultCallback<LoadPeopleResult>   {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private ImageView imageViewRound;
	private TextView txtTitle;
	
	private GoogleApiClient mGoogleApiClient;
	private String TAG = getClass().getSimpleName();
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(MainPage.this);
		setContentView(R.layout.activity_main_page);
		// Toolbar Reference
		final Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolBar);

		mToolBar.setNavigationIcon(R.drawable.menuw);
		txtTitle = (TextView) mToolBar.findViewById(R.id.toolbar_title);
		txtTitle.setTextSize(18);
		txtTitle.setText("Home");
		
		  mGoogleApiClient = new GoogleApiClient.Builder(MainPage.this)
          .addConnectionCallbacks((ConnectionCallbacks) MainPage.this)
           .addApi(Plus.API)
          .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		String personName,email,personPhotoUrl;

		// Get Shared preferences from Utility Package 
		DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(MainPage.this);
		personName = dealMonkPreferences.getString("USER_NAME");
		email=dealMonkPreferences.getString("USER_EMAIL");
		personPhotoUrl=dealMonkPreferences.getString("USER_PROFILE");
		String userid =dealMonkPreferences.getString("MAIN_USER_ID");
		
		// Textview References for drawer
		TextView name = (TextView)findViewById(R.id.textView1);
		TextView email1 = (TextView)findViewById(R.id.textView2);
		name.setText(personName);
		email1.setText(email);
	
		
		
		// UNIVERSAL IMAGE LOADER SETUP
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
						.cacheOnDisc(true).cacheInMemory(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.displayer(new FadeInBitmapDisplayer(300)).build();

				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						getApplicationContext())
						.defaultDisplayImageOptions(defaultOptions)
						.memoryCache(new WeakMemoryCache())
						.discCacheSize( 100 * 1024 * 1024).build();

				ImageLoader.getInstance().init(config);
				// END - UNIVERSAL IMAGE LOADER SETUP
		
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).resetViewBeforeLoading(true)
						.showImageForEmptyUri(R.drawable.userphoto128)
						.showImageOnFail(R.drawable.userphoto128)
						.showImageOnLoading(R.drawable.userphoto128).build();
				
		
		imageViewRound = (ImageView) findViewById(R.id.imageView_round);
		//download and display image from url
		imageLoader.displayImage(personPhotoUrl, imageViewRound, options);

		
	//	Bitmap icon = BitmapFactory.decodeResource(getResources(),
	//			R.drawable.userphoto128);
	//	imageViewRound.setImageBitmap(icon);

		DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		LinearLayout mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
		// ListView mDrawerListChild = (ListView)
		// findViewById(R.id.left_drawer_child);

		// mDrawerLayout.closeDrawer(mDrawerList);

		// Getting Names from string array (Navigation Drawer list items)
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// Gettings icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_child);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding Icons drawer items to array
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

		navMenuIcons.recycle();

		// setting the navigation drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.menuw, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility		
				) 
		{			
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle("thanks");
				setSupportActionBar(mToolBar);

				mToolBar.setNavigationIcon(R.drawable.menuw);
				String tittle = (String) getTitle();
				txtTitle.setText(tittle);
				// mToolBar.setTitle(mTitle);

				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle("Select");
				// mToolBar.setTitle(mDrawerTitle);
				setSupportActionBar(mToolBar);
				String tittle = (String) getTitle();
				txtTitle.setText(tittle);

				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	private void displayView(int position) {
		// update the main content by replacing fragments
		android.support.v4.app.Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new Deals();
			break;
		case 1:
			fragment = new Profile();
			break;
		case 2:
			fragment = new Brownie_points();
			break;
		case 3:
			fragment = new My_Deals();
			break;
		case 4:
			fragment = new Invite_Friend();
			break;
		case 5:
			fragment = new Settings_page();
			break;
		case 6:
			// Log Out from fb and g+

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// Yes button clicked
						
						
						final DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(MainPage.this);
						
						if (mGoogleApiClient.isConnected()) {
				            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				            mGoogleApiClient.disconnect();
	                        mGoogleApiClient.connect();
	                        dealMonkPreferences.clearData();
	                        
							Intent login = new Intent(MainPage.this, Login.class);
							startActivity(login);
							MainPage.this.finish();			           
				        } 
						else {
							LoginManager.getInstance().logOut();
							dealMonkPreferences.clearData();
							Intent intent1 = new Intent(MainPage.this,Login.class);
							startActivity(intent1);
							finish();							
						}
					
						

					case DialogInterface.BUTTON_NEGATIVE:
						// No button clicked
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
			builder.setMessage("Are you sure you want to Logout ?")
					.setIcon(R.drawable.ic_launcher).setTitle("Confirm")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();

		default:
			break;
		}

		if (fragment != null) {

			// Begin the transaction
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			// Replace the contents of the container with the new fragment
			ft.replace(R.id.content_frame, fragment).commit();
			// or ft.add(R.id.your_placeholder, new FooFragment());
			// Complete the changes added above

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawers();

		} else {
			// error in creating fragment
			
		}

	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	
	// --------------------------------------------for back
	// press--------------------------//

	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler = new Handler();

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			doubleBackToExitPressedOnce = false;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mHandler != null) {
			mHandler.removeCallbacks(mRunnable);
		}
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		mHandler.postDelayed(mRunnable, 2000);
	}

	@Override
	public void onResult(LoadPeopleResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}