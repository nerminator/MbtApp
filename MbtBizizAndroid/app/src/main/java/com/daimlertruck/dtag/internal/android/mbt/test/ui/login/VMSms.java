package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.CheckPhonePostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.LoginEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.login.LoginPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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
    MutableLiveData<Boolean> resetTimer = new MutableLiveData<>();

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

    public void checkPhone(){
        abstractApiUtils.checkPhone(createPostBody(), new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                isLoading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success){
                    resetTimer.postValue(true);
                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }

            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                isLoading.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                isLoading.postValue(false);
            }
        });
    }

    private CheckPhonePostBody createPostBody() {
        CheckPhonePostBody checkPhonePostBody = new CheckPhonePostBody();
        checkPhonePostBody.setPhoneNumber(phoneNumber.getValue());
        checkPhonePostBody.setAppVersion("1.4.0");
        return checkPhonePostBody;
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

    public MutableLiveData<Boolean> getResetTimer() {
        return resetTimer;
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
