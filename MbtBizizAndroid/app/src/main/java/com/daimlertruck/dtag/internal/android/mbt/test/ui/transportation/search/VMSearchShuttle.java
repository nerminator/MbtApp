package com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;

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
