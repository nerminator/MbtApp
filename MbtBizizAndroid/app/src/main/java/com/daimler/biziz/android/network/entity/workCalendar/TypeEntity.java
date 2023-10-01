package com.daimler.biziz.android.network.entity.workCalendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeEntity {

    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("typeColor")
    @Expose
    private String typeColor;
    @SerializedName("dayText")
    @Expose
    private String dayText;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }
}