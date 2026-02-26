
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhonesEntity {

    @SerializedName("phones")
    private ArrayList<PhonesCategoryEntity> phones;

    @SerializedName("santral")
    private String santral;

    public ArrayList<PhonesCategoryEntity> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<PhonesCategoryEntity> phones) {
        this.phones = phones;
    }

    public String getSantral() {
        return santral;
    }

    public void setSantral(String santral) {
        this.santral = santral;
    }
}
