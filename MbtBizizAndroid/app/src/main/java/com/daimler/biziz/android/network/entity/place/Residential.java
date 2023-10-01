package com.daimler.biziz.android.network.entity.place;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Residential implements Parcelable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("buildingList")
    private List<Building> buildings;

    @SerializedName("emergencyPoints")
    private List<Building> emergencyPoints;

    @SerializedName("isDefault")
    private boolean isDefault;

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

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<Building> getEmergencyPoints() {
        return emergencyPoints;
    }

    public void setEmergencyPoints(List<Building> emergencyPoints) {
        this.emergencyPoints = emergencyPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.buildings);
        dest.writeTypedList(this.emergencyPoints);
    }

    public Residential() {
    }

    protected Residential(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.buildings = in.createTypedArrayList(Building.CREATOR);
        this.emergencyPoints = in.createTypedArrayList(Building.CREATOR);
    }

    public static final Parcelable.Creator<Residential> CREATOR = new Parcelable.Creator<Residential>() {
        @Override
        public Residential createFromParcel(Parcel source) {
            return new Residential(source);
        }

        @Override
        public Residential[] newArray(int size) {
            return new Residential[size];
        }
    };

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
