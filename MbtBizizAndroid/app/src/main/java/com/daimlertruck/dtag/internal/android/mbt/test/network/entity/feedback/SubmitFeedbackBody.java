package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.feedback;

import com.google.gson.annotations.SerializedName;

public class SubmitFeedbackBody {

    @SerializedName("text")
    private String text;

    public SubmitFeedbackBody(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


