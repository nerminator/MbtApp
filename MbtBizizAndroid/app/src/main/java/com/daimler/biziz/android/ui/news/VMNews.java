package com.daimler.biziz.android.ui.news;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.food.DensityInfo;
import com.daimler.biziz.android.network.entity.food.FoodListEntity;
import com.daimler.biziz.android.network.entity.news.NewsEntity;
import com.daimler.biziz.android.network.entity.news.NewsPostBody;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

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
        }, discountType == null ? new NewsPostBody(type, page) : new NewsPostBody(type, page, discountType));
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return loading;
    }


    public MutableLiveData<NewsEntity> getData() {
        return data;
    }


}
