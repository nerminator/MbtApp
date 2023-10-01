package com.daimler.biziz.android.network.entity.workCalendar;

/**
 * Created by Berkay on 10.06.2018.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkCalendarEntity {

    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("dayList")
    @Expose
    private List<DayEntity> dayEntityList = null;
    @SerializedName("initialDayList")
    @Expose
    private List<TypeEntity> initializeDayEntityList=null;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<DayEntity> getDayList() {
        return dayEntityList;
    }

    public void setDayList(List<DayEntity> dayList) {
        this.dayEntityList = dayList;
    }

    public List<TypeEntity> getInitializeDayEntityList() {
        return initializeDayEntityList;
    }

    public void setInitializeDayEntityList(List<TypeEntity> initializeDayEntityList) {
        this.initializeDayEntityList = initializeDayEntityList;
    }
}
