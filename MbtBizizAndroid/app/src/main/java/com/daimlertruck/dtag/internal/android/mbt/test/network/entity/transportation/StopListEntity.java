package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StopListEntity implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    private Boolean targetStop = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTargetStop() {
        return targetStop;
    }

    public void setTargetStop(Boolean targetStop) {
        this.targetStop = targetStop;
    }

    public StopListEntity(Integer id, String name, Boolean targetStop) {
        this.id = id;
        this.name = name;
        this.targetStop = targetStop;
    }
}