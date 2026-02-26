package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.qr;

import com.google.gson.annotations.SerializedName;

public class SendQrCodePostBody {

    @SerializedName("code")
    private String code;

    public SendQrCodePostBody(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


