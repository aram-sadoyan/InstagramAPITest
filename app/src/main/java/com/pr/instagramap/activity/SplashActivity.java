package com.pr.instagramap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.RestClient;
import com.pr.instagramap.api.User;
import com.pr.instagramap.api.model.PromotionModel;
import com.pr.instagramap.api.model.Response2;
import com.pr.instagramap.controller.PromotionController;
import com.pr.instagramap.instaFiles.InstagramApp;
import com.pr.instagramap.payment.PaymentServiceAPI;
import com.pr.instagramap.util.AnalyticUtils;
import com.pr.instagramap.util.AppConstants;
import com.pr.instagramap.util.Utils;
import com.pr.instagramapitest.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.instagramap.util.AppConstants.TK;

public class SplashActivity extends AppCompatActivity {
	private InstagramApp instaObj;
	private User ourUser;
	private InterstitialAd interstitialAd;
	private InstagramUser instagramUser;
	private boolean needToShowAd = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//TODO try to login Instagram
		String clientId = getResources().getString(R.string.client_id);
		String clientSecret = getResources().getString(R.string.client_secret);
		String callBackUrl = getResources().getString(R.string.callback_url);


//		Map<String, String> likesMap = new HashMap<>();
//		likesMap.put("permalink1","12345");
//		Utils.saveMap("likes",likesMap);

		if (Utils.isConnected(getApplicationContext())) {
			createAndLoadInterstitialAd();

		} else {
			//todo handle no network state
			return;
		}

		instaObj = new InstagramApp(this, clientId,
				clientSecret, callBackUrl);
		instaObj.setListener(instagramAuthenticationListener);

		String accessToken = instaObj.getAccessToken();
		String userId = instaObj.getUserId();
		//check if user already Loged in
		PaymentServiceAPI paymentService = PaymentServiceAPI.getPaymentService(SplashActivity.this);

		if (!accessToken.isEmpty() && !userId.isEmpty()) {
			//TODO improve
			String userDataBAseUrl = "https://graph.instagram.com/" + userId
					+ "?fields=media,media_count,username"
					+ "&access_token=" + accessToken;
			requestInstagramUser(userDataBAseUrl);
			Log.d("dwd2", "user already signed in");
		} else {
			//try to Log in
			Log.d("dwd2", "trye to login");
			instaObj.authorize();
		}

	}

	private void createAndLoadInterstitialAd() {
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //todo replace test id by live ad
		interstitialAd.loadAd(new AdRequest.Builder().build());
//		final AdRequest adRequest = new AdRequest.Builder()
//				.build();

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.d("dwd", "onAdLoaded Interstitial");
				if (needToShowAd) {
					interstitialAd.show();
				}

				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				Log.d("dwd", "onAdFailedToLoad");
				if (needToShowAd && instagramUser != null) {
					startPostActivity(instagramUser);
				}
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when the ad is displayed.
			}

			@Override
			public void onAdLeftApplication() {
				Log.d("dwd", "onAdLeftApplication");

				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				//todo handle interstitialAd closing and open PostActivity
				// Code to be executed when when the interstitial ad is closed.
				if (instagramUser != null) {
					startPostActivity(instagramUser);
				}
				//interstitialAd.loadAd(new AdRequest.Builder().build());
//				if (needToToast) {
//					Toast.makeText(MainActivity.this, "walpaper has changed", Toast.LENGTH_LONG).show();
//				}

			}
		});
	}


	private void requestInstagramUser(String userDataBAseUrl) {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getInstagramUser(userDataBAseUrl).enqueue(new Callback<InstagramUser>() {
			@Override
			public void onResponse(Call<InstagramUser> call, Response<InstagramUser> response) {
				instagramUser = response.body();
				if (instagramUser != null) {
					int mediaCount = instagramUser.getMediaCount();
					if (mediaCount > 0) {

						//todo get users info - COIN, ID
						//or INSERT NEW ONE

						//instagramUser.setId("havaiUser3"); ///todo remove its for debug
						verifyUserByOurBackend(instagramUser);

					}
				} else {
					instaObj.authorize();
				}
			}

			@Override
			public void onFailure(Call<InstagramUser> call, Throwable t) {
				//todo check for internet case
				Log.d("dwd", "InstaUser Error");
				instaObj.authorize();
			}
		});
	}

	private void verifyUserByOurBackend(InstagramUser instagramUser) {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getUserById(instagramUser.getId(), TK).enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				User user = response.body();
				if (user != null) {
					String userId = instagramUser.getId();
					if ("no user".equals(user.getStatus())) {
						Log.d("SplashActivity", "verifyUserByOurBackend succes NO USER");
						insertNewUserToBackend(instagramUser);
					} else {
						ourUser = user;
						Log.d("SplashActivity", "verifyUserByOurBackend succes LOGIN SUCCES");
						////LOGIN SUCCES FROM BACKEND
						AnalyticUtils.sendLoginEvent(userId);
						//
						startPostActivityOrFullscreenAd(instagramUser);

					}
				}

			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Log.d("dwd", "fail");

			}
		});
	}

	private void startPostActivityOrFullscreenAd(InstagramUser instagramUser) {

		//todo check for fullscreen ad every 2nd login
		if (!checkFullScreenAd()) {
			startPostActivity(instagramUser);
		}

	}


	private boolean checkFullScreenAd() {
		SharedPreferences pSharedPref = getApplicationContext()
				.getSharedPreferences("loginValueCount", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			int c = pSharedPref.getInt("loginCount", 0);
			c++;
			pSharedPref.edit().putInt("loginCount", c).apply();
			if (c % 2 == 0) {
				if (interstitialAd.isLoaded()) {
					interstitialAd.show();
				} else {
					needToShowAd = true;
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}


	InstagramApp.OAuthAuthenticationListener instagramAuthenticationListener = new InstagramApp.OAuthAuthenticationListener() {
		@Override
		public void onSuccess() {
			String currentAccesToken = instaObj.getAccessToken();
			String userId = instaObj.getUserId();

			String userDataBAseUrl = "https://graph.instagram.com/"
					+ userId
					+ "?fields=media,media_count,username"
					+ "&access_token="
					+ currentAccesToken;

			requestInstagramUser(userDataBAseUrl);

			Log.d("dwd", "succes  " + instaObj.getName() + " " + instaObj.getUserId());
			Log.e("Userid", instaObj.getId());
			Log.e("Name", instaObj.getName());
			Log.e("UserId", instaObj.getUserId());

		}

		@Override
		public void onFail(String error) {
			Log.d("dwd", "error Insta OAuthAuthenticationListener");
			Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT)
					.show();
		}
	};


	@Override
	protected void onResume() {
		super.onResume();
	}

	private void startPostActivity(InstagramUser instagramUser) {


		PromotionController promotionController = PromotionController.getInstance();
		promotionController.setCallBack(new PromotionController.PromotionsCallBack() {
			@Override
			public void onPromotionReady(PromotionModel likePromotion, PromotionModel followerPromotion) {

				Intent intent = new Intent(SplashActivity.this, PostsActivity.class);
				intent.putExtra(AppConstants.EXTRA_KEY_INSTAGRAM_USER, instagramUser);
				intent.putExtra(AppConstants.EXTRA_KEY_LIKE_PROMOTIONS, likePromotion);
				intent.putExtra(AppConstants.EXTRA_KEY_FOLLOWER_PROMOTIONS, followerPromotion);
				intent.putExtra(AppConstants.EXTRA_KEY_OUR_USER, ourUser);
				startActivity(intent);
				finish();
			}

			@Override
			public void onPromotionFailed() {


			}
		});

		promotionController.preparePromotions();




	}


	private void insertNewUserToBackend(InstagramUser instagramUser) {
		String userId = instagramUser.getId();
		RestClient.getInstance(getApplicationContext()).getInstaApiService().insertUserToBakend2(userId, TK)
				.enqueue(new Callback<Response2>() {
					@Override
					public void onResponse(Call<Response2> call, Response<Response2> response) {
						Response2 response2 = response.body();
						if (response2 != null && "done".equals(response2.status)) {
							/////INSERT SUCCES
							//todo getUserAfterInsert ones
							Log.d("SplashActivity", "user inserted");
							verifyUserByOurBackend(instagramUser);

							//AnalyticUtils.sendLoginEvent(userId);
							//startPostActivityOrFullscreenAd(instagramUser);

						} else {
							Log.d("SplashActivity", "user inserted failed");
						}

					}

					@Override
					public void onFailure(Call<Response2> call, Throwable t) {
						Log.d("dwd", "user inserted fail");

					}
				});
	}
}
