package com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.SocialMediaAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivitySocialMediaBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

public class SocialMediaActivity extends BaseActivity<ActivitySocialMediaBinding> {

    private com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia.VMSocialMedia viewModel;
    SocialMediaAdapter adapter = new SocialMediaAdapter();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_social_media;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia.VMSocialMedia.class);
        binding.setLifecycleOwner(this);
        toolbarSetVM();
        initRecyclerView();
        viewModel.getSocialMediaItems().observe(this, socialMediaItems -> {
            adapter.setItems(socialMediaItems);
        });
        viewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });
    }

    private void initRecyclerView() {
        binding.recyclerViewSocialMedias.setAdapter(adapter);
        adapter.setCallback(this::redirectToUrl);
    }

    private void toolbarSetVM() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOGIN_HOME_ABOUT_US), null);
        binding.toolbar.setVm(vmToolbar);
    }

    private void redirectToUrl(@Nullable String url) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SocialMediaActivity.class);
        context.startActivity(starter);
    }
}