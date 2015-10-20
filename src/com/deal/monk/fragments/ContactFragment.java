package com.deal.monk.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deal.monk.Deal;
import com.deal.monk.model.OfferListModel;
import com.deal.monk.R;

public class ContactFragment extends Fragment {
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.deal_conatct, container, false);
		ArrayList<OfferListModel> offermodel = ((Deal)getActivity()).getRestaurantdetails();
		String name = offermodel.get(0).getRestaurantName();
		String Address1 = offermodel.get(0).getRestaurantAddress1();
		String Address2 = offermodel.get(0).getRestaurantAddress2();
		String Area = offermodel.get(0).getRestaurentArea();
		String ContactNo = offermodel.get(0).getRestaurantContactDetails();
		
		TextView txtRestaurent_Name = (TextView) rootView.findViewById(R.id.txtRestaurent_Name);
		TextView txtRestaurent_Street = (TextView) rootView.findViewById(R.id.txtRestaurent_Street);
		TextView txtRestaurent_City = (TextView) rootView.findViewById(R.id.txtRestaurent_City);
		TextView txtRestaurent_Country = (TextView) rootView.findViewById(R.id.txtRestaurent_Country);
		TextView txtRestaurent_PhoneNo = (TextView) rootView.findViewById(R.id.txtRestaurent_PhoneNo);
		
		if (!TextUtils.isEmpty(name)) {
			txtRestaurent_Name.setText(name);
		} 
		
		if (!TextUtils.isEmpty(Address1)) {
			txtRestaurent_Street.setText(Address1);
		}
		
		if (!TextUtils.isEmpty(Address2)) {
			txtRestaurent_City.setText(Address2);
		}
		
		if (!TextUtils.isEmpty(Area)) {
			txtRestaurent_Country.setText(Area);
		}
		
		if (!TextUtils.isEmpty(ContactNo)) {
			txtRestaurent_PhoneNo.setText(ContactNo);
		}
		
		return rootView;
		
	}
}
