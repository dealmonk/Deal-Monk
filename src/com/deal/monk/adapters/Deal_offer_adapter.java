package com.deal.monk.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deal.monk.Confirmation;
import com.deal.monk.model.OfferListModel;
import com.deal.monk.R;

public class Deal_offer_adapter extends BaseAdapter {

	private Context context;
	private List<OfferListModel> offerItems;
	// private Button b1;
	private ImageView circle;
	private LayoutInflater inflater;

	public Deal_offer_adapter(Context context, List<OfferListModel> offerItems) {
		super();
		this.context = context;
		this.offerItems = offerItems;
		inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	private static class ViewHolder {
		TextView txtTime;
		Button btnOffer;
		ImageView circle;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.deal_offer_items, null);
			holder = new ViewHolder();
			holder.txtTime = (TextView) convertView.findViewById(R.id.textView1);
			holder.btnOffer = (Button) convertView.findViewById(R.id.button1);
			holder.circle = (ImageView) convertView.findViewById(R.id.imageView2);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		OfferListModel offerItem = (OfferListModel) getItem(position);
		
		final String Offer_id = offerItem.getOfferId();
		final String Restautant_Name = offerItem.getRestaurantName();
		final String Restautant_location = offerItem.getRestaurentArea();
		final String Offer_Validity = offerItem.getStartTime()+" to "+offerItem.getEndTime();
		final String Offer = offerItem.getRestaurantOffer();
		final String Restaurant_Number= offerItem.getRestaurantContactDetails();
		final String Offer_Status = offerItem.getStatus();
				

		holder.txtTime.setText(offerItem.getStartTime() + "-"+ offerItem.getEndTime());
		holder.btnOffer.setText(offerItem.getRestaurantOffer());
		if (Offer_Status.equalsIgnoreCase("True")) {
			holder.btnOffer.setBackgroundColor(Color.parseColor("#ff0048"));
			holder.btnOffer.setTextColor(Color.parseColor("#ffffff"));
			holder.circle.setImageResource(R.drawable.shape_ring_change);			
		}
		else {
			holder.btnOffer.setBackgroundColor(Color.parseColor("#ffffff"));
			holder.circle.setImageResource(R.drawable.shape_ring);
			
		}
		
		holder.btnOffer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, Confirmation.class);
				intent.putExtra("Offer_id", Offer_id);
				intent.putExtra("Restautant_Name", Restautant_Name);
				intent.putExtra("Restautant_location", Restautant_location);
				intent.putExtra("Offer_Validity", Offer_Validity);
				intent.putExtra("Offer", Offer);
				intent.putExtra("Restaurant_Number", Restaurant_Number);
				context.startActivity(intent);
				
				
			}
		});

	/*	if (position == 0) {
			holder.btnOffer.setBackgroundColor(Color.parseColor("#ff0048"));
			holder.circle.setImageResource(R.drawable.shape_ring_change);

		} else {
			holder.btnOffer.setBackgroundColor(Color.parseColor("#ffffff"));
			holder.circle.setImageResource(R.drawable.shape_ring);
		}
*/
		return convertView;
	}

	@Override
	public int getCount() {
		return offerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return offerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return offerItems.indexOf(getItem(position));
	}

}