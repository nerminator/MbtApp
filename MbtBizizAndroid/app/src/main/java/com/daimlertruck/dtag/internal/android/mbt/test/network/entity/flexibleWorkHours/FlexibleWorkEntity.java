
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FlexibleWorkEntity implements Parcelable {

    @SerializedName("plusHoursText")
    private String plusHoursText;

    @SerializedName("minusHoursText")
    private String minusHoursText;

    @SerializedName("monthList")
    private List<FlexibleItemEntity> monthList;

    @SerializedName("dayList")
    private List<FlexibleItemEntity> dayList;

    @SerializedName("yearList")
    private List<Integer> yearList;

    @SerializedName("selectedYear")
    private Integer selectedYear;

    public List<FlexibleItemEntity> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<FlexibleItemEntity> monthList) {
        this.monthList = monthList;
    }

    public List<FlexibleItemEntity> getDayList() {
        return dayList;
    }

    public void setDayList(List<FlexibleItemEntity> dayList) {
        this.dayList = dayList;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<Integer> getYearList() {
        return yearList;
    }

    public void setYearList(List<Integer> yearList) {
        this.yearList = yearList;
    }

    public String getPlusHoursText() {
        return plusHoursText;
    }

    public void setPlusHoursText(String plusHoursText) {
        this.plusHoursText = plusHoursText;
    }

    public String getMinusHoursText() {
        return minusHoursText;
    }

    public void setMinusHoursText(String minusHoursText) {
        this.minusHoursText = minusHoursText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.plusHoursText);
        dest.writeString(this.minusHoursText);
        dest.writeTypedList(this.monthList);
        dest.writeTypedList(this.dayList);
        dest.writeList(this.yearList);
        dest.writeValue(this.selectedYear);
    }

    public FlexibleWorkEntity() {
    }

    protected FlexibleWorkEntity(Parcel in) {
        this.plusHoursText = in.readString();
        this.minusHoursText = in.readString();
        this.monthList = in.createTypedArrayList(FlexibleItemEntity.CREATOR);
        this.dayList = in.createTypedArrayList(FlexibleItemEntity.CREATOR);
        this.yearList = new ArrayList<Integer>();
        in.readList(this.yearList, Integer.class.getClassLoader());
        this.selectedYear = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlexibleWorkEntity> CREATOR = new Parcelable.Creator<FlexibleWorkEntity>() {
        @Override
        public FlexibleWorkEntity createFromParcel(Parcel source) {
            return new FlexibleWorkEntity(source);
        }

        @Override
        public FlexibleWorkEntity[] newArray(int size) {
            return new FlexibleWorkEntity[size];
        }
    };
}
