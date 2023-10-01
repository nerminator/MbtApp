package com.daimler.biziz.android.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.manager.SharedPreferenceManager;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().inject(this);

        if (!sharedPreferenceManager.isReadedTerms()){
            loadFragment(R.id.container,TermsAndConFragment.newInstance(),true);
        }
        else{
            loadFragment(R.id.container, LoginFragment.newInstance(), true);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }
}
