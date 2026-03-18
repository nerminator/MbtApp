package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Building implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("meetingRoomList")
    @Expose
    private List<MeetingRoom> meetingRoomList = null;

    @SerializedName("selected")
    @Expose
    private boolean selected = false;

    private boolean isEmergencyMeetingPlace = false;


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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<MeetingRoom> getMeetingRoomList() {
        return meetingRoomList;
    }

    public void setMeetingRoomList(List<MeetingRoom> meetingRoomList) {
        this.meetingRoomList = meetingRoomList;
    }

    public boolean isEmergencyMeetingPlace() {
        return isEmergencyMeetingPlace;
    }

    public void setEmergencyMeetingPlace(boolean emergencyMeetingPlace) {
        isEmergencyMeetingPlace = emergencyMeetingPlace;
    }

    public Building() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.shortName);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeValue(this.order);
        dest.writeTypedList(this.meetingRoomList);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEmergencyMeetingPlace ? (byte) 1 : (byte) 0);
    }

    protected Building(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.shortName = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.order = (Integer) in.readValue(Integer.class.getClassLoader());
        this.meetingRoomList = in.createTypedArrayList(MeetingRoom.CREATOR);
        this.selected = in.readByte() != 0;
        this.isEmergencyMeetingPlace = in.readByte() != 0;
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel source) {
            return new Building(source);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };
}
