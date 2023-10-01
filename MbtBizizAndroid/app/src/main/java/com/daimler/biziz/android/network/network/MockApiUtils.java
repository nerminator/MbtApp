package com.daimler.biziz.android.network.network;

import com.daimler.biziz.android.manager.TokenManager;
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
import com.daimler.biziz.android.network.mockResponse.MOCK_Birthday;
import com.daimler.biziz.android.network.mockResponse.MOCK_CheckPhone;
import com.daimler.biziz.android.network.mockResponse.MOCK_FoodMenu;
import com.daimler.biziz.android.network.mockResponse.MOCK_Login;
import com.daimler.biziz.android.network.mockResponse.MOCK_MonthlyWorkHours;
import com.daimler.biziz.android.network.mockResponse.MOCK_Profile;
import com.daimler.biziz.android.network.mockResponse.MOCK_YearlyWorkHours;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Callback;

public class MockApiUtils extends AbstractApiUtils {
    public static final int DELAY = 1;
    Gson gson = new Gson();

    public MockApiUtils(APIService APIService, TokenManager tokenManager) {
        super(APIService, tokenManager);
    }

    @Override
    public void getCustomTempData(NetworkCallback<String> callback) {
        callback.onSuccess("sana donen cevap burasi berko");
    }

    @Override
    public void checkPhone(CheckPhonePostBody checkPhonePostBody, NetworkCallback<BaseResponse> callback) {
        callback.onSuccess(gson.fromJson(MOCK_CheckPhone.CHECK_PHONE_SUCCESS, BaseResponse.class));
    }

    @Override
    public void login(LoginPostBody loginPostBody, NetworkCallback<BaseResponse<LoginEntity>> callback) {
        Type loginType = new TypeToken<BaseResponse<LoginEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_Login.LOGIN_SUCCESS, loginType ));
    }

    @Override
    public void getNewsDetails(String id, NetworkCallback<BaseResponse<NewsDetailEntity>> callback) {

    }

    @Override
    public void getBirthdays(NetworkCallback<BaseResponse<BirthdayEntity>> callback) {
        Type type = new TypeToken<BaseResponse<BirthdayEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_Birthday.BIRTHDAY_SUCCESS, type ));
    }

    @Override
    public void getFoodMenu( NetworkCallback<BaseResponse<FoodListEntity>> callback) {
        Type type = new TypeToken<BaseResponse<FoodListEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_FoodMenu.FOOD_MENU_SUCCESS, type ));
    }

    @Override
    public void getYearlyWorkHours(int year,NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback) {
        Type type = new TypeToken<BaseResponse<FlexibleWorkEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_YearlyWorkHours.YEARLY_WORK_HOURS, type ));
    }

    @Override
    public void getMonthlyWorkHours(int year,int month, NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback) {
        Type type = new TypeToken<BaseResponse<FlexibleWorkEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_MonthlyWorkHours.MONTHLY_WORK_HOURS, type ));
    }

    @Override
    public void getProfile(NetworkCallback<BaseResponse<ProfileEntity>> callback) {
        Type type = new TypeToken<BaseResponse<ProfileEntity>>(){}.getType();
        callback.onSuccess(gson.fromJson(MOCK_Profile.PROFILE, type ));
    }

    @Override
    public void getNews(NetworkCallback<BaseResponse<NewsEntity>> callback, NewsPostBody body) {
     //   Type type = new TypeToken<BaseResponse<NewsEntity>>(){}.getType();
       // callback.onSuccess(gson.fromJson(MOCK_Profile.PROFILE, type ));
    }

    @Override
    public void getShuttleOption(NetworkCallback<BaseResponse<ShuttleOptionEntity>> callback) {
    }

    @Override
    public void getShuttleList(ShuttleListPostBody shuttleListPostBody, NetworkCallback<BaseResponse<ShuttleListResponse>> callback) {

    }

    @Override
    public Callback getWorkCalendar(String date, NetworkCallback<BaseResponse<WorkCalendarEntity>> callback) {
        return null;
    }

    @Override
    public void getSettings(NetworkCallback<BaseResponse<SettingRootObject>> callback) {

    }

    @Override
    public void changeSettings(ChangeSettingsPostBody changeSettingsPostBody, NetworkCallback<BaseResponse> callback) {

    }

    @Override
    public void getBuildings(NetworkCallback<BaseResponse<List<Residential>>> callback) {

    }

    @Override
    public void getNotifications(NotificationPostBody body, NetworkCallback<BaseResponse<List<Notification>>> callback) {

    }

    @Override
    public void getBadgeCount(NetworkCallback<BaseResponse<Integer>> callback) {

    }

    @Override
    public void saveDeviceInfo(NetworkCallback<BaseResponse> callback, DeviceInfoBody body) {

    }

    @Override
    public void getLogout(NetworkCallback<BaseResponse> callback) {

    }

    @Override
    public void sendQrCode(NetworkCallback<BaseResponse> callback, SendQrCodePostBody body) {

    }

    @Override
    public void getUserConfig(NetworkCallback<BaseResponse<UserConfigEntity>> callback) {

    }

    @Override
    public void getCaptcha(NetworkCallback<BaseResponse<CaptchaEntity>> callback) {

    }

}
