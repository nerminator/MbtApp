package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverInfoEntity implements Serializable {

    @SerializedName("licensePlate")
    @Expose
    private String licensePlate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("telephone")
    @Expose
    private String telephone;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}