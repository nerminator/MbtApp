package com.daimler.biziz.android.ui.main.orchestra;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.profile.ProfileEntity;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VmOrchestra extends AndroidViewModel {
    MutableLiveData<ProfileEntity> profileEntity = new MutableLiveData<>();
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> logoutSucced = new SingleLiveEvent<>();


    @Inject
    AbstractApiUtils abstractApiUtils;

    public VmOrchestra(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMOrchestra(this);
        getProfile();
    }

    private void getProfile() {
        isLoading.postValue(true);
        abstractApiUtils.getProfile(new NetworkCallback<BaseResponse<ProfileEntity>>() {
            @Override
            public void onSuccess(BaseResponse<ProfileEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    profileEntity.postValue(response.getResponseData());
                }
                isLoading.postValue(false);
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

    public void logout() {
        isLoading.postValue(true);
        abstractApiUtils.getLogout(new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                isLoading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success){
                    logoutSucced.postValue(true);
                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                isLoading.postValue(false);
                logoutSucced.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                isLoading.postValue(false);
                logoutSucced.postValue(false);
            }
        });
    }


    //region Getter
    public MutableLiveData<ProfileEntity> getProfileEntity() {
        return profileEntity;
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public SingleLiveEvent<Boolean> getLogoutSucced() {
        return logoutSucced;
    }

    //endregion
}
