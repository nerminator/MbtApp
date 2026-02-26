package com.daimlertruck.dtag.internal.android.mbt.test.network.network;


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

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

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

    void getDiscountCode(String id, NetworkCallback<BaseResponse<GetDiscountCodeEntity>> callback);

    void init(NetworkCallback<BaseResponse<InitEntity>> callback, InitPostBody body);

    void getSocialClubLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback);

    void getSocialClubsById(int id, NetworkCallback<BaseResponse<UsefulLinksClubsEntity>> callback);

    void getPhoneLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback);

    void getPhonesById(int id, NetworkCallback<BaseResponse<PhonesEntity>> callback);

    void sendFeedback(SubmitFeedbackBody submitFeedbackBody, NetworkCallback<BaseResponse> callback);
    void menuIncrement(MenuIncrementBody body);

    void getSocialMedias(NetworkCallback<BaseResponse<SocialMediaEntity>> callback);

    public void sendCrashLog(CrashLogBody crashLogBody, NetworkCallback<BaseResponse> callback);

    void appStartUp(NetworkCallback<BaseResponse<AppStartUpEntity>> callback);

    void requestPayslipOtp(NetworkCallback<BaseResponse> callback);

    void verifyPayslipOtp(HashMap<String, Object> body, NetworkCallback<BaseResponse> callback);

    Callback fetchPayslip(HashMap<String, Object> body, NetworkCallback<BaseResponse<PayslipEntity>> callback);

    public void getUserBusinessCardState(NetworkCallback<BaseResponse<BusinessCardStateResponse>> callback);

    public void activateDigitalCard(NetworkCallback<BaseResponse<ActivateCardResponse>> callback);

    public void deactivateDigitalCard(NetworkCallback<BaseResponse> callback);

}
