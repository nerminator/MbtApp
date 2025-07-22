package com.daimlertruck.dtag.internal.android.mbt.test.network.network;

import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.CrashLogBody;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @GET("http://echo.jsontest.com/key/value/one/two")
    Call<String> getTempModel();

    @POST("checkPhone")
    Call<BaseResponse> checkPhone(@Body CheckPhonePostBody checkPhonePostBody);

    @POST("login")
    Call<BaseResponse<LoginEntity>> login(@Body LoginPostBody loginPostBody);

    @GET("newsDetail/{id}")
    Call<BaseResponse<NewsDetailEntity>> getNewsDetail(@Path("id") String id);

    @GET("birthdayList")
    Call<BaseResponse<BirthdayEntity>> getBirthdays();

    @GET("foodMenu")
    Call<BaseResponse<FoodListEntity>> getFoodMenu();

    @GET("yearlyWorkHours/{year}")
    Call<BaseResponse<FlexibleWorkEntity>> getYearlyWorkHours(@Path("year") int year);


    @GET("monthlyWorkHours/{year}/{month}")
    Call<BaseResponse<FlexibleWorkEntity>> getMonthlyWorkHours(@Path("year") int year, @Path("month") int month);

    @GET("profile")
    Call<BaseResponse<ProfileEntity>> getProfile();

    @POST("newsList")
    Call<BaseResponse<NewsEntity>> getNews(@Body NewsPostBody body);

    @GET("shuttleOptionList")
    Call<BaseResponse<ShuttleOptionEntity>> getShuttleOptions();

    @POST("shuttleList")
    Call<BaseResponse<ShuttleListResponse>> getShuttleList(@Body ShuttleListPostBody shuttleListPostBody);

    @GET("workCalendar/{date}")
    Call<BaseResponse<WorkCalendarEntity>> getWorkCalendar(@Path("date") String date);

    @GET("notificationSettings")
    Call<BaseResponse<SettingRootObject>> getSettings();

    @POST("changeNotificationSetting")
    Call<BaseResponse> changeNotificationSettings(@Body ChangeSettingsPostBody postBody);

    @GET("maps")
    Call<BaseResponse<List<Residential>>> getBuildings();

    @POST("notificationList")
    Call<BaseResponse<List<Notification>>> getNotifications(@Body NotificationPostBody body);

    @GET("notificationBadgeCount")
    Call<BaseResponse<Integer>> getBadgeCount();

    @POST("saveDeviceInfo")
    Call<BaseResponse> saveDeviceInfo(@Body DeviceInfoBody body);

    @GET("signOut")
    Call<BaseResponse> getLogout();

    @POST("sendQRCode")
    Call<BaseResponse> sendQrCode(@Body SendQrCodePostBody body);


    @GET("userConfig")
    Call<BaseResponse<UserConfigEntity>> getUserConfig();

    @GET("captcha")
    Call<BaseResponse<CaptchaEntity>> getCaptcha();

    @GET("getDiscountCode/{id}")
    Call<BaseResponse<GetDiscountCodeEntity>> getDiscountCode(@Path("id") String id);

    @POST("init")
    Call<BaseResponse<InitEntity>> init(@Body InitPostBody body);

    @GET("socialClubLocs")
    Call<BaseResponse<UsefulLinksCategoryLocationsEntity>> getSocialClubLocs();

    @GET("phoneLocs")
    Call<BaseResponse<UsefulLinksCategoryLocationsEntity>> getPhoneLocs();

    @GET("socialClubs/{id}")
    Call<BaseResponse<UsefulLinksClubsEntity>> getSocialClubsById(@Path("id") Integer id);

    @GET("phones/{id}")
    Call<BaseResponse<PhonesEntity>> getPhonesById(@Path("id") Integer id);

    @POST("submitFeedback")
    Call<BaseResponse> submitFeedback(@Body SubmitFeedbackBody body);

    @POST("sendCrashLog")
    Call<BaseResponse> sendCrashLog(@Body CrashLogBody body);

    @GET("medias")
    Call<BaseResponse<SocialMediaEntity>> getSocialMedias();

    @POST("appStartup")
    Call<BaseResponse<AppStartUpEntity>> appStartUp(@Body AppStartUpBody body);
}
