package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksCategoryLocationsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMUsefulLinksCategory extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    private MutableLiveData<UsefulLinksCategoryLocationsEntity> locationsEntityMutableLiveData = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();

    public VMUsefulLinksCategory(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMUsefulLinksCategory(this);

    }

    public void fetchSocialClubs() {
        isLoading.postValue(true);
        abstractApiUtils.getSocialClubLocs(new NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>>() {
            @Override
            public void onSuccess(BaseResponse<UsefulLinksCategoryLocationsEntity> response) {
                locationsEntityMutableLiveData.postValue(response.getResponseData());
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

    public void fetchPhones() {
        isLoading.postValue(true);
        abstractApiUtils.getPhoneLocs(new NetworkCallback<BaseResponse<UsefulLinksCategoryLocationsEntity>>() {
            @Override
            public void onSuccess(BaseResponse<UsefulLinksCategoryLocationsEntity> response) {
                locationsEntityMutableLiveData.postValue(response.getResponseData());
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

    public MutableLiveData<UsefulLinksCategoryLocationsEntity> getLocationsLiveData() {
        return locationsEntityMutableLiveData;
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }
}
