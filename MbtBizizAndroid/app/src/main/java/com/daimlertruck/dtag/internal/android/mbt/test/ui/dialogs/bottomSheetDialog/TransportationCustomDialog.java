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
import android.view.View;
import android.widget.FrameLayout;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.BottomSheetTransportationBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListEntity;

public class TransportationCustomDialog extends BottomSheetDialog {
    BottomSheetBehavior<FrameLayout> sheetBehavior;
    private FrameLayout bottomSheet;

    public static TransportationCustomDialog createTransportationDialog(Fragment fragment, ShuttleListEntity shuttleListEntity) {
        return new TransportationCustomDialog(fragment.getContext(), fragment, shuttleListEntity);
    }

    public TransportationCustomDialog(@NonNull Context context, Fragment fragment, ShuttleListEntity shuttleListEntity) {
        super(context);
        setCancelable(true);
        BottomSheetTransportationBinding bottomSheetCommonBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_transportation, null, false);
        bottomSheetCommonBinding.setData(shuttleListEntity);
        setContentView(bottomSheetCommonBinding.getRoot());

        bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", shuttleListEntity.getDriverInfo().getTelephone(), null));
                intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
                fragment.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}
