package com.deal.monk;

import com.deal.monk.R;
import com.deal.monk.fragments.Deal_Upcoming;
import com.deal.monk.fragments.Deal_all;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class My_Deals extends android.support.v4.app.Fragment {
	
	private FragmentTabHost mTabHost;
	
	
public My_Deals(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.my_deals, container, false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);        
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Upcoming"),
                Deal_Upcoming.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("All"),
                Deal_all.class, null);
        
        
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++) 
                { 
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    tv.setAllCaps(false);
                    tv.setTextSize(20);
                } 
                TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setAllCaps(false);
                tv.setTextSize(20);
        
        
        
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
        mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#424242"));
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff0048"));       
        } 
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				 for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
					 {
						 mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#424242"));
						 }

						 mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ff0048"));
						 }
		});

        
        
        
         
        return rootView;
	}}


