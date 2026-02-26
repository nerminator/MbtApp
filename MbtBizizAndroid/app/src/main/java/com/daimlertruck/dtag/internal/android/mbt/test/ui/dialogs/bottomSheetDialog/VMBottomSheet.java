/*
package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.daimlertruck.dtag.internal.android.mbt.R;

public class VMBottomSheet extends AndroidViewModel {

    String title;
    String subTitle;
    Drawable icon;
    String underIconText;
    String firstDescText;
    String firstDescValue;
    String secondDescText;
    String secondDescValue;


    public static VMBottomSheet createVMBottomSheet(Fragment fragment, @CustomBottomSheetDialog.BottomSheetType int bottomSheetType) {
        return ViewModelProviders.of(fragment, new VMBottomSheet.Factory(fragment.getActivity().getApplication(), fragment.getActivity(), bottomSheetType)).get(VMBottomSheet.class);
    }

    public VMBottomSheet(@NonNull Application application, Activity activity, @CustomBottomSheetDialog.BottomSheetType int bottomSheetType) {
        super(application);
        switch (bottomSheetType) {
            case CustomBottomSheetDialog.DRIVER_INFO:
                title = "A";
                subTitle="a";
                icon = ContextCompat.getDrawable(activity, R.drawable.ic_account_circle_black_24dp);
                underIconText="a";
                firstDescText="a";
                firstDescValue="a";
                secondDescText="a";
                secondDescValue="a";
                break;
            case CustomBottomSheetDialog.SUPPORT:
                title = "b";
                subTitle="b";
                icon = ContextCompat.getDrawable(activity, R.drawable.ic_oval);
                underIconText="b";
                firstDescText="b";
                firstDescValue="b";
                secondDescText="b";
                secondDescValue="b";
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getUnderIconText() {
        return underIconText;
    }

    public String getFirstDescText() {
        return firstDescText;
    }

    public String getFirstDescValue() {
        return firstDescValue;
    }

    public String getSecondDescText() {
        return secondDescText;
    }

    public String getSecondDescValue() {
        return secondDescValue;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private Application mApplication;


        private Activity activity;
        private int bottomSheetTyoe;

        public Factory(@NonNull Application mApplication, Activity activity, @CustomBottomSheetDialog.BottomSheetType int bottomSheetTyoe) {
            this.mApplication = mApplication;
            this.activity = activity;
            this.bottomSheetTyoe = bottomSheetTyoe;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new VMBottomSheet(mApplication, activity, bottomSheetTyoe);
        }
    }


}
*/
