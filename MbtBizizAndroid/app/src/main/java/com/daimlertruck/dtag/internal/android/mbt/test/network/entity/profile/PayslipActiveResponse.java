package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

import com.google.gson.annotations.SerializedName;

public class PayslipActiveResponse {
    @SerializedName("isActive")
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }
}
