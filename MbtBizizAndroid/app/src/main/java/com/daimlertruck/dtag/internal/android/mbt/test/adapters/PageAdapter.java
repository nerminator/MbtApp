package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> titleList;

    public PageAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragmentList = fragments;
        this.titleList = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList.size() > position){
            return titleList.get(position);
        }
        else return "";
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /*@Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }*/
}