package com.pr.instagramap.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BossLikeModel {

	@SerializedName("status")
	private String status;

	@SerializedName("success")
	private boolean success;

	@SerializedName("data")
	private Data data = new Data();

	public class Data{

		@SerializedName("task")
		private Task task = new Task();

		public Task getTask() {
			return task;
		}

		public class Task{

			@SerializedName("id")
			private String id = "";

			@SerializedName("count")
			private String count;

			@SerializedName("count_complete")
			private String countComplete;

			public String getId() {
				return id;
			}

			public String getCount() {
				return count;
			}

			public String getCountComplete() {
				return countComplete;
			}
		}

	}

	public String getStatus() {
		return status;
	}

	public boolean getSuccess() {
		return success;
	}

	public Data getData() {
		return data;
	}
}
