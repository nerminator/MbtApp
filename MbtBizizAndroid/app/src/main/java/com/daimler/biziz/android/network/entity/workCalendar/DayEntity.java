package com.daimler.biziz.android.network.entity.workCalendar;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayEntity {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("dayText")
    @Expose
    private String dayText;
    @SerializedName("typeList")
    @Expose
    private List<TypeEntity> typeEntityList = null;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public List<TypeEntity> getTypeList() {
        return typeEntityList;
    }

    public void setTypeList(List<TypeEntity> typeList) {
        this.typeEntityList = typeList;
    }

}