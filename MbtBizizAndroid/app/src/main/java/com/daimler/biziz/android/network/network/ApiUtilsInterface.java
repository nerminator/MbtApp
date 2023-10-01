package com.daimler.biziz.android.network.network;


import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.birthday.BirthdayEntity;
import com.daimler.biziz.android.network.entity.captcha.CaptchaEntity;
import com.daimler.biziz.android.network.entity.flexibleWorkHours.FlexibleWorkEntity;
import com.daimler.biziz.android.network.entity.food.FoodListEntity;
import com.daimler.biziz.android.network.entity.login.CheckPhonePostBody;
import com.daimler.biziz.android.network.entity.login.LoginEntity;
import com.daimler.biziz.android.network.entity.login.LoginPostBody;
import com.daimler.biziz.android.network.entity.news.NewsEntity;
import com.daimler.biziz.android.network.entity.news.NewsPostBody;
import com.daimler.biziz.android.network.entity.newsDetail.NewsDetailEntity;
import com.daimler.biziz.android.network.entity.notifications.DeviceInfoBody;
import com.daimler.biziz.android.network.entity.notifications.Notification;
import com.daimler.biziz.android.network.entity.notifications.NotificationPostBody;
import com.daimler.biziz.android.network.entity.place.Building;

import com.daimler.biziz.android.network.entity.place.Residential;
import com.daimler.biziz.android.network.entity.profile.ProfileEntity;
import com.daimler.biziz.android.network.entity.qr.SendQrCodePostBody;
import com.daimler.biziz.android.network.entity.qr.UserConfigEntity;
import com.daimler.biziz.android.network.entity.settings.ChangeSettingsPostBody;
import com.daimler.biziz.android.network.entity.settings.SettingRootObject;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListPostBody;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListResponse;
import com.daimler.biziz.android.network.entity.transportation.ShuttleOptionEntity;
import com.daimler.biziz.android.network.entity.workCalendar.WorkCalendarEntity;

import java.util.List;

import retrofit2.Callback;

interface ApiUtilsInterface {
    void getCustomTempData(NetworkCallback<String> callback);

    void checkPhone(CheckPhonePostBody checkPhonePostBody, NetworkCallback<BaseResponse> callback);

    void login(LoginPostBody loginPostBody, NetworkCallback<BaseResponse<LoginEntity>> callback);

    void getNewsDetails(String id, NetworkCallback<BaseResponse<NewsDetailEntity>> callback);

    void getBirthdays(NetworkCallback<BaseResponse<BirthdayEntity>> callback);

    void getFoodMenu(NetworkCallback<BaseResponse<FoodListEntity>> callback);

    void getYearlyWorkHours(int year, NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback);

    void getMonthlyWorkHours(int year, int month, NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback);

    void getProfile(NetworkCallback<BaseResponse<ProfileEntity>> callback);

    void getNews(NetworkCallback<BaseResponse<NewsEntity>> callback, NewsPostBody body);

    void getShuttleOption(NetworkCallback<BaseResponse<ShuttleOptionEntity>> callback);

    void getShuttleList(ShuttleListPostBody shuttleListPostBody, NetworkCallback<BaseResponse<ShuttleListResponse>> callback);

    Callback getWorkCalendar(String date, NetworkCallback<BaseResponse<WorkCalendarEntity>> callback);

    void getSettings(NetworkCallback<BaseResponse<SettingRootObject>> callback);

    void changeSettings(ChangeSettingsPostBody changeSettingsPostBody, NetworkCallback<BaseResponse> callback);

    void getBuildings(NetworkCallback<BaseResponse<List<Residential>>> callback);

    void getNotifications(NotificationPostBody body, NetworkCallback<BaseResponse<List<Notification>>> callback);

    void getBadgeCount(NetworkCallback<BaseResponse<Integer>> callback);

    void saveDeviceInfo(NetworkCallback<BaseResponse> callback, DeviceInfoBody body);

    void getLogout(NetworkCallback<BaseResponse> callback);

    void sendQrCode(NetworkCallback<BaseResponse> callback, SendQrCodePostBody body);

    void getUserConfig(NetworkCallback<BaseResponse<UserConfigEntity>> callback);

    void getCaptcha(NetworkCallback<BaseResponse<CaptchaEntity>> callback);

    //Observable<DataHolder<LoginModel>> login(String userName, String password, String otpCode, String captchaCode);


}
