package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail;

import com.google.gson.annotations.SerializedName;

public class NewsDetailPdf {

    @SerializedName("pdf")
    private String pdf;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    public String getPdf(){
        return pdf;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}