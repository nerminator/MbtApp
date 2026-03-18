package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayslipEntity {

    @Expose
    @SerializedName("base64")
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
