package com.daimler.biziz.android.network.entity.transportation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeListEntity implements Serializable {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}