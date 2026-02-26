package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;

import javax.inject.Inject;

public class VMLogin extends AndroidViewModel {
    private static final String TAG = "VMLogin";

    @Inject
    AbstractApiUtils abstractApiUtils;

    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> loginPressed = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();

    public VMLogin(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMLogin(this);
    }

    public void performClick(View view) {
        loginPressed.postValue(true);
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public SingleLiveEvent<Boolean> getLoginPressed() {
        return loginPressed;
    }

}
