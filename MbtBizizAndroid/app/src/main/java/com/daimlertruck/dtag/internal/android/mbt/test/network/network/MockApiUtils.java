package com.daimlertruck.dtag.internal.android.mbt.test.network.network;

import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.CrashLogBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.MenuIncrementBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.birthday.BirthdayEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.captcha.CaptchaEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.feedback.SubmitFeedbackBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleWorkEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.getDiscountCode.GetDiscountCodeEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.InitEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.InitPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.CheckPhonePostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.LoginEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.LoginPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.NewsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.NewsPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail.NewsDetailEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.DeviceInfoBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.Notification;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.NotificationPostBody;

import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.Residential;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.ActivateCardResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.BusinessCardStateResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.PayslipActiveResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.PayslipEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.ProfileEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.qr.SendQrCodePostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.qr.UserConfigEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.ChangeSettingsPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.settings.SettingRootObject;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.SocialMediaEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleOptionEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.PhonesEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksCategoryLocationsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksClubsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.WorkCalendarEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_Birthday;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_CheckPhone;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_FoodMenu;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_Login;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_MonthlyWorkHours;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_Profile;
import com.daimlertruck.dtag.internal.android.mbt.test.network.mockResponse.MOCK_YearlyWorkHours;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
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

    @Override
    public void getDiscountCode(String id, NetworkCallback<BaseResponse<GetDiscountCodeEntity>> callback) {

    }

    @Override
    public void init(NetworkCallback<BaseResponse<InitEntity>> callback, InitPostBody body) {

    }

    @Override
    public void getSocialClubLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback) {

    }

    @Override
    public void getSocialClubsById(int id, NetworkCallback<BaseResponse<UsefulLinksClubsEntity>> callback) {

    }

    @Override
    public void getPhoneLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback) {

    }

    @Override
    public void getPhonesById(int id, NetworkCallback<BaseResponse<PhonesEntity>> callback) {

    }

    @Override
    public void sendFeedback(SubmitFeedbackBody submitFeedbackBody, NetworkCallback<BaseResponse> callback) {

    }

    public void menuIncrement(MenuIncrementBody body) {

    }

    public void sendCrashLog(CrashLogBody crashLogBody, NetworkCallback<BaseResponse> callback) {

    }

    public Callback fetchPayslip(HashMap<String, Object> body, NetworkCallback<BaseResponse> callback) {
        return null;
    }

    public void verifyPayslipOtp(HashMap<String, Object> body, NetworkCallback<BaseResponse> callback) {
        return;
    }
    public void requestPayslipOtp(NetworkCallback<BaseResponse> callback) {
        return;
    }

    @Override
    public void getPayslipIsActive(NetworkCallback<BaseResponse<PayslipActiveResponse>> callback) {
        callback.onServiceFailure(501, "Not implemented");
    }

    @Override
    public void getSocialMedias(NetworkCallback<BaseResponse<SocialMediaEntity>> callback) {

    }

    @Override
    public void appStartUp(NetworkCallback<BaseResponse<AppStartUpEntity>> callback) {

    }

    @Override
    public void getUserBusinessCardState(NetworkCallback<BaseResponse<BusinessCardStateResponse>> callback) {

    }
    @Override
    public void activateDigitalCard(NetworkCallback<BaseResponse<ActivateCardResponse>> callback) {
    }
    @Override
    public void deactivateDigitalCard(NetworkCallback<BaseResponse> callback) {

    }
}
