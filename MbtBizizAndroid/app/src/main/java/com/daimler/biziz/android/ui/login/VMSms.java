package com.daimler.biziz.android.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.login.LoginEntity;
import com.daimler.biziz.android.network.entity.login.LoginPostBody;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import java.util.Observable;

import javax.inject.Inject;

public class VMSms extends AndroidViewModel {
    MutableLiveData<Boolean> isLoginButtonEnabled = new MutableLiveData<>();
    ObservableBoolean isSendAgainEnabled = new ObservableBoolean(false);
    MutableLiveData<String> timer = new MutableLiveData<>();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<Boolean> smsSuccessed = new MutableLiveData<>();
    MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    MutableLiveData<String> pin = new MutableLiveData<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessageForGoBack = new SingleLiveEvent<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    public VMSms(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMSms(this);
        isLoginButtonEnabled.postValue(false);
    }

    public void performClickLogIn(View view) {
        isLoading.postValue(true);
        abstractApiUtils.login(createLoginPostBody(), new NetworkCallback<BaseResponse<LoginEntity>>() {
            @Override
            public void onSuccess(BaseResponse<LoginEntity> response) {
                isLoading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success){
                    smsSuccessed.postValue(true);
                    sharedPreferenceManager.setisLogin("1");

                }else if(response.getStatuscode() == BaseResponseCodes.errorWithMessageGoBack) {
                    showErrorMessageForGoBack.postValue(response.getErrormessage());

                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                isLoading.postValue(false);
                smsSuccessed.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                isLoading.postValue(false);
                smsSuccessed.postValue(false);
            }
        });

    }

    private LoginPostBody createLoginPostBody() {
        LoginPostBody loginPostBody = new LoginPostBody();
        loginPostBody.setPin(pin.getValue());
        loginPostBody.setPhoneNumber(phoneNumber.getValue());
        return loginPostBody;
    }

    public MutableLiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public MutableLiveData<String> getPin() {
        return pin;
    }

    public MutableLiveData<Boolean> getIsLoginButtonEnabled() {
        return isLoginButtonEnabled;
    }

    public ObservableBoolean getIsSendAgainEnabled() {
        return isSendAgainEnabled;
    }

    public MutableLiveData<String> getTimer() {
        return timer;
    }

    public MutableLiveData<Boolean> getSmsSuccessed() {
        return smsSuccessed;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public SingleLiveEvent<String> getShowErrorMessageForGoBack() {
        return showErrorMessageForGoBack;
    }
}
