package com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.getDiscountCode.GetDiscountCodeEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail.NewsDetailEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.newsDetail.NewsDetailPdf;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import java.util.List;

import javax.inject.Inject;

public class VMNewsDetail extends AndroidViewModel {
    MutableLiveData<String> id = new MutableLiveData<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<NewsDetailEntity> newsDetail = new MutableLiveData<>();
    MutableLiveData<Boolean> linkVisibility = new MutableLiveData<>();
    MutableLiveData<List<NewsDetailPdf>> pdfListData = new MutableLiveData<>();
    MutableLiveData<DiscountCodeViewState> discountCodeViewState = new MutableLiveData<>();
    SingleLiveEvent<String> discountCodeCopied = new SingleLiveEvent<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMNewsDetail(@NonNull Application application, String id) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMNewsDetail(this);
        linkVisibility.postValue(true);
        setNewsDetailId(id);
        getNewsDetail(id);
    }

    private void getNewsDetail(String id) {
        isLoading.postValue(true);
        abstractApiUtils.getNewsDetails(id, new NetworkCallback<BaseResponse<NewsDetailEntity>>() {
            @Override
            public void onSuccess(BaseResponse<NewsDetailEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    newsDetail.postValue(response.getResponseData());
                    pdfListData.postValue(response.getResponseData().getPdfs());
                    if (TextUtils.isEmpty(response.getResponseData().getUrl())) {
                        linkVisibility.postValue(false);
                    }
                    discountCodeViewState.postValue(
                            new DiscountCodeViewState(
                                    response.getResponseData().getDiscountCodeType(),
                                    response.getResponseData().getDiscountCodeAll()
                            )
                    );
                } else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
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

    public void onDiscountCodeAreaClicked(View view) {
        if (discountCodeViewState.getValue() != null) {
            boolean isDiscountCodeShowing = discountCodeViewState.getValue().getDiscountCodeLabelVisibility() && !TextUtils.isEmpty(discountCodeViewState.getValue().getDiscountCode());
            boolean isGetDiscountCodeButtonActive = discountCodeViewState.getValue().getDiscountLabelVisibility();
            if (isDiscountCodeShowing) {
                discountCodeCopied.postValue(discountCodeViewState.getValue().getDiscountCode());
            } else if (isGetDiscountCodeButtonActive) {
                getPersonalDiscountCode();
            }
        }
    }

    private void getPersonalDiscountCode() {
        isLoading.postValue(true);
        if (id == null) {
            return;
        }
        abstractApiUtils.getDiscountCode(id.getValue(), new NetworkCallback<BaseResponse<GetDiscountCodeEntity>>() {
            @Override
            public void onSuccess(BaseResponse<GetDiscountCodeEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    //if ()
                    discountCodeViewState.postValue(new DiscountCodeViewState(discountCodeViewState.getValue().getDiscountCodeType(), response.getResponseData().getCode()));
                } else if (response.getStatuscode() == BaseResponseCodes.errorWithMessage && !TextUtils.isEmpty(response.getErrormessage())) {
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
