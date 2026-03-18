package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.databinding.DialogYearPickerBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class YearPickerDialog extends DialogFragment {

    //private Year binding;

    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";
    //private ConfirmationDialog.ConfirmationDialogListener listener;
    private DialogYearPickerBinding binding;

    public static YearPickerDialog newInstance(String title,String description) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE,title);
        args.putString(KEY_DESCRIPTION,description);
        YearPickerDialog fragment = new YearPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

   /* public interface ConfirmationDialogListener {
        void onConfirmed(boolean isConfirmed);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.dialog_year_picker, container, false);
        setTextsToDialog();
        setRange();
        setListener();
        return binding.getRoot();

    }

    private void setListener() {
        binding.yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Toast.makeText(picker.getContext(), "Value was: " + Integer.toString(oldVal) + " is now: " + Integer.toString(newVal), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setTextsToDialog(){
        String title=getArguments().getString(KEY_TITLE);
        binding.tvHeader.setText(title);
        binding.yearPicker.setWrapSelectorWheel(false);
    }

    private void setRange() {
        int currentYear=Calendar.getInstance().get(Calendar.YEAR);
        int minYear=currentYear-10;
        binding.yearPicker.setDisplayedValues(getDisplayValues(minYear,currentYear));
        binding.yearPicker.setMaxValue(getDisplayValues(minYear,currentYear).length - 1);
    }


    public String[] getDisplayValues(int minimumInclusive, int maximumInclusive) {

        ArrayList<String> result = new ArrayList<String>();

        for(int i = maximumInclusive; i >= minimumInclusive; i--) {
            result.add("deneme");
        }

        return result.toArray(new String[0]);
    }

}
