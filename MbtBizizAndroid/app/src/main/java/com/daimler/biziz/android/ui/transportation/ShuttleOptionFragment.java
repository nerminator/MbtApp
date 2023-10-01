package com.daimler.biziz.android.ui.transportation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseFragment;
import com.daimler.biziz.android.databinding.FragmentShuttleOptionBinding;
import com.daimler.biziz.android.network.entity.transportation.CompanyLocationListEntity;
import com.daimler.biziz.android.network.entity.transportation.TypeListEntity;
import com.daimler.biziz.android.ui.dialogs.bottomSheetDialog.PickerBottomSheetDialog;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShuttleOptionFragment extends BaseFragment {


    private VMShuttleOption vmShuttleOption;
    private FragmentShuttleOptionBinding binding;
    private ArrayList<String> listTT = new ArrayList<>();
    private ArrayList<String> listTP = new ArrayList<>();

    private List<TypeListEntity> typeListEntities = new ArrayList<>();
    private List<CompanyLocationListEntity> companyLocationListEntities = new ArrayList<>();

    private TypeListEntity selectedType;
    private CompanyLocationListEntity selectedPoint;

    private int selectedPointIndex = 0, selectedTypeIndex = 0;
    private ArrayList<String> dialogList;

    public ShuttleOptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shuttle_option, container, false);
        vmShuttleOption = ViewModelProviders.of(this).get(VMShuttleOption.class);
        binding.setViewmodel(vmShuttleOption);
        binding.setLifecycleOwner(this);
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOGIN_HOME_SHUTTLE), null);
        binding.toolbar.setVm(vmToolbar);
        observe();
        setClickListener();
        return binding.getRoot();
    }

    private void setClickListener() {
        View.OnClickListener dialog = view -> {
            switch (view.getId()) {
                case R.id.txtChooseLocation:
                    createBottomSheetDialog(true);
                    break;
                case R.id.txtChooseShuttleType:
                    createBottomSheetDialog(false);
                    break;
            }
        };
        binding.txtChooseLocation.setOnClickListener(dialog);
        binding.txtChooseShuttleType.setOnClickListener(dialog);

        binding.btnNext.setOnClickListener(view -> {
                    if (selectedType != null && selectedPoint != null) {
                        ShuttleOptionFragment.this.loadFragment(R.id.container, TransportationFragment.newInstance(selectedType, selectedPoint), true);
                    } else
                        showMessageDialog(getString(R.string.TXT_COMMON_WARNING), getString(R.string.option_cannot_be_empty));
                }
        );
    }

    private void observe() {
        vmShuttleOption.getShuttleOptionEntity().observe(this, shuttleOptionEntity -> {
            listTT.clear();
            listTP.clear();
            if (shuttleOptionEntity != null) {
                typeListEntities = shuttleOptionEntity.getTypeList();
                companyLocationListEntities = shuttleOptionEntity.getCompanyLocationList();
                if (shuttleOptionEntity.getCompanyLocationList() != null) {
                    for (int i = 0; i < shuttleOptionEntity.getCompanyLocationList().size(); i++) {
                        CompanyLocationListEntity cl = shuttleOptionEntity.getCompanyLocationList().get(i);
                        if (cl.getIsDefault())
                            listTP.add(0, shuttleOptionEntity.getCompanyLocationList().get(i).getName());
                        else
                            listTP.add(shuttleOptionEntity.getCompanyLocationList().get(i).getName());
                    }
                }
                if (shuttleOptionEntity.getTypeList() != null) {
                    for (int i = 0; i < shuttleOptionEntity.getTypeList().size(); i++) {
                        TypeListEntity typeListEntity = shuttleOptionEntity.getTypeList().get(i);
                        listTT.add(i, typeListEntity.getName());
                    }
                }
            }
        });

        vmShuttleOption.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });
    }

    private void createBottomSheetDialog(boolean isLocation) {
        String pickerType = "";
        if (isLocation) {
            binding.txtChooseLocation.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_shuttle_option));
            pickerType = PickerBottomSheetDialog.TYPE_LOCATION;
        } else {
            binding.txtChooseShuttleType.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_shuttle_option));
            pickerType = PickerBottomSheetDialog.TYPE_OPTION;
        }
        PickerBottomSheetDialog bottomSheet;
        if (isLocation) dialogList = listTP;
        else dialogList = listTT;

        int selectIndex = isLocation ? selectedPointIndex : selectedTypeIndex;

        bottomSheet = PickerBottomSheetDialog.createCustomDialog(getContext(), pickerType, dialogList, selectIndex);
        bottomSheet.setListener(index -> {
            if (isLocation) {
                binding.txtChooseLocation.setText(dialogList.get(index));

                for (int j = 0; j < companyLocationListEntities.size(); j++) {
                    CompanyLocationListEntity ccll = companyLocationListEntities.get(j);
                    if (ccll.getName().equals(dialogList.get(index))) {
                        selectedPoint = ccll;
                        selectedPointIndex = index;
                        break;
                    }
                }

            } else {
                binding.txtChooseShuttleType.setText(dialogList.get(index));

                for (int j = 0; j < typeListEntities.size(); j++) {
                    TypeListEntity tle = typeListEntities.get(j);
                    if (tle.getName().equals(listTT.get(index))) {
                        selectedType = tle;
                        selectedTypeIndex = index;
                    }
                }
            }
            cleanUpSelection();
            bottomSheet.dismiss();
        });
        bottomSheet.setOnCancelListener(dialogInterface -> {
            cleanUpSelection();
        });
        bottomSheet.show();
    }

    public static ShuttleOptionFragment newInstance() {
        Bundle args = new Bundle();
        ShuttleOptionFragment fragment = new ShuttleOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        vmShuttleOption.selectedPointIndex.postValue(selectedPointIndex);
        vmShuttleOption.selectedTypeIndex.postValue(selectedTypeIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        Integer pointIndex = vmShuttleOption.getSelectedPointIndex().getValue();

        if (pointIndex != null) {
            binding.txtChooseLocation.setText(listTP.get(pointIndex));

            for (int j = 0; j < companyLocationListEntities.size(); j++) {
                CompanyLocationListEntity ccll = companyLocationListEntities.get(j);
                if (ccll.getName().equals(listTP.get(pointIndex))) {
                    selectedPoint = ccll;
                    selectedPointIndex = pointIndex;
                    break;
                }
            }
        }

        Integer typeIndex = vmShuttleOption.getSelectedTypeIndex().getValue();
        if (typeIndex != null) {
            binding.txtChooseShuttleType.setText(listTT.get(typeIndex));

            for (int j = 0; j < typeListEntities.size(); j++) {
                TypeListEntity tle = typeListEntities.get(j);
                if (tle.getName().equals(listTT.get(typeIndex))) {
                    selectedType = tle;
                    selectedTypeIndex = typeIndex;
                }
            }
        }
    }

    public void cleanUpSelection() {
        binding.txtChooseLocation.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_nav));
        binding.txtChooseShuttleType.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_nav));
    }

}
