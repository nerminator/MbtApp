package com.daimler.biziz.android.ui.transportation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.view.View;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListPostBody;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListResponse;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMTransportation extends AndroidViewModel {
    MutableLiveData<ShuttleListResponse> shuttleListResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSwitchButtonClicked = new MutableLiveData<>();
    public ObservableBoolean viewPagerModeEnabled = new ObservableBoolean(false);
    public MutableLiveData<Boolean> toClicked = new MutableLiveData<>();
    public MutableLiveData<Boolean> fromClicked = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMTransportation(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMTransportation(this);
    }

    private void getShuttleList(int selectedPoint, int selectedType) {
        isLoading.postValue(true);
        ShuttleListPostBody shuttleListPostBody = new ShuttleListPostBody(selectedType, selectedPoint);
        abstractApiUtils.getShuttleList(shuttleListPostBody, new NetworkCallback<BaseResponse<ShuttleListResponse>>() {
            @Override
            public void onSuccess(BaseResponse<ShuttleListResponse> response) {
                shuttleListResponse.postValue(response.getResponseData());
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

    public MutableLiveData<ShuttleListResponse> getShuttleListResponse() {
        return shuttleListResponse;
    }

    public void setVariables(int selectedPoint, int selectedType){
        getShuttleList(selectedPoint, selectedType);
    }

    public MutableLiveData<Boolean> getIsSwitchButtonClicked() {
        return isSwitchButtonClicked;
    }

    public void performClickSwitch(View view) {
        isSwitchButtonClicked.postValue(true);
    }

    public void performClickTo(View view) {
        toClicked.postValue(true);
    }

    public void performClickFrom(View view) {
        fromClicked.postValue(true);
    }

    public ObservableBoolean getViewPagerModeEnabled() {
        return viewPagerModeEnabled;
    }
}
