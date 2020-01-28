package com.pr.instagramapitest.api;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface WatchApiService {

//	@FormUrlEncoded
//	@GET("")
//	Call<Response> addShopPackage();

	@GET("TimeWatch/NewJson.php")
	public Call<Response> getQiTest();


//	@GET("/TimeWatch/kyaj.html")
//	public Call<List<Brand>> getBrandList();

//	@GET("/TimeWatch/NewJson.php")
//	public Call<List<Brand>> getBrandList();
//
//	@GET("/TimeWatch/JsonArduino.php")
//	public Call<List<SensorModelData>> getSensorId();

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




}
