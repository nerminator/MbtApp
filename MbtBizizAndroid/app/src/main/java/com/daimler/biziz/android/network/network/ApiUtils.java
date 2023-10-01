package com.daimler.biziz.android.network.network;

import android.util.Log;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils extends AbstractApiUtils {


    public ApiUtils(APIService APIService, TokenManager tokenManager) {
        super(APIService, tokenManager);
    }

    @Override
    public void getCustomTempData(NetworkCallback<String> callback) {
        sendRequest(APIService.getTempModel(), callback);
    }

    @Override
    public void checkPhone(CheckPhonePostBody checkPhonePostBody, NetworkCallback<BaseResponse> callback) {
        sendRequest(APIService.checkPhone(checkPhonePostBody), callback);

    }

    @Override
    public void login(LoginPostBody loginPostBody, NetworkCallback<BaseResponse<LoginEntity>> callback) {
        sendRequest(APIService.login(loginPostBody), callback);

    }

    @Override
    public void getNewsDetails(String id, NetworkCallback<BaseResponse<NewsDetailEntity>> callback) {
        sendRequest(APIService.getNewsDetail(id), callback);
    }

    @Override
    public void getBirthdays(NetworkCallback<BaseResponse<BirthdayEntity>> callback) {
        sendRequest(APIService.getBirthdays(), callback);
    }

    @Override
    public void getFoodMenu(NetworkCallback<BaseResponse<FoodListEntity>> callback) {
        sendRequest(APIService.getFoodMenu(), callback);
    }

    @Override
    public void getYearlyWorkHours(int year, NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback) {
        sendRequest(APIService.getYearlyWorkHours(year), callback);
    }

    @Override
    public void getMonthlyWorkHours(int year, int month, NetworkCallback<BaseResponse<FlexibleWorkEntity>> callback) {
        sendRequest(APIService.getMonthlyWorkHours(year, month), callback);
    }

    @Override
    public void getProfile(NetworkCallback<BaseResponse<ProfileEntity>> callback) {
        sendRequest(APIService.getProfile(), callback);
    }

    @Override
    public void getNews(NetworkCallback<BaseResponse<NewsEntity>> callback, NewsPostBody body) {
        sendRequest(APIService.getNews(body), callback);
    }

    @Override
    public void getShuttleOption(NetworkCallback<BaseResponse<ShuttleOptionEntity>> callback) {
        sendRequest(APIService.getShuttleOptions(), callback);
    }

    @Override
    public void getShuttleList(ShuttleListPostBody shuttleListPostBody, NetworkCallback<BaseResponse<ShuttleListResponse>> callback) {
        sendRequest(APIService.getShuttleList(shuttleListPostBody), callback);
    }

    @Override
    public Callback getWorkCalendar(String date, NetworkCallback<BaseResponse<WorkCalendarEntity>> callback) {
        return sendRequest(APIService.getWorkCalendar(date), callback);
    }

    @Override
    public void getSettings(NetworkCallback<BaseResponse<SettingRootObject>> callback) {
        sendRequest(APIService.getSettings(), callback);
    }

    @Override
    public void changeSettings(ChangeSettingsPostBody changeSettingsPostBody, NetworkCallback<BaseResponse> callback) {
        sendRequest(APIService.changeNotificationSettings(changeSettingsPostBody), callback);
    }

    @Override
    public void getBuildings(NetworkCallback<BaseResponse<List<Residential>>> callback) {
        sendRequest(APIService.getBuildings(), callback);
    }

    @Override
    public void getNotifications(NotificationPostBody body, NetworkCallback<BaseResponse<List<Notification>>> callback) {
        sendRequest(APIService.getNotifications(body), callback);
    }

    @Override
    public void getBadgeCount(NetworkCallback<BaseResponse<Integer>> callback) {
        sendRequest(APIService.getBadgeCount(), callback);
    }

    @Override
    public void saveDeviceInfo(NetworkCallback<BaseResponse> callback, DeviceInfoBody body) {
        sendRequest(APIService.saveDeviceInfo(body), callback);
    }

    @Override
    public void getLogout(NetworkCallback<BaseResponse> callback) {
        sendRequest(APIService.getLogout(),callback);

    }

    @Override
    public void sendQrCode(NetworkCallback<BaseResponse> callback, SendQrCodePostBody body) {
        sendRequest(APIService.sendQrCode(body),callback);

    }

    @Override
    public void getUserConfig(NetworkCallback<BaseResponse<UserConfigEntity>> callback) {
        sendRequest(APIService.getUserConfig(),callback);
    }

    @Override
    public void getCaptcha(NetworkCallback<BaseResponse<CaptchaEntity>> callback) {
        sendRequest(APIService.getCaptcha(),callback);
    }

    public <R> Callback sendRequest(Call<R> call, final NetworkCallback<R> callBack) {
        Callback<R> requestCallback = new Callback<R>() {
            @Override
            public void onResponse(Call<R> call, retrofit2.Response<R> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        callBack.onSuccess(response.body());
                        callBack.onFinish();
                    }
                } else {
                    if (callBack != null) {
                        callBack.onServiceFailure(response.code(), "");
                    }
                }


            }

            @Override
            public void onFailure(Call<R> call, Throwable throwable) {
                if (callBack != null) {
                    callBack.onNetworkFailure(throwable);
                    callBack.onFinish();
                }
            }
        };
        call.enqueue(requestCallback);
        return requestCallback;
    }


/*    @Override
    public Observable<DataHolder<LoginModel>> login(String userName, String password, String otpCode, String captchaCode) {
        Observable<DataHolder<LoginModel>> loginObservable = customCallbackSender.sendRequest(APIService.firstLogin("password", userName.toUpperCase(), password, otpCode, captchaCode));
        Observable<DataHolder<LoginModel>> starttedObservable = Observable.just(DataHolder.createStartedDataHolderForRemote());
        Observable concated = starttedObservable.concatWith(loginObservable);
        return concated;
    }*/


}