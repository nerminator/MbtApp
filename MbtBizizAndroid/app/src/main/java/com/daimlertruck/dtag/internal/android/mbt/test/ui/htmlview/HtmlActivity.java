package com.daimlertruck.dtag.internal.android.mbt.test.ui.htmlview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityHtmlBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.socialmedia.SocialMediaActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

public class HtmlActivity extends BaseActivity<ActivityHtmlBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_html;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(KEY_HTML_URL);
        binding.webView.clearCache(true);
        binding.webView.loadUrl(url);
        binding.webView.setBackgroundColor(Color.TRANSPARENT);
        toolbarSetVM();
        setVisibilities();
        binding.textViewSocialMedia.setOnClickListener(v -> {
            SocialMediaActivity.start(this);
        });
    }

    private void toolbarSetVM() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOGIN_HOME_ABOUT_US), null);
        binding.toolbarHtml.setVm(vmToolbar);
    }

    public static void start(Context context, String htmlUrl, Boolean isSocialMediaActive) {
        Intent starter = new Intent(context, HtmlActivity.class);
        starter.putExtra(KEY_HTML_URL, htmlUrl);
        starter.putExtra(SOCIAL_MEDIA_FLAG, isSocialMediaActive);
        context.startActivity(starter);
    }

    private void setVisibilities() {
        boolean isSocialMediaActive = getIntent().getBooleanExtra(SOCIAL_MEDIA_FLAG, false);
        if (isSocialMediaActive) {
            binding.frameLayoutSocialMedia.setVisibility(View.VISIBLE);
        } else {
            binding.frameLayoutSocialMedia.setVisibility(View.GONE);
        }
    }

    private static final String KEY_HTML_URL = "KEY_HTML_URL";
    private static final String SOCIAL_MEDIA_FLAG = "SOCIAL_MEDIA_FLAG";
}
