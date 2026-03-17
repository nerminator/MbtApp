
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShuttleList implements Parcelable {

    @SerializedName("location")
    private String Location;
    @SerializedName("timeList")
    private List<TimeList> TimeList;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public List<TimeList> getTimeList() {
        return TimeList;
    }

    public void setTimeList(List<TimeList> timeList) {
        TimeList = timeList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Location);
        dest.writeTypedList(this.TimeList);
    }

    public ShuttleList() {
    }

    protected ShuttleList(Parcel in) {
        this.Location = in.readString();
        this.TimeList = in.createTypedArrayList(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.TimeList.CREATOR);
    }

    public static final Parcelable.Creator<ShuttleList> CREATOR = new Parcelable.Creator<ShuttleList>() {
        @Override
        public ShuttleList createFromParcel(Parcel source) {
            return new ShuttleList(source);
        }

        @Override
        public ShuttleList[] newArray(int size) {
            return new ShuttleList[size];
        }
    };
}
