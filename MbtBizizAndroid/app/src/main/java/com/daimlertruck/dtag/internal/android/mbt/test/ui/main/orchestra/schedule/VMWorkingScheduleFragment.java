package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.WorkCalendarEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

import retrofit2.Callback;

public class VMWorkingScheduleFragment extends AndroidViewModel {

    public MutableLiveData<Boolean> isLeftButtonClicked = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRightButtonClicked = new MutableLiveData<>();
    public MutableLiveData<String> pickerDateString = new MutableLiveData<>();
    public MutableLiveData<WorkCalendarEntity> workCalendarData = new MutableLiveData<>();
    private Callback callback;

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMWorkingScheduleFragment(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMWorkingSchedule(this);
        isLeftButtonClicked.postValue(false);
        isRightButtonClicked.postValue(false);
        pickerDateString.postValue("");
    }

    public void getWorkCalendar(String date) {
        callback = abstractApiUtils.getWorkCalendar(date, new NetworkCallback<BaseResponse<WorkCalendarEntity>>() {
            @Override
            public void onSuccess(BaseResponse<WorkCalendarEntity> response) {
                workCalendarData.postValue(response.getResponseData());
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        });
    }

    public void cancelRequest() {
        callback = null;
    }

    public void performClickArrowLeft(View view) {
        isLeftButtonClicked.postValue(true);
    }

    public void performClickArrowRight(View view) {
        isRightButtonClicked.postValue(true);
    }

    public MutableLiveData<Boolean> getIsLeftButtonClicked() {
        return isLeftButtonClicked;
    }

    public MutableLiveData<Boolean> getIsRightButtonClicked() {
        return isRightButtonClicked;
    }

    public MutableLiveData<String> getPickerDateString() {
        return pickerDateString;
    }

    public MutableLiveData<WorkCalendarEntity> getWorkCalendarData() {
        return workCalendarData;
    }
}
