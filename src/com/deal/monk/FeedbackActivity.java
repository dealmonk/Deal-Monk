package com.deal.monk;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.R;

public class FeedbackActivity extends Activity {

	EditText Write;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("Share Feedback");
		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Write = (EditText) findViewById(R.id.etFeedBackWrite_Feedback);
		btn = (Button) findViewById(R.id.btnsend);
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new SendFeedback().execute();
				Write.setText("");
			}
		});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

	private class SendFeedback extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... string) {
			String feedbackmsg = Write.getText().toString();
			DealMonkPreferences dealMonkPreferences = DealMonkPreferences.getInstance(FeedbackActivity.this);
			String userid =dealMonkPreferences.getString("MAIN_USER_ID");
			
			ServerUtility ut = ServerUtility.getInstance();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("feedback", feedbackmsg);
				jsonObject.put("user_id", userid);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String response = ut.makeHttpPostRequest(DealMonkConstants.URL_SHARE_FEEDBACK,jsonObject.toString());
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Toast.makeText(getApplicationContext(), "Feedback Send Sucessful." ,Toast.LENGTH_SHORT).show();
		}

	}
}
