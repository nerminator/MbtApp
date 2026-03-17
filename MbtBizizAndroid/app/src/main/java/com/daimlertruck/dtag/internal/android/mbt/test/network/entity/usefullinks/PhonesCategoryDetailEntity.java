
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks;

import com.google.gson.annotations.SerializedName;

public class PhonesCategoryDetailEntity {

    @SerializedName("unit")
    private String unit;

    @SerializedName("note")
    private String note;

    @SerializedName("internal")
    private String internal;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }
}
