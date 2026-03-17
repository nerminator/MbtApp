
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FlexibleItemEntity implements Parcelable {

    //Common
    @SerializedName("minusHours")
    private String minusHours;

    @SerializedName("plusHours")
    private String plusHours;
    @SerializedName("totalHours")
    private String totalHours;

    //Monthly
    @SerializedName("hoursText")
    private String hoursText;
    @SerializedName("dayText")
    private String dayText;

    //Yearly
    @SerializedName("month")
    private Integer month;


    public String getMinusHours() {
        return minusHours;
    }

    public void setMinusHours(String minusHours) {
        this.minusHours = minusHours;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getPlusHours() {
        return plusHours;
    }

    public void setPlusHours(String plusHours) {
        this.plusHours = plusHours;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getHoursText() {
        return hoursText;
    }

    public void setHoursText(String hoursText) {
        this.hoursText = hoursText;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.minusHours);
        dest.writeString(this.plusHours);
        dest.writeString(this.totalHours);
        dest.writeString(this.hoursText);
        dest.writeString(this.dayText);
        dest.writeValue(this.month);
    }

    public FlexibleItemEntity() {
    }

    protected FlexibleItemEntity(Parcel in) {
        this.minusHours = in.readString();
        this.plusHours = in.readString();
        this.totalHours = in.readString();
        this.hoursText = in.readString();
        this.dayText = in.readString();
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlexibleItemEntity> CREATOR = new Parcelable.Creator<FlexibleItemEntity>() {
        @Override
        public FlexibleItemEntity createFromParcel(Parcel source) {
            return new FlexibleItemEntity(source);
        }

        @Override
        public FlexibleItemEntity[] newArray(int size) {
            return new FlexibleItemEntity[size];
        }
    };
}
