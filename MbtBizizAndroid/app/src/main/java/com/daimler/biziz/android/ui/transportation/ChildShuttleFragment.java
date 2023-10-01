package com.daimler.biziz.android.ui.transportation;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.TransportationAdapter;
import com.daimler.biziz.android.base.BaseFragment;
import com.daimler.biziz.android.databinding.FragmentChildShuttleBinding;
import com.daimler.biziz.android.network.entity.transportation.CompanyListEntity;
import com.daimler.biziz.android.network.entity.transportation.DriverInfoEntity;
import com.daimler.biziz.android.network.entity.transportation.ShuttleListEntity;
import com.daimler.biziz.android.ui.dialogs.bottomSheetDialog.TransportationCustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildShuttleFragment extends BaseFragment {

    private TransportationAdapter transportationAdapter;
    private List<ShuttleListEntity> shuttleList = new ArrayList<>();
    private FragmentChildShuttleBinding binding;
    private CompanyListEntity companyListEntity;

    public ChildShuttleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_child_shuttle, container, false);
        setAdapter();
        init();
        return binding.getRoot();
    }

    private void setAdapter() {
        transportationAdapter = new TransportationAdapter(shuttleList, driverInfoEntity -> TransportationCustomDialog.createTransportationDialog(ChildShuttleFragment.this, driverInfoEntity).show());
        binding.recyclerShuttle.setAdapter(transportationAdapter);
    }

    private void init() {
        companyListEntity = (CompanyListEntity) getArguments().getSerializable("companyEntity");
        if (companyListEntity != null && companyListEntity.getShuttleList() != null) {
            shuttleList.addAll(companyListEntity.getShuttleList());
            transportationAdapter.notifyDataSetChanged();
        }
    }

    public static ChildShuttleFragment newInstance(CompanyListEntity companyListEntity) {
        Bundle args = new Bundle();
        args.putSerializable("companyEntity", companyListEntity);
        ChildShuttleFragment fragment = new ChildShuttleFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
