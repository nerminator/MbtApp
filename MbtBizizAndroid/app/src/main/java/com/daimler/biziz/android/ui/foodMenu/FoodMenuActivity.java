package com.daimler.biziz.android.ui.foodMenu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.PageAdapter;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityFoodMenuBinding;
import com.daimler.biziz.android.network.entity.food.DensityInfo;
import com.daimler.biziz.android.network.entity.food.FoodListEntity;
import com.daimler.biziz.android.ui.dialogs.bottomSheetDialog.PickerBottomSheetDialog;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

import java.util.ArrayList;
import java.util.List;

public class  FoodMenuActivity extends BaseActivity<ActivityFoodMenuBinding> {

    private PageAdapter adapter;
    private VMFoodMenu vmFoodMenu;
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
        vmFoodMenu = ViewModelProviders.of(this).get(VMFoodMenu.class);
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
        adapter = new PageAdapter(getSupportFragmentManager());
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
        adapter = new PageAdapter(getSupportFragmentManager());
        int todayIndex=0;
        for (int i = 0; i < vmFoodMenu.getData().getValue().getFoodInfo().size(); i++) {
            value = vmFoodMenu.getData().getValue();
            adapter.addFragment(FoodMenuFragment.newInstance(value.getFoodInfo().get(i),value.getShuttleInfo()),value.getFoodInfo().get(i).getDateTitle());
            if (value.getFoodInfo().get(i).getIsToday()){
                todayIndex =i;
            }
        }

        binding.vpFood.setAdapter(adapter);
        binding.tabFood.setupWithViewPager(binding.vpFood);
        binding.tabFood.getTabAt(todayIndex).select();

    }





}
