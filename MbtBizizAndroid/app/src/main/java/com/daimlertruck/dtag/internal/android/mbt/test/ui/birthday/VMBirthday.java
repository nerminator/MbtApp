package com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.birthday.BirthdayEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

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
