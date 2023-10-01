package com.daimler.biziz.android.network.entity.news;

import com.google.gson.annotations.SerializedName;

public class NewsPostBody {


    @SerializedName("type")
    private Integer type;
    @SerializedName("pageNumber")
    private Integer pageNumber;
    @SerializedName("discountType")
    private Integer discountType;

    public NewsPostBody(Integer type, Integer pageNumber, Integer discountType) {
        this.type = type;
        this.pageNumber = pageNumber;
        this.discountType = discountType;
    }

    public NewsPostBody(Integer type, Integer pageNumber) {
        this.type = type;
        this.pageNumber = pageNumber;
    }
}
