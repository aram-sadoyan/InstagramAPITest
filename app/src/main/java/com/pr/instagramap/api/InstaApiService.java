package com.pr.instagramap.api;

import com.pr.instagramap.api.model.BannerModel;
import com.pr.instagramap.api.model.BossLikeModel;
import com.pr.instagramap.api.model.PromotionModel;
import com.pr.instagramap.api.model.PurchaseModel;
import com.pr.instagramap.api.model.Response2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InstaApiService {


//	@GET("TimeWatch/NewJson.php")
//	public Call<Response2> getQiTest();

	@FormUrlEncoded
	@POST("oauth/access_token")
	public Call<AccessToken> proceedLogin2(
			@Field("client_id") String clientId,
			@Field("client_secret") String mClientSecret,
			@Field("grant_type") String grantType,
			@Field("redirect_uri") String redirectUrl,
			@Field("code") String code
	);

	@FormUrlEncoded
	@POST("https://graph.instagram.com/")
	public Call<AccessToken> getInstagramUser2(
			@Field("client_id") String clientId,
			@Field("client_secret") String mClientSecret,
			@Field("grant_type") String grantType,
			@Field("redirect_uri") String redirectUrl,
			@Field("code") String code
	);

	@GET
	Call<InstagramUser> getInstagramUser(@Url String url);

	@GET
	Call<InstagramPost> getInstagramPostyId(@Url String url);

	@GET
	Call<LongLiveAccesToken> getLongLiveAccesToken(@Url String url);

	@GET
	Call<ProfilePictureUrl> getProfilePictureUrl(@Url String url);

//
//	@FormUrlEncoded
//	@POST("https://www.qicharge.am/instalikes/getUsers.php")
//	Call<User> getUserByUserName(@Field("user_name") String email);


	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getPrices.php")
	Call<List<PurchaseModel>> getPurchaseList(@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/firstPageBanner.php")
	Call<BannerModel> getBanner(@Field("taqun_kod") String tK);


	/////////////////// OUR BACKEND REQUESTS
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getUser.php")
	Call<User> getUserById(@Field("user_name") String id,
						   @Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/insertUser.php")
	Call<User> insertUserToBakend(@Field("user_name") String id,
								  @Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/insertUser.php")
	Call<Response2> insertUserToBakend2(@Field("user_name") String id,
										@Field("taqun_kod") String tK);


	///////////// https://www.qicharge.am/instalikes/getLikePrices.php
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getLikePrices.php")
	Call<PromotionModel> getLikePromotionList(@Field("taqun_kod") String tK);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getFollowerPrices.php")
	Call<PromotionModel> getFollowerPromotionList(@Field("taqun_kod") String tK);


	////BOSSS LIKE REQUESTS    1- like 3 - follow
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/makePost.php")
	Call<BossLikeModel> requestMakePost(@Field("taqun_kod") String tK,
										@Field("task_type") int taskType,
										@Field("service_url") String serviceUrl,
										@Field("count") int count
	);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/insertCheck.php")
	Call<Response2> requestInsertCheck
			(@Field("taqun_kod") String tK,
			 @Field("type") int taskType,
			 @Field("user_name") String userId,
			 @Field("cost") int count,
			 @Field("id_task") String idTask
			);

	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/getInfoTask.php")
	Call<BossLikeModel> getInfoTask
			(@Field("taqun_kod") String tK,
			 @Field("id_task") String idTask
			);


	//////ANALYTICS
	//updateLoginCount.php
	@FormUrlEncoded
	@POST("https://www.qicharge.am/instalikes/updateLoginCount.php")
	Call<Response2> addLoginCount(@Field("user_name") String userId,
								  @Field("taqun_kod") String tK);


}
