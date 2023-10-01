
package com.daimler.biziz.android.network.entity.birthday;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BirthdayEntity {

    @SerializedName("birthdayList")
    private ArrayList<BirthdayListEntity> BirthdayListEntity;
    @SerializedName("dateInfo")
    private String dateInfo;

    public ArrayList<BirthdayListEntity> getBirthdayListEntity() {
        return BirthdayListEntity;
    }

    public void setBirthdayListEntity(ArrayList<BirthdayListEntity> birthdayListEntity) {
        BirthdayListEntity = birthdayListEntity;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }
}
