package com.deal.monk.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deal.monk.R;

public class Review_class_adapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final String[] listitem;
	private final String[] restname;
	
	public Review_class_adapter(Activity context,
	String[] listitem,String[] restname) {
	super(context, R.layout.review_screen_list_item, listitem);
	this.context = context;
	this.listitem = listitem;	
	this.restname=restname;
	 
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.review_screen_list_item, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.NameOfCanditate);
	TextView resname = (TextView) rowView.findViewById(R.id.txtDescription);
	 
	
	txtTitle.setText(listitem[position]);
	resname.setText(restname[position]);
	 
	
	return rowView;
	}

}