package com.daimler.biziz.android.ui.transportation.search;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.view.View;

import com.daimler.biziz.android.base.BaseApplication;

import java.util.Observable;

public class VMSearchShuttle extends AndroidViewModel {
    ObservableBoolean isResultShowing=new ObservableBoolean(false);

    public VMSearchShuttle(@NonNull Application application) {
        super(application);
        ((BaseApplication) getApplication()).getComponent().injectVMSearchShuttle(this);
    }

    public void performBack(View view) {

    }

    public void performNotification(View view) {

    }

    public ObservableBoolean getIsResultShowing() {
        return isResultShowing;
    }
}
