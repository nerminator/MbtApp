package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory;

import static com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.TYPE_SOCIAL_CLUBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentUsefulLinksCategoryBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.usefullinks.UsefulLinksCategoryLocation;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail.UsefulLinksCategoryDetailFragment;

import java.util.ArrayList;

/**
 * x
 * A simple {@link Fragment} subclass.
 */
public class UsefulLinksCategoryFragment extends BaseFragment {

    private VMUsefulLinksCategory vmUsefulLinksCategory;
    private FragmentUsefulLinksCategoryBinding binding;

    private final ArrayList<UsefulLinksCategoryLocation> categoryList = new ArrayList<>();
    private final UsefulLinksCategoryAdapter adapter = new UsefulLinksCategoryAdapter(categoryList, this::openCategoryDetail);

    public static UsefulLinksCategoryFragment newInstance(boolean isPhone) {
        Bundle args = new Bundle();
        args.putBoolean(IS_PHONE_SELECTED, isPhone);
        UsefulLinksCategoryFragment fragment = new UsefulLinksCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UsefulLinksCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_useful_links_category, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectUsefulLinksCategoryFragment(this);

        vmUsefulLinksCategory = ViewModelProviders.of(getActivity()).get(VMUsefulLinksCategory.class);
        binding.setLifecycleOwner(this);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        initRecyclerView();
        fetchCategories();
        observe();
    }

    private void observe() {
        vmUsefulLinksCategory.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressDialog();
                } else dismissProgressDialog();
            }
        });
        vmUsefulLinksCategory.getLocationsLiveData().observe(getViewLifecycleOwner(), usefulLinksCategoryLocationsEntity -> {
            updateList(usefulLinksCategoryLocationsEntity.getLocations());
        });
    }

    private void updateList(ArrayList<UsefulLinksCategoryLocation> locations) {
        if (!locations.isEmpty()) {
            categoryList.clear();
            categoryList.addAll(locations);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchCategories() {
        if (getArguments() == null) return;
        if (getArguments().getBoolean(IS_PHONE_SELECTED)) {
            vmUsefulLinksCategory.fetchPhones();
        } else {
            vmUsefulLinksCategory.fetchSocialClubs();
        }
    }

    private void initRecyclerView() {
        binding.recyclerViewUsefulLinksCategory.setAdapter(adapter);
    }

    private void openCategoryDetail(Integer categoryId) {
        if (getArguments() == null) return;
        boolean isPhoneSelected = getArguments().getBoolean(IS_PHONE_SELECTED);
        if (isPhoneSelected){
            ((UsefulLinksActivity)getActivity()).addFragment(UsefulLinksCategoryDetailFragment.newInstance(isPhoneSelected, categoryId));
        } else {
            ((UsefulLinksActivity)getActivity()).addFragment(NewsListingFragment.newInstance(TYPE_SOCIAL_CLUBS, categoryId));
        }
    }

    private static final String IS_PHONE_SELECTED = "IS_PHONE_SELECTED";

    public void onDestroyView() {
        boolean isPhoneSelected = getArguments().getBoolean(IS_PHONE_SELECTED);
        if (!isPhoneSelected){
            UsefulLinksActivity activity = (UsefulLinksActivity) getActivity();
            if (activity != null) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Check if the backstack is decreasing (going back)
                if (fragmentManager.getBackStackEntryCount() == 1) {
                    activity.updateToolbarTitle(activity.previousToolbarTitle);
                }
            }
        }
        super.onDestroyView();
    }
}
