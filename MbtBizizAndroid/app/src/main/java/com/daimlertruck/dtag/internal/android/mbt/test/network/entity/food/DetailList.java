
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DetailList implements Parcelable {

    @SerializedName("name")
    private String Name;
    @SerializedName("value")
    private Long Value;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getValue() {
        return Value;
    }

    public void setValue(Long value) {
        Value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeValue(this.Value);
    }

    public DetailList() {
    }

    protected DetailList(Parcel in) {
        this.Name = in.readString();
        this.Value = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<DetailList> CREATOR = new Parcelable.Creator<DetailList>() {
        @Override
        public DetailList createFromParcel(Parcel source) {
            return new DetailList(source);
        }

        @Override
        public DetailList[] newArray(int size) {
            return new DetailList[size];
        }
    };
}
