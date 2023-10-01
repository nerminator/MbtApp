package com.daimler.biziz.android.ui.residential;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.news.NewsEntity;
import com.daimler.biziz.android.network.entity.place.Building;

import com.daimler.biziz.android.network.entity.place.Residential;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import java.util.List;

import javax.inject.Inject;

public class VMResidential extends AndroidViewModel {
    @Inject
    AbstractApiUtils abstractApiUtils;

    SingleLiveEvent<Boolean> loading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<List<Residential>> data = new MutableLiveData<>();

    public VMResidential(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMResidential(this);

    }

    public void getBuildings() {
        loading.postValue(true);
        abstractApiUtils.getBuildings(new NetworkCallback<BaseResponse<List<Residential>>>() {

            @Override
            public void onSuccess(BaseResponse<List<Residential>> response) {
                loading.setValue(false);
                data.setValue(response.getResponseData());

            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                loading.setValue(false);

            }

            @Override
            public void onNetworkFailure(Throwable message) {
                loading.setValue(false);

            }
        });
    }
}
