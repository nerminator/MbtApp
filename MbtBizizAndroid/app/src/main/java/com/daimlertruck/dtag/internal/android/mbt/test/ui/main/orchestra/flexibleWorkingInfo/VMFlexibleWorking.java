package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleWorkEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.utils.BottomSheelHelper;

import java.util.ArrayList;

import javax.inject.Inject;

public class VMFlexibleWorking extends AndroidViewModel {
    @Inject
    AbstractApiUtils abstractApiUtils;

    private SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    private SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> yearPickerVisibility = new MutableLiveData<>();

    private MutableLiveData<ArrayList<FlexibleItemEntity>> list = new MutableLiveData<>();
    private MutableLiveData<ArrayList<FlexibleItemEntity>> listDay = new MutableLiveData<>();
    private MutableLiveData<FlexibleWorkEntity> flexibleWorkEntity = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> yearlist = new MutableLiveData<>();
    private MutableLiveData<String> selectedYear = new MutableLiveData<>();

    public VMFlexibleWorking(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMFlexibleWorking(this);
        yearPickerVisibility.postValue(true);
        getYearlyWorkHours(0);

    }

    public void getYearlyWorkHours(int i) {
        isLoading.postValue(true);
        abstractApiUtils.getYearlyWorkHours(i, new NetworkCallback<BaseResponse<FlexibleWorkEntity>>() {
            @Override
            public void onSuccess(BaseResponse<FlexibleWorkEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success){
                    flexibleWorkEntity.postValue(response.getResponseData());
                    //Recycler items
                    if (response.getResponseData().getMonthList()!= null && response.getResponseData().getMonthList().size()>0) {
                        list.postValue((ArrayList<FlexibleItemEntity>) response.getResponseData().getMonthList());
                    }
                    //Number Picker Items
                    if (response.getResponseData().getYearList() !=null && response.getResponseData().getYearList().size()>0){
                        yearlist.postValue(BottomSheelHelper.createYearList((ArrayList<Integer>) response.getResponseData().getYearList()));
                    }
                    if(response.getResponseData().getSelectedYear()!=null){
                        selectedYear.postValue(String.valueOf(response.getResponseData().getSelectedYear()));
                    }
                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
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

    public void getMonthlyWorkHours(int year,int month){
        isLoading.postValue(true);
        abstractApiUtils.getMonthlyWorkHours(year,month, new NetworkCallback<BaseResponse<FlexibleWorkEntity>>() {
            @Override
            public void onSuccess(BaseResponse<FlexibleWorkEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success){
                    flexibleWorkEntity.postValue(response.getResponseData());
                    if (response.getResponseData().getDayList()!= null && response.getResponseData().getDayList().size()>0) {
                        listDay.postValue((ArrayList<FlexibleItemEntity>) response.getResponseData().getDayList());
                    }

                }else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
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

    //region Getter
    public MutableLiveData<Boolean> getYearPickerVisibility() {
        return yearPickerVisibility;
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public MutableLiveData<ArrayList<FlexibleItemEntity>> getList() {
        return list;
    }

    public MutableLiveData<ArrayList<String>> getYearlist() {
        return yearlist;
    }

    public MutableLiveData<String> getSelectedYear() {
        return selectedYear;
    }

    public MutableLiveData<ArrayList<FlexibleItemEntity>> getListDay() {
        return listDay;
    }

    public MutableLiveData<FlexibleWorkEntity> getFlexibleWorkEntity() {
        return flexibleWorkEntity;
    }

    //endregion
}
