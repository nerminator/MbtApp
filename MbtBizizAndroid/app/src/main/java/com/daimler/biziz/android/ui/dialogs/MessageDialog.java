package com.daimler.biziz.android.ui.dialogs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.databinding.DialogMessageBinding;


public class MessageDialog extends DialogFragment {
    private DialogMessageBinding binding;
    private static final String TITLE="title";
    private static final String MESSAGE="message";
    private DialogConfirmedListener listener;

    public interface DialogConfirmedListener {
        void onConfirmed();
    }

    public static MessageDialog newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString(TITLE,title);
        args.putString(MESSAGE,message);
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.dialog_message, container, false);
        setTextsToDialog();
        binding.btnConfirm.setOnClickListener(v -> confirmDialog());
        return binding.getRoot();
    }

    private void setTextsToDialog(){
        String title=getArguments().getString(TITLE);
        String message=getArguments().getString(MESSAGE);
        binding.tvTitle.setText(title);
        binding.tvMessage.setText(message);
    }

    public void setListener(DialogConfirmedListener listener) {
        this.listener = listener;
    }

    private void confirmDialog(){
        if (listener!=null) {
            listener.onConfirmed();
        }
        dismiss();
    }


}
