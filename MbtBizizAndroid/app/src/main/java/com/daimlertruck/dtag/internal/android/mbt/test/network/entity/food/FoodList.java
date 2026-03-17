
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FoodList implements Parcelable {

    @SerializedName("calorie")
    private Long Calorie;
    @SerializedName("detailList")
    private List<DetailList> DetailList;
    @SerializedName("info")
    private String Info;
    @SerializedName("name")
    private String Name;

    public Long getCalorie() {
        return Calorie;
    }

    public void setCalorie(Long calorie) {
        Calorie = calorie;
    }

    public List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList> detailList) {
        DetailList = detailList;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.Calorie);
        dest.writeList(this.DetailList);
        dest.writeString(this.Info);
        dest.writeString(this.Name);
    }

    public FoodList() {
    }

    protected FoodList(Parcel in) {
        this.Calorie = (Long) in.readValue(Long.class.getClassLoader());
        this.DetailList = new ArrayList<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList>();
        in.readList(this.DetailList, com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList.class.getClassLoader());
        this.Info = in.readString();
        this.Name = in.readString();
    }

    public static final Parcelable.Creator<FoodList> CREATOR = new Parcelable.Creator<FoodList>() {
        @Override
        public FoodList createFromParcel(Parcel source) {
            return new FoodList(source);
        }

        @Override
        public FoodList[] newArray(int size) {
            return new FoodList[size];
        }
    };
}
