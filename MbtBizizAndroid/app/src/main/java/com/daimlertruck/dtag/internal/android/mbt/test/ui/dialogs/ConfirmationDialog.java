package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.DialogConfirmationBinding;


public class ConfirmationDialog extends DialogFragment {
    private DialogConfirmationBinding binding;
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_POZ="KEY_POZ";
    private static final String KEY_NEG="KEY_NEG";
    private ConfirmationDialogListener listener;

    public static ConfirmationDialog newInstance(String title,String description) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE,title);
        args.putString(KEY_DESCRIPTION,description);
        ConfirmationDialog fragment = new ConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ConfirmationDialog newInstance(String title,String description,String poz,String neg) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE,title);
        args.putString(KEY_DESCRIPTION,description);
        args.putString(KEY_POZ,poz);
        args.putString(KEY_NEG,neg);
        ConfirmationDialog fragment = new ConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ConfirmationDialogListener {
        void onConfirmed(boolean isConfirmed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.dialog_confirmation, container, false);
        setTextsToDialog();
        setClickListeners();
        return binding.getRoot();
    }

    private void setTextsToDialog(){
        String title=getArguments().getString(KEY_TITLE);
        String message=getArguments().getString(KEY_DESCRIPTION);
        String poz=getArguments().getString(KEY_POZ);
        String neg=getArguments().getString(KEY_NEG);
        binding.tvHeader.setText(title);
        binding.tvDescription.setText(message);
        binding.btnSend.setText(poz);
        binding.btnDismiss.setText(neg);
    }


    private void setClickListeners() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSend:
                        listener.onConfirmed(true);
                        break;
                    case R.id.btnDismiss:
                        listener.onConfirmed(false);
                        break;
                }
                dismiss();

            }
        };
        binding.btnSend.setOnClickListener(clickListener);
        binding.btnDismiss.setOnClickListener(clickListener);
    }

    public void setListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }

}
