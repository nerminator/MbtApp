package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login;

import com.google.gson.annotations.SerializedName;

public class CheckPhonePostBody {

    @SerializedName("phoneNumber")
    String phoneNumber;

    @SerializedName("appVersion")
    String appVersion;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
