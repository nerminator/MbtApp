package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import androidx.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingObject extends BaseObservable {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("value")
    @Expose
    private Integer value;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public Integer getValue() {
        return value;
    }

    @Bindable
    public void setValue(Integer value) {
        this.value = value;
        notifyPropertyChanged(BR.value);
    }
}