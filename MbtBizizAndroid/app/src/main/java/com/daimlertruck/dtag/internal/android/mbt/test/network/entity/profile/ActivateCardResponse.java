package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class ActivateCardResponse {
    @SerializedName("digitalCardUrl")
    private String digitalCardUrl;

    public String getDigitalCardUrl() { return digitalCardUrl; }
}