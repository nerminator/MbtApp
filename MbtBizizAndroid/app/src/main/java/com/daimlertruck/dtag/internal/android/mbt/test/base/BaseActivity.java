package com.daimlertruck.dtag.internal.android.mbt.test.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.daimlertruck.dtag.internal.android.mbt.BuildConfig;
import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.receiver.LogOutReceiver;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.BizizProgressDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.MessageDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.login.LoginActivity;

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {


    private BizizProgressDialog progressDialog;

    public DB binding;
    private LogOutReceiver logOutReceiver = new LogOutReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

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
        registerReceiver(
                logOutReceiver,
                new IntentFilter("com.daimler.biziz.intent.LOG_OUT"),
                BuildConfig.APPLICATION_ID + ".permission.INTERNAL_BROADCAST",
                null
        );

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
