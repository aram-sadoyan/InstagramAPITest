package com.pr.instagramap.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pr.instagramap.api.InstagramMedia;
import com.pr.instagramapitest.R;
import com.pr.instagramap.activity.PostsActivity;
import com.pr.instagramap.api.InstagramPost;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<String> itemUrls = new ArrayList<>();
	private int selectedPosition = 0;
	private FrescoLoader frescoLoader;
	private InstagramUser instagramUser = null;
	private String curentAccesToken = null;
	private Context context = null;

	private final PostsActivity.OnItemClickListener listener;


	public PostAdapter(
			Context context,
			List<String> itemUrls,
			InstagramUser instagramUser,
			String curentAccesToken,
			PostsActivity.OnItemClickListener onItemClickListener) {
		this.itemUrls = itemUrls;
		frescoLoader = new FrescoLoader();
		this.listener = onItemClickListener;
		this.instagramUser = instagramUser;
		this.curentAccesToken = curentAccesToken;
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
		InstagramMedia.InstagramData item = instagramUser.getInstagramMedia().getInstagramData().get(position);
		ItemViewHolder holder = (ItemViewHolder) itemViewHolder;

		String postUrlById = "https://graph.instagram.com/"
				+ item.getId()
				+ "?fields=id,media_type,media_url,caption,username,timestamp,permalink"
				+ "&access_token=" + curentAccesToken;
		RestClient.getInstance(context).getWatchApiService()
				.getInstagramPostyId(postUrlById).enqueue(new Callback<InstagramPost>() {
			@Override
			public void onResponse(Call<InstagramPost> call, Response<InstagramPost> response) {
				Log.d("dwd", "Post succes");
				InstagramPost instagramPost = response.body();
				if (instagramPost != null) {
				//	instagramPosts.add(instagramPost);
					Log.d("dwd", "Posts index " + instagramPost.getPermaLink());
					holder.permanentLinkTxtView.setText(instagramPost.getPermaLink());
				} else {
					Log.d("dwd", "Post null with enque try execute");
				}


//				if (instagramPosts.size() == instagramDatas.size()) {
//					getElementsFromHtml();
//				}


			}

			@Override
			public void onFailure(Call<InstagramPost> call, Throwable t) {
				Log.d("dwd", "Post succes");

			}
		});

	}

	@Override
	public int getItemCount() {
		return instagramUser.getMediaCount();
	}


	public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private SimpleDraweeView icModel;
		private SimpleDraweeView indicatorView;
		private TextView permanentLinkTxtView = null;


		ItemViewHolder(View view) {
			super(view);
			itemView.setOnClickListener(this);
			//icModel = view.findViewById(R.id.watchitem);
			//indicatorView = view.findViewById(R.id.indicatorView);
			permanentLinkTxtView = view.findViewById(R.id.permanentLinkTxtView);

		}


		@Override
		public void onClick(View v) {
			Log.d("dwd", "item clicked");
//			if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
//			if (selectedPosition != getAdapterPosition()) {
//				int adapterPosition = getAdapterPosition();
//				listener.onItemClick(itemUrls.get(adapterPosition), adapterPosition);
//			}
//			notifyItemChanged(selectedPosition);
//			selectedPosition = getAdapterPosition();
//			notifyItemChanged(selectedPosition);
		}
	}
}
