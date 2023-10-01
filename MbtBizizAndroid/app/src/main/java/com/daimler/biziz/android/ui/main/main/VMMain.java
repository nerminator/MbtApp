package com.daimler.biziz.android.ui.main.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMMain extends AndroidViewModel {


    @Inject
    AbstractApiUtils apiUtils;
    MutableLiveData<String> badgeCount = new MutableLiveData<>();

    public VMMain(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectMainVM(this);

        badgeCount.setValue("0");
        getBadgeCount();
    }


    public void getBadgeCount() {
        apiUtils.getBadgeCount(new NetworkCallback<BaseResponse<Integer>>() {
            @Override
            public void onSuccess(BaseResponse<Integer> response) {
                if (response.getResponseData() != null)
                    badgeCount.setValue(response.getResponseData().toString());
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        });
    }

}
