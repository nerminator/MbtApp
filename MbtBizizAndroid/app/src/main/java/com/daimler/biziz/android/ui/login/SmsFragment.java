package com.daimler.biziz.android.ui.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.base.BaseFragment;
import com.daimler.biziz.android.databinding.FragmentSmsBinding;
import com.daimler.biziz.android.ui.main.main.MainActivity;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;
import com.daimler.biziz.android.uiControls.PinView;
import com.daimler.biziz.android.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsFragment extends BaseFragment {
    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";
    private VMSms vmSms;
    private FragmentSmsBinding binding;

    public static SmsFragment newInstance(String phoneNumber) {
        Bundle args = new Bundle();
        SmsFragment fragment = new SmsFragment();
        args.putString(KEY_PHONE_NUMBER,phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SmsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms, container, false);
        vmSms = ViewModelProviders.of(this).get(VMSms.class);
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOGIN_PIN_TITLE), null);
        binding.setViewmodel(vmSms);
        binding.buttonDemiCS.setActivated(false);

        binding.toolbar.setVm(vmToolbar);
        binding.setLifecycleOwner(this);
        init();
        return binding.getRoot();
    }

    private void observe() {
        vmSms.isLoading.observe(this, (Boolean aBoolean) -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });

        vmSms.smsSuccessed.observe(this, aBoolean -> {
            if (aBoolean) {
            Utils.hideKeyboard(binding.txtSmsEnterPin);
            getActivity().finish();
            MainActivity.start(getContext());
            }
        });


        vmSms.showErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showMessageDialog(null,s);
            }
        });

        vmSms.showErrorMessageForGoBack.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ((BaseActivity)getActivity()).showMessageDialog("",s,()-> {
                    getActivity().onBackPressed();
                });
            }
        });
    }

    private void init() {
        vmSms.phoneNumber.postValue(getArguments().getString(KEY_PHONE_NUMBER));
        binding.pinView.requestPinEntryFocus();
        binding.pinView.setPinViewEventListener(new PinView.PinViewEventListener() {
            @Override
            public void onDataEntered(PinView pinview, boolean fromUser, boolean allEntered) {
                vmSms.getPin().postValue(pinview.getValue());
                vmSms.isLoginButtonEnabled.postValue(allEntered);
            }

            @Override
            public void onKeyboardDoneClicked() {
                binding.btnLogin.performClick();
            }
        });

        timer();
        observe();
    }

    private void timer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                vmSms.timer.postValue("" + millisUntilFinished / 1000);
                vmSms.isSendAgainEnabled.set(false);
            }

            public void onFinish() {
                vmSms.isSendAgainEnabled.set(true);
                binding.buttonDemiCS.setActivated(true);

            }

        }.start();
    }
}
