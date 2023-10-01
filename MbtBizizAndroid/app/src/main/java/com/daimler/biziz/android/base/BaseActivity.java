package com.daimler.biziz.android.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.receiver.LogOutReceiver;
import com.daimler.biziz.android.ui.dialogs.BizizProgressDialog;
import com.daimler.biziz.android.ui.dialogs.ConfirmationDialog;
import com.daimler.biziz.android.ui.dialogs.MessageDialog;
import com.daimler.biziz.android.ui.login.LoginActivity;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {


    private BizizProgressDialog progressDialog;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    public DB binding;
    private LogOutReceiver logOutReceiver = new LogOutReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            sharedPreferenceManager.logout();

            Intent intentForLogin = new Intent(BaseActivity.this, LoginActivity.class);
            intentForLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentForLogin);
        }
    };

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
    }


    public void loadFragment(int contanerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack("");
        }
        ft.replace(contanerId, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(logOutReceiver, new IntentFilter("com.daimler.biziz.intent.LOG_OUT"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(logOutReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //region Progress

    public void showProgressDialog() {
        progressDialog = BizizProgressDialog.getDialog();
        progressDialog.show(this);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showMessageDialog(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.TXT_COMMON_WARNING);
        }
        MessageDialog messageDialog = MessageDialog.newInstance(
                title,
                content
        );
        messageDialog.show(getSupportFragmentManager(), "");
    }

    public void showMessageDialog(String title, String content, MessageDialog.DialogConfirmedListener listener) {
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.TXT_COMMON_WARNING);
        }
        MessageDialog messageDialog = MessageDialog.newInstance(
                title,
                content
        );
        messageDialog.setListener(listener);
        messageDialog.show(getSupportFragmentManager(), "");
    }

    //endregion
    @Override
    public void onBackPressed() {
      /*  if (getSupportFragmentManager().getBackStackEntryCount() < 1 && (this instanceof DashboardActivity || this instanceof LoginActivity)) {
            ConfirmationDialog exit = ConfirmationDialog.newInstance(getString(R.string.logout), getString(R.string.are_you_sure));
            exit.setListener(isConfirmed -> {
                if (isConfirmed) {
                    finish();
                }
            });
            exit.show(getSupportFragmentManager(), "");
        } else {
            super.onBackPressed();
        }*/
        super.onBackPressed();
    }

}
