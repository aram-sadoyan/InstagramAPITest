package com.pr.instagramap;

import com.pr.instagramap.api.model.PurchaseModel;

import java.util.ArrayList;
import java.util.List;


public class AppSettings {

	private List<PurchaseModel> purchaseModels = new ArrayList<>();

	private static AppSettings instance;

	public static AppSettings getInstance() {
		if (instance == null) {
			instance = new AppSettings();
		}
		return instance;
	}


	private AppSettings() {
//		if (context == null) {
//			setContext(SocialinV3.getInstance().getContext());
//		}
//		loadSharedPreferences(false);
	}







//	private void loadSharedPreferences(boolean force) {
//		if (context == null) {
//			context = SocialinV3.getInstance().getContext();
//		}
//		if (context != null) {
//			if (force) {
//				sharedPreferences = context.getSharedPreferences(SUBSCRIPTION_SHARED_PREFERENCES, MODE_PRIVATE);
//			} else {
//				new SharedPreferencesLoader().loadSharedPreference(context,
//						SUBSCRIPTION_SHARED_PREFERENCES,
//						MODE_PRIVATE,
//						loadedSharedPreferences -> sharedPreferences = loadedSharedPreferences);
//			}
//		}
//	}







	public List<PurchaseModel> getPurchaseModels() {
		return purchaseModels;
	}

	public void setPurchaseModels(List<PurchaseModel> purchaseModels) {
		this.purchaseModels.clear();
		this.purchaseModels.addAll(purchaseModels);
	}

}
