
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class NewsDetailEntity {

    @SerializedName("dateInfo")
    private String dateInfo;
    @SerializedName("image")
    private String image;
    @SerializedName("subText")
    private String subText;
    @SerializedName("subTitle")
    private String subTitle;
    @SerializedName("text")
    private String text;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private int type;
    @SerializedName("pdfs")
    private List<NewsDetailPdf> pdfs;
    @SerializedName("images")
    private List<String> images;
    @SerializedName("discountCodeType")
    private Integer discountCodeType;
    @SerializedName("discountCodeAll")
    private String discountCodeAll;

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<NewsDetailPdf> getPdfs(){
        return pdfs;
    }

    public List<String> getImages(){
        return images;
    }

    public Integer getDiscountCodeType() {
        return discountCodeType;
    }

    public void setDiscountCodeType(Integer discountCodeType) {
        this.discountCodeType = discountCodeType;
    }

    public String getDiscountCodeAll() {
        return discountCodeAll;
    }

    public void setDiscountCodeAll(String discountCodeAll) {
        this.discountCodeAll = discountCodeAll;
    }
}
