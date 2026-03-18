package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class NewsPostBody {


    @SerializedName("type")
    private Integer type;
    @SerializedName("pageNumber")
    private Integer pageNumber;
    @SerializedName("discountType")
    private Integer discountType;
    @SerializedName("locId")
    private Integer locId;

    public NewsPostBody(Integer type, Integer pageNumber, Integer discountType, @Nullable Integer locId) {
        this.type = type;
        this.pageNumber = pageNumber;
        this.discountType = discountType;
        this.locId = locId;
    }

    public NewsPostBody(Integer type, Integer pageNumber) {
        this.type = type;
        this.pageNumber = pageNumber;
    }
}
