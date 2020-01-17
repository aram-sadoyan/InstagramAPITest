package com.pr.instagramapitest.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InstagramUser implements Serializable {

	@SerializedName("access_token")
	private String accesToken;

	@SerializedName("user")
	private User user;

	public String getAccesToken() {
		return accesToken;
	}

	public void setAccesToken(String accesToken) {
		this.accesToken = accesToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
