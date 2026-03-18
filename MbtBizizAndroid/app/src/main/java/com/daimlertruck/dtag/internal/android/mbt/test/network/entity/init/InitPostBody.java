package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init;

import com.google.gson.annotations.SerializedName;

public class InitPostBody {

    @SerializedName("version")
    private String code;

    @SerializedName("osType")
    private Integer osType;

    @SerializedName("newApp")
    private boolean newApp;

    public InitPostBody(String code) {
        this.code = code;
        this.osType = 1;
        this.newApp = true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}