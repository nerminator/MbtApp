package com.daimler.biziz.android.network.entity.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atakankersit on 05/06/2017.
 */

public class BaseResponse<T> {

    @SerializedName("statusCode")
    @Expose
    private Integer statuscode;

    @SerializedName("responseData")
    @Expose
    private T responseData;

    @SerializedName("errorMessage")
    @Expose
    private String errormessage;

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }


    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
