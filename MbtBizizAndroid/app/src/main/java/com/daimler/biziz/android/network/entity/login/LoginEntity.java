package com.daimler.biziz.android.network.entity.login;

import com.google.gson.annotations.SerializedName;

public class LoginEntity {

    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
