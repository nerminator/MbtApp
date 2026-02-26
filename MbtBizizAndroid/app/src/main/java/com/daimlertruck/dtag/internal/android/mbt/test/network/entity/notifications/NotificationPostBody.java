package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications;

import com.google.gson.annotations.SerializedName;

public class NotificationPostBody {


    @SerializedName("pageNumber")
    private String pageNumber;

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public NotificationPostBody(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
