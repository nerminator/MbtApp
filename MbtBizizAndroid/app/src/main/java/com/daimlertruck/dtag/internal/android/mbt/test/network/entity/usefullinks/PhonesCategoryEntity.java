
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhonesCategoryEntity {

    @SerializedName("name")
    private String name;

    @SerializedName("details")
    private ArrayList<PhonesCategoryDetailEntity> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PhonesCategoryDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<PhonesCategoryDetailEntity> details) {
        this.details = details;
    }
}
