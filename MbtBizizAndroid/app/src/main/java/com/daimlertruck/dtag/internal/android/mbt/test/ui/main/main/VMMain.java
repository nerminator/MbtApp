package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.about.AppStartUpEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMMain extends AndroidViewModel {

    @Inject
    AbstractApiUtils apiUtils;
    MutableLiveData<String> badgeCount = new MutableLiveData<>();

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

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
