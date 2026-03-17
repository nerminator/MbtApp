package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.notification.NotificationFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.OrchestraFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.portal.PortalFragment;

public class MainPageAdapter extends FragmentStatePagerAdapter {

    public MainPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PortalFragment.newInstance();
            case 1:
                return NotificationFragment.newInstance();
            case 2:
                return OrchestraFragment.newInstance();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return "Noti";
            case 2:
                return "Orc";
            default:
                return "Portal";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    /*@Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }*/
}