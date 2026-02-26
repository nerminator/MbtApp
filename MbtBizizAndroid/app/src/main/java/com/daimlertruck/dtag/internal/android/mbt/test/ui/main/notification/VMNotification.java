package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.notification;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.Notification;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.NotificationPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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
