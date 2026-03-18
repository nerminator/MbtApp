
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsefulLinksClubsEntity {

    @SerializedName("clubs")
    private ArrayList<ClubCategoryEntity> clubs;

    public ArrayList<ClubCategoryEntity> getClubs() {
        return clubs;
    }

    public void setClubs(ArrayList<ClubCategoryEntity> clubs) {
        this.clubs = clubs;
    }
}
