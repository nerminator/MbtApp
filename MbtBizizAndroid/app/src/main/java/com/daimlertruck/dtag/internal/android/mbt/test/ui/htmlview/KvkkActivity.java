package com.daimlertruck.dtag.internal.android.mbt.test.ui.htmlview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityKvkkBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

public class KvkkActivity extends BaseActivity<ActivityKvkkBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_kvkk;
    }

    public static Intent getIntent(Context ctx) {
        Intent i = new Intent(ctx, KvkkActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "https://bizizapp.com/bizizPanel/public/kvkk.html";
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient());

        binding.webView.clearCache(true);
        binding.webView.setBackgroundColor(Color.TRANSPARENT);
        binding.webView.loadUrl(url);

        toolbarSetVM();

        binding.btnConfirm.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("approved", true);
            setResult(Activity.RESULT_OK, result);
            finish();
        });

        binding.btnDecline.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("approved", false);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
    private void toolbarSetVM() {
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(
                this,
                getString(R.string.TXT_KVKK_TITLE),
                null
        );
        binding.toolbarHtml.setVm(vmToolbar);
    }

    private static final String KEY_KVKK_URL = "KEY_KVKK_URL";
}