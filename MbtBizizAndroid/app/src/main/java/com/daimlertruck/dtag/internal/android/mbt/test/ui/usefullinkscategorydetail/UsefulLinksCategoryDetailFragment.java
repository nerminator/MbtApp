package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategorydetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseApplication;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentUsefulLinksCategoryDetailBinding;

import java.util.ArrayList;

/**
 * x
 * A simple {@link Fragment} subclass.
 */
public class UsefulLinksCategoryDetailFragment extends Fragment implements UsefulLinksCategoryDetailAdapter.PhoneNumberClickCallback {

    private VMUsefulLinksCategoryDetail vmUsefulLinksCategoryDetail;
    private FragmentUsefulLinksCategoryDetailBinding binding;
    private ArrayList<UsefulLinksCategoryItem> items = new ArrayList<>();
    private final UsefulLinksCategoryDetailAdapter adapter = new UsefulLinksCategoryDetailAdapter(items, this);

    public static UsefulLinksCategoryDetailFragment newInstance(boolean isPhone, int categoryId) {
        Bundle args = new Bundle();
        args.putBoolean(IS_PHONE_CLICKED, isPhone);
        args.putInt(CATEGORY_ID, categoryId);
        UsefulLinksCategoryDetailFragment fragment = new UsefulLinksCategoryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UsefulLinksCategoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_useful_links_category_detail, container, false);
        ((BaseApplication) getActivity().getApplication()).getComponent().injectUsefulLinksCategoryDetailFragment(this);

        vmUsefulLinksCategoryDetail = ViewModelProviders.of(getActivity()).get(VMUsefulLinksCategoryDetail.class);
        binding.setLifecycleOwner(this);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        initRecyclerView();
        fetchPage();
        observe();
    }

    private void observe() {
        vmUsefulLinksCategoryDetail.getDetailsLiveData().observe(getViewLifecycleOwner(), this::updateList);
    }

    private void updateList(ArrayList<UsefulLinksCategoryItem> locations) {
        if (!locations.isEmpty()) {
            items.clear();
            items.addAll(locations);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchPage() {
        if (getArguments() == null) return;
        int categoryId = getArguments().getInt(CATEGORY_ID);
        if (getArguments().getBoolean(IS_PHONE_CLICKED)) {
            vmUsefulLinksCategoryDetail.fetchPhones(categoryId);
        } else {
            vmUsefulLinksCategoryDetail.fetchSocialClubs(categoryId);
        }
    }

    private void initRecyclerView() {
        binding.recyclerViewUsefulLinksCategory.setAdapter(adapter);
    }

    private static final String IS_PHONE_CLICKED = "IS_PHONE_CLICKED";
    private static final String CATEGORY_ID = "CATEGORY_ID";

    @Override
    public void onItemClick(UsefulLinksCategoryItem item) {
        if (item.getFilteredPhoneNumber() != null) {
            String phoneNumber = item.getFilteredPhoneNumber().replace(" ", "");
            if (phoneNumber.getBytes().length == 11) {
                call(phoneNumber);
            } else {
                if (item.getSantral() != null) {
                    String santral = item.getSantral();
                    santral.replace(" ", "");
                    call(santral+item.getFilteredPhoneNumber());
                }
            }
        }
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}
