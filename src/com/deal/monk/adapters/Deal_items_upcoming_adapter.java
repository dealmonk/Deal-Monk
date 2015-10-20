package com.deal.monk.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deal.monk.model.MyDealModel;
import com.deal.monk.R;

public class Deal_items_upcoming_adapter extends BaseAdapter {

	private Context context;
	private List<MyDealModel> OfferItems;
	private LayoutInflater inflater;
	
	public Deal_items_upcoming_adapter(Context context,List<MyDealModel> offerItems) {
		super();
		this.context = context;
		OfferItems = offerItems;
		inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}
	private static class ViewHolder {
		TextView liveOffer,offerDate;
		}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.deal_upcoming_items, null);
			holder = new ViewHolder();
			holder.liveOffer=(TextView) convertView.findViewById(R.id.itemhead);
			holder.offerDate=(TextView) convertView.findViewById(R.id.itemvalue);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		holder.liveOffer.setText(OfferItems.get(position).getOfferStartTime()
				+ ","+OfferItems.get(position).getOfferName() 
				+ ","+OfferItems.get(position).getRestaurentName());
		holder.offerDate.setText(OfferItems.get(position).getBookingDate());
		
		return convertView;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return OfferItems.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return OfferItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return OfferItems.indexOf(getItem(position));
	}

}