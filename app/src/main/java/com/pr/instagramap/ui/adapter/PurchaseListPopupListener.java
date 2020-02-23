package com.pr.instagramap.ui.adapter;


import com.pr.instagramap.api.model.PurchaseModel;

public abstract class PurchaseListPopupListener {

	public abstract void onButtonClick(PurchaseModel isActionButton);

	public void onDismiss() {

	}

	public  void onCancel() {

	}

	public void onShow() {

	}
}