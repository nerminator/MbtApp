package com.daimler.biziz.android.network.entity.login;

import com.google.gson.annotations.SerializedName;

public class LoginPostBody {

    @SerializedName("phoneNumber")
    String phoneNumber;

    @SerializedName("pin")
    String pin;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
