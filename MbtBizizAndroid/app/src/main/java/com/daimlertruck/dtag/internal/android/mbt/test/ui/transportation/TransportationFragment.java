package com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.PageAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.TransportationAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentTransportationBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.CompanyListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.CompanyLocationListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.StopListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.TypeListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.TransportationCustomDialog;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search.SearchShuttleActivity;
import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransportationFragment extends BaseFragment {
    private static final int ON_SEARCH_COMPLETED = 123;
    private TransportationAdapter transportationAdapter;
    private List<ShuttleListEntity> shuttleList = new ArrayList<>();
    private VMTransportation vmTransportation;
    private FragmentTransportationBinding binding;
    boolean isTakeOff = true;
    private ShuttleListResponse shuttleListResponse;

    private CompanyLocationListEntity selectedPoint;
    private TypeListEntity selectedType;
    private PageAdapter pagerAdapter;
    private int selectedListPosition = 0;

    private boolean filterAccepted = false;
    private ShuttleListResponse shuttleListResponseFilterAccepted;
    private String responseString;

    public static final int DepertureField = 1;
    public static final int ArrivalField = 2;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    @Retention(SOURCE)
    @IntDef({DepertureField, ArrivalField})
    public @interface stopType {
    }

    public TransportationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transportation, container, false);
        vmTransportation = ViewModelProviders.of(this).get(VMTransportation.class);
        binding.setViewmodel(vmTransportation);
        if (getArguments() != null) {
            selectedPoint = (CompanyLocationListEntity) getArguments().getSerializable("selectedPoint");
            selectedType = (TypeListEntity) getArguments().getSerializable("selectedType");
            vmTransportation.setVariables(selectedPoint.getId(), selectedType.getType());
        }

        String header = "";
        if (selectedType != null && !TextUtils.isEmpty(selectedType.getName()))
            header = selectedType.getName();
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, header, null);
        binding.toolbar.setVm(vmToolbar);
        init();
        return binding.getRoot();
    }

    private void init() {
        transportationAdapter = new TransportationAdapter(shuttleList, driverInfoEntity -> TransportationCustomDialog.createTransportationDialog(TransportationFragment.this, driverInfoEntity).show());
        binding.recyclerShuttle.setAdapter(transportationAdapter);
        observe();
        binding.txtFromNav.setText(selectedPoint.getName());
        pagerAdapter = new PageAdapter(getChildFragmentManager(), mFragmentList, mFragmentTitleList);
        binding.vpShuttle.setAdapter(pagerAdapter);
        binding.vpShuttle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedListPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        clearText();
    }

    private void clearText() {
        binding.imgCloseBottom.setOnClickListener(view -> {
            view.setVisibility(View.GONE);
            binding.txtToNav.setText("");
            filterAccepted = false;
            updateRecycler();
        });
        binding.imgCloseTop.setOnClickListener(view -> {
            view.setVisibility(View.GONE);
            binding.txtFromNav.setText("");
            filterAccepted = false;
            updateRecycler();
        });
    }

    private void observe() {
        vmTransportation.getShuttleListResponse().observe(getViewLifecycleOwner(), slr -> {
            //mock
            /*Gson gson = new Gson();
            BaseResponse<ShuttleListResponse> bslr;
            bslr = gson.fromJson(streamToString(getResources().openRawResource(R.raw.mock_transportation)), new TypeToken<BaseResponse<ShuttleListResponse>>() {
            }.getType());

            shuttleListResponse = bslr.getResponseData();*/

            Gson gson = new Gson();
            responseString = gson.toJson(slr);
            shuttleListResponse = slr;
            /*if (shuttleListResponse != null && shuttleListResponse.getFromCompanyList() != null) {
                for (int i = 0; i < shuttleListResponse.getFromCompanyList().size(); i++) {
                    for (int j = 0; j < shuttleListResponse.getFromCompanyList().get(i).getShuttleList().size(); j++) {
                        ShuttleListEntity shuttleListEntity = shuttleListResponse.getFromCompanyList().get(i).getShuttleList().get(j);
                        StopListEntity stopListEntity=new StopListEntity(-1,selectedPoint.getName()+shuttleListEntity.getArrivalTime(), false);
                        shuttleListEntity.getStopList().add(stopListEntity);
                    }
                }
            }*/
            List<CompanyListEntity> shuttleCompanyList = getActualShuttleList();
            if (shuttleCompanyList.size() == 1) {
                vmTransportation.viewPagerModeEnabled.set(false);
                shuttleList.addAll(shuttleCompanyList.get(0).getShuttleList());
                transportationAdapter.notifyDataSetChanged();
                selectedListPosition = 0;
            } else if (shuttleCompanyList.size() > 1) {
                //ViewPager
                selectedListPosition = 0;
                vmTransportation.viewPagerModeEnabled.set(true);
                setupViewPager(shuttleCompanyList);
            }
        });

        vmTransportation.isSwitchButtonClicked.observe(getViewLifecycleOwner(), (Boolean bool) -> {
            isTakeOff = !isTakeOff;
            if (!isTakeOff) {
                binding.txtFromNav.setText(binding.txtToNav.getText());
                binding.txtToNav.setText(selectedPoint.getName());
                if (filterAccepted) {
                    binding.imgCloseTop.setVisibility(View.VISIBLE);
                    binding.imgCloseBottom.setVisibility(View.GONE);
                }
            } else {
                binding.txtToNav.setText(binding.txtFromNav.getText());
                binding.txtFromNav.setText(selectedPoint.getName());
                if (filterAccepted) {
                    binding.imgCloseTop.setVisibility(View.GONE);
                    binding.imgCloseBottom.setVisibility(View.VISIBLE);
                }
            }

            shuttleList.clear();
            List<CompanyListEntity> shuttleCompanyList = getActualShuttleList();
            if (shuttleCompanyList.size() == 1) {
                vmTransportation.viewPagerModeEnabled.set(false);
                shuttleList.addAll(shuttleCompanyList.get(0).getShuttleList());
                transportationAdapter.notifyDataSetChanged();
                selectedListPosition = 0;
            } else if (shuttleCompanyList.size() > 1) {
                //ViewPager
                selectedListPosition = 0;
                vmTransportation.viewPagerModeEnabled.set(true);
                setupViewPager(shuttleCompanyList);
            }
        });

        vmTransportation.toClicked.observe(getViewLifecycleOwner(), aBoolean -> {
            if (isTakeOff) openSearchScreen(ArrivalField);
        });

        vmTransportation.fromClicked.observe(getViewLifecycleOwner(), aBoolean -> {
            if (!isTakeOff) openSearchScreen(DepertureField);
        });

        vmTransportation.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

    /*private static String streamToString(InputStream in) {
        String l;
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder s = new StringBuilder();
        try {
            while ((l = r.readLine()) != null) {
                s.append(l + "\n");
            }
        } catch (IOException e) {
        }
        return s.toString();
    }*/

    private void openSearchScreen(Integer type) {
        SearchShuttleActivity.start(this, ON_SEARCH_COMPLETED, isTakeOff, shuttleListResponse, selectedListPosition, type);
    }

    private List<CompanyListEntity> getActualShuttleList() {
        List<CompanyListEntity> shuttleCompanyList;
        ShuttleListResponse slr = new Gson().fromJson(responseString, ShuttleListResponse.class);
        if (!isTakeOff) {
            if (filterAccepted)
                shuttleCompanyList = shuttleListResponseFilterAccepted.getToCompanyList();
            else
                shuttleCompanyList = slr.getToCompanyList();
        } else {
            if (filterAccepted)
                shuttleCompanyList = shuttleListResponseFilterAccepted.getFromCompanyList();
            else
                shuttleCompanyList = slr.getFromCompanyList();
        }
        return fakeShuttleList(shuttleCompanyList);
    }

    private List<CompanyListEntity> fakeShuttleList(List<CompanyListEntity> shuttleCompanyList) {
        for (int i = 0; i < shuttleCompanyList.size(); i++) {
            for (int j = 0; j < shuttleCompanyList.get(i).getShuttleList().size(); j++) {
                ShuttleListEntity currentShuttleListEntity = shuttleCompanyList.get(i).getShuttleList().get(j);
                List<StopListEntity> currentStopList = shuttleCompanyList.get(i).getShuttleList().get(j).getStopList();
                StopListEntity stopListEntity = new StopListEntity(-1, selectedPoint.getName().toUpperCase(), false);
                if (isTakeOff) {
                    currentStopList.add(0, stopListEntity);
                } else {
                    currentStopList.add(stopListEntity);
                }
                if (currentStopList.size() >= 1) {
                    String depertureStopName;
                    if (isTakeOff) depertureStopName = String.valueOf(stopListEntity.getName());
                    else
                        depertureStopName = String.valueOf(currentShuttleListEntity.getStopList().get(0).getName());
                    currentStopList.get(0).setName(depertureStopName + " - " + currentShuttleListEntity.getDepartureTime() + " " + getString(R.string.TXT_SHUTTLE_SHUTTLE_DESTINATION_SMALL));
                    String arrivalStopName;
                    if (!isTakeOff) arrivalStopName = String.valueOf(stopListEntity.getName());
                    else
                        arrivalStopName = String.valueOf(currentStopList.get(currentStopList.size() - 1).getName());
                    currentStopList.get(currentStopList.size() - 1).setName(arrivalStopName + " - " + currentShuttleListEntity.getArrivalTime() + " " + getString(R.string.TXT_SHUTTLE_SHUTTLE_ARRIVAL_SMALL));
                }
            }
        }
        return shuttleCompanyList;
    }

    private void updateRecycler() {
        shuttleList.clear();
        List<CompanyListEntity> shuttleCompanyList = getActualShuttleList();
        if (shuttleCompanyList.size() == 1) {
            vmTransportation.viewPagerModeEnabled.set(false);
            shuttleList.addAll(shuttleCompanyList.get(0).getShuttleList());
            transportationAdapter.notifyDataSetChanged();
            selectedListPosition = 0;
        } else if (shuttleCompanyList.size() > 1) {
            //ViewPager
            selectedListPosition = 0;
            vmTransportation.viewPagerModeEnabled.set(true);
            setupViewPager(shuttleCompanyList);
        }
    }

    public static TransportationFragment newInstance(TypeListEntity selectedType, CompanyLocationListEntity selectedPoint) {
        Bundle args = new Bundle();
        args.putSerializable("selectedPoint", selectedPoint);
        args.putSerializable("selectedType", selectedType);
        TransportationFragment fragment = new TransportationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setupViewPager(List<CompanyListEntity> shuttleCompanyList) {
        mFragmentList.clear();
        mFragmentTitleList.clear();

        binding.vpShuttle.setOffscreenPageLimit(shuttleCompanyList.size());
        for (int i = 0; i < shuttleCompanyList.size(); i++) {
            mFragmentList.add(ChildShuttleFragment.newInstance(shuttleCompanyList.get(i)));
            mFragmentTitleList.add(shuttleCompanyList.get(i).getName());
        }
        pagerAdapter.notifyDataSetChanged();
        binding.tabShuttle.setupWithViewPager(binding.vpShuttle);
    }

    public interface IDriverInfo {
        void infoClicked(ShuttleListEntity shuttleListEntity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_SEARCH_COMPLETED && resultCode == RESULT_OK) {
            //shuttleList.clear();
            //vmTransportation.viewPagerModeEnabled.set(false);
            //shuttleList.addAll(companyListEntity.getShuttleList());
            ShuttleListResponse slr = (ShuttleListResponse) data.getSerializableExtra("companyListEntity");
            if (slr != null) {
                filterAccepted = true;
                shuttleListResponseFilterAccepted = slr;
                String stopName = "";
                stopName = data.getStringExtra("stopName");

                if (!isTakeOff) {
                    binding.txtFromNav.setText(stopName);
                    binding.imgCloseTop.setVisibility(View.VISIBLE);
                } else {
                    binding.txtToNav.setText(stopName);
                    binding.imgCloseBottom.setVisibility(View.VISIBLE);
                }
                updateRecycler();
                //transportationAdapter.notifyDataSetChanged();
                selectedListPosition = 0;
            }
        }
    }
}
