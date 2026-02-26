package com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;

public class VMToolbar extends AndroidViewModel {
    private final Activity activity;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> subTitle = new MutableLiveData<>();
    public ObservableBoolean isVisible = new ObservableBoolean(true);

    public static VMToolbar createVMToolbarFor(Fragment fragment, boolean isBack) {
        return createVMToolbarFor(fragment, null, null);
    }

    public static VMToolbar createVMToolbarFor(AppCompatActivity activity, String title, String subTitle) {
        return ViewModelProviders.of(activity, new Factory(activity.getApplication(), activity, title, subTitle)).get(VMToolbar.class);
    }

    public static VMToolbar createVMToolbarFor(Fragment fragment, String title, String subTitle) {
        return ViewModelProviders.of(fragment, new Factory(fragment.getActivity().getApplication(), fragment.getActivity(), title, subTitle)).get(VMToolbar.class);
    }

    public VMToolbar(@NonNull Application application, Activity activity, String title, String subTitle) {
        super(application);
        if (!(activity instanceof AboutActivity))
            this.isVisible.set(true);
        else this.isVisible.set(false);
        this.activity = activity;
        this.title.setValue(title);
        this.subTitle.setValue(subTitle);
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getSubTitle() {
        return subTitle;
    }

    public void performNotification(View view) {
        AboutActivity.start(activity);
    }

    public void performBack(View view) {
        activity.onBackPressed();
    }

    public void setTexts(String title, String subTitle) {
        this.title.postValue(title);
        this.subTitle.postValue(subTitle);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private Application mApplication;


        private Activity activity;
        private String title;
        private String subTitle;

        public Factory(@NonNull Application mApplication, Activity activity, String title, String subTitle) {
            this.mApplication = mApplication;
            this.activity = activity;
            this.title = title;
            this.subTitle = subTitle;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new VMToolbar(mApplication, activity, title, subTitle);
        }
    }
}
