
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.birthday;

import com.google.gson.annotations.SerializedName;

public class BirthdayListEntity {

    @SerializedName("name")
    private String name;
    @SerializedName("title")
    private String title;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
