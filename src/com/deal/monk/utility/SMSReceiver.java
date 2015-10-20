package com.deal.monk.utility;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.deal.monk.OtpConfirmationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		String phoneNumbers = PreferenceManager.getDefaultSharedPreferences(
				context).getString("phone_entries", "");
		StringTokenizer tokenizer = new StringTokenizer("MD-DLMONK", ",");
		Set<String> phoneEnrties = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			phoneEnrties.add(tokenizer.nextToken().trim());
		}
		Bundle bundle = intent.getExtras();
		Object[] pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] messages = new SmsMessage[pdus.length];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String address = messages[i].getOriginatingAddress();
			if (phoneEnrties.contains(address)) {
				Intent newintent = new Intent(context, OtpConfirmationActivity.class);
				newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				newintent.putExtra("address", address);
				newintent.putExtra("message",messages[i].getDisplayMessageBody());
				context.startActivity(newintent);
			}
		}
	}
}