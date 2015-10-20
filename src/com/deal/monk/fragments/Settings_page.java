package com.deal.monk.fragments;

import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.deal.monk.About_us;
import com.deal.monk.FeedbackActivity;
import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.R;

public class Settings_page extends Fragment {

	public Settings_page() {
	}
	
	private LinearLayout l1, l2;
	private ToggleButton toggleBtnSMSAlert, toggleBtnEmailAlerts;
	private DealMonkPreferences dealMonkPreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.settings_page, container,false);
		setupUi(rootView);

		return rootView;
	}

	private void setupUi(View rootView) {
		l1 = (LinearLayout) rootView.findViewById(R.id.lLayoutShareFeedback_DealMonkSettings);
		l2 = (LinearLayout) rootView.findViewById(R.id.lLayoutAboutUs_DealMonkSettings);
		toggleBtnSMSAlert = (ToggleButton) rootView.findViewById(R.id.toggleBtnSMSAlert_DealMonkSettings);
		toggleBtnEmailAlerts = (ToggleButton) rootView.findViewById(R.id.toggleBtnEmailAlerts_DealMonkSettings);
	
		dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
		String email = dealMonkPreferences.getString("email_alert");
		String sms = dealMonkPreferences.getString("sms_alert");
		
		if (email.equals("1")) {
			toggleBtnEmailAlerts.setChecked(true);
		} else {
			toggleBtnEmailAlerts.setChecked(false);
		}
		
		if (sms.equals("1")) {

			toggleBtnSMSAlert.setChecked(true);
		} else {
			toggleBtnSMSAlert.setChecked(false);
		}
		
		l1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i1 = new Intent(getActivity(), FeedbackActivity.class);
				startActivity(i1);

			}
		});

		l2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i2 = new Intent(getActivity(), About_us.class);
				startActivity(i2);

			}
		});

		toggleBtnSMSAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
						int isActivate = 0;
						if (isChecked) {
							isActivate = 1;
						} else {
							isActivate = 0;
						}

						new UpdateUserSetting().execute(Integer.toString(isActivate),DealMonkConstants.URL_SETTING_SMS,"sms_alert");

					}
				});

		toggleBtnEmailAlerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
						int isActivate = 0;
						if (isChecked) {
							isActivate = 0;
						} else {
							isActivate = 1;
						}

						new UpdateUserSetting().execute(Integer.toString(isActivate),DealMonkConstants.URL_SETTING_EMAIL,"email_alert");
					}
				});

	}

	private class UpdateUserSetting extends AsyncTask<String, String, String> {
		private String alertType = "";
		private String setState = "";

		@Override
		protected String doInBackground(String... params) {
			String result = " ";
			try {
				alertType = params[2];
				setState = params[0];

				ServerUtility serverUtility = ServerUtility.getInstance();

				JSONObject jsonObject = new JSONObject();
				dealMonkPreferences = DealMonkPreferences.getInstance(getActivity());
				String userId = dealMonkPreferences.getString("user_id");

				jsonObject.put("user_id", 1);
				jsonObject.put(alertType, setState);

				result = serverUtility.makeHttpPostRequest(params[1],jsonObject.toString());
				Log.d("response result is :",result);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			
			try {

				if (!TextUtils.isEmpty(result)) {

					JSONObject jsonObject = new JSONObject(result);

					boolean isSuccess = jsonObject.getBoolean("success");

					if (isSuccess) {
					

						if (alertType.equals("email_alert")) {
							dealMonkPreferences.setString("email_alert",setState);
						} else {
							dealMonkPreferences.setString("sms_alert", setState);
						}

					} else {
						}

				} else {
					}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}