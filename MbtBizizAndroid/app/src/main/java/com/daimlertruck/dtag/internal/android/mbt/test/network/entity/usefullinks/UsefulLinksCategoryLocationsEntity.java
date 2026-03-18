
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsefulLinksCategoryLocationsEntity {

    @SerializedName("locs")
    private ArrayList<UsefulLinksCategoryLocation> locations;

    public ArrayList<UsefulLinksCategoryLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<UsefulLinksCategoryLocation> locations) {
        this.locations = locations;
    }
}
