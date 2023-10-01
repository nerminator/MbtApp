package com.daimler.biziz.android.network.entity.captcha;

import com.google.gson.annotations.SerializedName;

public class CaptchaEntity {

    @SerializedName("token")
    private String uuid;

    @SerializedName("image")
    private String image;

    public CaptchaEntity(String uuid, String image) {
        this.uuid = uuid;
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


