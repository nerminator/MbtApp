package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

public class PayslipOtpVerifyRequest {
    private String code;

    public PayslipOtpVerifyRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}