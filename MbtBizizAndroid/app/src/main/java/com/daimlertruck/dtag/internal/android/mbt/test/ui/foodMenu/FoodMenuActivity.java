package com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.PageAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityFoodMenuBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DensityInfo;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.PickerBottomSheetDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class  FoodMenuActivity extends BaseActivity<ActivityFoodMenuBinding> {

    private PageAdapter adapter;
    private List<Fragment> pageAdapterFragments = new ArrayList<Fragment>();
    private List<String> pageAdapterFragmentTitles = new ArrayList<String>();
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu.VMFoodMenu vmFoodMenu;
    private List<DensityInfo> densityInfo;
    private FoodListEntity value;

    public static void start(Context context) {
        Intent starter = new Intent(context, FoodMenuActivity.class);
        context.startActivity(starter);
    }
    @Override
    public int getLayoutRes() {
        return R.layout.activity_food_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_food_menu);
        toolbarSetVM();
        vmFoodMenu = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu.VMFoodMenu.class);
        binding.setVm(vmFoodMenu);
        binding.setLifecycleOwner(this);
        observe();

    }

    private void observe() {
        vmFoodMenu.getData().observe(this, new Observer<FoodListEntity>() {
            @Override
            public void onChanged(@Nullable FoodListEntity foodListEntity) {
                initView();
            }
        });

        vmFoodMenu.getSelectedDensityInfo().observe(this, new Observer<DensityInfo>() {
            @Override
            public void onChanged(@Nullable DensityInfo densityInfo) {
                binding.constDensity.setVisibility(View.VISIBLE);
                binding.imgFirst.setImageLevel(1);
                binding.imgSecond.setImageLevel(0);
                binding.imgThird.setImageLevel(0);
                if (densityInfo.getPercent()>=33) {
                    binding.imgSecond.setImageLevel(1);
                }
                else if (densityInfo.getPercent()>=66){
                    binding.imgThird.setImageLevel(1);
                }



            }
        });

        vmFoodMenu.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });

    }

    private void toolbarSetVM() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_MENU_MENU_TITLE), null);
        binding.toolbar.setVm(vmToolbar);
    }


    private void initView() {
        setupViewPager();
        setClickListener();
    }

    private void setClickListener() {
        binding.constDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDensitySelectDialog();
                createBottomSheetDialog();
            }
        });
    }


    void showDensitySelectDialog(){
        List<String> listItems = new ArrayList<String>();
        for (DensityInfo info : vmFoodMenu.getData().getValue().getDensityInfo()) {
            listItems.add(info.getLocationName());
        }
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        AlertDialog alertDialog ;
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        View view = LayoutInflater.from(FoodMenuActivity.this).inflate(R.layout.layout_title_density, null);

        builder.setTitle(getString(R.string.TXT_MENU_MENU_DENSITY))
                .setCustomTitle(view)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        vmFoodMenu.getSelectedDensityInfo().postValue(vmFoodMenu.getData().getValue().getDensityInfo().get(which));
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        alertDialog=builder.create();
        alertDialog.show();
    }

    private void createBottomSheetDialog() {
        ArrayList<String> listItems = new ArrayList<String>();
        for (DensityInfo info : vmFoodMenu.getData().getValue().getDensityInfo()) {
            listItems.add(info.getLocationName());
        }
        PickerBottomSheetDialog bottomSheet;
        bottomSheet  = PickerBottomSheetDialog.createCustomDialog(FoodMenuActivity.this, PickerBottomSheetDialog.TYPE_FOOD, listItems);
        bottomSheet.setListener(new PickerBottomSheetDialog.SelectedIndexListener() {
            @Override
            public void selectedIndex(int index) {
                if (index != -1) {
                    vmFoodMenu.getSelectedDensityInfo().postValue(vmFoodMenu.getData().getValue().getDensityInfo().get(index));
                }
                bottomSheet.dismiss();
            }
        });
        bottomSheet.show();
    }




    private void setupViewPager() {
        binding.vpFood.setOffscreenPageLimit(7);
        int todayIndex=0;
        for (int i = 0; i < vmFoodMenu.getData().getValue().getFoodInfo().size(); i++) {
            value = vmFoodMenu.getData().getValue();
            pageAdapterFragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu.FoodMenuFragment.newInstance(value.getFoodInfo().get(i),value.getShuttleInfo()));
            pageAdapterFragmentTitles.add(value.getFoodInfo().get(i).getDateTitle());
            if (value.getFoodInfo().get(i).getIsToday()){
                todayIndex =i;
            }
        }
        adapter = new PageAdapter(getSupportFragmentManager(), pageAdapterFragments, pageAdapterFragmentTitles);

        binding.vpFood.setAdapter(adapter);
        binding.tabFood.setupWithViewPager(binding.vpFood);
        TabLayout.Tab tab = binding.tabFood.getTabAt(todayIndex);
        if (tab != null) {
            tab.select();
        }
    }
}
