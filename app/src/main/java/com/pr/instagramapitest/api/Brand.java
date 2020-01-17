package com.pr.instagramapitest.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brand implements Serializable {

	@SerializedName("BrendName")
	private String name;

	@SerializedName("BrendLogo")
	private String icUrl;

//	@SerializedName("models")
//	private
//	List<Model> models = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcUrl() {
		return icUrl;
	}

	public void setIcUrl(String icUrl) {
		this.icUrl = icUrl;
	}

//	public List<Model> getModels() {
//		return models;
//	}
//
//	public void setModels(List<Model> models) {
//		this.models = models;
//	}
}