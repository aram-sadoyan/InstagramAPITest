package com.pr.instagramapitest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class AuthenticationDialog extends Dialog {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.auth_dialog);
	}


	private final String redirect_url;
	private final String request_url;
	private AuthenticationListener listener;

	public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
		super(context);
		this.listener = listener;
		this.redirect_url = context.getResources().getString(R.string.callback_url);
		this.request_url = context.getResources().getString(R.string.base_url) +
				"oauth/authorize/?client_id=" +
				context.getResources().getString(R.string.client_id) +
				"&redirect_uri=" + redirect_url +
				"&response_type=token&display=touch&scope=public_content";
	}
}
