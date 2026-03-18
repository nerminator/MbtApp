
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClubCategoryEntity {

    @SerializedName("name")
    private String name;

    @SerializedName("details")
    private ArrayList<ClubCategoryDetailEntity> details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ClubCategoryDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ClubCategoryDetailEntity> details) {
        this.details = details;
    }
}
