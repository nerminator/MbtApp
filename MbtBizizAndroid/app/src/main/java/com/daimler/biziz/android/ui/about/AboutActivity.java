package com.daimler.biziz.android.ui.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(R.id.container, AboutFragment.newInstance(), true);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) finish();
        else super.onBackPressed();
    }
}
