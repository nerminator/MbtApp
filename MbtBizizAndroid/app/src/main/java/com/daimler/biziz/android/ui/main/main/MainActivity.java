package com.daimler.biziz.android.ui.main.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.PageAdapter;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityMainBinding;
import com.daimler.biziz.android.ui.main.notification.NotificationFragment;
import com.daimler.biziz.android.ui.main.orchestra.OrchestraFragment;
import com.daimler.biziz.android.ui.main.portal.PortalFragment;
import com.daimler.biziz.android.ui.newsDetail.NewsDetailActivity;
import com.daimler.biziz.android.ui.splash.SplashActivity;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends BaseActivity<ActivityMainBinding> {
    public static final String KEY_NEWS_ID = "newsId";
    public static final String KEY_FROM_NOTI = "fromNoti";
    private PageAdapter adapter;
    VMMain vmMain;
    //region Fragments
    private PortalFragment portalFragment = PortalFragment.newInstance();
    private NotificationFragment notificationFragment = NotificationFragment.newInstance();
    private OrchestraFragment orchestraFragment = OrchestraFragment.newInstance();
    private TextView badge;
    //endregion Fragments


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vmMain = ViewModelProviders.of(this).get(VMMain.class);
        binding.setViewmodel(vmMain);
        binding.setLifecycleOwner(this);
        initView();
        if (getIntent().getExtras() != null) {
            boolean fromNoti = getIntent().getExtras().getBoolean(KEY_FROM_NOTI);
            if (fromNoti) {
                NewsDetailActivity.start(this, getIntent().getExtras().getString(KEY_NEWS_ID));
            }
        }


    }

    private void initView() {
        adapter = new PageAdapter(getSupportFragmentManager());
        setTablayout();

    }


    private void setTablayout() {
        setupViewPager();
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
                        //imageViewNotification.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tab_feed_notification));
                        //notificationFragment.getNotificationRequest(false);
                        vmMain.badgeCount.setValue("0");
                        break;
                    case 2:
                        //profileFragment.loadMoreEvent(true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
           /*     if (tab.getPosition() == 0) {
                    eventFragment.scrollToTop();
                } else if (tab.getPosition() == 1) {
                    notificationFragment.scrollToTop();
                } else if (tab.getPosition() == 2) {
                    profileFragment.scrollToTop();
                }*/

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

    private void setupViewPager() {
        binding.vpMain.setOffscreenPageLimit(3);
        adapter.addFragment(portalFragment, "Portal");
        adapter.addFragment(notificationFragment, "Noti");
        adapter.addFragment(orchestraFragment, "Orc");
        binding.vpMain.setAdapter(adapter);
    }

    /* class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //private final List<String> mFragmentTitleList = new ArrayList<>();
        private int[] imageResId = {R.drawable.ic_account_circle_black_24dp, R.drawable.ic_account_circle_black_24dp, R.drawable.ic_account_circle_black_24dp};
        private String[] tvResId = {"Event", "Notification", "Profile"};

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


        private View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tablayout_item_feed, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgTabFeed);
            TextView text = (TextView) v.findViewById(R.id.tvTabFeed);
            img.setImageResource(imageResId[position]);
            text.setText(tvResId[position]);
            return v;
        }
    }*/

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String newsId) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(KEY_FROM_NOTI, true);
        starter.putExtra(KEY_NEWS_ID, newsId);
        context.startActivity(starter);
    }


}
