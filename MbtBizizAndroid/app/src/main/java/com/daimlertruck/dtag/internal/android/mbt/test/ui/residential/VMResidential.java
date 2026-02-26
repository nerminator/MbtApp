package com.daimlertruck.dtag.internal.android.mbt.test.ui.residential;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;

import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.place.Residential;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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
