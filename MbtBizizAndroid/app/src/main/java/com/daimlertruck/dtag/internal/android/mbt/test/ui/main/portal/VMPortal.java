package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponseCodes;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.notifications.DeviceInfoBody;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.qr.UserConfigEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalItem;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.recycler.PortalItemType;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import javax.inject.Inject;

public class VMPortal extends AndroidViewModel {

    @Inject
    AbstractApiUtils abstractApiUtils;

    MutableLiveData<Boolean> qrButtonVisibility = new MutableLiveData<>();

    public VMPortal(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMPortal(this);

    }

    public void sendDeviceInfo(String token) {
        abstractApiUtils.saveDeviceInfo(new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

                //Log.d("VMPortal", "Success");

            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {

            }

            @Override
            public void onNetworkFailure(Throwable message) {

            }
        }, new DeviceInfoBody(token));
    }


    public void getUserConfig() {
        qrButtonVisibility.postValue(false);
        abstractApiUtils.getUserConfig(new NetworkCallback<BaseResponse<UserConfigEntity>>() {
            @Override
            public void onSuccess(BaseResponse<UserConfigEntity> response) {
                if (response.getStatuscode() == BaseResponseCodes.success) {
                    qrButtonVisibility.postValue(true);
                } else {
                    qrButtonVisibility.postValue(false);
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                qrButtonVisibility.postValue(false);
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                qrButtonVisibility.postValue(false);
            }
        });
    }

    public ArrayList<PortalItem> getPortalList() {
        ArrayList<PortalItem> portalItems = new ArrayList<>();
        portalItems.add(new PortalItem(R.drawable.ic_icn_aboutus, R.drawable.img_about_us, R.string.TXT_LOGIN_HOME_ABOUT_US, PortalItemType.ABOUT_US));
        portalItems.add(new PortalItem(R.drawable.ic_icn_haberler, R.drawable.img_haberler, R.string.TXT_LOGIN_HOME_NEWS, PortalItemType.NEWS));
        portalItems.add(new PortalItem(R.drawable.ic_icn_events, R.drawable.img_events, R.string.TXT_LOGIN_HOME_EVENTS, PortalItemType.EVENTS));
        portalItems.add(new PortalItem(R.drawable.ic_icn_yerlesim, R.drawable.img_yerlesim, R.string.TXT_LOGIN_HOME_MAP, PortalItemType.LOCATION));
        portalItems.add(new PortalItem(R.drawable.ic_icn_ulasim, R.drawable.img_ulasim, R.string.TXT_LOGIN_HOME_SHUTTLE, PortalItemType.SHUTTLE));
        portalItems.add(new PortalItem(R.drawable.ic_icn_yemek, R.drawable.img_yemek, R.string.TXT_LOGIN_HOME_MENU, PortalItemType.MEAL_MENU));
        portalItems.add(new PortalItem(R.drawable.ic_icn_discounts, R.drawable.img_discounts, R.string.TXT_LOGIN_HOME_DISCOUNTS, PortalItemType.DISCOUNTS));
        portalItems.add(new PortalItem(R.drawable.ic_icn_links, R.drawable.img_links, R.string.TXT_LOGIN_HOME_USEFUL_LINKS, PortalItemType.USEFUL_LINKS));
        portalItems.add(new PortalItem(R.drawable.ic_icn_phonenumbers, R.drawable.img_phonenumbers, R.string.TXT_LOGIN_HOME_EMERGENCY_NUMBERS, PortalItemType.EMERGENCY_NUMBERS));
        return portalItems;
    }

    public MutableLiveData<Boolean> getQrButtonVisibility() {
        return qrButtonVisibility;
    }

    public void getFcmTokenAndTryToSendDeviceInfo() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        sendDeviceInfo(token);
                    } else {
                        Log.d("FCMTokenError", "error");
                    }
                });
    }
}
