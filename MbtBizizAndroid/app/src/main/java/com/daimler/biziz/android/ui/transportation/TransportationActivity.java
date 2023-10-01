package com.daimler.biziz.android.ui.transportation;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityTransportationBinding;

public class TransportationActivity extends BaseActivity<ActivityTransportationBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_transportation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(R.id.container, ShuttleOptionFragment.newInstance(), true);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TransportationActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) finish();
        else super.onBackPressed();
    }
}
