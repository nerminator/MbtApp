package com.daimler.biziz.android.ui.splash;

import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.ui.dialogs.MessageDialog;
import com.daimler.biziz.android.ui.login.LoginActivity;
import com.daimler.biziz.android.ui.login.LoginFragment;
import com.daimler.biziz.android.ui.main.main.MainActivity;
import com.daimler.biziz.android.utils.RootUtil;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().inject(this);
        if (RootUtil.isDeviceRooted()) {
            showMessageDialog("", getString(R.string.TXT_LOGIN_SPLASH_ROOTED_DEVICE), () -> {
                finish();
                System.exit(0);
            });
        } else {
            if (sharedPreferenceManager.isLogin()) {
                if (getIntent().getExtras() != null && getIntent().getExtras().getString("newsId") != null) {
                    MainActivity.start(this, getIntent().getExtras().getString("newsId"));
                }
                else {
                    MainActivity.start(this);
                    finish();
                }
            } else {
                LoginActivity.start(this);
                finish();
            }
        }
    }
}
