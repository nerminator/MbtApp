package com.daimlertruck.dtag.internal.android.mbt.test.ui.news;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.PageAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ActivityNewsBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity<ActivityNewsBinding> {

    private NewsType newsType = null;

    public static void start(Context context, NewsType newsType) {
        Intent starter = new Intent(context, NewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE, String.valueOf(newsType));
        starter.putExtras(bundle);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initPager();
        initPage();
        toolbarSetVM();
    }

    private void initPage() {
        String newsTypeValue = getIntent().getExtras().getString(NEWS_TYPE);
        newsType = null;
        for (NewsType type : NewsType.getEntries()) {
            if (String.valueOf(type).equals(newsTypeValue)) {
                newsType = type;
                break;
            }
        }

        Fragment fragment = null;
        if (newsType != null) {
            switch (newsType) {
                case DISCOUNTS:
                    fragment = com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(3);
                    break;
                case EVENTS:
                    fragment = com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(2);
                    break;
                case ANNOUNCEMENT:
                    fragment = com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(7);
                    break;
                case USEFUL_LINKS:
                    fragment = com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(8);
                    break;

            }
            if (fragment != null) {
                loadFragment(R.id.containerNews, fragment, false);
            }
        }
    }

    private void initPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        List<String> titles = new ArrayList<String>();

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(1));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE1));

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(2));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE2));

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(7));
        titles.add(getString(R.string.TXT_COMMON_ANNOUNCEMENT));

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(8));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE8));

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(9));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE9));

        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(3));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE3));

        //adapter.addFragment(NewsListingFragment.newInstance(4), getString(R.string.TXT_LOGIN_NEWS_TYPE4));
        //adapter.addFragment(NewsListingFragment.newInstance(5), getString(R.string.TXT_LOGIN_NEWS_TYPE5));
        fragments.add(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.NewsListingFragment.newInstance(6));
        titles.add(getString(R.string.TXT_LOGIN_NEWS_TYPE6));

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), fragments, titles);

        //binding.vpNews.setAdapter(adapter);
        //binding.tabNews.setupWithViewPager(binding.vpNews);
    }

    private void toolbarSetVM() {
        try {
            Integer titleId = null;
            switch (newsType){
                case ANNOUNCEMENT:
                    titleId = R.string.TXT_COMMON_ANNOUNCEMENT;
                    break;
                case EVENTS:
                    titleId = R.string.TXT_LOGIN_NEWS_TYPE2;
                    break;
                case USEFUL_LINKS:
                    titleId = R.string.TXT_LOGIN_NEWS_TYPE8;
                    break;
                case DISCOUNTS:
                    titleId = R.string.TXT_LOGIN_NEWS_TYPE3;
                    break;
            }
            String title = "";
            if (titleId != null) {
                title = getString(titleId);
            }
            VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, title, null);
            binding.toolbar.setVm(vmToolbar);
        } catch (Exception e) {
        }

    }

    /*@Override
    public void onBackPressed() {
        if (binding.vpNews.getCurrentItem() != 0) {
            binding.vpNews.setCurrentItem(0);
        } else super.onBackPressed();
    }*/

    private static final String NEWS_TYPE = "NEWS_TYPE";
}
