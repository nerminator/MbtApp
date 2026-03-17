package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia;

import com.google.gson.annotations.SerializedName;

public class DetailsItem{

	@SerializedName("id")
	private int id;

	@SerializedName("account")
	private String account;

	@SerializedName("url")
	private String url;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return account;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}