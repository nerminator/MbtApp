package com.daimler.biziz.android.ui.newsDetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.base.SingleLiveEvent;
import com.daimler.biziz.android.network.entity.base.BaseResponse;
import com.daimler.biziz.android.network.entity.base.BaseResponseCodes;
import com.daimler.biziz.android.network.entity.newsDetail.NewsDetailEntity;
import com.daimler.biziz.android.network.network.AbstractApiUtils;
import com.daimler.biziz.android.network.network.NetworkCallback;

import javax.inject.Inject;

public class VMNewsDetail extends AndroidViewModel {
    MutableLiveData<String> id = new MutableLiveData<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<NewsDetailEntity> newsDetail = new MutableLiveData<>();
    MutableLiveData<Boolean> linkVisibility = new MutableLiveData<>();


    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMNewsDetail(@NonNull Application application,String id) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMNewsDetail(this);
        linkVisibility.postValue(true);
        getNewsDetail(id);
    }

    private void getNewsDetail(String id) {
        isLoading.postValue(true);
        abstractApiUtils.getNewsDetails(id, new NetworkCallback<BaseResponse<NewsDetailEntity>>() {
            @Override
            public void onSuccess(BaseResponse<NewsDetailEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success){
                    newsDetail.postValue(response.getResponseData());
                    if (TextUtils.isEmpty(response.getResponseData().getUrl())){
                        linkVisibility.postValue(false);
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

    //region Getter - Setter
    public void setNewsDetailId(String idParam) {
        id.postValue(idParam);
    }

    public MutableLiveData<String> getId() {
        return id;
    }

    public SingleLiveEvent<String> getShowErrorMessage() {
        return showErrorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<NewsDetailEntity> getNewsDetail() {
        return newsDetail;
    }

    public MutableLiveData<Boolean> getLinkVisibility() {
        return linkVisibility;
    }

    //endregion

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private Application mApplication;
        private String newsId;

        public Factory(@NonNull Application mApplication, String newsId) {
            this.mApplication = mApplication;
            this.newsId = newsId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new VMNewsDetail(mApplication, newsId);
        }
    }
}
