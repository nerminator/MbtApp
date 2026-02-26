package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import com.google.gson.annotations.SerializedName;

public class ShuttleListPostBody {
    @SerializedName("type")
    Integer type;

    @SerializedName("companyLocationId")
    Integer companyLocationId;

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

    public ShuttleListPostBody(Integer type, Integer companyLocationId) {
        this.type = type;
        this.companyLocationId = companyLocationId;
    }
}
