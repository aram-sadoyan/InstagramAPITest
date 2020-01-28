package com.pr.instagramap.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pr.instagramap.api.InstagramMedia;
import com.pr.instagramap.api.InstagramPost;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.RestClient;
import com.pr.instagramap.ui.adapter.PostAdapter;
import com.pr.instagramapitest.R;
import com.pr.instagramap.util.AppConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.instagramap.util.AppConstants.API_ACCESS_TOKEN;
import static com.pr.instagramap.util.AppConstants.SHARED;

public class PostsActivity extends AppCompatActivity {

	private List<InstagramPost> instagramPosts = new ArrayList<>();
	private RecyclerView recyclerView = null;
	private PostAdapter postAdapter = null;
	private int selectedAdapterPosition = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posts);
		recyclerView = findViewById(R.id.recView);

		InstagramUser instagramUser = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			instagramUser = (InstagramUser) getIntent().getExtras()
					.getSerializable(AppConstants.EXTRA_KEY_INSTAGRAM_USER);
		}
		if (instagramUser != null) {
			instagramPosts.clear();
			//requestPostData(instagramUser);
			//requestPostS(instagramUser);

			initRecyclerView(instagramUser);
		}
	}


	private void initRecyclerView(InstagramUser instagramUser) {
		//todo init recycler view with PostAdapter
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
				LinearLayoutManager.VERTICAL, false));
		recyclerView.setItemViewCacheSize(10);
		recyclerView.setDrawingCacheEnabled(true);
		recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		recyclerView.setHasFixedSize(true);
		List<String> itemUrls = new ArrayList<>();

		//TODO saved acces token
		SharedPreferences sharedPref = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		String currentAccesToken = sharedPref.getString(API_ACCESS_TOKEN, null);

		postAdapter = new PostAdapter(getApplicationContext(), itemUrls, instagramUser, currentAccesToken, (icUrl, adapterPosition) -> {
			selectedAdapterPosition = adapterPosition;
			//frescoLoader.loadWithParams(Uri.parse(AppConstants.IMG_URL_PREFFIX + icUrl), mainIcView, false);
			//AppSettings.getInstance().setCurrentMainIcUrl(icUrl);


		});
		recyclerView.setAdapter(postAdapter);
	}


	private void requestPostS(InstagramUser instagramUser) {
		List<InstagramMedia.InstagramData> instagramDatas = instagramUser.getInstagramMedia().getInstagramData();
		SharedPreferences sharedPref = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		String currentAccesToken = sharedPref.getString(API_ACCESS_TOKEN, null);
		for (InstagramMedia.InstagramData instagramData : instagramDatas) {
			String postUrlById = "https://graph.instagram.com/"
					+ instagramData.getId()
					+ "?fields=id,media_type,media_url,caption,username,timestamp,permalink"
					+ "&access_token=" + currentAccesToken;
			RestClient.getInstance(getApplicationContext()).getWatchApiService()
					.getInstagramPostyId(postUrlById).enqueue(new Callback<InstagramPost>() {
				@Override
				public void onResponse(Call<InstagramPost> call, Response<InstagramPost> response) {
					Log.d("dwd", "Post succes");
					InstagramPost instagramPost = response.body();
					if (instagramPost != null) {
						instagramPosts.add(instagramPost);
						Log.d("dwd", "Posts index " + instagramPosts.size());
					} else {
						Log.d("dwd", "Post null with enque try execute");
					}

					if (instagramPosts.size() == instagramDatas.size()) {
						getElementsFromHtml();
					}


				}

				@Override
				public void onFailure(Call<InstagramPost> call, Throwable t) {
					Log.d("dwd", "Post succes");

				}
			});


		}


	}

	private void getElementsFromHtml() {
		//File input = new File("/tmp/input.html");
		String permalink = instagramPosts.get(0).getPermaLink();
		(new ParseURL()).execute(new String[]{permalink});

	}


	private class ParseURL extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... strings) {
			StringBuilder buffer = new StringBuilder();
			try {
				//Log.d("JSwa", "Connecting to [" + strings[0] + "]");
				Document doc = Jsoup.connect(strings[0]).get();
				//Log.d("JSwa", "Connected to [" + strings[0] + "]");
				// Get document (HTML page) title
				//String title = doc.title();
				//Log.d("JSwA", "Title [" + title + "]");
				//buffer.append("Title: " + title + "rn");

				// Get meta info
				Elements metaElems = doc.select("meta");
				//buffer.append("META DATArn");
				//todo remove dublicate Like logic
				for (Element metaElem : metaElems) {
					//String name = metaElem.attr("name");
					String contentStr = metaElem.attr("content");
					buffer.append("content [").append(contentStr).append("] rn");
					if (contentStr.contains("Likes")) {
						getLikeFromHtmlContent(contentStr);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return buffer.toString();
		}


		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Log.d("dwd", "delkjfwf " + s);
			//respText.setText(s);
		}

	}

	private void getLikeFromHtmlContent(String contentStr) {
		Log.d("dwd", "content from Html " + contentStr);
	}

	public interface OnItemClickListener {
		void onItemClick(String icUrl, int adapterPosition);
	}

}
