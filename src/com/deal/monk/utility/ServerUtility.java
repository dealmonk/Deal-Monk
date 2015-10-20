package com.deal.monk.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class ServerUtility {

	private static ServerUtility serverUtility;
	private String TAG = getClass().getSimpleName();
	
	public static ServerUtility getInstance() {
		
		if (serverUtility == null) {
			serverUtility = new ServerUtility();
		}
		return serverUtility;
	}
	
	public String makeHttpPostRequest(String url, String jsonString){
		
		String response = "";
		
		try {
		
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			Log.i(TAG, "URL is :-" +url);
			
			StringEntity stringEntity = new StringEntity(jsonString);
            httpPost.setEntity(stringEntity);
			httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);
			
			StringBuilder stringBuilder = new StringBuilder();
			String strLine = null;
			
			while ((strLine = reader.readLine()) != null) {
				stringBuilder.append(strLine + "\n");
			}
			
			inputStream.close();
			
			response = stringBuilder.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * This function is used to make request for the GET method.
	 * 
	 * Please test this function before using.
	 * 
	 * @param url
	 * @return
	 */
	public String makeHttpGetRequest(String url){
		
		String response = "";
		
		try {
		
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
//			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);
			
			StringBuilder stringBuilder = new StringBuilder();
			String strLine = null;
			
			while ((strLine = reader.readLine()) != null) {
				stringBuilder.append(strLine + "\n");
			}
			
			inputStream.close();
			
			response = stringBuilder.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
}