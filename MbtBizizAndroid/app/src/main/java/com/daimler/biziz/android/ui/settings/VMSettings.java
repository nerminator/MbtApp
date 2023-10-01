package com.daimler.biziz.android.ui.settings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.settings.ChangeSettingsPostBody;
import com.daimler.biziz.android.network.entity.settings.SettingObject;
import com.daimler.biziz.android.network.entity.settings.SettingRootObject;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

/**
 * Created by Berkay on 12.06.2018.
 */

public class VMSettings extends AndroidViewModel {
    private MutableLiveData<SettingRootObject> settingRootObject = new MutableLiveData<>();
    private MutableLiveData<SettingObject> settingObject = new MutableLiveData<>();
    private MutableLiveData<SettingObject> cantChangedSettingObject = new MutableLiveData<>();
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> logoutSucced = new SingleLiveEvent<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMSettings(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMSettings(this);
        getSettings();
    }

    private void getSettings() {
        abstractApiUtils.getSettings(new NetworkCallback<BaseResponse<SettingRootObject>>() {
            @Override
            public void onSuccess(BaseResponse<SettingRootObject> response) {
                settingRootObject.postValue(response.getResponseData());
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

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

    public void changeSettings(SettingObject so) {
        int newVal=so.getValue()==1 ? 0 : 1;
        so.setValue(newVal);
        ChangeSettingsPostBody changeSettingsPostBody=new ChangeSettingsPostBody(so.getType(), so.getValue());
        abstractApiUtils.changeSettings(changeSettingsPostBody, new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatuscode()==200) settingObject.postValue(so);
                else{
                    int responseVal=so.getValue()==1 ? 0 : 1;
                    so.setValue(responseVal);
                    settingObject.postValue(so);
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        });
    }

    public MutableLiveData<SettingRootObject> getSettingRootObject() {
        return settingRootObject;
    }

    public MutableLiveData<SettingObject> getSettingObject() {
        return settingObject;
    }

    public MutableLiveData<SettingObject> getCantChangedSettingObject() {
        return cantChangedSettingObject;
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
}
