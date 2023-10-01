package com.daimler.biziz.android.ui.main.orchestra;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.databinding.FragmentOrchestraBinding;
import com.daimler.biziz.android.ui.about.AboutActivity;
import com.daimler.biziz.android.ui.dialogs.ConfirmationDialog;
import com.daimler.biziz.android.ui.main.orchestra.flexibleWorkingInfo.FlexibleWorkingActivity;
import com.daimler.biziz.android.ui.main.orchestra.schedule.WorkingScheduleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrchestraFragment extends Fragment {
    private FragmentOrchestraBinding binding;
    private VmOrchestra vmOrchestra;

    public static OrchestraFragment newInstance() {

        Bundle args = new Bundle();

        OrchestraFragment fragment = new OrchestraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public OrchestraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orchestra, container, false);
        vmOrchestra = ViewModelProviders.of(getActivity()).get(VmOrchestra.class);
        binding.setViewmodel(vmOrchestra);
        binding.setLifecycleOwner(this);
        setClickListeners();
        observer();
        return binding.getRoot();
    }

    private void observer() {
        vmOrchestra.logoutSucced.observe(this, aBoolean -> {
            if (aBoolean) {
                Intent intent = new Intent();
                intent.setAction("com.daimler.biziz.intent.LOG_OUT");
                getActivity().sendBroadcast(intent);
            }
        });
    }

    private void setClickListeners() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnWorkInfo:
                        FlexibleWorkingActivity.start(getContext(),
                                vmOrchestra.getProfileEntity().getValue().getYearlyWorkHoursText(),
                                vmOrchestra.getProfileEntity().getValue().getMonthlyWorkHoursText());
                        break;
                    case R.id.btnWorkCalender:
                        WorkingScheduleActivity.start(getContext());
                        break;
                    case R.id.imgInfo:
                        AboutActivity.start(getContext());
                        break;
                    case R.id.imgSetting:
                        //Logout
                        //SettingsActivity.start(getContext());
                        getAlertDialog();
                        break;

                }
            }
        };
        binding.btnWorkCalender.setOnClickListener(clickListener);
        binding.btnWorkInfo.setOnClickListener(clickListener);
        binding.imgInfo.setOnClickListener(clickListener);
        binding.imgSetting.setOnClickListener(clickListener);
    }

    public void getAlertDialog() {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(getString(R.string.TXT_SETTINGS_ALERT_DIALOG_TITLE),
                getString(R.string.TXT_PROFILE_SETTINGS_QUIT_WARNING),
                getString(android.R.string.yes),
                getString(android.R.string.no)
        );
        confirmationDialog.setListener(new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onConfirmed(boolean isConfirmed) {
                if (isConfirmed){
                    vmOrchestra.logout();
                }
            }
        });
        confirmationDialog.show(getChildFragmentManager(),"");
    }


}
