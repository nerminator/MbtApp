package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingRoom implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;


    @SerializedName("buildName")
    @Expose
    private String buildName;
    private boolean selected = false;

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public MeetingRoom() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.order);
        dest.writeString(this.buildName);
    }

    protected MeetingRoom(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.order = (Integer) in.readValue(Integer.class.getClassLoader());
        this.buildName = in.readString();
    }

    public static final Creator<MeetingRoom> CREATOR = new Creator<MeetingRoom>() {
        @Override
        public MeetingRoom createFromParcel(Parcel source) {
            return new MeetingRoom(source);
        }

        @Override
        public MeetingRoom[] newArray(int size) {
            return new MeetingRoom[size];
        }
    };

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
