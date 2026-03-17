package com.daimlertruck.dtag.internal.android.mbt.test.ui.about.appfeedback;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.feedback.SubmitFeedbackBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMAppFeedback extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> showInfoDialog = new SingleLiveEvent<>();

    public SingleLiveEvent<Boolean> getShowInfoDialog() {
        return showInfoDialog;
    }

    public VMAppFeedback(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMAppFeedback(this);
    }
    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public void sendFeedback(String feedbackText){

        isLoading.postValue(true); // load spinner
        abstractApiUtils.sendFeedback(new SubmitFeedbackBody(feedbackText), new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                isLoading.postValue(false); // hide spinner
                showInfoDialog.postValue(true);
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                isLoading.postValue(false); // hide spinner
                showInfoDialog.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                isLoading.postValue(false); // hide spinner
                showInfoDialog.postValue(false);
            }
        });
    }

}
