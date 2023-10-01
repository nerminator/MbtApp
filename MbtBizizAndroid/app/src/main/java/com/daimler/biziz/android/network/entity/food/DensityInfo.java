
package com.daimler.biziz.android.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DensityInfo implements Parcelable {

    @SerializedName("locationText")
    private String LocationName;
    @SerializedName("locationNumber")
    private Long LocationNumber;
    @SerializedName("percent")
    private Long Percent;
    @SerializedName("text")
    private String Text;

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public Long getLocationNumber() {
        return LocationNumber;
    }

    public void setLocationNumber(Long locationNumber) {
        LocationNumber = locationNumber;
    }

    public Long getPercent() {
        return Percent;
    }

    public void setPercent(Long percent) {
        Percent = percent;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.LocationName);
        dest.writeValue(this.LocationNumber);
        dest.writeValue(this.Percent);
        dest.writeString(this.Text);
    }

    public DensityInfo() {
    }

    protected DensityInfo(Parcel in) {
        this.LocationName = in.readString();
        this.LocationNumber = (Long) in.readValue(Long.class.getClassLoader());
        this.Percent = (Long) in.readValue(Long.class.getClassLoader());
        this.Text = in.readString();
    }

    public static final Parcelable.Creator<DensityInfo> CREATOR = new Parcelable.Creator<DensityInfo>() {
        @Override
        public DensityInfo createFromParcel(Parcel source) {
            return new DensityInfo(source);
        }

        @Override
        public DensityInfo[] newArray(int size) {
            return new DensityInfo[size];
        }
    };
}
