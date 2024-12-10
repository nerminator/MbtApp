package com.daimlertruck.dtag.internal.android.mbt.test.network.network;

import com.daimlertruck.dtag.internal.android.mbt.test.manager.TokenManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
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
import retrofit2.Callback;

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
        sendRequest(APIService.getLogout(), callback);

    }

    @Override
    public void sendQrCode(NetworkCallback<BaseResponse> callback, SendQrCodePostBody body) {
        sendRequest(APIService.sendQrCode(body), callback);

    }

    @Override
    public void getUserConfig(NetworkCallback<BaseResponse<UserConfigEntity>> callback) {
        sendRequest(APIService.getUserConfig(), callback);
    }

    @Override
    public void getCaptcha(NetworkCallback<BaseResponse<CaptchaEntity>> callback) {
        sendRequest(APIService.getCaptcha(), callback);
    }

    @Override
    public void getDiscountCode(String id, NetworkCallback<BaseResponse<GetDiscountCodeEntity>> callback) {
        sendRequest(APIService.getDiscountCode(id), callback);
    }

    @Override
    public void init(NetworkCallback<BaseResponse<InitEntity>> callback, InitPostBody body) {
        sendRequest(APIService.init(body), callback);
    }

    @Override
    public void getSocialClubLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback) {
        sendRequest(APIService.getSocialClubLocs(), callback);
    }

    @Override
    public void getSocialClubsById(int id, NetworkCallback<BaseResponse<UsefulLinksClubsEntity>> callback) {
        sendRequest(APIService.getSocialClubsById(id), callback);
    }

    @Override
    public void getPhoneLocs(NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>> callback) {
        sendRequest(APIService.getPhoneLocs(), callback);
    }

    @Override
    public void getPhonesById(int id, NetworkCallback<BaseResponse<PhonesEntity>> callback) {
        sendRequest(APIService.getPhonesById(id), callback);
    }

    @Override
    public void sendFeedback(SubmitFeedbackBody submitFeedbackBody, NetworkCallback<BaseResponse> callback) {
        sendRequest(APIService.submitFeedback(submitFeedbackBody), callback);
    }

    @Override
    public void getSocialMedias(NetworkCallback<BaseResponse<SocialMediaEntity>> callback) {
        sendRequest(APIService.getSocialMedias(), callback);
    }

    @Override
    public void appStartUp(NetworkCallback<BaseResponse<AppStartUpEntity>> callback) {
        sendRequest(APIService.appStartUp(new AppStartUpBody("0", "1")), callback);
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