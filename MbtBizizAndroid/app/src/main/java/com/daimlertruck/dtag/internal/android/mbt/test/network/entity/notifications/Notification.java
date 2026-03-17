package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_DISCOUNT;
import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_EVENT;
import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_FARAWELL;
import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_DECEASE;
import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_NEWS;
import static com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News.TYPE_OTHER;

public class Notification {
    @Expose
    private Integer id;
    @SerializedName("newsId")
    @Expose
    private Integer newsId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("listText")
    @Expose
    private String listText;
    @SerializedName("discountType")
    @Expose
    private Integer discountType;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getListText() {
        return listText;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer typeName() {
        switch (type) {
            case TYPE_EVENT:
                return R.string.TXT_LOGIN_NEWS_TYPE2;
            case TYPE_DISCOUNT:
                return R.string.TXT_LOGIN_NEWS_TYPE3;

            case TYPE_FARAWELL:
                return R.string.TXT_LOGIN_NEWS_TYPE4;

            case TYPE_OTHER:
                return R.string.TXT_LOGIN_NEWS_TYPE6;

            case TYPE_DECEASE:
                return R.string.TXT_LOGIN_NEWS_TYPE5;

            case TYPE_NEWS:
                return R.string.TXT_LOGIN_NEWS_TYPE7;

        }
        return R.string.TXT_LOGIN_NEWS_TYPE2;
    }
}
