package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimlertruck.dtag.internal.android.mbt.BuildConfig;
import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentOrchestraBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.base.BaseResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.ActivateCardResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.BusinessCardStateResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile.PayslipEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.WorkCalendarEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.APIService;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.AbstractApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.ApiUtils;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.ConfirmationDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.htmlview.KvkkActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.flexibleWorkingInfo.FlexibleWorkingActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule.WorkingScheduleActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.network.NetworkCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

// add these:
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrchestraFragment extends Fragment {
    private FragmentOrchestraBinding binding;
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.VmOrchestra vmOrchestra;
    private retrofit2.Callback currentCall;
    private static final String TAG = "OrchestraFragment";

    @Inject
    public AbstractApiUtils abstractApiUtils;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    //SharedPreferenceManager sharedPreferenceManager=new SharedPreferenceManager(this.getContext());


    private ActivityResultLauncher<Intent> kvkkLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    boolean approved = result.getData().getBooleanExtra("approved", false);

                    if (approved) {
                        // CALL DIGITAL CARD ACTIVATION API HERE
                        activateCard();
                    } else {
                        // user rejected KVKK
                        //Toast.makeText(getContext(), "KVKK not approved", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
                Intent intent = new Intent("com.daimler.biziz.intent.LOG_OUT");
                intent.setPackage(requireContext().getPackageName()); // veya BuildConfig.APPLICATION_ID
                requireActivity().sendBroadcast(intent, BuildConfig.APPLICATION_ID + ".permission.INTERNAL_BROADCAST");
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

                    /*case R.id.btnPayslip:
                        com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.PayslipOtpActivity.start(getContext());
                        break;*/

                    case R.id.btnDigitalCard:
                        handleDigitalCardClick();
                }
            }
        };
        binding.btnWorkCalender.setOnClickListener(clickListener);
        binding.btnWorkInfo.setOnClickListener(clickListener);
        binding.imgInfo.setOnClickListener(clickListener);
        binding.imgSetting.setOnClickListener(clickListener);
        //binding.btnPayslip.setOnClickListener(clickListener);
        binding.btnDigitalCard.setOnClickListener(clickListener);
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
    public void handleDigitalCardClick (){

         abstractApiUtils.getUserBusinessCardState( new NetworkCallback<BaseResponse<BusinessCardStateResponse>>() {
             @Override
             public void onSuccess(BaseResponse<BusinessCardStateResponse> response) {
                    if (response.getResponseData().isActive()) {
                        // ACTIVE → show action sheet
                        showDigitalCardBottomSheet(response.getResponseData().getDigitalCardUrl());
                    } else {
                        // önce KVKK onayi al
                        Intent i = KvkkActivity.getIntent(getContext());
                        kvkkLauncher.launch(i);
                    }
             }
             @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                Toast.makeText(getContext(), "Business card service failed", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNetworkFailure(Throwable message) {
                Toast.makeText(getContext(), "Business card network failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void showDigitalCardBottomSheet(String cardUrl) {

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_business_card, null);
        dialog.setContentView(view);

        view.findViewById(R.id.showCard).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cardUrl));
            startActivity(browserIntent);
            dialog.dismiss();
        });

        view.findViewById(R.id.shareCard).setOnClickListener(v -> {
            shareDigitalCard(cardUrl);
            dialog.dismiss();
        });

        view.findViewById(R.id.deactivateCard).setOnClickListener(v -> {
            showDeactivateConfirmation();
            dialog.dismiss();
        });

        view.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showDeactivateConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.TXT_CARD_MENU_CANCEL)
                .setMessage(R.string.TXT_CARD_DEACTIVATE_MESSAGE)
                .setPositiveButton(R.string.TXT_COMMON_YES, (d, w) -> {
                    deactivateBusinessCard();
                })
                .setNegativeButton(R.string.TXT_COMMON_CANCEL, null)
                .show();
    }
    private void shareDigitalCard(String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.TXT_CARD_SHARE_PREFIX) );
        startActivity(shareIntent);
    }

    private void activateCard() {
        abstractApiUtils.activateDigitalCard(new NetworkCallback<BaseResponse<ActivateCardResponse>>() {

            @Override
            public void onSuccess(BaseResponse<ActivateCardResponse> response) {
                if (response.getResponseData().getDigitalCardUrl() != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            android.net.Uri.parse(response.getResponseData().getDigitalCardUrl()));
                    startActivity(browserIntent);
                }
            }

            @Override
            public void onServiceFailure(int httpResponseCode, String message) {
                Toast.makeText(getContext(), "Service error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(Throwable message) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deactivateBusinessCard() {

        abstractApiUtils.deactivateDigitalCard(new NetworkCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse res) {
                Toast.makeText(getContext(), R.string.TXT_CARD_DEACTIVATE_TOAST, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceFailure(int code, String message) {
                Toast.makeText(getContext(), "Service error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetworkFailure(Throwable t) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

