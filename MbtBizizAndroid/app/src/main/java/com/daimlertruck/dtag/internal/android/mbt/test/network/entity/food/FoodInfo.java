
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FoodInfo implements Parcelable {

    @SerializedName("dateText")
    private String DateText;
    @SerializedName("dateTitle")
    private String DateTitle;
    @SerializedName("foodList")
    private List<FoodList> FoodList;
    @SerializedName("isToday")
    private Boolean IsToday;

    public String getDateText() {
        return DateText;
    }

    public void setDateText(String dateText) {
        DateText = dateText;
    }

    public String getDateTitle() {
        return DateTitle;
    }

    public void setDateTitle(String dateTitle) {
        DateTitle = dateTitle;
    }

    public List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodList> getFoodList() {
        return FoodList;
    }

    public void setFoodList(List<FoodList> foodList) {
        FoodList = foodList;
    }

    public Boolean getIsToday() {
        return IsToday;
    }

    public void setIsToday(Boolean isToday) {
        IsToday = isToday;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DateText);
        dest.writeString(this.DateTitle);
        dest.writeList(this.FoodList);
        dest.writeValue(this.IsToday);
    }

    public FoodInfo() {
    }

    protected FoodInfo(Parcel in) {
        this.DateText = in.readString();
        this.DateTitle = in.readString();
        this.FoodList = new ArrayList<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodList>();
        in.readList(this.FoodList, com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodList.class.getClassLoader());
        this.IsToday = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<FoodInfo> CREATOR = new Parcelable.Creator<FoodInfo>() {
        @Override
        public FoodInfo createFromParcel(Parcel source) {
            return new FoodInfo(source);
        }

        @Override
        public FoodInfo[] newArray(int size) {
            return new FoodInfo[size];
        }
    };
}
