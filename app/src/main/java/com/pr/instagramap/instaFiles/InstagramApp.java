package com.pr.instagramap.instaFiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.pr.instagramap.api.AccessToken;
import com.pr.instagramap.api.LongLiveAccesToken;
import com.pr.instagramap.api.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstagramApp {

	private InstagramSession instaSession;
	private InstagramDialog instDialog;
	private OAuthAuthenticationListener mListener;
	private ProgressDialog mProgress;
	private String mAccessToken;
	private Context mCtx;

	private String mClientId;
	private String mClientSecret;

	private static int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private static int WHAT_FETCH_INFO = 2;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.github.com/)
	 */

	public static String mCallbackUrl = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
	private static final String API_URL = "https://api.instagram.com/v1";

	private static final String TAG = "InstagramAPI";

	public InstagramApp(Context context, String clientId, String clientSecret,
						String callbackUrl) {

		mClientId = clientId;
		mClientSecret = clientSecret;
		mCtx = context;
		instaSession = new InstagramSession(context);
		mAccessToken = instaSession.getAccessToken();
		mCallbackUrl = callbackUrl;

		String mAuthUrl = AUTH_URL
				+ "?client_id="
				+ clientId
				+ "&redirect_uri="
				+ mCallbackUrl
				+ "&scope="
				+ "user_profile,user_media"
				+ "&response_type=code";

		InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");
			}
		};

		instDialog = new InstagramDialog(context, mAuthUrl, listener);
		mProgress = new ProgressDialog(context);
		mProgress.setCancelable(false);
	}

	private void getAccessToken(final String code) {
		String newCode = code.replace("#_", "");

		mProgress.setMessage("Getting access token ...");
		mProgress.show();

		Log.d("dwd", "Getting access token");
		RestClient.getInstance(mCtx).getWatchApiService().proceedLogin2(
				mClientId,
				mClientSecret,
				"authorization_code",
				mCallbackUrl,
				newCode
		).enqueue(new Callback<AccessToken>() {
			@Override
			public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
				AccessToken accessToken = response.body();
				if (accessToken != null) {
					getLongLiveAccessToken(accessToken.getAccessToken(), accessToken.getUserId());
				} else {
					instDialog.dismiss();
					mListener.onFail("boddy is null");
				}
			}

			@Override
			public void onFailure(Call<AccessToken> call, Throwable t) {
				instDialog.dismiss();
				mListener.onFail("boddy is null");
			}
		});
	}

	private void getLongLiveAccessToken(String shortLiveAccessToken, long userId) {
		String url = "https://graph.instagram.com/access_token?grant_type=ig_exchange_token"
				+ "&client_secret="+mClientSecret
				+ "&access_token="+shortLiveAccessToken;
		RestClient.getInstance(mCtx).getWatchApiService().getLongLiveAccesToken(url).enqueue(new Callback<LongLiveAccesToken>() {
			@Override
			public void onResponse(Call<LongLiveAccesToken> call, Response<LongLiveAccesToken> response) {
				Log.d("dwd","succes Long Live id");
				LongLiveAccesToken longLiveAccesToken = response.body();
				if (longLiveAccesToken != null) {
					instaSession.storeAccessToken(
							longLiveAccesToken.getLonLiveAcToken(),
							"someId",
							String.valueOf(userId),
							"Kj",
							"img");

					mListener.onSuccess();
				} else {
					mListener.onFail("boddy is null");
				}
				instDialog.dismiss();
			}

			@Override
			public void onFailure(Call<LongLiveAccesToken> call, Throwable t) {
				instDialog.dismiss();
				mListener.onFail("retrofit error");
			}
		});
	}

//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			if (msg.what == WHAT_ERROR) {
//				mProgress.dismiss();
//				if (msg.arg1 == 1) {
//					mListener.onFail("Failed to get access token");
//				} else if (msg.arg1 == 2) {
//					mListener.onFail("Failed to get user information");
//				}
//			} else if (msg.what == WHAT_FETCH_INFO) {
//				mProgress.dismiss();
//				mListener.onSuccess();
//				// fetchUserName();
//			} else {
//				// mProgress.dismiss();
//				// mListener.onSuccess();
//			}
//		}
//	};

//	public boolean hasAccessToken() {
//		return (mAccessToken == null) ? false : true;
//	}

	public void setListener(OAuthAuthenticationListener listener) {
		mListener = listener;
	}

	// getting username
	public String getUserId() {
		return instaSession.getUserId();
	}

	// getting user id
	public String getId() {
		return instaSession.getId();
	}

	// getting username
	public String getName() {
		return instaSession.getName();
	}

	// getting user image
	public String getUserPicture() {
		return instaSession.getUserImage();
	}

	// getting accesstoken
	public String getAccessToken() {
		return instaSession.getAccessToken();
	}

	public void authorize() {
		// Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
		// webAuthIntent.setData(Uri.parse(AUTH_URL));
		// mCtx.startActivity(webAuthIntent);
		instDialog.show();
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			instaSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	public interface OAuthAuthenticationListener {
		public abstract void onSuccess();

		public abstract void onFail(String error);
	}
}