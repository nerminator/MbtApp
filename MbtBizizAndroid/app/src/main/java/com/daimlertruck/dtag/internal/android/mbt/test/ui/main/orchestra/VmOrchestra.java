package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.MsalManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.ProfileEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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

        MsalManager.getSingleMsalManager().signOut(new MsalManager.ISignoutCallback() {
            @Override
            public void onSuccess() {
                isLoading.postValue(false);
                logoutSucced.postValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.postValue(false);
                logoutSucced.postValue(true);
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
