package com.pr.instagramapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pr.instagramapitest.api.Brand;
import com.pr.instagramapitest.api.RestClient;
import com.pr.instagramapitest.api.User;
import com.pr.instagramapitest.instaFiles.InstagramApp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


	private InstagramApp instaObj;
	public static final String CLIENT_ID = "550625228857474";
	public static final String CLIENT_SECRET = "67ba19ebb9e12c4228d07ef8b2058629";
	public static final String CALLBACK_URL = "https://socialsizzle.heroku.com/auth/";


	public Button loginBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


//
//		RestClient.getInstance(getApplicationContext()).getWatchApiService().getUsers().enqueue(new Callback<List<Brand>>() {
//			@Override
//			public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
//				Log.d("dwd","edkojihefef");
//
//			}
//
//			@Override
//			public void onFailure(Call<List<Brand>> call, Throwable t) {
//				Log.d("dwd","error");
//
//			}
//		});

		instaObj = new InstagramApp(this, CLIENT_ID,
				CLIENT_SECRET, CALLBACK_URL);
		instaObj.setListener(listener);




		instaObj.authorize();

		loginBtn = findViewById(R.id.btn_login);
		//add this in your button click or wherever you need to call the instagram api

		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				instaObj = new InstagramApp(getApplicationContext(), CLIENT_ID,
						CLIENT_SECRET, CALLBACK_URL);
				instaObj.setListener(listener);


				instaObj.authorize();
			}
		});

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
