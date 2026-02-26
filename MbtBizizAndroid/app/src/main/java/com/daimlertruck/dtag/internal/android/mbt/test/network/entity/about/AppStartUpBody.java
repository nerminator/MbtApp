package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about;

import com.google.gson.annotations.SerializedName;

public class AppStartUpBody {

    @SerializedName("page")
    private String text;

    @SerializedName("type")
    private String type;

    public AppStartUpBody(String text, String type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


