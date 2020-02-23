package com.pr.instagramap.activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pr.instagramap.AppSettings;
import com.pr.instagramap.api.InstagramPost;
import com.pr.instagramap.api.InstagramUser;
import com.pr.instagramap.api.ProfilePictureUrl;
import com.pr.instagramap.api.RestClient;
import com.pr.instagramap.api.User;
import com.pr.instagramap.api.model.BannerModel;
import com.pr.instagramap.api.model.BossLikeModel;
import com.pr.instagramap.api.model.PromotionModel;
import com.pr.instagramap.api.model.OfferCardItem;
import com.pr.instagramap.api.model.PurchaseModel;
import com.pr.instagramap.api.model.PurchaseSkuDetail;
import com.pr.instagramap.api.model.Response2;
import com.pr.instagramap.callBack.PurchaseSkusDetailsCallBack;
import com.pr.instagramap.controller.CoinPurchaseController;
import com.pr.instagramap.ui.adapter.FrescoLoader;
import com.pr.instagramap.ui.adapter.PopupBuilder;
import com.pr.instagramap.ui.adapter.PostAdapter;
import com.pr.instagramap.ui.adapter.PurchaseListPopupListener;
import com.pr.instagramap.ui.adapter.ZoomCenterCardLayoutManager;
import com.pr.instagramap.util.AppConstants;
import com.pr.instagramap.util.Utils;
import com.pr.instagramapitest.R;
import com.google.android.gms.ads.AdRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.instagramap.util.AppConstants.API_ACCESS_TOKEN;
import static com.pr.instagramap.util.AppConstants.AVATAR_URL_PREFFIX_END;
import static com.pr.instagramap.util.AppConstants.AVATAR_URL_PREFFIX_START;
import static com.pr.instagramap.util.AppConstants.EXTRA_KEY_FOLLOWER_PROMOTIONS;
import static com.pr.instagramap.util.AppConstants.EXTRA_KEY_INSTAGRAM_USER;
import static com.pr.instagramap.util.AppConstants.EXTRA_KEY_LIKE_PROMOTIONS;
import static com.pr.instagramap.util.AppConstants.SHARED;
import static com.pr.instagramap.util.AppConstants.TK;

public class PostsActivity extends AppCompatActivity {

	private List<InstagramPost> instagramPosts = new ArrayList<>();


	private PostAdapter postAdapter = null;
	RecyclerView horizontalRecView = null;
	LinearLayout offerContainer = null;
	LinearLayout addContainer = null;
	SimpleDraweeView avatarImgView = null;
	SimpleDraweeView bottomImgView = null;
	ImageView addCoinTxtView = null;
	TextView coinCountTxtView = null;
	TextView usernameTxtView = null;
	LinearLayout bannerContainer = null;
	SimpleDraweeView bannerImgView = null;
	private View dotView = null;
	private boolean isProfileMode = true;

	List<OfferCardItem> likeCardPromotions = new ArrayList<>();
	List<OfferCardItem> followerCardPromotions = new ArrayList<>();

	private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
	private List<PurchaseSkuDetail> purchaseSkuDetails = new ArrayList<>();
	private List<PurchaseModel> purchaseModels = new ArrayList<>();

	private User ourUser = null;
	private InstagramUser instagramUser = null;

	private int selectedAdapterPosition = -1;

	Map<String, String> savedPostMap = new HashMap<String, String>();

	private void findViews() {
		horizontalRecView = findViewById(R.id.horizontalRecView);
		offerContainer = findViewById(R.id.offerContainer);
		avatarImgView = findViewById(R.id.avatarImgView);
		addCoinTxtView = findViewById(R.id.addCoinTxtView);
		addContainer = findViewById(R.id.addContainer);
		bannerContainer = findViewById(R.id.bannerContainer);
		bannerImgView = findViewById(R.id.bannerImgView);
		dotView = findViewById(R.id.dotView);
		bottomImgView = findViewById(R.id.bottomImgView);
		coinCountTxtView = findViewById(R.id.coinCountTxtView);
		usernameTxtView = findViewById(R.id.usernameTxtView);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		findViews();
		setUpBannerAd();

		PromotionModel likePromotions = null;
		PromotionModel followerPromotions = null;


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			instagramUser = (InstagramUser) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_INSTAGRAM_USER);
			likePromotions = (PromotionModel) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_LIKE_PROMOTIONS);
			followerPromotions = (PromotionModel) getIntent().getExtras()
					.getSerializable(EXTRA_KEY_FOLLOWER_PROMOTIONS);
			ourUser = (User) getIntent().getExtras()
					.getSerializable(AppConstants.EXTRA_KEY_OUR_USER);
		}

		likeCardPromotions = Utils.getPromotionCardItems(likePromotions);
		followerCardPromotions = Utils.getPromotionCardItems(followerPromotions);

		savedPostMap = Utils.loadMap("likes");

//		Map<String, String> savedPostMap2 = Utils.loadMap("likes");
//		savedPostMap2.put("permalink2","4567");
//		Utils.saveMap("likes",savedPostMap2);


		//todo rework this
		if (instagramUser != null) {
			setToolbarTexts(instagramUser.getUserName());
			initAvatarImage(instagramUser.getUserName());
			instagramPosts.clear();
			initBannerView();
			initRecyclerView(instagramUser);
			isProfileMode = true;
			initOffersView(isProfileMode);
			getAndSaveLikePromotion();
			initBottomImageView();
		}


		addCoinTxtView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startPurchaseListDialog();
			}
		});


		//todo for test
		List<PurchaseModel> purchaseModels1 = AppSettings.getInstance().getPurchaseModels();

		if (purchaseModels1.isEmpty()) {
			//todo set defautl values
		}
		CoinPurchaseController.getInstance().reuestPurchases(purchaseModels1, new PurchaseSkusDetailsCallBack() {
			@Override
			public void onSuccess(List<PurchaseModel> addedItems) {
				Log.d("dwd", "jhugyf");
				if (!addedItems.isEmpty()) {
					purchaseModels.addAll(addedItems);

					Log.d("dwd", "kjwhqd");
				} else {
					//todo set default values (purchase list)
				}
			}

			@Override
			public void onFailure() {
				Log.d("dwd", "fau=il");
				//todo handle error case
			}
		});

	}

	private void setToolbarTexts(String userName) {
		if (ourUser != null) {
			coinCountTxtView.setText(ourUser.getCoins());
			usernameTxtView.setText("@" + userName);
		}
	}

	private void initBottomImageView() {
		new FrescoLoader().loadWithParams(
				Uri.parse("https://www.qicharge.am/instalikes/footer/bottom.png"),
				bottomImgView, false);
	}

	private void getAndSaveLikePromotion() {
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().getLikePromotionList(TK)
				.enqueue(new Callback<PromotionModel>() {
					@Override
					public void onResponse(Call<PromotionModel> call, Response<PromotionModel> response) {
						Log.d("dwd", "lkjhkj");
					}

					@Override
					public void onFailure(Call<PromotionModel> call, Throwable t) {
						Log.d("dwd", "fail");

					}
				});
	}

	private void initAvatarImage(String userName) {
		String profileImageUrl = AVATAR_URL_PREFFIX_START + userName + AVATAR_URL_PREFFIX_END;
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService().getProfilePictureUrl(profileImageUrl)
				.enqueue(new Callback<ProfilePictureUrl>() {
					@Override
					public void onResponse(Call<ProfilePictureUrl> call, Response<ProfilePictureUrl> response) {
						if (response.body() == null) {
							return;
						}
						ProfilePictureUrl.Graphql graphql = response.body().getGraphql();
						if (graphql == null) {
							return;
						}
						ProfilePictureUrl.User2 user2 = graphql.getUser();
						if (user2 == null) {
							return;
						}
						String profilePicUrl = user2.getProfilePictureUrl();
						if (profileImageUrl.isEmpty()) {
							return;
						}
						new FrescoLoader().loadWithParams(
								Uri.parse(profilePicUrl),
								avatarImgView, false);


						avatarImgView.setOnClickListener(v -> {
							if (!isProfileMode) {
								isProfileMode = true;
								initOffersView(isProfileMode);
							}
						});
					}


					@Override
					public void onFailure(Call<ProfilePictureUrl> call, Throwable t) {

					}
				});


	}

	private void initBannerView() {
		RestClient.getInstance(getApplicationContext()).
				getInstaApiService().getBanner(TK).enqueue(new Callback<BannerModel>() {
			@Override
			public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
				BannerModel bannerModel = response.body();
				if (bannerModel == null || bannerModel.getUrl().isEmpty()) {
					return;
				}

				new FrescoLoader().loadWithParams(
						Uri.parse(AppConstants.BANNER_URL_PREFFIX + bannerModel.getUrl()),
						bannerImgView, false);


				bannerContainer.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//todo banner click

					}
				});

			}

			@Override
			public void onFailure(Call<BannerModel> call, Throwable t) {
				Log.d("dwd", "fail");

			}
		});


	}

	private void setUpBannerAd() {
		onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				AdView adView = new AdView(getApplicationContext());
				adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int targetAdWidth = (int) (addContainer.getWidth() / displayMetrics.density);
				int targetAdHeight = targetAdWidth * 50 / 320;

				AdSize adSize = new AdSize(targetAdWidth, targetAdHeight);
				//adView.setAdSize(AdSize.FULL_BANNER);
				adView.setAdSize(adSize);

				addContainer.addView(adView);

				AdRequest adRequest = new AdRequest.Builder().build();
				adView.loadAd(adRequest);
//				adView.setAdListener(new AdListener() {
//					@Override
//					public void onAdLoaded() {
//
//					}
//
//				});
				addContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		};
		addContainer.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	private void startPurchaseListDialog() {
		PopupBuilder popupBuilder = new PopupBuilder(PostsActivity.this, purchaseModels);
		popupBuilder.setListeners(new PurchaseListPopupListener() {
			@Override
			public void onButtonClick(PurchaseModel purchaseModel) {
				//start billing flow by this purchaseModels sku
				Log.d("dwd", "kljh");
			}


			@Override
			public void onShow() {
				super.onShow();
			}
		});

		popupBuilder.show();
	}

	private void initOffersView(boolean isProfileMode) {
		offerContainer.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(PostsActivity.this);
		List<OfferCardItem> offerCardItems;
		@DrawableRes int resId = R.drawable.ic_heart;

		if (isProfileMode) {
			//PROFILE CONFIG LIST
			dotView.setSelected(true);
			offerCardItems = likeCardPromotions;
			resId = R.drawable.follower;
		} else {
			//POST CONFIG LIST
			dotView.setSelected(false);
			offerCardItems = followerCardPromotions;
		}

		for (int i = 0; i < offerCardItems.size(); i++) {
			View v = inflater.inflate(R.layout.layout_offer_card_item, offerContainer, false);
			OfferCardItem offerCardItem = offerCardItems.get(i);
			TextView offerTxtView = v.findViewById(R.id.offerTxtView);
			TextView costCoinTxtView = v.findViewById(R.id.costCoinTxtView);
			ImageView leftIcView = v.findViewById(R.id.icLeftView);
			leftIcView.setBackgroundResource(resId);
			offerTxtView.setText(offerCardItem.getOfferCount());
			costCoinTxtView.setText(String.valueOf(offerCardItem.getCostCoinCount()));
			v.setTag(R.id.is_profile_mode, isProfileMode);
			v.setTag(offerCardItem.getCostCoinCount());
			offerContainer.addView(v);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					//todo check for promotion pending

					proceedPromotion((Integer) v.getTag());

				}
			});
		}
	}

	private void proceedPromotion(int likesCount) {
		String currentPermalink = "";
		final int likesCountLocal = likesCount;
		final int taskType = isProfileMode ? 3 : 1;
		if (isProfileMode) {
			//todo get profile permalink url

		} else {
			if (postAdapter != null) {
				currentPermalink = postAdapter.getSelectedPermanentLink(selectedAdapterPosition);
			}
		}

		Map<String, String> likesMap = Utils.loadMap("likes");
		likesMap.put(currentPermalink,"currentPostId");
		Utils.saveMap("likes",likesMap);

		if (true){  ///todo remove its for test
			return;
		}

		//////todo check for coin bavakanacnel local

		RestClient.getInstance(getApplicationContext())
				.getInstaApiService()
				.requestMakePost(
						TK,
						taskType,
						currentPermalink,
						likesCountLocal
				).enqueue(new Callback<BossLikeModel>() {
			@Override
			public void onResponse(Call<BossLikeModel> call, Response<BossLikeModel> response) {
				BossLikeModel bossLikeModel = response.body();
				if (bossLikeModel != null && bossLikeModel.getSuccess()) {

					String bossLikeTaskId = bossLikeModel.getData().getTask().getId();

					Log.d("dwd", "boss like request succes " + bossLikeModel.getData().getTask().getId());
					insertCheckFromOurBackand(
							instagramUser.getId(),
							likesCountLocal,
							taskType,
							bossLikeModel.getData().getTask().getId()
					);
				}

			}

			@Override
			public void onFailure(Call<BossLikeModel> call, Throwable t) {

				Log.d("dwd", "kjh");
			}
		});
	}

	private void insertCheckFromOurBackand(String id, int costCount, int taskType, String idTask) {
		RestClient.getInstance(getApplicationContext())
				.getInstaApiService()
				.requestInsertCheck(TK, taskType, id, costCount, idTask).enqueue(new Callback<Response2>() {
			@Override
			public void onResponse(Call<Response2> call, Response<Response2> response) {
				Log.d("dwd", "insertCheckFromOurBackand succes");
			}

			@Override
			public void onFailure(Call<Response2> call, Throwable t) {
				Log.d("dwd", "insertCheckFromOurBackand fail");

			}
		});

	}


	private void initRecyclerView(InstagramUser instagramUser) {

		List<String> itemUrls = new ArrayList<>();

		SharedPreferences sharedPref = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		String currentAccesToken = sharedPref.getString(API_ACCESS_TOKEN, null);

		postAdapter = new PostAdapter(PostsActivity.this, itemUrls, instagramUser, currentAccesToken, (icUrl, adapterPosition) -> {
			//selectedAdapterPosition = adapterPosition;

		});
		horizontalRecView.setAdapter(postAdapter);

		horizontalRecView.setLayoutManager(new ZoomCenterCardLayoutManager(getApplicationContext(),
				LinearLayoutManager.HORIZONTAL, false));
		horizontalRecView.setItemViewCacheSize(10);
		horizontalRecView.setDrawingCacheEnabled(true);
		horizontalRecView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		horizontalRecView.setHasFixedSize(true);
		horizontalRecView.setOnFlingListener(null);
		PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
		pagerSnapHelper.attachToRecyclerView(horizontalRecView);

		final Handler handler = new Handler();
		handler.postDelayed(() -> horizontalRecView.smoothScrollToPosition(2), 500);


		horizontalRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				Log.d("dwdscroll", "newState " + newState);
				if (isProfileMode) {
					isProfileMode = false;
					initOffersView(isProfileMode);
				}

				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					ZoomCenterCardLayoutManager layoutManager = (ZoomCenterCardLayoutManager) recyclerView.getLayoutManager();
					if (layoutManager == null) {
						return;
					}
					int from = layoutManager.findFirstCompletelyVisibleItemPosition();
					selectedAdapterPosition = from;
					Log.d("dwdscroll", "firstFullVisibleItem" + from);
				}
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				Log.d("dwdscroll", "newState onScrolled " + dx);
			}
		});

		selectedAdapterPosition = 0;

	}


	public interface OnItemClickListener {
		void onItemClick(String icUrl, int adapterPosition);
	}


	@Override
	protected void onDestroy() {
		//AppDataBase.destroyInstance();
		super.onDestroy();
	}

	private void setAvatarMode() {

	}

	private void setPostMode() {

	}

}
