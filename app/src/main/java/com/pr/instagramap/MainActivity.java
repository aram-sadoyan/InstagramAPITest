package com.pr.instagramap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.pr.instagramap.activity.PostsActivity;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.RestClient;
import com.pr.instagramap.instaFiles.InstagramApp;
import com.pr.instagramap.util.AppConstants;
import com.pr.instagramapitest.R;

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
		//Crashlytics.getInstance().crash();
		setContentView(R.layout.activity_main);
		//TODO Create and move this into Main Applcation
		Fresco.initialize(this);

		instaObj = new InstagramApp(this, CLIENT_ID,
				CLIENT_SECRET, CALLBACK_URL);
		instaObj.setListener(listener);

		//TODO GET INSTA USER PROFILE
		String currentAccesToken = instaObj.getAccessToken();
		String userId = instaObj.getUserId();

		String userDataBAseUrl = "https://graph.instagram.com/" + userId
				+ "?fields=media,media_count,username"
				+ "&access_token=" + currentAccesToken;

		requestInstagramUser(userDataBAseUrl);

		loginBtn = findViewById(R.id.btn_login);
		//add this in your button click or wherever you need to call the instagram api

//		loginBtn.setOnClickListener(v -> {
//			instaObj = new InstagramApp(getApplicationContext(), CLIENT_ID,
//					CLIENT_SECRET, CALLBACK_URL);
//			instaObj.setListener(listener);
//
//			instaObj.authorize();
//		});

	}

	private void requestInstagramUser(String userDataBAseUrl) {
		RestClient.getInstance(getApplicationContext()).getWatchApiService()
				.getInstagramUser(userDataBAseUrl).enqueue(new Callback<InstagramUser>() {
			@Override
			public void onResponse(Call<InstagramUser> call, Response<InstagramUser> response) {
				InstagramUser instagramUser = response.body();
				if (instagramUser != null) {
					int mediaCount = instagramUser.getMediaCount();
					if (mediaCount > 0) {
						Intent postActivityIntent = new Intent(MainActivity.this, PostsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(AppConstants.EXTRA_KEY_INSTAGRAM_USER, instagramUser);
						postActivityIntent.putExtra(AppConstants.EXTRA_KEY_INSTAGRAM_USER, instagramUser);

						startActivity(postActivityIntent);
					}
				} else {
					instaObj.authorize();
				}
			}

			@Override
			public void onFailure(Call<InstagramUser> call, Throwable t) {
				Log.d("dwd", "InstaUser Error");
				instaObj.authorize();
			}
		});
	}


	InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			String currentAccesToken = instaObj.getAccessToken();
			String userId = instaObj.getUserId();

			String userDataBAseUrl = "https://graph.instagram.com/" + userId
					+ "?fields=media,media_count,username"
					+ "&access_token=" + currentAccesToken;
			requestInstagramUser(userDataBAseUrl);

			Log.d("dwd", "succes  " + instaObj.getName() + " " + instaObj.getUserId());
			Log.e("Userid", instaObj.getId());
			Log.e("Name", instaObj.getName());
			Log.e("UserId", instaObj.getUserId());

		}

		@Override
		public void onFail(String error) {
			Log.d("dwd", "error");

			Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT)
					.show();
		}
	};
}
