package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base;

import com.google.gson.annotations.SerializedName;

public class MenuIncrementBody {

    @SerializedName("menu_key")
    private String menu_key;

    public MenuIncrementBody(String menu_key) {
        this.menu_key = menu_key;
    }

    public String getMenuKey() {
        return menu_key;
    }

    public void setMenuKey(String menu_key) {
        this.menu_key = menu_key;
    }
}


