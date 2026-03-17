package com.daimlertruck.dtag.internal.android.mbt.test.ui.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.InitEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.init.InitPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;


import javax.inject.Inject;

public class VMSplash extends AndroidViewModel {

    @Inject
    AbstractApiUtils apiUtils;

    SingleLiveEvent<Boolean> continueToApp = new SingleLiveEvent<>();
    SingleLiveEvent<InitEntity> showDialog = new SingleLiveEvent<>();

    public VMSplash(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectSplashVM(this);
    }


    public void checkVersion(String versionName) {
        apiUtils.init(new NetworkCallback<BaseResponse<InitEntity>>() {
            @Override
            public void onSuccess(BaseResponse<InitEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    if (response.getResponseData().getButtonList() != null && !response.getResponseData().getButtonList().isEmpty()) {
                        InitEntity initModel = response.getResponseData();
                        showDialog.postValue(initModel);
                    } else {
                        continueToApp.postValue(true);
                    }
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
            }

            @Override
            public void onNetworkFailure(Throwable message) {
            }
        }, new InitPostBody(versionName));
    }

}
