package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShuttleListResponse implements Serializable {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("companyLocationId")
    @Expose
    private Integer companyLocationId;
    @SerializedName("toCompanyList")
    @Expose
    private List<CompanyListEntity> toCompanyList = null;
    @SerializedName("fromCompanyList")
    @Expose
    private List<CompanyListEntity> fromCompanyList = null;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCompanyLocationId() {
        return companyLocationId;
    }

    public void setCompanyLocationId(Integer companyLocationId) {
        this.companyLocationId = companyLocationId;
    }

    public List<CompanyListEntity> getToCompanyList() {
        return toCompanyList;
    }

    public void setToCompanyList(List<CompanyListEntity> toCompanyList) {
        this.toCompanyList = toCompanyList;
    }

    public List<CompanyListEntity> getFromCompanyList() {
        return fromCompanyList;
    }

    public void setFromCompanyList(List<CompanyListEntity> fromCompanyList) {
        this.fromCompanyList = fromCompanyList;
    }
}