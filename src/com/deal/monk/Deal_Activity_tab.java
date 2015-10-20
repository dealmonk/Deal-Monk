package com.deal.monk;

import com.deal.monk.R;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class Deal_Activity_tab extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal__activity_tab);
		// create the TabHost that will contain the Tabs
		final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");
		TabSpec tab3 = tabHost.newTabSpec("third Tab");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		tab1.setIndicator(null, getResources().getDrawable(R.drawable.offer64w));
		tab1.setContent(new Intent(this, Select_list.class));

		tab2.setIndicator(null, getResources()
				.getDrawable(R.drawable.review64w));
		tab2.setContent(new Intent(this, Select_Map.class));

		tab3.setIndicator(null,
				getResources().getDrawable(R.drawable.contact64w));
		tab3.setContent(new Intent(this, Select_Map.class));

		tabHost.setBackgroundColor(Color.parseColor("#424242"));

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor("#424242"));
			tabHost.getTabWidget().getChildAt(0)
					.setBackgroundColor(Color.parseColor("#ff0048"));
		}

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
					tabHost.getTabWidget().getChildAt(i)
							.setBackgroundColor(Color.parseColor("#424242"));
				}

				tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
						.setBackgroundColor(Color.parseColor("#ff0048"));
			}
		});

		/** Add the tabs to the TabHost to display. */
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);

	}

}
