package com.daimlertruck.dtag.internal.android.mbt.test.ui.fullscreenimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivityFullscreenImageBinding;
import com.squareup.picasso.Picasso;

public class FullscreenImageActivity extends BaseActivity<ActivityFullscreenImageBinding> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_fullscreen_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        initPhotoView();
    }

    private void initPhotoView() {
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        if (imageUrl != null) {
            Picasso.get().load(imageUrl).fit().centerInside().into(binding.photoView);
        }

        binding.photoView.setMaximumScale(10f);
    }

    public static void start(Context context, String imageUrl) {
        Intent intent = new Intent(context, FullscreenImageActivity.class);
        intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        intent.putExtra("IMAGE_URL", imageUrl);
        context.startActivity(intent);
    }
}