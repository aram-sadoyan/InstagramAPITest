package com.pr.instagramap.payment;

import android.app.Activity;
import android.content.Context;


import com.pr.instagramap.api.model.PurchaseModel;
import com.pr.instagramap.callBack.PurchaseSkusDetailsCallBack;

import java.util.List;

public abstract class PaymentServiceAPI {

	public static PaymentServiceAPI getPaymentService(Context context) {
		return GoogleInAppBillingPaymentService.getInstance(context);
	}


	public abstract void requestPurchase(Activity activity,final String itemType);



	public abstract void getShopItemsPrices(List<PurchaseModel> purchaseModels, PurchaseSkusDetailsCallBack callBack);

}
