package com.pr.instagramapitest.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InstagramUser implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("username")
	private String userName;

	@SerializedName("media_count")
	private int mediaCount;

	@SerializedName("media")
	private InstagramMedia instagramMedia;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}

	public InstagramMedia getInstagramMedia() {
		return instagramMedia;
	}

	public void setInstagramMedia(InstagramMedia instagramMedia) {
		this.instagramMedia = instagramMedia;
	}


}
