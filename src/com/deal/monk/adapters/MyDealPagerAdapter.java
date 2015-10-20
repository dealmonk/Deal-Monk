package com.deal.monk.adapters;

import java.util.List;

import android.support.v4.app.FragmentPagerAdapter;

public class MyDealPagerAdapter extends FragmentPagerAdapter {

	private int COUNT = 2;
	private List<android.support.v4.app.Fragment> mList;
	private String [] lMyBooking = {"ALL","UPCOMING"}; 
	
	public MyDealPagerAdapter(android.support.v4.app.FragmentManager fragmentManager,List<android.support.v4.app.Fragment> lFragments) {
		super(fragmentManager);
		this.mList = lFragments;
	}
	
	@Override
	public android.support.v4.app.Fragment getItem(int pPosition) {
		return mList.get(pPosition);
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return lMyBooking[position];
	}
}