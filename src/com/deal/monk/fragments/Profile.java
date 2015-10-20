package com.deal.monk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.monk.utility.DealMonkPreferences;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.deal.monk.R;

public class Profile extends android.support.v4.app.Fragment {

	public Profile() {
	}

	private ImageView imageViewRound;
	  String personName,email,personPhotoUrl,firstname,lastname,contact_no;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile, container, false);
		DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
		personName = dealMonkPreferences.getString("USER_NAME");
		email=dealMonkPreferences.getString("USER_EMAIL");
		personPhotoUrl=dealMonkPreferences.getString("USER_PROFILE");
		firstname=dealMonkPreferences.getString("FIRST_NAME");
		lastname=dealMonkPreferences.getString("LAST_NAME");
		contact_no = dealMonkPreferences.getString("Contact_Number");
		
		TextView myemail = (TextView) rootView.findViewById(R.id.tvEmailID_profilenew);
		TextView firstnametxt = (TextView) rootView.findViewById(R.id.tvFirstName_profilenew);
		TextView lastnametxt = (TextView) rootView.findViewById(R.id.tvLastName_profilenew);
		TextView contactno = (TextView) rootView.findViewById(R.id.tvPhone_profilenew);
		
		
		myemail.setText(email);
		firstnametxt.setText(firstname);
		lastnametxt.setText(lastname);
		contactno.setText(contact_no);
		
		
		// UNIVERSAL IMAGE LOADER SETUP
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
						.cacheOnDisc(true).cacheInMemory(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.displayer(new FadeInBitmapDisplayer(300)).build();

				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						getActivity())
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
				
		
		imageViewRound = (ImageView) rootView.findViewById(R.id.imageView_round);
		//download and display image from url
		imageLoader.displayImage(personPhotoUrl, imageViewRound, options);

		
		
		
		

		return rootView;
	}

}
