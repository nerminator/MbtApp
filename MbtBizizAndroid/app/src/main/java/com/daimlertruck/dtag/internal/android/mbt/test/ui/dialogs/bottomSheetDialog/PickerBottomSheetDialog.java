package com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.DialogYearPickerBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.utils.BottomSheelHelper;

import java.lang.annotation.Retention;
import java.util.ArrayList;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PickerBottomSheetDialog extends BottomSheetDialog {


    public static final String TYPE_FOOD = "TYPE_FOOD";
    public static final String TYPE_YEAR = "TYPE_YEAR";
    public static final String TYPE_LOCATION = "TYPE_LOCATION";
    public static final String TYPE_OPTION = "TYPE_OPTION";

    @Retention(SOURCE)
    @StringDef({
            TYPE_FOOD,
            TYPE_YEAR,
            TYPE_LOCATION,
            TYPE_OPTION
    })
    public @interface Type_BottomSheet {}



    public static PickerBottomSheetDialog createCustomDialog(Context context,@Type_BottomSheet String type,ArrayList<String> list) {
        return new PickerBottomSheetDialog(context,type,list);
    }

    public static PickerBottomSheetDialog createCustomDialog(Context context,@Type_BottomSheet String type,ArrayList<String> list,int selectedIndex) {
        return new PickerBottomSheetDialog(context,type,list,selectedIndex);
    }
    private DialogYearPickerBinding binding;
    private final String type;
    private final ArrayList<String> list;
    private SelectedIndexListener listener;
    int selectedIndex=0;

    public PickerBottomSheetDialog(@NonNull Context context, @Type_BottomSheet String type, ArrayList<String> list,int selectedIndex) {
        super(context);
        binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_year_picker, null, false);
        this.type = type;
        this.list = list;
        this.selectedIndex = selectedIndex;
        setContentView(binding.getRoot());
        init();
    }


    public PickerBottomSheetDialog(@NonNull Context context, @Type_BottomSheet String type, ArrayList<String> list) {
        super(context);
        binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_year_picker, null, false);
        this.type = type;
        this.list = list;
        setContentView(binding.getRoot());
        init();
    }
    public void setListener(SelectedIndexListener mlistener){
        listener=mlistener;
    }
    private void init(){
        setTextsToDialog();
        setRange();
        setListener();
    }

    private void setListener() {
        binding.yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedIndex = newVal;
                //Toast.makeText(picker.getContext(), "Value was: " + Integer.toString(oldVal) + " is now: " + Integer.toString(newVal), Toast.LENGTH_SHORT).show();

            }
        });

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectedIndex(selectedIndex);
            }
        });
    }

    private void setTextsToDialog(){
        //String title=getArguments().getString(KEY_TITLE);
        if(type.equals(TYPE_FOOD)){
            binding.tvHeader.setText(getContext().getString(R.string.TXT_MENU_MENU_DENSITY));
        } else if (type.equals(TYPE_OPTION)){
            binding.tvHeader.setText(getContext().getString(R.string.TXT_SHUTTLE_SHUTTLE_TYPE));
        } else  if (type.equals(TYPE_LOCATION)){
            binding.tvHeader.setText(getContext().getString(R.string.TXT_SHUTTLE_SHUTTLE_COMPANY_LOCATION));
        } else {
            binding.tvHeader.setText(getContext().getString(R.string.TXT_PROFILE_FLEXIBLE_WORK_YEAR));
        }

        binding.yearPicker.setWrapSelectorWheel(false);
    }

    private void setRange() {
        if(type.equals(TYPE_YEAR) || type.equals(TYPE_LOCATION) || type.equals(TYPE_OPTION)){
            binding.yearPicker.setDisplayedValues(BottomSheelHelper.getDisplayValues(list));
            binding.yearPicker.setMaxValue(list.size() - 1);
        }
        else {
            binding.yearPicker.setDisplayedValues(list.toArray(new String[0]));
            binding.yearPicker.setMaxValue(list.size() - 1);
        }
        binding.yearPicker.setValue(selectedIndex);
    }


    public interface SelectedIndexListener {
        void selectedIndex(int index);
    }
}
