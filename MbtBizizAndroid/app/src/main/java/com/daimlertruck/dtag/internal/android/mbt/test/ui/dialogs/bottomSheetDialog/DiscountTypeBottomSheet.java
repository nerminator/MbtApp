package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog;


import android.app.Dialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentDiscountTypeBottomSheetBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountTypeBottomSheet extends BottomSheetDialogFragment {


    private int selectedIndex;
    private DiscountSelectCallback callback;

    public static DiscountTypeBottomSheet newInstance(DiscountSelectCallback callback, int selectedIndex) {
        DiscountTypeBottomSheet fragment = new DiscountTypeBottomSheet();
        fragment.callback = callback;
        fragment.selectedIndex = selectedIndex;
        return fragment;
    }

    /*public static String[] catNames = {"Eğitim", "E-Ticaret", "Ev/Dekorasyon", "Gastronomi", "Gayrimenkul"
            , "Giyim", "Otomotiv", "Sağlık", "Spor ve Yaşam Merkezleri", "Temizlik ve Bakıcı Hizmetleri", "Turizm ve Konaklama", "Diğer"};*/
    public static String[] catNames;
    public static Integer[] catId = {2, 3, 4, 5, 6, 7, 8, 9, 12, 14,13};
    FragmentDiscountTypeBottomSheetBinding binding;

    public DiscountTypeBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discount_type_bottom_sheet, container, false);
        initList();
        initUi();
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        catNames=getResources().getStringArray(R.array.categoryNames);
        return super.onCreateDialog(savedInstanceState);
    }

    private void initUi() {
        if (selectedIndex != 1) {
            binding.categoryPicker.setValue(selectedIndex - 2);
        }
        binding.categoryPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedIndex = newVal;
            //Toast.makeText(picker.getContext(), "Value was: " + Integer.toString(oldVal) + " is now: " + Integer.toString(newVal), Toast.LENGTH_SHORT).show();

        });
        binding.tvOk.setOnClickListener(v -> {
            callback.onDiscountSelected(catId[selectedIndex], catNames[selectedIndex]);
            dismiss();
        });

        binding.categoryPicker.setWrapSelectorWheel(false);
    }

    private void initList() {
        binding.categoryPicker.setDisplayedValues(catNames);
        binding.categoryPicker.setMaxValue(catNames.length - 1);
    }

    public interface DiscountSelectCallback {
        void onDiscountSelected(int id, String catName);
    }
}
