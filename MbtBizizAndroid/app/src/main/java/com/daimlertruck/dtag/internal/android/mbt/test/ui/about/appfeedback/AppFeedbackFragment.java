package com.daimlertruck.dtag.internal.android.mbt.test.ui.about.appfeedback;


import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentAppFeedbackBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.MessageDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppFeedbackFragment extends BaseFragment {

    private FragmentAppFeedbackBinding binding;
    private VMAppFeedback vmAppFeedback;

    public AppFeedbackFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_feedback, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectAppFeedbackFragment(this);
        vmAppFeedback = ViewModelProviders.of(getActivity()).get(VMAppFeedback.class);
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_APP_FEEDBACK), null);
        binding.toolbar.setVm(vmToolbar);
        binding.toolbar.imgvInfoToolbar.setVisibility(View.GONE);
        initView();
        observe();
        return binding.getRoot();
    }

    private void observe() {
        vmAppFeedback.getShowInfoDialog().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showFeedbackDialog(getString(R.string.TXT_FEEDBACK_SUCCESS), false);
            } else {
                showFeedbackDialog(getString(R.string.TXT_FEEDBACK_ERROR), true);
            }
        });

        vmAppFeedback.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });
    }

    private void initView() {
        binding.cardSendFeedback.setOnClickListener(v -> sendFeedback());
        binding.edtFeedback.requestFocus();
        new Handler().postDelayed(() -> {
            binding.edtFeedback.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
            binding.edtFeedback.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
        }, 200);
    }

    private void sendFeedback() {
        String feedbackText = String.valueOf(binding.edtFeedback.getText());
        if (!TextUtils.isEmpty(feedbackText) && feedbackText.length() > 2) {
            vmAppFeedback.sendFeedback(feedbackText);
        } else {
            showFeedbackDialog(getString(R.string.TXT_FEEDBACK_CHARACTER_ERROR), true);
        }
    }

    private void showFeedbackDialog(String description, Boolean isError) {
        MessageDialog messageDialog = MessageDialog.newInstance(
                getString(R.string.TXT_FEEDBACK_DIALOG_TITLE),
                description
        );
        messageDialog.setOnDismissListener(() -> {
            if (!isError) {
                BaseActivity activity = (BaseActivity) getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.onBackPressed();
                }
            }
        });
        messageDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    public static AppFeedbackFragment newInstance() {
        Bundle args = new Bundle();
        AppFeedbackFragment fragment = new AppFeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
