package com.deal.monk.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class DealMonkPreferences {
	
	private static DealMonkPreferences dealmonkPreferences;
	private static SharedPreferences sharedPreferences;
	
	public static DealMonkPreferences getInstance(Context context){
		if (dealmonkPreferences == null) {
			dealmonkPreferences = new DealMonkPreferences();
			sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		}
		
		return dealmonkPreferences;
	}
	
	public void setString(String key,String value){
		
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void setInt(String key,int value){
		
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void setBoolean(String key,boolean value){
		
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public String getString(String key){
		return sharedPreferences.getString(key, null);
	}
	
	public int getInteger(String key){
		return sharedPreferences.getInt(key, -1);
	}
	
	public Boolean getBoolean(String key){
		return sharedPreferences.getBoolean(key, false);
	}
	
	public void clearData(){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}