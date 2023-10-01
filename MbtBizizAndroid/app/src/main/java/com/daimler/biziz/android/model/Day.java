package com.daimler.biziz.android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.daimler.biziz.android.BR;
import com.daimler.biziz.android.network.entity.workCalendar.TypeEntity;

import java.util.List;

public class Day extends BaseObservable{
    private String DayNumber;
    private Boolean fromCurrentMonth;
    private Boolean isToday;
    private String dayText;
    private String dateText;
    private List<TypeEntity> typeEntityList = null;
    private boolean isSelected=false;

    public String getDayNumber() {
        return DayNumber;
    }

    public void setDayNumber(String dayNumber) {
        DayNumber = dayNumber;
    }

    public Day() {
    }

    public Day(String dayNumber, Boolean fromCurrentMonth) {
        DayNumber = dayNumber;
        this.fromCurrentMonth = fromCurrentMonth;
        this.isToday = false;
    }

    public Day(String dayNumber, Boolean fromCurrentMonth, Boolean isToday) {
        DayNumber = dayNumber;
        this.fromCurrentMonth = fromCurrentMonth;
        this.isToday = isToday;
    }

    public Boolean getFromCurrentMonth() {
        return fromCurrentMonth;
    }

    public void setFromCurrentMonth(Boolean fromCurrentMonth) {
        this.fromCurrentMonth = fromCurrentMonth;
    }

    public Boolean getToday() {
        return isToday;
    }

    public void setToday(Boolean today) {
        isToday = today;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public List<TypeEntity> getTypeEntityList() {
        return typeEntityList;
    }

    public void setTypeEntityList(List<TypeEntity> typeEntityList) {
        this.typeEntityList = typeEntityList;
    }

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    @Bindable
    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public void setObject(Day day) {
        DayNumber = day.getDayNumber();
        this.fromCurrentMonth = day.getFromCurrentMonth();
        this.isToday = day.getToday();
        this.dayText = day.getDayText();
        this.dateText = day.getDateText();
        this.typeEntityList = day.getTypeEntityList();
    }
}
