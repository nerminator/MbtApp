package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.BottomSheetSupportBinding;

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

        bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetCommonBinding.button2.setOnClickListener(v -> dismiss());
        bottomSheetCommonBinding.btnAra.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", fragment.getString(R.string.TXT_LOGIN_SUPPORT_PHONE_FOR_CALL), null));
            fragment.getActivity().startActivity(intent);
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
