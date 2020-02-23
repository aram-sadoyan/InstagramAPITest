package com.pr.instagramap.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfilePictureUrl implements Serializable {

	@SerializedName("logging_page_id")
	private String loggingPageId;

	@SerializedName("graphql")
	private Graphql graphql;

	public String getLoggingPageId() {
		return loggingPageId;
	}

	public void setLoggingPageId(String loggingPageId) {
		this.loggingPageId = loggingPageId;
	}

	public class Graphql {
		@SerializedName("user")
		private User2 user;

		public User2 getUser() {
			return user;
		}

		public void setUser(User2 user) {
			this.user = user;
		}
	}

	public class User2{
		@SerializedName("full_name")
		private String fullName;
		@SerializedName("profile_pic_url")
		private String profilePictureUrl;


		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getProfilePictureUrl() {
			return profilePictureUrl;
		}

		public void setProfilePictureUrl(String profilePictureUrl) {
			this.profilePictureUrl = profilePictureUrl;
		}
	}


	public Graphql getGraphql() {
		return graphql;
	}

	public void setGraphql(Graphql graphql) {
		this.graphql = graphql;
	}
}
