package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.flexibleHours;


import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentFlexibleHourBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.VMFlexibleWorking;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlexibleHourFragment extends Fragment {
    private FragmentFlexibleHourBinding binding;
    private VMFlexibleWorking vmFlexibleWorkingActivity;

    public static FlexibleHourFragment newInstance() {
        Bundle args = new Bundle();
        FlexibleHourFragment fragment = new FlexibleHourFragment();

        fragment.setArguments(args);
        return fragment;
    }

    public FlexibleHourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_flexible_hour, container, false);
        // TODO: 17.04.2018 VM olusturulacak. simdilik sadece tasarim
        vmFlexibleWorkingActivity = ViewModelProviders.of(getActivity()).get(VMFlexibleWorking.class);
        setAdapterRecycler();
        setRecyclerViewScrollListener();
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_flexible_hour, container, false);
    }


    private void setRecyclerViewScrollListener() {

        binding.rcyclerFlexibleHours.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (IsRecyclerViewAtTop()){
                    //vmFlexibleWorkingActivity.getYearPickerVisibility().postValue(true);
                }
                else {
                    //vmFlexibleWorkingActivity.getYearPickerVisibility().postValue(false);
                }
            }
        });
    }

    private boolean IsRecyclerViewAtTop()   {
        if(binding.rcyclerFlexibleHours.getChildCount() == 0)
            return true;
        return binding.rcyclerFlexibleHours.getChildAt(0).getTop() == 0;
    }
    private void setAdapterRecycler() {
   /*     WorkInfoAdapter adapter = new WorkInfoAdapter();
        binding.rcyclerFlexibleHours.setAdapter(adapter);*/
    }

}
