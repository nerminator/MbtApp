package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShuttleOptionEntity {
    @SerializedName("typeList")
    @Expose
    private List<TypeListEntity> typeList = null;
    @SerializedName("companyLocationList")
    @Expose
    private List<CompanyLocationListEntity> companyLocationList = null;

    public List<TypeListEntity> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListEntity> typeList) {
        this.typeList = typeList;
    }

    public List<CompanyLocationListEntity> getCompanyLocationList() {
        return companyLocationList;
    }

    public void setCompanyLocationList(List<CompanyLocationListEntity> companyLocationList) {
        this.companyLocationList = companyLocationList;
    }

}
