package com.daimlertruck.dtag.internal.android.mbt.test.ui.news;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.NewsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.NewsPostBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMNews extends AndroidViewModel {
    @Inject
    AbstractApiUtils abstractApiUtils;

    SingleLiveEvent<Boolean> loading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<NewsEntity> data = new MutableLiveData<>();


    public VMNews(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMNews(this);

    }

    public void getNews(Integer type, Integer page, Integer discountType) {
        loading.postValue(true);
        abstractApiUtils.getNews(new NetworkCallback<BaseResponse<NewsEntity>>() {
            @Override
            public void onSuccess(BaseResponse<NewsEntity> response) {
                loading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    data.postValue(response.getResponseData());
                } else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                loading.postValue(false);
                showErrorMessage.postValue(message);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                loading.postValue(false);

            }
        }, discountType == null ? new NewsPostBody(type, page) : new NewsPostBody(type, page, discountType, null));
    }

    public void getNewsWithLocationId(Integer type, Integer page, Integer locationId) {
        loading.postValue(true);
        abstractApiUtils.getNews(new NetworkCallback<BaseResponse<NewsEntity>>() {
            @Override
            public void onSuccess(BaseResponse<NewsEntity> response) {
                loading.postValue(false);
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    data.postValue(response.getResponseData());
                } else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
                    showErrorMessage.postValue(response.getErrormessage());
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                loading.postValue(false);
                showErrorMessage.postValue(message);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                loading.postValue(false);

            }
        }, new NewsPostBody(type, page, null, locationId));
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return loading;
    }


    public MutableLiveData<NewsEntity> getData() {
        return data;
    }


}
