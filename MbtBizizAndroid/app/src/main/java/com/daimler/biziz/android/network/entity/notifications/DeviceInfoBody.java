package com.daimler.biziz.android.network.entity.notifications;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onur Karteper on 3.07.2018.
 */

public class DeviceInfoBody {

    @SerializedName("deviceToken")
    private String deviceToken;


    @SerializedName("osType")
    private Integer osType;

    public DeviceInfoBody(String deviceToken) {
        this.deviceToken = deviceToken;
        osType = 1;
    }

    public String getDeviceToken() {

        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}


