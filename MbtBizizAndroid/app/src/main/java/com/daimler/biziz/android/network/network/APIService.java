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
import com.daimler.biziz.android.network.entity.transportation.ShuttleListEntity;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListPostBody;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListResponse;
import com.daimler.biziz.android.network.entity.transportation.ShuttleOptionEntity;
import com.daimler.biziz.android.network.entity.workCalendar.WorkCalendarEntity;

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
}
