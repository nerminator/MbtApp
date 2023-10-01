package com.daimler.biziz.android.ui.dialogs.bottomSheetDialog;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.databinding.BottomSheetSupportBinding;

public class SupportCustomBottomSheetDialog extends BottomSheetDialog {
    BottomSheetBehavior<FrameLayout> sheetBehavior;
    private FrameLayout bottomSheet;

    public static SupportCustomBottomSheetDialog createCustomDialog(Fragment fragment) {
        return new SupportCustomBottomSheetDialog(fragment.getContext(), fragment);
    }

    public SupportCustomBottomSheetDialog(@NonNull Context context, Fragment fragment) {
        super(context);
        setCancelable(true);
        BottomSheetSupportBinding bottomSheetCommonBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_support, null, true);
        setContentView(bottomSheetCommonBinding.getRoot());

        bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetCommonBinding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bottomSheetCommonBinding.btnAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", fragment.getString(R.string.TXT_LOGIN_SUPPORT_PHONE1), null));
                fragment.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public SupportCustomBottomSheetDialog(@NonNull Context context, int theme, Fragment fragment) {
        super(context, theme);
    }



}
