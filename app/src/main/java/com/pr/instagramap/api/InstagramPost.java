package com.pr.instagramap.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InstagramPost implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("media_url")
	private String mediaUrl;

	@SerializedName("username")
	private String userName;

	@SerializedName("timestamp")
	private String timeStamp;

	@SerializedName("permalink")
	private String permaLink;

	@SerializedName("caption")
	private String captionTxt;

	@SerializedName("thumbnail_url")
	private String thumbnailUrl;

	public String getCaptionTxt() {
		return captionTxt;
	}

	public void setCaptionTxt(String captionTxt) {
		this.captionTxt = captionTxt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPermaLink() {
		return permaLink;
	}

	public void setPermaLink(String permaLink) {
		this.permaLink = permaLink;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
