package com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DensityInfo;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMFoodMenu extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<FoodListEntity> data =new MutableLiveData<>();
    MutableLiveData<DensityInfo> selectedDensityInfo =new MutableLiveData<>();

    public VMFoodMenu(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMFoodMenu(this);
        getFoodMenu();
    }

    private void getFoodMenu() {
        isLoading.postValue(true);
        abstractApiUtils.getFoodMenu(new NetworkCallback<BaseResponse<FoodListEntity>>() {
            @Override
            public void onSuccess(BaseResponse<FoodListEntity> response) {
                isLoading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success){
                    data.postValue(response.getResponseData());
                    if (response.getResponseData().getDensityInfo() !=null &&response.getResponseData().getDensityInfo().size()>0) {
                        selectedDensityInfo.postValue(response.getResponseData().getDensityInfo().get(0));
                    }
                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                isLoading.postValue(false);
                showErrorMessage.postValue(message);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                isLoading.postValue(false);

            }
        });
    }

    //region Getter


    public MutableLiveData<DensityInfo> getSelectedDensityInfo() {
        return selectedDensityInfo;
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public MutableLiveData<FoodListEntity> getData() {
        return data;
    }
    //endregion
}
