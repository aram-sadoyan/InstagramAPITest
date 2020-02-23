package com.pr.instagramap.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pr.instagramap.api.InstagramMedia;
import com.pr.instagramap.payment.PaymentServiceAPI;
import com.pr.instagramap.util.Utils;
import com.pr.instagramapitest.R;
import com.pr.instagramap.activity.PostsActivity;
import com.pr.instagramap.api.InstagramPost;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.RestClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	//private List<String> itemUrls = new ArrayList<>();
	private int selectedPosition = 0;
	private FrescoLoader frescoLoader;
	private InstagramUser instagramUser = null;
	private String curentAccesToken = null;
	private Activity context = null;
	List<InstagramMedia.InstagramData> items = new ArrayList<>();
	Map<Integer, String> permaLinkMap = new HashMap<>();

	Map<String, String> savedIdTasks = new HashMap<>();


	private final PostsActivity.OnItemClickListener listener;


	public PostAdapter(
			Activity context,
			List<String> itemUrls,
			InstagramUser instagramUser,
			String curentAccesToken,
			PostsActivity.OnItemClickListener onItemClickListener) {
		this.context = context;
		frescoLoader = new FrescoLoader();
		this.listener = onItemClickListener;
		this.instagramUser = instagramUser;
		this.items = instagramUser.getInstagramMedia().getInstagramData();
		this.curentAccesToken = curentAccesToken;
		savedIdTasks = Utils.loadMap("likes");
	}

	public String getSelectedPermanentLink(int position) {
		String permaLink = "";
		permaLink = permaLinkMap.get(position + 1);
		return permaLink;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.post_adapter_item, parent, false);
		return new PostAdapter.ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemViewHolder, int position) {
		ItemViewHolder holder = (ItemViewHolder) itemViewHolder;
		if (position == 0 || position > items.size()) {
			holder.postParentContainer.setVisibility(View.GONE);
			return;
		}
		InstagramMedia.InstagramData item = items.get(position - 1);
		holder.postParentContainer.setVisibility(View.VISIBLE);


		String postUrlById = "https://graph.instagram.com/"
				+ item.getId()
				+ "?fields=id,media_type,media_url,caption,username,timestamp,permalink,thumbnail_url"
				+ "&access_token=" + curentAccesToken;
		RestClient.getInstance(context).getInstaApiService()
				.getInstagramPostyId(postUrlById).enqueue(new Callback<InstagramPost>() {
			@Override
			public void onResponse(Call<InstagramPost> call, Response<InstagramPost> response) {

				InstagramPost instagramPost = response.body();
				if (instagramPost != null) {
					String currentPermalInk = instagramPost.getPermaLink();
					permaLinkMap.put(position, currentPermalInk);

					// https://www.instagram.com/p/B8UwarYgrpZ/
					Log.d("dwd", "currentPermalInk from adapter " + currentPermalInk);
					String idTask = savedIdTasks.get(currentPermalInk);
					if (idTask != null && !idTask.isEmpty()) {
						/////todo show and set loading bar its exsists





					} else {
						//todo show likes


						if ("VIDEO".equals(instagramPost.getMediaType())) {
							frescoLoader.loadWithParams(Uri.parse(instagramPost.getThumbnailUrl()), holder.postImgView, false);
						} else {
							frescoLoader.loadWithParams(Uri.parse(instagramPost.getMediaUrl()), holder.postImgView, false);
						}
						getElementsFromHtml(instagramPost.getPermaLink(), holder);

					}



				} else {
					Log.d("dwd", "Post null with enque try execute");
				}
			}

			@Override
			public void onFailure(Call<InstagramPost> call, Throwable t) {
				Log.d("dwd", "Post succes");
			}
		});

	}

	private void getElementsFromHtml(String permaLink, ItemViewHolder holder) {
		//File input = new File("/tmp/input.html");
		//String permalink = instagramPosts.get(0).getPermaLink();
		(new ParseURL(holder)).execute(new String[]{permaLink});

	}

	private class ParseURL extends AsyncTask<String, Void, String> {
		ItemViewHolder holder = null;

		public ParseURL(ItemViewHolder holder) {
			this.holder = holder;
		}

		@Override
		protected String doInBackground(String... strings) {
			//StringBuilder buffer = new StringBuilder();
			String returnFinalString = "";
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


					//buffer.append("content [").append(contentStr).append("] rn");

					if (contentStr.contains("Likes")) {
						returnFinalString = contentStr;
						//getLikeFromHtmlContent(contentStr, holder);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return returnFinalString;
		}


		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			getLikeFromHtmlContent(s, holder);
			//Log.d("dwd", "delkjfwf " + s);
			//respText.setText(s);
		}

	}

	@Override
	public int getItemCount() {
		return instagramUser.getMediaCount() + 2;
	}


	private void getLikeFromHtmlContent(String contentStr, ItemViewHolder holder) {
		if (contentStr.isEmpty()) {
			return;
		}
		long likeCount = -1;
		long commentCount = -1;
		try {
			likeCount = (Objects.requireNonNull(NumberFormat.getInstance().parse(contentStr)).intValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		holder.likeTxtView.setText(String.valueOf(likeCount));

		if (likeCount != -1) {
			String likeCountStr = String.valueOf(likeCount);
			contentStr = contentStr.replaceFirst(likeCountStr, "");
			commentCount = Long.parseLong(contentStr.replaceAll("[\\D]", ""));

		}

		holder.commentTxtView.setText(String.valueOf(commentCount));

	}

	public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private SimpleDraweeView postImgView;
		private TextView likeTxtView;
		private TextView commentTxtView;

		private CardView postParentContainer = null;


		ItemViewHolder(View view) {
			super(view);
			itemView.setOnClickListener(this);
			postImgView = view.findViewById(R.id.postImgView);
			postParentContainer = view.findViewById(R.id.postParentContainer);
			likeTxtView = view.findViewById(R.id.likeTxtView);
			commentTxtView = view.findViewById(R.id.commentTxtView);
		}


		@Override
		public void onClick(View v) {
			Log.d("dwd", "item clicked");
//			PaymentServiceAPI paymentService = PaymentServiceAPI.getPaymentService(context);
//			paymentService.requestPurchase(context, "");


			if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
			if (selectedPosition != getAdapterPosition()) {
				int adapterPosition = getAdapterPosition();
//				listener.onItemClick(itemUrls.get(adapterPosition), adapterPosition);
			}
//			notifyItemChanged(selectedPosition);
//			selectedPosition = getAdapterPosition();
//			notifyItemChanged(selectedPosition);
		}
	}
}
