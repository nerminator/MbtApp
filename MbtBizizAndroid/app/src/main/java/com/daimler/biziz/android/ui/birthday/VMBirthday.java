package com.daimler.biziz.android.ui.birthday;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.birthday.BirthdayEntity;
import com.daimler.biziz.android.network.entity.birthday.BirthdayListEntity;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class VMBirthday extends AndroidViewModel {
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<BirthdayEntity> birthdayLiveData= new MutableLiveData<>();

    @Inject
    AbstractApiUtils abstractApiUtils;
    public VMBirthday(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMBirthday(this);
        getBirthdays();


    }

    private void getBirthdays() {

        isLoading.postValue(true);
        abstractApiUtils.getBirthdays(new NetworkCallback<BaseResponse<BirthdayEntity>>() {
            @Override
            public void onSuccess(BaseResponse<BirthdayEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    birthdayLiveData.postValue(response.getResponseData());
                }
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

    //region Getter - Setter

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public MutableLiveData<BirthdayEntity> getBirthdayLiveData() {
        return birthdayLiveData;
    }
    //endregion
}
