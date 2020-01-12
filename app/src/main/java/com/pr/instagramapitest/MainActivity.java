package com.pr.instagramapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pr.instagramapitest.instaFiles.InstagramApp;

public class MainActivity extends AppCompatActivity {


	private InstagramApp instaObj;
	public static final String CLIENT_ID = "550625228857474";
	public static final String CLIENT_SECRET = "67ba19ebb9e12c4228d07ef8b2058629";
	public static final String CALLBACK_URL = "https://instagram.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		instaObj = new InstagramApp(this, CLIENT_ID,
				CLIENT_SECRET, CALLBACK_URL);
		instaObj.setListener(listener);


		instaObj.authorize();  //add this in your button click or wherever you need to call the instagram api

	}

//	@Override
//	public void onTokenReceived(String auth_token) {
//
//	}


	InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			Log.d("dwd", "succes  " + instaObj.getName() + " " + instaObj.getUserName());
			Log.e("Userid", instaObj.getId());
			Log.e("Name", instaObj.getName());
			Log.e("UserName", instaObj.getUserName());

		}

		@Override
		public void onFail(String error) {
			Log.d("dwd", "error");

			Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT)
					.show();
		}
	};
}
