package com.daimler.biziz.android.ui.main.portal;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.notifications.DeviceInfoBody;
import com.daimler.biziz.android.network.entity.qr.UserConfigEntity;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

public class VMPortal extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    MutableLiveData<Boolean> qrButtonVisibility = new MutableLiveData<>();

    public VMPortal(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMPortal(this);

    }

    public void sendDeviceInfo() {
        abstractApiUtils.saveDeviceInfo(new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

                //Log.d("VMPortal", "Success");

            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        }, new DeviceInfoBody(FirebaseInstanceId.getInstance().getToken()));
    }


    public void getUserConfig() {
        qrButtonVisibility.postValue(false);
        abstractApiUtils.getUserConfig(new NetworkCallback<BaseResponse<UserConfigEntity>>() {
            @Override
            public void onSuccess(BaseResponse<UserConfigEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    qrButtonVisibility.postValue(true);
                } else {
                    qrButtonVisibility.postValue(false);
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                qrButtonVisibility.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                qrButtonVisibility.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getQrButtonVisibility() {
        return qrButtonVisibility;
    }
}
