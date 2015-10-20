package com.deal.monk;

import com.deal.monk.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About_us extends Activity {

	Button b1, b2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_tittle);
		TextView head = (TextView) findViewById(R.id.actionbar_textview);
		head.setText("About Us");
		Button back = (Button) findViewById(R.id.button1);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		b1 = (Button) findViewById(R.id.btnTermOfService);
		b2 = (Button) findViewById(R.id.btnPrivacypolicy);

		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent terms = new Intent(About_us.this, Term_services.class);
				startActivity(terms);

			}
		});

		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent policy = new Intent(About_us.this, Privacy_policy.class);
				startActivity(policy);

			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
