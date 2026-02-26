
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TimeList implements Parcelable {

    @SerializedName("name")
    private String Name;
    @SerializedName("timeText")
    private String TimeText;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTimeText() {
        return TimeText;
    }

    public void setTimeText(String timeText) {
        TimeText = timeText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.TimeText);
    }

    public TimeList() {
    }

    protected TimeList(Parcel in) {
        this.Name = in.readString();
        this.TimeText = in.readString();
    }

    public static final Parcelable.Creator<TimeList> CREATOR = new Parcelable.Creator<TimeList>() {
        @Override
        public TimeList createFromParcel(Parcel source) {
            return new TimeList(source);
        }

        @Override
        public TimeList[] newArray(int size) {
            return new TimeList[size];
        }
    };
}
