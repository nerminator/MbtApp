package com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleOptionEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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
