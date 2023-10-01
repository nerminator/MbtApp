package com.daimler.biziz.android.ui.transportation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.transportation.ShuttleOptionEntity;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMShuttleOption extends AndroidViewModel {
    private MutableLiveData<ShuttleOptionEntity> shuttleOptionEntity = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedTypeIndex=new MutableLiveData<>();
    public MutableLiveData<Integer> selectedPointIndex=new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMShuttleOption(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMShuttleOption(this);
        getShuttleOptions();
    }

    private void getShuttleOptions() {
        isLoading.postValue(true);
        abstractApiUtils.getShuttleOption(new NetworkCallback<BaseResponse<ShuttleOptionEntity>>() {
            @Override
            public void onSuccess(BaseResponse<ShuttleOptionEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    shuttleOptionEntity.postValue(response.getResponseData());
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

    public MutableLiveData<ShuttleOptionEntity> getShuttleOptionEntity() {
        return shuttleOptionEntity;
    }

    public MutableLiveData<Integer> getSelectedTypeIndex() {
        return selectedTypeIndex;
    }

    public MutableLiveData<Integer> getSelectedPointIndex() {
        return selectedPointIndex;
    }
}
