package com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.RecentSearchsAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.ShuttleSearchAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivitySearchShuttleBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.manager.SharedPreferenceManager;
import com.daimlertruck.dtag.internal.android.mbt.test.model.ShuttleAdapterModel;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.CompanyListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.ShuttleListResponse;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.transportation.StopListEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.TransportationFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchShuttleActivity extends BaseActivity<ActivitySearchShuttleBinding> {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    private com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search.VMSearchShuttle vmSearchShuttle;
    private boolean isTakeOff;
    private Integer stopType;
    private CompanyListEntity companyListEntity = new CompanyListEntity();
    private CompanyListEntity companyListEntitySwitch = new CompanyListEntity();
    private Locale trlocale = new Locale("tr", "TR");
    private ShuttleSearchAdapter shuttleSearchAdapter;
    private RecentSearchsAdapter recentSearchsAdapter;
    private List<ShuttleAdapterModel> adapterStopList = new ArrayList<>();
    private List<String> recentSearches = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search_shuttle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getComponent().injectSearchShuttleActivity(this);
        vmSearchShuttle = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.transportation.search.VMSearchShuttle.class);
        binding.setVm(vmSearchShuttle);
        setAdapter();
        getDatas();
        searchViewConfig();
    }

    private void setAdapter() {
        shuttleSearchAdapter = new ShuttleSearchAdapter(adapterStopList, stop -> {
            Intent intent = new Intent();
            intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
            intent.putExtra("companyListEntity", createResultObject(stop));
            intent.putExtra("stopName", stop.getStopListEntity().getName());
            String rs = sharedPreferenceManager.getRecentSearchs();
            String[] rsArray = rs.split("@");
            if (!Arrays.asList(rsArray).contains(stop.getStopListEntity().getName())) {
                sharedPreferenceManager.putRecentSearch(stop.getStopListEntity().getName());
            }
            setResult(RESULT_OK, intent);
            finish();
        });
        binding.recyclerSearchShuttle.setAdapter(shuttleSearchAdapter);

        recentSearchsAdapter = new RecentSearchsAdapter(recentSearches, model -> {
            binding.edtSearch.setText(model);
        });
        binding.recyclerRecentSearches.setAdapter(recentSearchsAdapter);
    }

    private ShuttleListResponse createResultObject(ShuttleAdapterModel stop) {
        CompanyListEntity cleResult = new CompanyListEntity();
        cleResult.setName(companyListEntity.getName());
        List<ShuttleListEntity> shuttleListEntities = new ArrayList<>();
        for (int i = 0; i < stop.getShuttleListEntity().getStopList().size(); i++) {
            if (stop.getShuttleListEntity().getStopList().get(i).getId() == stop.getStopListEntity().getId()) {
                stop.getShuttleListEntity().getStopList().get(i).setTargetStop(true);
                break;
            }
        }
        shuttleListEntities.add(stop.getShuttleListEntity());
        cleResult.setShuttleList(shuttleListEntities);

        CompanyListEntity cleResultSwitch = new CompanyListEntity();
        if (companyListEntitySwitch!=null) {
            cleResultSwitch.setName(companyListEntitySwitch.getName());
            List<ShuttleListEntity> shuttleListEntitiesSwitch = new ArrayList<>();
            for (int i = 0; i < companyListEntitySwitch.getShuttleList().size(); i++) {
                ShuttleListEntity shuttleListEntity = new ShuttleListEntity(companyListEntitySwitch.getShuttleList().get(i));
                if (shuttleListEntity.getId() == stop.getShuttleListEntity().getId()) {
                    boolean contains = false;
                    for (int j = 0; j < shuttleListEntity.getStopList().size(); j++) {
                        if (shuttleListEntity.getStopList().get(j).getName().equals(stop.getStopListEntity().getName())) {
                            shuttleListEntity.getStopList().get(j).setTargetStop(true);
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        shuttleListEntitiesSwitch.add(shuttleListEntity);
                        cleResultSwitch.setShuttleList(shuttleListEntitiesSwitch);
                        break;
                    }
                }
            }
        }

        ShuttleListResponse shuttleListResponse = new ShuttleListResponse();

        List<CompanyListEntity> toList = new ArrayList<>();
        List<CompanyListEntity> fromList = new ArrayList<>();

        if (isTakeOff) {
            fromList.add(cleResult);
            if (cleResultSwitch.getShuttleList() != null && cleResultSwitch.getShuttleList().size() > 0) {
                toList.add(cleResultSwitch);
            }
        } else {
            toList.add(cleResult);
            if (cleResultSwitch.getShuttleList() != null && cleResultSwitch.getShuttleList().size() > 0) {
                fromList.add(cleResultSwitch);
            }
        }
        shuttleListResponse.setFromCompanyList(fromList);
        shuttleListResponse.setToCompanyList(toList);

        return shuttleListResponse;
    }

    private void getDatas() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isTakeOff = bundle.getBoolean("isTakeOff");
            stopType=bundle.getInt("stopType",1);
            ShuttleListResponse shuttleListResponse = (ShuttleListResponse) bundle.getSerializable("shuttleList");
            int position = bundle.getInt("position");
            if (shuttleListResponse != null) {
                companyListEntity = isTakeOff ? shuttleListResponse.getFromCompanyList().get(position) : shuttleListResponse.getToCompanyList().get(position);
                if ((isTakeOff && shuttleListResponse.getToCompanyList().size() >= position) || (!isTakeOff && shuttleListResponse.getFromCompanyList().size() >= position))
                    companyListEntitySwitch = isTakeOff ? shuttleListResponse.getToCompanyList().get(position) : shuttleListResponse.getFromCompanyList().get(position);
            }
        }
        String rs = sharedPreferenceManager.getRecentSearchs();
        String[] rsArray = rs.split("@");
        recentSearches.addAll(Arrays.asList(rsArray));
        recentSearchsAdapter.notifyDataSetChanged();
    }

    private void searchViewConfig() {
        /*binding.searchView.setOnClickListener(view -> {
            binding.searchView.setIconified(false);
        });
        EditText searchEditText = (EditText) binding.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);*/
        EditText searchEditText = binding.edtSearch;
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.fiftyPercentOpacityTextColor));
        searchEditText.setHint(stopType.equals(TransportationFragment.DepertureField) ? R.string.TXT_SHUTTLE_SHUTTLE_DEST_PLCH : R.string.TXT_SHUTTLE_SHUTTLE_ARRIVAL_PLCH);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 3) {
                    vmSearchShuttle.isResultShowing.set(true);
                    adapterStopList.clear();
                    String word = editable.toString();

                    for (int j = 0; j < companyListEntity.getShuttleList().size(); j++) {
                        ShuttleListEntity currentEntity = companyListEntity.getShuttleList().get(j);
                        //Duraklar
                        for (int k = 0; k < currentEntity.getStopList().size(); k++) {
                            StopListEntity stopListEntity = currentEntity.getStopList().get(k);
                            if (stopListEntity.getName().toLowerCase(trlocale).contains(word.toLowerCase(trlocale))) {
                                StopListEntity stp = new StopListEntity(stopListEntity.getId(), stopListEntity.getName(), true);
                                ShuttleAdapterModel shuttleAdapterModel = new ShuttleAdapterModel(currentEntity, stp);
                                adapterStopList.add(shuttleAdapterModel);
                            }
                        }
                    }
                    shuttleSearchAdapter.notifyDataSetChanged();
                } else {
                    vmSearchShuttle.isResultShowing.set(false);
                }
            }
        });
    }

    public static void start(Fragment context, int requestCode, boolean isTakeOff, ShuttleListResponse shuttleListResponse, int selectedListPosition, Integer stopType) {
        Intent starter = new Intent(context.getContext(), SearchShuttleActivity.class);
        starter.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        Bundle bundle = new Bundle();
        bundle.putSerializable("shuttleList", shuttleListResponse);
        bundle.putBoolean("isTakeOff", isTakeOff);
        bundle.putInt("stopType", stopType);
        bundle.putInt("position", selectedListPosition);
        starter.putExtras(bundle);
        context.startActivityForResult(starter, requestCode);
    }
}
