package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShuttleListEntity implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("departureTime")
    @Expose
    private String departureTime;
    @SerializedName("arrivalTime")
    @Expose
    private String arrivalTime;
    @SerializedName("driverInfo")
    @Expose
    private DriverInfoEntity driverInfo;
    @SerializedName("stopList")
    @Expose
    private List<StopListEntity> stopList = null;

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

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public DriverInfoEntity getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfoEntity driverInfo) {
        this.driverInfo = driverInfo;
    }

    public List<StopListEntity> getStopList() {
        return stopList;
    }

    public void setStopList(List<StopListEntity> stopList) {
        this.stopList = stopList;
    }

    public ShuttleListEntity() {
    }

    public ShuttleListEntity(ShuttleListEntity shuttleListEntity) {
        this.id = shuttleListEntity.id;
        this.name = shuttleListEntity.name;
        this.departureTime = shuttleListEntity.departureTime;
        this.arrivalTime = shuttleListEntity.arrivalTime;
        this.driverInfo = shuttleListEntity.driverInfo;
        this.stopList = shuttleListEntity.stopList;
    }
}