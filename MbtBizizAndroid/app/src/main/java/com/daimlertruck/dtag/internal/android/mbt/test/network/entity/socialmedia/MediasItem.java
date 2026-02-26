package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MediasItem{

	@SerializedName("name")
	private String name;

	@SerializedName("details")
	private List<DetailsItem> details;

	@SerializedName("id")
	private int id;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDetails(List<DetailsItem> details){
		this.details = details;
	}

	public List<DetailsItem> getDetails(){
		return details;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}