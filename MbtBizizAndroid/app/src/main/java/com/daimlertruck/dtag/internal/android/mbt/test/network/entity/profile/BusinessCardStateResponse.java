package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class BusinessCardStateResponse {
    @SerializedName("isActivated")
    private boolean isActive;

    @SerializedName("digitalCardUrl")
    private String digitalCardUrl;

    public boolean isActive() { return isActive; }
    public String getDigitalCardUrl() { return digitalCardUrl; }
}