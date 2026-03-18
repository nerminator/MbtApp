package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.ClubCategoryDetailEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.ClubCategoryEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.PhonesCategoryDetailEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.PhonesCategoryEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.PhonesEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksClubsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class VMUsefulLinksCategoryDetail extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    private MutableLiveData<ArrayList<UsefulLinksCategoryItem>> detailsLiveData = new MutableLiveData<>();

    public VMUsefulLinksCategoryDetail(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMUsefulLinksCategoryDetail(this);

    }

    public void fetchPhones(int id) {
        abstractApiUtils.getPhonesById(id, new NetworkCallback<BaseResponse<PhonesEntity>>() {
            @Override
            public void onSuccess(BaseResponse<PhonesEntity> response) {
                mapPhonesToUIModel(response);
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        });
    }

    public void fetchSocialClubs(int id) {
        abstractApiUtils.getSocialClubsById(id, new NetworkCallback<BaseResponse<UsefulLinksClubsEntity>>() {
            @Override
            public void onSuccess(BaseResponse<UsefulLinksClubsEntity> response) {
                mapClubsToUIModel(response);
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        });
    }

    private void mapClubsToUIModel(BaseResponse<UsefulLinksClubsEntity> response) {
        ArrayList<UsefulLinksCategoryItem> uiList = new ArrayList<>();
        for (ClubCategoryEntity clubCategory : response.getResponseData().getClubs()) {
            //add header
            uiList.add(new UsefulLinksCategoryItem(clubCategory.getName(), null, null, null, null));
            //add items
            for (ClubCategoryDetailEntity detail : clubCategory.getDetails()) {
                uiList.add(new UsefulLinksCategoryItem(null, detail.getResponsible(), detail.getContact(), null, null));
            }
        }
        detailsLiveData.postValue(uiList);
    }

    private void mapPhonesToUIModel(BaseResponse<PhonesEntity> response) {
        ArrayList<UsefulLinksCategoryItem> uiList = new ArrayList<>();
        for (PhonesCategoryEntity phones : response.getResponseData().getPhones()) {
            //add header
            uiList.add(new UsefulLinksCategoryItem(phones.getName(), null, null, null, null));
            //add items
            for (PhonesCategoryDetailEntity detail : phones.getDetails()) {
                uiList.add(new UsefulLinksCategoryItem(null, detail.getUnit(), detail.getInternal(), response.getResponseData().getSantral(), filterPhoneNumber(detail.getInternal())));
            }
        }
        detailsLiveData.postValue(uiList);
    }

    public MutableLiveData<ArrayList<UsefulLinksCategoryItem>> getDetailsLiveData() {
        return detailsLiveData;
    }

    public String filterPhoneNumber(String str) {
        int startIndex = str.indexOf("(");
        int endIndex = str.indexOf(")", startIndex);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Remove the substring between startChar and endChar, inclusive
            return str.substring(0, startIndex) + str.substring(endIndex + 1);
        }

        // If the start or end character is not found, return the original string
        return str;
    }
}
