package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base;

import com.google.gson.annotations.SerializedName;

public class CrashLogBody {
    @SerializedName("platform")
    private String platform;

    @SerializedName("message")
    private String message;

    @SerializedName("stack_trace")
    private String stackTrace;

    @SerializedName("device_model")
    private String deviceModel;

    @SerializedName("os_version")
    private String osVersion;

    @SerializedName("app_version")
    private String appVersion;

    public CrashLogBody() {
    }

    // Getters and setters (optional, useful if needed elsewhere)
    public String getPlatform() { return platform; }
    public String getMessage() { return message; }
    public String getStackTrace() { return stackTrace; }
    public String getDeviceModel() { return deviceModel; }
    public String getOsVersion() { return osVersion; }
    public String getAppVersion() { return appVersion; }

    public void setPlatform(String platform) { this.platform = platform; }
    public void setMessage(String message) { this.message = message; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }
    public void setOsVersion(String osVersion) { this.osVersion = osVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
}
