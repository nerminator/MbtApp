package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Berkay on 12.06.2018.
 */

public class ChangeSettingsPostBody {
    @SerializedName("type")
    private int type;
    @SerializedName("value")
    private int value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ChangeSettingsPostBody(int type, int value) {
        this.type = type;
        this.value = value;
    }
}
