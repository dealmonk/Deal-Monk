package com.deal.monk.adapters;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.monk.DealMonkApplication;
import com.deal.monk.model.LiveDetailsModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.deal.monk.R;

public class SelectList1 extends BaseAdapter {
	
	
	private ArrayList<LiveDetailsModel> liveDetailsModels ;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	
	public SelectList1(Context context,ArrayList<LiveDetailsModel> liveDetailsModels) {
	
		layoutInflater = LayoutInflater.from(context);
		this.liveDetailsModels = liveDetailsModels;
		imageLoader = DealMonkApplication.getImageLoader(context);
	}
	
	@Override
	public int getCount() {
		return liveDetailsModels.size();
	}
	
	@Override
	public LiveDetailsModel getItem(int position) {
		return liveDetailsModels.get(position); 
	}
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		ViewHolder viewHoder ;
		
		if (view == null) {
			
			view = layoutInflater.inflate(R.layout.select_list_item, null);
			viewHoder = new ViewHolder();
			viewHoder.txtLiveOffer	= (TextView) view.findViewById(R.id.liveOfferDisplay);
			viewHoder.txtRestaurentName = (TextView) view.findViewById(R.id.txtRestaurentName);
			viewHoder.txtLocation = (TextView) view.findViewById(R.id.txtLocation);
			viewHoder.txtDistance = (TextView) view.findViewById(R.id.txtDistance);
			viewHoder.txtFoodType = (TextView) view.findViewById(R.id.txtFoodtype);
			viewHoder.imgRestaurent = (ImageView) view.findViewById(R.id.imgFoodItem);
			
			view.setTag(viewHoder);
			
		} else {
			
			viewHoder = (ViewHolder) view.getTag(); 
		}
		
		
	
	viewHoder.txtLiveOffer.setText(liveDetailsModels.get(position).getLive_detail());
	viewHoder.txtRestaurentName.setText(liveDetailsModels.get(position).getName());
	viewHoder.txtLocation.setText(liveDetailsModels.get(position).getLocation());
	viewHoder.txtDistance.setText(liveDetailsModels.get(position).getDistance());
	
	StringBuilder stringBuilder = new StringBuilder();
	
	ArrayList<String> couisineTypes = liveDetailsModels.get(position).getCuisine();
	for (int i = 0; i < couisineTypes.size(); i++) {
		
		if (i == couisineTypes.size() - 1) {
			stringBuilder.append(couisineTypes.get(i) );
		} else {
			stringBuilder.append(couisineTypes.get(i) + " ,");
		}
	}
	
	viewHoder.txtFoodType.setText(stringBuilder.toString());
	 
	imageLoader.displayImage(liveDetailsModels.get(position).getRestaurentUrl(), viewHoder.imgRestaurent);
	return view;
	}
	
	private static class ViewHolder {
		
		private TextView txtRestaurentName,txtLocation,txtDistance,txtFoodType,txtLiveOffer;
		private ImageView imgRestaurent;
	}

}