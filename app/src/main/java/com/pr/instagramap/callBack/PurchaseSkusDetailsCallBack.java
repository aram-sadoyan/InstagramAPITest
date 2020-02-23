package com.pr.instagramap.callBack;

import com.pr.instagramap.api.model.PurchaseModel;

import java.util.List;

public interface PurchaseSkusDetailsCallBack {
	void onSuccess(List<PurchaseModel> addedItems);

	void onFailure();

}
