package com.deal.monk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deal.monk.R;

public class Search_Fragment extends android.support.v4.app.Fragment {

	Button b1, b2, b3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.search_fragment1, container,
				false);

		return view;
	}

}
