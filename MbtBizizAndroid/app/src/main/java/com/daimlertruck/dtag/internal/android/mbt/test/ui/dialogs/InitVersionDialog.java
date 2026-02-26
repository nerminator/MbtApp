package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.DialogInitVersionBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.splash.InitUpdateDialogType;


public class InitVersionDialog extends DialogFragment {
    private DialogInitVersionBinding binding;
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DOWNLOAD = "KEY_DOWNLOAD";
    private static final String KEY_CONTINUE = "KEY_CONTINUE";
    private static final String KEY_DOWNLOAD_LINK = "KEY_DOWNLOAD_LINK";
    private static final String KEY_IS_SOFT_UPDATE = "KEY_SOFT_UPDATE";
    private VersionDialogListener listener;

    public static InitVersionDialog newInstance(String title, String description, String downloadApp, String continueToApp, InitUpdateDialogType dialogType, String downloadLink) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_DESCRIPTION, description);
        args.putString(KEY_DOWNLOAD, downloadApp);
        args.putString(KEY_CONTINUE, continueToApp);
        args.putString(KEY_DOWNLOAD_LINK, downloadLink);
        args.putBoolean(KEY_IS_SOFT_UPDATE, dialogType == InitUpdateDialogType.SOFT);
        InitVersionDialog fragment = new InitVersionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface VersionDialogListener {
        void onDownloadApp(String url);

        void onContinueToApp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_init_version, container, false);
        setTextsToDialog();
        setClickListeners();
        setVisibility();
        setCancelableStatus();
        return binding.getRoot();
    }

    private void setCancelableStatus() {
        boolean isSoftUpdate = getArguments().getBoolean(KEY_IS_SOFT_UPDATE);
        setCancelable(isSoftUpdate);
    }

    private void setVisibility() {
        boolean isSoftUpdate = getArguments().getBoolean(KEY_IS_SOFT_UPDATE);
        int btnContinueVisibility = View.GONE;
        if (isSoftUpdate) btnContinueVisibility = View.VISIBLE;
        binding.btnContinue.setVisibility(btnContinueVisibility);
    }

    private void setTextsToDialog() {
        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_DESCRIPTION);
        String continueToApp = getArguments().getString(KEY_CONTINUE);
        String downloadApp = getArguments().getString(KEY_DOWNLOAD);
        binding.tvHeader.setText(title);
        binding.tvDescription.setText(message);
        binding.btnContinue.setText(continueToApp);
        binding.btnDownloadApp.setText(downloadApp);
    }


    private void setClickListeners() {
        String downloadUrl = getArguments().getString(KEY_DOWNLOAD_LINK);
        View.OnClickListener clickListener = v -> {
            if (v.getId() == R.id.btnContinue) {
                listener.onContinueToApp();
            } else if (v.getId() == R.id.btnDownloadApp) {
                listener.onDownloadApp(downloadUrl);
            }
            dismiss();
        };
        binding.btnDownloadApp.setOnClickListener(clickListener);
        binding.btnContinue.setOnClickListener(clickListener);
    }

    public void setListener(VersionDialogListener listener) {
        this.listener = listener;
    }

}
