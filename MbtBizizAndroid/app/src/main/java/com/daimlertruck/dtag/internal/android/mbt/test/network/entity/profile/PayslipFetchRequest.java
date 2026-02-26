package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

public class PayslipFetchRequest {
    private int year;
    private int month;

    public PayslipFetchRequest(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}