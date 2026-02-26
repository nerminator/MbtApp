
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShuttleInfo implements Parcelable {

    @SerializedName("header")
    private String Header;
    @SerializedName("shuttleList")
    private List<ShuttleList> ShuttleList;

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleList> getShuttleList() {
        return ShuttleList;
    }

    public void setShuttleList(List<com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleList> shuttleList) {
        ShuttleList = shuttleList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Header);
        dest.writeTypedList(this.ShuttleList);
    }

    public ShuttleInfo() {
    }

    protected ShuttleInfo(Parcel in) {
        this.Header = in.readString();
        this.ShuttleList = in.createTypedArrayList(com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleList.CREATOR);
    }

    public static final Parcelable.Creator<ShuttleInfo> CREATOR = new Parcelable.Creator<ShuttleInfo>() {
        @Override
        public ShuttleInfo createFromParcel(Parcel source) {
            return new ShuttleInfo(source);
        }

        @Override
        public ShuttleInfo[] newArray(int size) {
            return new ShuttleInfo[size];
        }
    };
}
