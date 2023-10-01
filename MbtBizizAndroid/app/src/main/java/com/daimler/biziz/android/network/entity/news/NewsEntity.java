package com.daimler.biziz.android.network.entity.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsEntity {


    @SerializedName("newsList")
    private List<News> news;

    @SerializedName("birthdayCount")
    private Integer birthdayCount;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public Integer getBirthdayCount() {
        return birthdayCount;
    }

    public void setBirthdayCount(Integer birthdayCount) {
        this.birthdayCount = birthdayCount;
    }
}



