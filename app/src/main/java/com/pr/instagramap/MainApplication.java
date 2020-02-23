package com.pr.instagramap;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;
import com.pr.instagramap.api.RestClient;
import com.pr.instagramap.api.model.PurchaseModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.instagramap.util.AppConstants.TK;

public class MainApplication extends Application {
	public static MainApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		//TODO check NO NETWORK case
		instance = this;

		MobileAds.initialize(this, initializationStatus ->
				Log.d("dwd","initializationStatus= " + initializationStatus.getAdapterStatusMap()));


		//test AdMob app id
		Fresco.initialize(this);

		requestPurchaseList();

	}

	private void requestPurchaseList() {
		RestClient.getInstance(getApplicationContext()).getInstaApiService()
				.getPurchaseList(TK).enqueue(new Callback<List<PurchaseModel>>() {
			@Override
			public void onResponse(Call<List<PurchaseModel>> call, @NotNull Response<List<PurchaseModel>> response) {
				List<PurchaseModel> purchaseModels = response.body();
				if (purchaseModels != null && !purchaseModels.isEmpty()){
					AppSettings.getInstance().setPurchaseModels(purchaseModels);
				}

			}

			@Override
			public void onFailure(Call<List<PurchaseModel>> call, Throwable t) {
				Log.d("dwd","purchase Lkist isempty");

			}

		});

	}

	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	public static MainApplication getInstance() {
		return instance;
	}
}
