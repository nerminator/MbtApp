package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityWorkingScheduleBinding;

public class WorkingScheduleActivity extends BaseActivity<ActivityWorkingScheduleBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_working_schedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFragment(R.id.container, com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule.WorkingScheduleFragment.newInstance(), true);
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
