package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.DialogMessageBinding;


public class MessageDialog extends DialogFragment {
    private DialogMessageBinding binding;
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private DialogConfirmedListener listener;

    private DialogOnDismissListener dismissListener;

    public interface DialogConfirmedListener {
        void onConfirmed();
    }

    public interface DialogOnDismissListener {
        void onDismiss();
    }

    public static MessageDialog newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_message, container, false);
        setTextsToDialog();
        binding.btnConfirm.setOnClickListener(v -> confirmDialog());
        return binding.getRoot();
    }

    private void setTextsToDialog() {
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        binding.tvTitle.setText(title);
        binding.tvMessage.setText(message);
    }

    public void setListener(DialogConfirmedListener listener) {
        this.listener = listener;
    }

    public void setOnDismissListener(DialogOnDismissListener listener) {
        this.dismissListener = listener;
    }

    private void confirmDialog() {
        if (listener != null) {
            listener.onConfirmed();
        }
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        dismissListener.onDismiss();
        super.onDismiss(dialog);
    }
}
