package com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.SingleLiveEvent;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.DetailsItem;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.MediasItem;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.SocialMediaEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.socialmedia.SocialMediaItem;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VMSocialMedia extends AndroidViewModel {
    SingleLiveEvent<Boolean> isLoading = new SingleLiveEvent<>();
    SingleLiveEvent<String> showErrorMessage = new SingleLiveEvent<>();
    MutableLiveData<List<SocialMediaItem>> socialMediaItems = new MutableLiveData<>();

    @Inject
    AbstractApiUtils abstractApiUtils;

    public VMSocialMedia(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMSocialMedia(this);
        getSocialMediaLinks();
    }

    private void getSocialMediaLinks() {
        isLoading.postValue(true);
        abstractApiUtils.getSocialMedias(new NetworkCallback<BaseResponse<SocialMediaEntity>>() {
            @Override
            public void onSuccess(BaseResponse<SocialMediaEntity> response) {
                isLoading.postValue(false);
                ArrayList<SocialMediaItem> items = new ArrayList<>();
                for (MediasItem socialMediaItem : response.getResponseData().getMedias()) {
                    items.add(new SocialMediaItem(socialMediaItem.getName(), null, true));
                    for (DetailsItem detailItem : socialMediaItem.getDetails()) {
                        items.add(new SocialMediaItem(detailItem.getAccount(), detailItem.getUrl(), false));
                    }
                }
                socialMediaItems.postValue(items);
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

    public MutableLiveData<List<SocialMediaItem>> getSocialMediaItems() {
        return socialMediaItems;
    }
    //endregion
}
