package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.WorkInfoAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityFlexibleWorkingBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.interfaces.IWorkInfoClick;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleWorkEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.ArrayList;

public class FlexibleWorkingDetailActivity extends BaseActivity<ActivityFlexibleWorkingBinding> {

    public static final String KEY_FLEXIBLE_TITLE="KEY_FLEXIBLE_TITLE";
    public static final String KEY_FLEXIBLE_LIST="KEY_FLEXIBLE_LIST";
    public static final String KEY_FLEXIBLE_TEXT_DATA="KEY_FLEXIBLE_TEXT_DATA";
    private ArrayList<FlexibleItemEntity> list = new ArrayList<>();
    private WorkInfoAdapter adapter;

    public static void start(Context context, String title, ArrayList<FlexibleItemEntity> list, FlexibleWorkEntity textData) {
        Intent starter = new Intent(context, FlexibleWorkingDetailActivity.class);
        starter.putExtra(KEY_FLEXIBLE_TITLE,title);
        starter.putExtra(KEY_FLEXIBLE_LIST,list);
        starter.putExtra(KEY_FLEXIBLE_TEXT_DATA,textData);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_flexible_working;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flexible_working);


        //Toolbar Set VM
        list.addAll(getIntent().getParcelableArrayListExtra(KEY_FLEXIBLE_LIST));
        toolbarSetVM();
        setAdapterRecycler();
        binding.btnDatePicker.setVisibility(View.GONE);

    }




    private void toolbarSetVM() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getIntent().getStringExtra(KEY_FLEXIBLE_TITLE), null);
        binding.toolbar.setVm(vmToolbar);
    }



    private void setAdapterRecycler() {
         adapter = new WorkInfoAdapter(list, getIntent().getParcelableExtra(KEY_FLEXIBLE_TEXT_DATA), new IWorkInfoClick() {
             @Override
             public void click(FlexibleItemEntity entity) {
             }
         });
        binding.rcyclerYearly.setAdapter(adapter);
    }













}
