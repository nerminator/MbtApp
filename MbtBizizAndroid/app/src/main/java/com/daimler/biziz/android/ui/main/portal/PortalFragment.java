package com.daimler.biziz.android.ui.main.portal;


import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseApplication;
import com.daimler.biziz.android.databinding.FragmentPortalBinding;
import com.daimler.biziz.android.manager.SharedPreferenceManager;
import com.daimler.biziz.android.ui.about.AboutActivity;
import com.daimler.biziz.android.ui.dialogs.ConfirmationDialog;
import com.daimler.biziz.android.ui.dialogs.bottomSheetDialog.SupportCustomBottomSheetDialog;
import com.daimler.biziz.android.ui.foodMenu.FoodMenuActivity;
import com.daimler.biziz.android.ui.news.NewsActivity;

import com.daimler.biziz.android.ui.residential.ResidentialActivity;
import com.daimler.biziz.android.ui.transportation.TransportationActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortalFragment extends Fragment {

    private static final int REQUEST_APP_SETTINGS = 168;

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    private VMPortal vmPortal;
    private FragmentPortalBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private AlertDialog alertDialog;


    public static PortalFragment newInstance() {
        PortalFragment fragment = new PortalFragment();
        return fragment;
    }

    public PortalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //FragmentPortalBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_portal, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portal, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectPortalFragment(this);

        vmPortal = ViewModelProviders.of(getActivity()).get(VMPortal.class);
        binding.setViewmodel(vmPortal);
        binding.setLifecycleOwner(this);
        initView();
        vmPortal.sendDeviceInfo();
        vmPortal.getUserConfig();
        return binding.getRoot();
    }

    private void initView() {
        decideShowNotificationPopUp();
        setClickListener();
    }

    private void decideShowNotificationPopUp() {
        if (!sharedPreferenceManager.isShowedNotificationPopup()){
            showNotificationDialog();
        }
    }




    public void showNotificationDialog() {
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(getString(R.string.TXT_COMMON_WARNING),
                getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE),
                getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE_CHANGE_SETTINGS),
                getString(R.string.TXT_NOTIFICATION_ALERT_MESSAGE_KEEP_SETTINGS)
        );
        confirmationDialog.setListener(new ConfirmationDialog.ConfirmationDialogListener() {
            @Override
            public void onConfirmed(boolean isConfirmed) {
                if (isConfirmed){
                    goToSettings();
                }
                sharedPreferenceManager.setShowedNotificationPopup("1");
            }
        });
        confirmationDialog.show(getChildFragmentManager(),"");
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    //region Bottom Sheet


    private void setClickListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.imgCallCenter:
                        SupportCustomBottomSheetDialog.createCustomDialog(PortalFragment.this).show();
                        break;
                    case R.id.imgInfo:
                        AboutActivity.start(getContext());
                        break;
                    case R.id.cardEatingMenu:
                        FoodMenuActivity.start(getContext());
                        break;
                    case R.id.cardNews:
                        NewsActivity.start(getContext());
                        break;
                    case R.id.cardTransportation:
                        TransportationActivity.start(getContext());
                        break;
                    case R.id.cardPlace:
                        ResidentialActivity.start(getContext());
                        break;

                }
            }
        };
        binding.imgCallCenter.setOnClickListener(onClickListener);
        binding.imgInfo.setOnClickListener(onClickListener);
        binding.cardEatingMenu.setOnClickListener(onClickListener);
        binding.cardTransportation.setOnClickListener(onClickListener);
        binding.cardNews.setOnClickListener(onClickListener);
        binding.cardPlace.setOnClickListener(onClickListener);
    }
    //endregion


}
