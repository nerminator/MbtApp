package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.qr;

import com.google.gson.annotations.SerializedName;

public class UserConfigEntity {

    @SerializedName("shouldShowQrCode")
    private Boolean shouldShowQrCode;

    public Boolean getShouldShowQrCode() {
        return shouldShowQrCode;
    }

    public void setShouldShowQrCode(Boolean shouldShowQrCode) {
        this.shouldShowQrCode = shouldShowQrCode;
    }
}


