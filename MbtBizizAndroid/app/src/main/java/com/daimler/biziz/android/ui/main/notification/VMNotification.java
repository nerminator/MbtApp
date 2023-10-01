package com.daimler.biziz.android.ui.main.notification;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.news.NewsEntity;
import com.daimler.biziz.android.network.entity.notifications.Notification;
import com.daimler.biziz.android.network.entity.notifications.NotificationPostBody;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import java.util.List;

import javax.inject.Inject;

public class VMNotification extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    SingleLiveEvent<Boolean> loading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<List<Notification>> data = new MutableLiveData<>();


    public VMNotification(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMNotifications(this);
        loading.setValue(false);

    }

        public void getNotifications(Integer page) {
        loading.setValue(true);
        abstractApiUtils.getNotifications(new NotificationPostBody(page.toString()), new NetworkCallback<BaseResponse<List<Notification>>>() {
            @Override
            public void onSuccess(BaseResponse<List<Notification>> response) {
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
