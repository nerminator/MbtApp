package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;


import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentOrchestraBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.ConfirmationDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.FlexibleWorkingActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule.WorkingScheduleActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrchestraFragment extends Fragment {
    private FragmentOrchestraBinding binding;
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.VmOrchestra vmOrchestra;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    //SharedPreferenceManager sharedPreferenceManager=new SharedPreferenceManager(this.getContext());

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orchestra, container, false);
        vmOrchestra = ViewModelProviders.of(getActivity()).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.VmOrchestra.class);
        binding.setViewmodel(vmOrchestra);
        binding.setLifecycleOwner(this);
        setClickListeners();
        observer();
        return binding.getRoot();
    }

    private void observer() {
        vmOrchestra.logoutSucced.observe(this, aBoolean -> {
            if (aBoolean) {
                sharedPreferenceManager.logout();
                Intent intent = new Intent();
                intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
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
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(
                getString(R.string.TXT_SETTINGS_ALERT_DIALOG_TITLE),
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
