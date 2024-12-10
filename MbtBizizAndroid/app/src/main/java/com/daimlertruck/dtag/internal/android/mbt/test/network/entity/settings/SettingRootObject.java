package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingRootObject {

@SerializedName("notificationSettingList")
@Expose
private List<SettingObject> notificationSettingList = null;

public List<SettingObject> getNotificationSettingList() {
return notificationSettingList;
}

public void setNotificationSettingList(List<SettingObject> notificationSettingList) {
this.notificationSettingList = notificationSettingList;
}

}