package com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityTransportationBinding;

public class TransportationActivity extends BaseActivity<ActivityTransportationBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_transportation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(R.id.container, com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.ShuttleOptionFragment.newInstance(), true);
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
