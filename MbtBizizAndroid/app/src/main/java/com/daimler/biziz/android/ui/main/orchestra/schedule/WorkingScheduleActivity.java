package com.daimler.biziz.android.ui.main.orchestra.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityWorkingScheduleBinding;

public class WorkingScheduleActivity extends BaseActivity<ActivityWorkingScheduleBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_working_schedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(R.id.container, WorkingScheduleFragment.newInstance(), true);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, WorkingScheduleActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) finish();
        else super.onBackPressed();
    }
}
