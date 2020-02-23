package com.pr.instagramap.controller;

import android.content.Context;
import android.util.Log;

import com.pr.instagramap.MainApplication;
import com.pr.instagramap.api.model.PurchaseModel;
import com.pr.instagramap.api.model.PurchaseSkuDetail;
import com.pr.instagramap.callBack.PurchaseSkusDetailsCallBack;
import com.pr.instagramap.payment.PaymentServiceAPI;

import java.util.List;

public class CoinPurchaseController {

	private Context context;
	public static CoinPurchaseController instance;

	private static PaymentServiceAPI paymentService = null;


	public static CoinPurchaseController getInstance() {
		if (instance == null) {
			instance = new CoinPurchaseController();
		}
		return instance;
	}

	public static CoinPurchaseController getInstance(Context context) {
		if (instance == null) {
			instance = new CoinPurchaseController();
			instance.setContext(context);
		}
		return instance;
	}

	private CoinPurchaseController() {
		if (context == null) {
			setContext(MainApplication.getInstance().getApplicationContext());
		}

		//paymentService = PaymentServiceAPI.getPaymentService(context);

//		loadSharedPreferences(false);
	}


	public void reuestPurchases(List<PurchaseModel> purchaseModels, PurchaseSkusDetailsCallBack purchaseSkusDetailsCallBack){
		PaymentServiceAPI.getPaymentService(context).getShopItemsPrices(purchaseModels,purchaseSkusDetailsCallBack);
	}



	private void setContext(Context context) {
		this.context = context;
	}


}
