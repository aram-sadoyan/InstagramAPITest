package com.pr.instagramapitest.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InstagramMedia implements Serializable {

	@SerializedName("data")
	private List<InstagramData> instagramData = new ArrayList<>();

	@SerializedName("paging")
	private Instagrampaging paging;

	public Instagrampaging getPaging() {
		return paging;
	}

	public void setPaging(Instagrampaging paging) {
		this.paging = paging;
	}

	public List<InstagramData> getInstagramData() {
		return instagramData;
	}

	public void setInstagramData(List<InstagramData> instagramData) {
		this.instagramData = instagramData;
	}

	public class InstagramData implements Serializable {

		@SerializedName("id")
		private String id;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public class Instagrampaging implements Serializable{

		@SerializedName("cursors")
		private Cursors cursors;


		public Cursors getCursors() {
			return cursors;
		}

		public void setCursors(Cursors cursors) {
			this.cursors = cursors;
		}

		public class Cursors implements Serializable{

			@SerializedName("before")
			private String beforStr;

			@SerializedName("after")
			private String after;


			public String getBeforStr() {
				return beforStr;
			}

			public void setBeforStr(String beforStr) {
				this.beforStr = beforStr;
			}

			public String getAfter() {
				return after;
			}

			public void setAfter(String after) {
				this.after = after;
			}
		}

	}
}



