package com.deal.monk.fragments;


import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deal.monk.Deal;
import com.deal.monk.R;
import com.deal.monk.adapters.Deal_offer_adapter;
import com.deal.monk.model.OfferListModel;

public class OfferFragment extends Fragment {

	ListView list;
	ImageView imageView2;
	ArrayList<OfferListModel> offerItems = new ArrayList<OfferListModel>();


	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		

		View rootView = inflater.inflate(R.layout.deal_offer, container, false);
		imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
		TextView dialog = (TextView)rootView.findViewById(R.id.textView1);
		list = (ListView) rootView.findViewById(R.id.dealofferlist);

		ArrayList<OfferListModel> offermodel = ((Deal)getActivity()).getLiveDetails();
		
		if (offermodel.size() > 0) {
			Deal_offer_adapter adapter = new Deal_offer_adapter(getActivity(),offermodel);
			list.setAdapter(adapter);
		
		}
		else {
			list.setVisibility(View.GONE);
			dialog.setVisibility(View.VISIBLE);
			dialog.setText("Sorry. There are no live deals for this restaurant at the moment.");
			
		}
		

		
		return rootView;
	}
}
