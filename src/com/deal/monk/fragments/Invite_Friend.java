package com.deal.monk.fragments;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.R.drawable;
import com.deal.monk.R.id;
import com.deal.monk.R.layout;
import com.deal.monk.R.string;
import com.deal.monk.utility.DealMonkPreferences;
import com.google.android.gms.plus.PlusShare;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.deal.monk.DealMonkApplication;
import com.deal.monk.R;


public class Invite_Friend extends android.support.v4.app.Fragment {
	LinearLayout sms_Referral, gmail_Referral,googlePlus_Referral,twitter_Referral,fb_referral,whatsapp_referrals;
	
	public Invite_Friend() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.invite_friend, container,false);
		setupUI(rootView);
		
		DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
		String personName = dealMonkPreferences.getString("USER_NAME");		
		String personPhotoUrl=dealMonkPreferences.getString("USER_PROFILE");
		ImageView ProfilePic = (ImageView)rootView.findViewById(R.id.imageView_round);
		TextView username =(TextView)rootView.findViewById(R.id.tvFullName_referrals);
		ImageLoader imageLoader = DealMonkApplication.getImageLoader(getActivity());
		
		username.setText(personName);
		imageLoader.displayImage(personPhotoUrl, ProfilePic);
		
		whatsapp_referrals.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				  Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
			        whatsappIntent.setType("text/plain");
			        whatsappIntent.setPackage("com.whatsapp");
			        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Now get incredible Real-time deals with the DealMonk App. Take it for a spin."
			        	+"\n"+"https://play.google.com/store/apps/details?id=com.deal.monk");
			        try {
			            getActivity().startActivity(whatsappIntent);
			        } catch (android.content.ActivityNotFoundException ex) {
			        	Toast.makeText(getActivity(), "Whats app have not been installed.",Toast.LENGTH_SHORT).show();
			        	
			        }
				
				
				
			}
		});
		

		sms_Referral.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(
						android.content.Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("sms_body", "Now get incredible Real-time deals with the DealMonk App. Take it for a spin."
						+"\n"+"https://play.google.com/store/apps/details?id=com.deal.monk");
				startActivity(smsIntent);
			}
		});
		gmail_Referral.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.setType("plain/text");
				sendIntent.setData(Uri.parse(""));
				sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation for Trying Deal-Monk");
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Now get incredible Real-time deals with the DealMonk App. Take it for a spin."
						+"\n"+"https://play.google.com/store/apps/details?id=com.deal.monk");
				startActivity(sendIntent);
				
			}
		});
		googlePlus_Referral.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent shareIntent = new PlusShare.Builder(getActivity())
		          .setType("text/plain")
		          .setText("Now get incredible Real-time deals with the DealMonk App. Take it for a spin..."
		        		  +"\n"+"https://play.google.com/store/apps/details?id=com.deal.monk")
		         // .setContentUrl(Uri.parse("https://developers.google.com/+/"))
		          .getIntent();

		      startActivityForResult(shareIntent, 0);
				
			}
		});
		
		String Twitter_link_browser ;
		twitter_Referral.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try
			    {
			        // Check if the Twitter app is installed on the phone.
			        getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
			        Intent intent = new Intent(Intent.ACTION_SEND);
			        intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
			        intent.setType("text/plain");
			        intent.putExtra(Intent.EXTRA_TEXT, "Now get incredible Real-time deals with the DealMonk App. Take it for a spin..."
			        		  +"\n"+"https://play.google.com/store/apps/details?id=com.deal.monk");
			        startActivity(intent);

			    }
			    catch (Exception e)
			    {
			        Toast.makeText(getActivity(),"Twitter is not installed on this device",Toast.LENGTH_LONG).show();

			    }
			}
		});
		fb_referral.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String urlToShare = "https://play.google.com/store/apps/details?id=com.deal.monk";
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
				// See if official Facebook app is found
				boolean facebookAppFound = false;
				List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
				for (ResolveInfo info : matches) {
				    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) {
				        intent.setPackage(info.activityInfo.packageName);
				        facebookAppFound = true;
				        break;
				    }
				}
				//If facebook app not found, load sharer.php in a browser
				if (!facebookAppFound) {
				    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=https%3A//play.google.com/store/apps/details?id=com.deal.monk" + urlToShare;
				    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
				}
				startActivity(intent);
			}
		});
		return rootView;
	}

	private void setupUI(View rootView) {
		sms_Referral = (LinearLayout) rootView.findViewById(R.id.inside_lLayoutReferBySMS_referrals);
		gmail_Referral = (LinearLayout) rootView.findViewById(R.id.inside_lLayoutReferByEmail_referrals);
		googlePlus_Referral=(LinearLayout) rootView.findViewById(R.id.inside_lLayoutGooglePlus_referrals);
		twitter_Referral=(LinearLayout)rootView.findViewById(R.id.inside_lLayoutTwitter_referrals);
		fb_referral=(LinearLayout)rootView.findViewById(R.id.inside_lLayoutFacebook_referrals);
		whatsapp_referrals =(LinearLayout)rootView.findViewById(R.id.inside_lLayoutwhatsapp_referrals);
		
	}

}
