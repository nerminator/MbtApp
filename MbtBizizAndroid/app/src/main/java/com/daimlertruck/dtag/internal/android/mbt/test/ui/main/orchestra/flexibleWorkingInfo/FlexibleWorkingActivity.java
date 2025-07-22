package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.WorkInfoAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivityFlexibleWorkingBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.interfaces.IWorkInfoClick;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.MessageDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.PickerBottomSheetDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.ArrayList;

public class FlexibleWorkingActivity extends BaseActivity<ActivityFlexibleWorkingBinding> {

/*
    public static final int TYPE_BEYAZ_YAKA = 1;
    public static final int TYPE_MAVI_YAKA = 2;

    @Retention(SOURCE)
    @IntDef({TYPE_BEYAZ_YAKA, TYPE_MAVI_YAKA})
    public @interface employeeType {
    }
*/



    public static final String  KEY_YEARLY_TEXT = "KEY_YEARLY_TEXT";
    public static final String  KEY_MONTHLY_TEXT = "KEY_MONTHLY_TEXT";
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.VMFlexibleWorking viewModel;
    private ArrayList<FlexibleItemEntity> list = new ArrayList<>();
    private WorkInfoAdapter adapter;
    private String detailTitle = "";

/*
    public static void start(Context context, @employeeType int type) {
        Intent starter = new Intent(context, FlexibleWorkingActivity.class);
        starter.putExtra(KEY_YEARLY_TEXT,type);
        context.startActivity(starter);
    }
*/

    public static void start(Context context, String yearlyWorkHoursText, String monthlyWorkHoursText) {
        Intent starter = new Intent(context, FlexibleWorkingActivity.class);
        starter.putExtra(KEY_YEARLY_TEXT,yearlyWorkHoursText);
        starter.putExtra(KEY_MONTHLY_TEXT,monthlyWorkHoursText);
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
        viewModel = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.VMFlexibleWorking.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        //Toolbar Set VM
        toolbarSetVM();

        setClickListeners();
        observers();
        setRecyclerViewScrollListener();
    }


    private void toolbarSetVM() {
      /*  String  title =getString(R.string.TXT_PROFILE_PROFILE_1ST_BUTTON_WHITE);
        if (getIntent().getIntExtra(KEY_YEARLY_TEXT,1)==TYPE_MAVI_YAKA){
            title =getString(R.string.TXT_PROFILE_PROFILE_1ST_BUTTON_BLUE);
        }*/
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this,getIntent().getStringExtra(KEY_YEARLY_TEXT) , null);
        binding.toolbar.setVm(vmToolbar);
    }

    private void observers() {

        viewModel.getList().observe(this, new Observer<ArrayList<FlexibleItemEntity>>() {
            @Override
            public void onChanged(@Nullable ArrayList<FlexibleItemEntity> flexibleItemEntities) {
                // TODO: 23.05.2018 Adapter Doldurulacak
                    list.clear();
                    list.addAll(flexibleItemEntities);
                    setAdapterRecycler();
            }
        });
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });

        viewModel.getListDay().observe(this, new Observer<ArrayList<FlexibleItemEntity>>() {
            @Override
            public void onChanged(@Nullable ArrayList<FlexibleItemEntity> list) {
                com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.FlexibleWorkingDetailActivity.start(FlexibleWorkingActivity.this, detailTitle, list,viewModel.getFlexibleWorkEntity().getValue());
            }
        });

        viewModel.getShowErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                FlexibleWorkingActivity.this.showMessageDialog("", s, new MessageDialog.DialogConfirmedListener() {
                    @Override
                    public void onConfirmed() {
                        FlexibleWorkingActivity.this.finish();
                    }
                });
            }
        });
    }


    private void setAdapterRecycler() {
        if (binding.rcyclerYearly.getAdapter() == null) {
            adapter = new WorkInfoAdapter(list,viewModel.getFlexibleWorkEntity().getValue(), new IWorkInfoClick() {
                @Override
                public void click(FlexibleItemEntity entity) {
                    String[] custom_months = FlexibleWorkingActivity.this.getResources().getStringArray(R.array.custom_months);
                    detailTitle = custom_months[entity.getMonth() - 1] + " " + viewModel.getSelectedYear().getValue()+" " + getIntent().getStringExtra(KEY_MONTHLY_TEXT);
                    //Toast.makeText(FlexibleWorkingActivity.this,custom_months[entity.getMonth()-1] +" "+ viewModel.getSelectedYear().getValue() , Toast.LENGTH_SHORT).show();
                    viewModel.getMonthlyWorkHours(Integer.parseInt(viewModel.getSelectedYear().getValue()), entity.getMonth());
                }
            });
            binding.rcyclerYearly.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }
    }


    private void setClickListeners() {
        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBottomSheetDialog();
            }
        });
    }

    private void createBottomSheetDialog() {
        PickerBottomSheetDialog bottomSheet;
        ArrayList<String> list = viewModel.getYearlist().getValue();
        PickerBottomSheetDialog.SelectedIndexListener listener = null;
        int currentYear = viewModel.getYearlist().getValue().indexOf(viewModel.getSelectedYear().getValue());
        bottomSheet = PickerBottomSheetDialog.createCustomDialog(FlexibleWorkingActivity.this, PickerBottomSheetDialog.TYPE_YEAR, list, currentYear);
        bottomSheet.setListener(new PickerBottomSheetDialog.SelectedIndexListener() {
            @Override
            public void selectedIndex(int index) {
                viewModel.getYearlyWorkHours(Integer.parseInt(viewModel.getYearlist().getValue().get(index)));
                //Toast.makeText(FlexibleWorkingActivity.this, list.get(index), Toast.LENGTH_SHORT).show();
                bottomSheet.dismiss();
            }
        });
        bottomSheet.show();
    }


    //region scrollListener
    private void setRecyclerViewScrollListener() {
        binding.rcyclerYearly.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private LinearLayoutManager layoutManager = (LinearLayoutManager) binding.rcyclerYearly.getLayoutManager();
            ;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                 if (layoutManager.findLastVisibleItemPosition() == viewModel.getList().getValue().size() - 1 && layoutManager.findFirstVisibleItemPosition() != 0) {
                    viewModel.getYearPickerVisibility().postValue(false);
                } else {
                    viewModel.getYearPickerVisibility().postValue(true);
                }
            }
        });
    }

    //endregion


}
