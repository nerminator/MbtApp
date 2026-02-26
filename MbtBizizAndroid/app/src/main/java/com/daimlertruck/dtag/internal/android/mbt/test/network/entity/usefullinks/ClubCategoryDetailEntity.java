
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClubCategoryDetailEntity {

    @SerializedName("responsible")
    private String responsible;

    @SerializedName("contact")
    private String contact;

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
