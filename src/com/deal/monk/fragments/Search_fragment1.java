package com.deal.monk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deal.monk.Select;
import com.deal.monk.R;

public class Search_fragment1 extends android.support.v4.app.Fragment {

	Button btnOk, btnCancel;
	EditText etLocation , etRestaurentName ,etCusine ;
	int mHour,mMinute;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.search_fragment2, container,false);

		btnOk = (Button) view.findViewById(R.id.button1);
		btnCancel = (Button) view.findViewById(R.id.button2);
		etLocation = (EditText)view.findViewById(R.id.editText1);
		etRestaurentName = (EditText)view.findViewById(R.id.editText2);
		etCusine = (EditText)view.findViewById(R.id.editText3);
	
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Search_Fragment s1 = new Search_Fragment();
				android.support.v4.app.FragmentManager fm = getFragmentManager();
				android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.My_Container_1_ID, s1);
				ft.commit();

			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				String loc = etLocation.getText().toString();
				String tim = etRestaurentName.getText().toString();
				String cui = etCusine.getText().toString();
				
				if (loc.matches("") && tim.matches("") && cui.matches("")) {
					Toast.makeText(getActivity(), "Please Enter Atleast One Field", Toast.LENGTH_SHORT).show();
				
				} else {
					
					Intent select = new Intent(getActivity(), Select.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean("IsFromRightHereRightnow", false);
					
					select.putExtra("SEARCH_LOCATION", loc);
					select.putExtra("SEARCH_REST_NAME", tim);
					select.putExtra("SEARCH_CUISINE", cui);
//					select.putExtra("IsFromRightHereRightnow", false);
					startActivity(select);
					
					// clear field for return
					etLocation.setText("");
					etRestaurentName.setText("");
					etCusine.setText("");
				}
			}
		});

		return view;
	}

}
