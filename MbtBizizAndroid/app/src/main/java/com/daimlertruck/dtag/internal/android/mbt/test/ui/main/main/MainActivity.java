package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;

import android.Manifest;
import com.google.android.material.tabs.TabLayout;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivityMainBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.NewsDetailActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    public static final String KEY_NEWS_ID = "newsId";
    public static final String KEY_FROM_NOTI = "fromNoti";
    com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.VMMain vmMain;
    //region Fragments
    private TextView badge;
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    //endregion Fragments


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        vmMain = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.VMMain.class);
        binding.setViewmodel(vmMain);
        binding.setLifecycleOwner(this);
        initView();
        if (getIntent().getExtras() != null) {
            boolean fromNoti = getIntent().getExtras().getBoolean(KEY_FROM_NOTI);
            if (fromNoti) {
                NewsDetailActivity.start(this, getIntent().getExtras().getString(KEY_NEWS_ID));
            }
        }

        requestPermission();
    }

    private void initView() {
        com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainPageAdapter pageAdapter = new com.daimlertruck.dtag.internal.android.mbt.test.ui.main.main.MainPageAdapter(getSupportFragmentManager());
        binding.vpMain.setOffscreenPageLimit(3);
        binding.vpMain.setAdapter(pageAdapter);
        setTablayout();
    }


    private void setTablayout() {
        binding.tabMain.setupWithViewPager(binding.vpMain);
        binding.tabMain.setSelectedTabIndicatorHeight(0);

        //region Event Tab
        final View portalTab = LayoutInflater.from(this).inflate(R.layout.tablayout_main_item, null);
        ImageView imgPortal = (ImageView) portalTab.findViewById(R.id.imgTab);
        TextView tvPortal = (TextView) portalTab.findViewById(R.id.tvTab);
        tvPortal.setText(getString(R.string.TXT_LOGIN_HOME_TAB1));
        imgPortal.setImageResource(R.drawable.tab_portal);
        //endregion

        //region Notification Tab
        final View notificationTab = LayoutInflater.from(this).inflate(R.layout.tablayout_main_item, null);
        ImageView imgNotification = (ImageView) notificationTab.findViewById(R.id.imgTab);
        TextView tvNotification = (TextView) notificationTab.findViewById(R.id.tvTab);
        badge = notificationTab.findViewById(R.id.badge);
        tvNotification.setText(getString(R.string.TXT_LOGIN_HOME_TAB2));
        imgNotification.setImageResource(R.drawable.tab_notification);


        //endregion

        //region Profile Tab
        final View orchestraTab = LayoutInflater.from(this).inflate(R.layout.tablayout_main_item, null);
        ImageView imgOrchestra = (ImageView) orchestraTab.findViewById(R.id.imgTab);
        TextView tvOrchestra = (TextView) orchestraTab.findViewById(R.id.tvTab);
        tvOrchestra.setText(getString(R.string.TXT_LOGIN_HOME_TAB3));
        imgOrchestra.setImageResource(R.drawable.tab_orchestra);
        //endregion

        binding.tabMain.getTabAt(0).setCustomView(portalTab);
        binding.tabMain.getTabAt(1).setCustomView(notificationTab);
        binding.tabMain.getTabAt(2).setCustomView(orchestraTab);

        binding.tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        vmMain.badgeCount.setValue("0");
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        observe();
    }

    private void observe() {
        vmMain.badgeCount.observe(this, s -> {
            if (badge == null) return;
            if (s != null && !s.equals("0")) {

                badge.setText(s);
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setText("");
                badge.setVisibility(View.GONE);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        context.startActivity(starter);
    }

    public static void start(Context context, String newsId) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        starter.putExtra(KEY_FROM_NOTI, true);
        starter.putExtra(KEY_NEWS_ID, newsId);
        context.startActivity(starter);
    }


    private  void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the notification permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            } else {
                // Permission already granted
            }
        } else {
            // Permission not required for versions below Android 13
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                //showNotification();
            } else {
                // Permission denied
                //Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
