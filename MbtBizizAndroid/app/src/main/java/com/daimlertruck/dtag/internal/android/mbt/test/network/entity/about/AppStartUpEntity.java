
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about;

import com.google.gson.annotations.SerializedName;

public class AppStartUpEntity {
    @SerializedName("aboutText")
    private String aboutText;

    @SerializedName("appDescriptionText")
    private String appDescription;

    public String getAboutText() {
        return aboutText;
    }

    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }
}
