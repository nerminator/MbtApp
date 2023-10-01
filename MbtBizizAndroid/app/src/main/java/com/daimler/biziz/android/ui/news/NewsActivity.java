package com.daimler.biziz.android.ui.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.adapters.PageAdapter;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityNewsBinding;
import com.daimler.biziz.android.ui.foodMenu.FoodMenuActivity;
import com.daimler.biziz.android.ui.toolbar.VMToolbar;

public class NewsActivity extends BaseActivity<ActivityNewsBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, NewsActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPager();
        toolbarSetVM();
    }

    private void initPager() {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(NewsListingFragment.newInstance(1), getString(R.string.TXT_LOGIN_NEWS_TYPE1));
        adapter.addFragment(NewsListingFragment.newInstance(2), getString(R.string.TXT_LOGIN_NEWS_TYPE2));
        adapter.addFragment(NewsListingFragment.newInstance(7), getString(R.string.TXT_COMMON_ANNOUNCEMENT));
        adapter.addFragment(NewsListingFragment.newInstance(8), getString(R.string.TXT_LOGIN_NEWS_TYPE8));
        adapter.addFragment(NewsListingFragment.newInstance(9), getString(R.string.TXT_LOGIN_NEWS_TYPE9));
        adapter.addFragment(NewsListingFragment.newInstance(3), getString(R.string.TXT_LOGIN_NEWS_TYPE3));
        //adapter.addFragment(NewsListingFragment.newInstance(4), getString(R.string.TXT_LOGIN_NEWS_TYPE4));
        //adapter.addFragment(NewsListingFragment.newInstance(5), getString(R.string.TXT_LOGIN_NEWS_TYPE5));
        adapter.addFragment(NewsListingFragment.newInstance(6), getString(R.string.TXT_LOGIN_NEWS_TYPE6));

        binding.vpNews.setAdapter(adapter);
        binding.tabNews.setupWithViewPager(binding.vpNews);
    }

    private void toolbarSetVM() {
        try {
            VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_LOGIN_HOME_NEWS), null);
            binding.toolbar.setVm(vmToolbar);
        } catch (Exception e) {
        }

    }

    @Override
    public void onBackPressed() {
        if (binding.vpNews.getCurrentItem() != 0) {
            binding.vpNews.setCurrentItem(0);
        } else super.onBackPressed();


    }
}
