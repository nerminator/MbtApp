package com.daimlertruck.dtag.internal.android.mbt.test.ui.foodMenu;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.FoodMenuAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.ShuttleAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentFoodMenuBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodInfo;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleInfo;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleList;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodMenuFragment extends BaseFragment {

    private FragmentFoodMenuBinding binding;
    private static final String KEY_FOOD_INFO = "KEY_FOOD_INFO";
    private static final String KEY_SHUTTLE_INFO = "KEY_SHUTTLE_INFO";
    FoodInfo foodInfo;
    ShuttleInfo shuttleInfo;


    public static FoodMenuFragment newInstance(FoodInfo foodInfo, ShuttleInfo shuttleInfo) {
        Bundle args = new Bundle();
        FoodMenuFragment fragment = new FoodMenuFragment();
        args.putParcelable(KEY_FOOD_INFO,foodInfo);
        args.putParcelable(KEY_SHUTTLE_INFO,shuttleInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public FoodMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_food_menu, container, false);

        foodInfo= (FoodInfo) getArguments().get(KEY_FOOD_INFO);
        shuttleInfo= (ShuttleInfo) getArguments().get(KEY_SHUTTLE_INFO);
        setBindingData();
        setAdapter();
        return binding.getRoot();
    }

    private void setBindingData() {
        if (foodInfo !=null) {
            binding.setData(foodInfo);

        }
        if (shuttleInfo !=null) {
           binding.setDataShuttle(shuttleInfo);
        }
    }

    private void setAdapter() {
            if (foodInfo !=null) {
                FoodMenuAdapter foodMenuAdapter = new FoodMenuAdapter(foodInfo);
                binding.rcyclerFoodMenu.setAdapter(foodMenuAdapter);


            }
            if (shuttleInfo !=null) {
                ShuttleAdapter shuttleAdapter = new ShuttleAdapter((ArrayList<ShuttleList>) shuttleInfo.getShuttleList());
                binding.rcyclerShuttle.setAdapter(shuttleAdapter);
            }
        }

}
