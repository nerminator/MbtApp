package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.DiscountTypeBottomSheet;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    public static final int TYPE_EVENT = 2;
    public static final int TYPE_DISCOUNT = 3;
    public static final int TYPE_FARAWELL = 4;
    public static final int TYPE_DECEASE = 5;
    public static final int TYPE_OTHER = 6;
    public static final int TYPE_NEWS = 7;
    public static final int TYPE_LINKS = 8;
    public static final int TYPE_CONTACTS = 9;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("listText")
    @Expose
    private String listText;

    @SerializedName("monthName")
    @Expose
    private String monthName;

    @SerializedName("discountType")
    @Expose
    private Integer discountType;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("phone")
    @Expose
    private String phone;

    public News(Integer id, Integer type, String listText, String monthName, Integer discountType, String image,String url, String phone) {
        this.id = id;
        this.type = type;
        this.listText = listText;
        this.monthName = monthName;
        this.discountType = discountType;
        this.image = image;
        this.url=url;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
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

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }

    public void setUrl(String url) {   this.url = url;    }

    public void setPhone(String phone) {
        this.phone = phone;
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

            case TYPE_LINKS:
                return R.string.TXT_LOGIN_NEWS_TYPE8;

            case TYPE_CONTACTS:
                return R.string.TXT_LOGIN_NEWS_TYPE9;
        }
        return R.string.TXT_LOGIN_NEWS_TYPE6;
    }

    public String discountTypeName() {
        try {
            return DiscountTypeBottomSheet.catNames[discountType - 2].toUpperCase();

        } catch (Exception e) {

        }
        return null;
    }
}
