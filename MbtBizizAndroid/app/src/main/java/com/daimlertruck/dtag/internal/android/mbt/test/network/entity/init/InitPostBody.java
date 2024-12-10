package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init;

import com.google.gson.annotations.SerializedName;

public class InitPostBody {

    @SerializedName("version")
    private String code;

    @SerializedName("osType")
    private Integer osType;

    public InitPostBody(String code) {
        this.code = code;
        this.osType = 1;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}