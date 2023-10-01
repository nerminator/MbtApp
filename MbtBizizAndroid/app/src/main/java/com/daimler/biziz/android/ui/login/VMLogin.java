package com.daimler.biziz.android.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.daimler.biziz.android.BuildConfig;
import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.login.CheckPhonePostBody;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMLogin extends AndroidViewModel {
    private static final String TAG = "VMLogin";

    @Inject
    AbstractApiUtils abstractApiUtils;

    MutableLiveData<String> phone = new MutableLiveData<>();
    MutableLiveData<String> phoneValue = new MutableLiveData<>();
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> isButtonEnabled = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> loginSuccessed = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();

    public VMLogin(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMLogin(this);
        isButtonEnabled.postValue(false);
    }

    public void performClick(View view) {
        isLoading.postValue(true);
        abstractApiUtils.checkPhone(createPostBody(), new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                isLoading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success){
                    loginSuccessed.postValue(true);
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
        checkPhonePostBody.setPhoneNumber(phoneValue.getValue());
        checkPhonePostBody.setAppVersion("1.2.0");
        return checkPhonePostBody;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getPhoneValue() {
        return phoneValue;
    }

    public SingleLiveEvent<Boolean> getIsButtonEnabled() {
        return isButtonEnabled;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

}
