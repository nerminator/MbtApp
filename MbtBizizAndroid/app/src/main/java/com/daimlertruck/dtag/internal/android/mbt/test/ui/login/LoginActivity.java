package com.daimlertruck.dtag.internal.android.mbt.test.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainActivity;

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
            loadFragment(R.id.container, com.daimlertruck.dtag.internal.android.mbt.test.ui.login.TermsAndConFragment.newInstance(),false);
        }
        else{
            loadFragment(R.id.container, com.daimlertruck.dtag.internal.android.mbt.test.ui.login.LoginFragment.newInstance(), false);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String newsId) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(KEY_NEWS_ID, newsId);
        context.startActivity(starter);
    }

    public static final String KEY_NEWS_ID = "newsId";
}
