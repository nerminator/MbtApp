package com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityUsefulLinksBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinkscategory.UsefulLinksCategoryFragment;

public class UsefulLinksActivity extends BaseActivity<ActivityUsefulLinksBinding> {

    boolean isFromContacts = false;
    public String previousToolbarTitle;
    public static void start(Context context, boolean isFromContacts) {
        Intent starter = new Intent(context, UsefulLinksActivity.class);
        starter.putExtra(IS_FROM_CONTACTS, isFromContacts);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_useful_links;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarSetVM();
        binding.setLifecycleOwner(this);
        loadFragments();
    }

    private void loadFragments() {
        boolean isFromContacts = getIntent().getBooleanExtra(IS_FROM_CONTACTS, false);
        if (isFromContacts) {
            loadFragment(binding.containerUsefulLinks.getId(), UsefulLinksCategoryFragment.newInstance(true), true);
        } else {
            loadFragment(binding.containerUsefulLinks.getId(), com.daimlertruck.dtag.internal.android.mbt.test.ui.usefullinks.UsefulLinksPortalFragment.newInstance(), true);
        }
    }

    private void toolbarSetVM() {
        try {
            boolean isFromContacts = getIntent().getBooleanExtra(IS_FROM_CONTACTS, false);
            String toolBarTitle = getString(R.string.TXT_LOGIN_HOME_USEFUL_LINKS);
            if (isFromContacts) {
                toolBarTitle = getString(R.string.TXT_LOGIN_HOME_EMERGENCY_NUMBERS);
            }
            previousToolbarTitle = toolBarTitle;
            VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, toolBarTitle, null);
            binding.toolbar.setVm(vmToolbar);
        } catch (Exception e) {
        }

    }

    public void updateToolbarTitle(String newTitle) {
        if (binding != null && binding.toolbar != null) {
            VMToolbar vmToolbar = binding.toolbar.getVm();
            if (vmToolbar != null) {
                // Update toolbar title
                vmToolbar.setTexts(newTitle, null);
            }
        }
    }

    public void addFragment(Fragment fragment) {
        loadFragment(binding.containerUsefulLinks.getId(), fragment, true);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) finish();
        else super.onBackPressed();
    }

    private static final String IS_FROM_CONTACTS = "IS_FROM_CONTACTS";
}
