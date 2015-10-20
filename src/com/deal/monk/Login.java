package com.deal.monk;

import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deal.monk.utility.DealMonkConstants;
import com.deal.monk.utility.DealMonkPreferences;
import com.deal.monk.utility.DealmonkProgressDialog;
import com.deal.monk.utility.ServerUtility;
import com.deal.monk.utility.Utility;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class Login extends Activity implements OnClickListener,ConnectionCallbacks, OnConnectionFailedListener,
                    ResultCallback<LoadPeopleResult> {
	
	
	private Button  btn_gplus;	
	private LoginButton btn_facebook;	
	private String TAG = getClass().getSimpleName();
	private GoogleApiClient mGoogleApiClient;	
	private ConnectionResult mConnectionResult;
	private static final int RC_SIGN_IN = 0;
	
	
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	
	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	private ProfileTracker profileTracker;
	private AlertDialog alertDialog; 
	private DealMonkPreferences dealMonkPreferences;
	
	ProgressDialog ProgDialog;
	
	String personPhoto;
	String personName,firstName,lastName,email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(Login.this);
		setContentView(R.layout.activity_login);
		dealMonkPreferences = DealMonkPreferences.getInstance(Login.this);
		TextView skip = (TextView) findViewById(R.id.skiplogin);
		setupUI();
		
				
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		callbackManager = CallbackManager.Factory.create();
		
		
		skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, MainPage.class);
				startActivity(intent);
				finish();
			}
		});

	     }

	     @Override
	     protected void onStart() {
	    	super.onStart();
	    	mGoogleApiClient.connect();
	     }

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
		
	}



	private void setupUI() {
		
		btn_gplus = (Button) findViewById(R.id.button2);
		btn_facebook=(LoginButton)findViewById(R.id.button1);
		btn_facebook.setReadPermissions(Arrays.asList("public_profile, email"));
		
		
		callbackManager = CallbackManager.Factory.create();

		// Initialize the SDK before executing any other operations,
		// especially, if you're using Facebook UI elements.
		accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldToken,
					AccessToken newToken) {

			}
		};

		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldprofile,
					Profile newprofile) {
				// WelcomeMessageDisplay(newprofile);
			}
		};


		btn_gplus.setOnClickListener(this);
		btn_facebook.setOnClickListener(this);
		

	}

	/*
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * super.onPause(); finish(); }
	 */
	
	@Override
	public void onClick(View v) {
		if (v == btn_gplus) {

			if (Utility.getInstance(Login.this).isConnectingToInternet()) {

				if (checkPlayServices()) {
					if (!mGoogleApiClient.isConnecting()) {
						mSignInClicked = true;
						resolveSignInError();
					}

				} else {
					Toast.makeText(Login.this,
							"Please update your playservices to use our app.",
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(Login.this,
						"Please check your internet connectivity.",
						Toast.LENGTH_LONG).show();
			}

		} else {

			if (Utility.getInstance(Login.this).isConnectingToInternet()) {

				 loginWithFB();

			} else {
				Toast.makeText(Login.this,
						"Please check your internet connectivity.",
						Toast.LENGTH_LONG).show();

			}
		}

	}
	

	
		@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

		Log.i(TAG, "Connection error :- " + result);

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		if (requestCode == RC_SIGN_IN) {

			if (responseCode == RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}

		} else {
			 callbackManager.onActivityResult(requestCode, responseCode, data);
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
				this);
		getGmailProfileInformation();

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onResult(LoadPeopleResult peopleData) {

		Log.i(TAG, "OnResult :- " + peopleData);

		
		getGmailProfileInformation();
	}

	private void getGmailProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				personName = currentPerson.getDisplayName();
				firstName = currentPerson.getName().getGivenName();
				lastName = currentPerson.getName().getFamilyName();
				personPhoto = currentPerson.getImage().getUrl();
				personPhoto = personPhoto.substring(0,personPhoto.length() - 2)
			                +200;
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);				
				
				new LoginUser().execute(personName,email,personPhoto,"GOOGLE");
            		
				
				dealMonkPreferences.setString("USER_NAME", personName);
				dealMonkPreferences.setString("USER_EMAIL", email);
				dealMonkPreferences.setString("USER_PROFILE",personPhoto);
				dealMonkPreferences.setString("FIRST_NAME", firstName);
				dealMonkPreferences.setString("LAST_NAME", lastName);
				

				//startDrawerScreen();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void startDrawerScreen() {
		Intent intent = new Intent(Login.this, MainPage.class);
		startActivity(intent);
		finish();
		}
	
	private void StartOTP(){
		
		Intent intent = new Intent(Login.this, OtpActivity.class);
		startActivity(intent);
		finish();
		
	}

		

	private void googlePlusLogin() {
		if (!mGoogleApiClient.isConnecting()) {
			resolveSignInError();
		}
	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	protected boolean checkPlayServices() {
		final int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(Login.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						resultCode, Login.this, 100);
				if (dialog != null) {
					dialog.show();
					dialog.setOnDismissListener(new OnDismissListener() {

						public void onDismiss(DialogInterface dialog) {

							if (ConnectionResult.SERVICE_INVALID == resultCode)
								finish();
						}
					});
					return false;
				}
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
			builder.setMessage("Google Play Services Error, This device is not supported for required Goole Play Services.");
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});

			AlertDialog alertDialog = builder.create();
			alertDialog.show();

			return false;
		}
		return true;
		
		
	}
	
	//------------------------------------------------Data buffer leak ----------------------
	
	public void onTurnBasedMatchesLoaded(int statusCode, LoadMatchesResponse response) 
	{
	    TurnBasedMatchBuffer buff = response.getMyTurnMatches();
	    // do some stuff with buff
	    buff.close();
	}

	public void onPlayersLoaded(int statusCode, PlayerBuffer buff) 
	{
	    Player p = buff.get(0);
	    buff.close();
	}
	
	//-----------------------------------------------------------------------------------------
	
	private void loginWithFB() {

		LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {				
						
						GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
		                        new GraphRequest.GraphJSONObjectCallback() {
		                            @Override
		                            public void onCompleted(JSONObject object,GraphResponse response) {
		                                // Application code
		                                Log.v("LoginActivity", response.toString());
		                                
		                                try {
										
		                                	if (response != null) {
											
		                                		JSONObject jsonObject = response.getJSONObject();
			                                	
			                                	String name = jsonObject.getString("name");
			                                	String email = jsonObject.getString("email");
			                                	String id = jsonObject.getString("id");
			                                	String firstname = jsonObject.getString("first_name");
			                                	String lastname = jsonObject.getString("last_name");
			                                	String imagePath = "http://graph.facebook.com/"+id+"/picture?type=large";
			                                	                            	
			                                	dealMonkPreferences.setString("USER_NAME", name);
			                    				dealMonkPreferences.setString("USER_EMAIL", email);
			                    				dealMonkPreferences.setString("USER_PROFILE",imagePath);
			                    				dealMonkPreferences.setString("FIRST_NAME", firstname);
			                    				dealMonkPreferences.setString("LAST_NAME", lastname);
			                                	
			                                	if (!TextUtils.isEmpty(email)) {
					    							new LoginUser().execute(name,email,imagePath,"FACEBOOK");
			                                		
												} else {
													
													showErrorDialog();
												}
											}
		                                	
			    							
										} catch (Exception e) {
											e.printStackTrace();
										}
		                                
		                            }
									
		                        });
						
		                Bundle parameters = new Bundle();
		                parameters.putString("fields", "id,name,email,first_name,last_name");
		                request.setParameters(parameters);
		                request.executeAsync();
						
						Log.d(TAG, "Login result :-" + loginResult);
					}

					@Override
					public void onCancel() {
						
					}

					@Override
					public void onError(FacebookException exception) {
						Log.d(TAG, "Debug error" + exception);
					}
					
				});
	}

	
	
	
	
	private void showErrorDialog() {
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
		builder.setMessage("We are unable to get your email id. Please Gmail to login.");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				((DialogInterface) builder).cancel();
			}
		});
		
		
		builder.show();
	}
	
	  private void showProgressDialog() {
	        if (ProgDialog == null) {
	            ProgDialog =DealmonkProgressDialog.createProgressDialog(Login.this);
	            ProgDialog.setIndeterminate(false);
	            ProgDialog.setCancelable(false);
	        }
	        ProgDialog.show();
	    }
	
	private void dismissProgressDialog() {
        if (ProgDialog != null && ProgDialog.isShowing()) {
            ProgDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
	
	private class LoginUser extends AsyncTask<String, String, String> {
		
		private String signUpSource;	

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
					
			String response = "";

			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("first_name",params[0]);
				jsonObject.put("email",params[1]);
				jsonObject.put("sign_up_via", params[3]);
				jsonObject.put("sign_up_source", "anroid app");
				jsonObject.put("user_profile_image", params[2]);
				
				signUpSource = params[3];
				
				Log.d("Sending Parameters", jsonObject.toString());
				ServerUtility serverUtility = ServerUtility.getInstance();	
				response = serverUtility.makeHttpPostRequest(DealMonkConstants.URL_LOGIN_FB_GPLUS, jsonObject.toString());
				Log.d("Login Response Message",response);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return response;
		}
	     @Override
	    protected void onPostExecute(String result) {
	    	// TODO Auto-generated method stub
	    	super.onPostExecute(result);
	    	dismissProgressDialog();
	    	if (!TextUtils.isEmpty(result)) {

				try {
					    JSONObject jsonObject = new JSONObject(result);
					    
					    if (jsonObject.getBoolean("success")) {
						
					    	JSONObject Userinfo = jsonObject.getJSONObject("user_info");						
							String name = Userinfo.getString("first_name");
							String userid = Userinfo.getString("user_id");
							String email = Userinfo.getString("email");
							String userProfile = Userinfo.getString("user_profile_image");
							String email_status = Userinfo.getString("email_alert");
							String sms_status = Userinfo.getString("sms_alert");
							
							if (Userinfo.getBoolean("contactStatus")) {
							String Contact_number =Userinfo.getString("contact_no");
							dealMonkPreferences.setString("Contact_Number", Contact_number);
								startDrawerScreen();								
							}
							else {
								StartOTP();
							}
							
							dealMonkPreferences.setString("MAIN_USER_ID", userid);						    
						    dealMonkPreferences.setString("Main_sign_in_Source", signUpSource);
						    dealMonkPreferences.setString("email_alert", email_status);
						    dealMonkPreferences.setString("sms_alert", sms_status);
						    
							
						} else {
							
							String message = jsonObject.getString("message");
							JSONObject Userinfo = jsonObject.getJSONObject("user_info");						
							String name = Userinfo.getString("first_name");
							String userid = Userinfo.getString("user_id");
							String email = Userinfo.getString("email");
							String userProfile = Userinfo.getString("user_profile_image");
							String email_status = Userinfo.getString("email_alert");
							String sms_status = Userinfo.getString("sms_alert");
							
							if (Userinfo.getBoolean("contactStatus")) {
								String Contact_number =Userinfo.getString("contact_no");
								dealMonkPreferences.setString("Contact_Number", Contact_number);
									startDrawerScreen();								
								}
								else {
									StartOTP();
								}							
							
							dealMonkPreferences.setString("MAIN_USER_ID", userid);
						    dealMonkPreferences.setString("Main_sign_in_Source", signUpSource);
						    dealMonkPreferences.setString("email_alert", email_status);
						    dealMonkPreferences.setString("sms_alert", sms_status);
						}
					    
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(Login.this,"Please try after some time.", Toast.LENGTH_LONG).show();
			}

		}

	    }	
}
