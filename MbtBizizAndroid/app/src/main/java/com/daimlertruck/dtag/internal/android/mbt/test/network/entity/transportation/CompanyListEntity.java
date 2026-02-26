package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyListEntity implements Serializable, Cloneable{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shuttleList")
    @Expose
    private List<ShuttleListEntity> shuttleList = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShuttleListEntity> getShuttleList() {
        return shuttleList;
    }

    public void setShuttleList(List<ShuttleListEntity> shuttleList) {
        this.shuttleList = shuttleList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CompanyListEntity() {
    }
}