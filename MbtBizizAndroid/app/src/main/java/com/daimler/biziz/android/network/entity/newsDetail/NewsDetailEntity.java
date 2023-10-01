
package com.daimler.biziz.android.network.entity.newsDetail;

import com.google.gson.annotations.SerializedName;


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
}
