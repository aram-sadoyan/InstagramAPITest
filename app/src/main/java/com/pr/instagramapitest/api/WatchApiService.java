package com.pr.instagramapitest.api;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
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


	@POST()
	public Call<AccessToken> proceedLogin(
			@Url String url
	);

	@GET
	public Call<User> testToken();


//	@GET("/api/users/{id}")
//	public Call<UserApiResponse> getUser(@Path("id") long id);
//
//	@GET("TimeWatch/NewJson.php")
//	Call<List<Brand>> getUsers();

}
