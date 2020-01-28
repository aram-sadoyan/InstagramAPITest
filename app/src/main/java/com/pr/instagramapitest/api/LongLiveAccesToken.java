package com.pr.instagramapitest.api;

import com.google.gson.annotations.SerializedName;

public class LongLiveAccesToken {

//	 "access_token":"{long-lived-user-access-token}",
//			 "token_type": "bearer",
//			 "expires_in": 5183944

	@SerializedName("access_token")
	private String lonLiveAcToken;

	@SerializedName("token_type")
	private String tokenType;

	@SerializedName("expires_in")
	private long expiresIn;

	public String getLonLiveAcToken() {
		return lonLiveAcToken;
	}

	public void setLonLiveAcToken(String lonLiveAcToken) {
		this.lonLiveAcToken = lonLiveAcToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
}
