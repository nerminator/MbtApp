package com.daimler.biziz.android.ui.newsDetail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.base.BaseActivity;
import com.daimler.biziz.android.databinding.ActivityNewsDetailBinding;
import com.daimler.biziz.android.network.entity.newsDetail.NewsDetailEntity;
import com.daimler.biziz.android.ui.about.AboutActivity;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NewsDetailActivity extends BaseActivity<ActivityNewsDetailBinding> {
    private static final String KEY_NEWS_DETAIL_ID = "KEY_NEWS_DETAIL_ID";
    private VMNewsDetail vmNewsDetail;

    public static void start(Context context, String newsDetailId) {
        Intent starter = new Intent(context, NewsDetailActivity.class);
        starter.putExtra(KEY_NEWS_DETAIL_ID, newsDetailId);
        context.startActivity(starter);
    }


/*// TODO: 2.05.2018 Daha sonra yapilacak buralar

    // News Types
    public static final int EVENT = 1;
    public static final int GASTRONOMI = 2;
    public static final int DEAD = 3;
    public static final int BIRTHDAY = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            EVENT,
            GASTRONOMI,
            DEAD,
            BIRTHDAY
    })
    public @interface NewsDetailType {
    }*/

    @Override
    public int getLayoutRes() {
        return R.layout.activity_news_detail;
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_news_detail);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);

        setToolbar();



        vmNewsDetail = ViewModelProviders.of(this, new VMNewsDetail.Factory(
                getApplication(),
                getIntent().getStringExtra(KEY_NEWS_DETAIL_ID)
                //"200"
        )).get(VMNewsDetail.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(vmNewsDetail);
        observe();
        nestedScrollListener();
        setClickListener();


    }

    private void setClickListener() {
        binding.btnLink.setOnClickListener(new View.OnClickListener() {
            private String url;
            @Override
            public void onClick(View v) {
                url = vmNewsDetail.getNewsDetail().getValue().getUrl();
                if (!TextUtils.isEmpty(url)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });
    }

    private void nestedScrollListener() {
        binding.nestedNewsDetail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
                    vmNewsDetail.linkVisibility.postValue(false);
                }
                else {
                    if (!TextUtils.isEmpty(vmNewsDetail.getNewsDetail().getValue().getUrl())) {
                        vmNewsDetail.linkVisibility.postValue(true);
                    }
                }
            }
        });
    }

    private void observe() {
        vmNewsDetail.showErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showMessageDialog(null, s);
            }
        });

        vmNewsDetail.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });

        vmNewsDetail.newsDetail.observe(this, new Observer<NewsDetailEntity>() {
            @Override
            public void onChanged(@Nullable NewsDetailEntity newsDetailEntity) {
                setActionBarTitle(newsDetailEntity.getType());
                if (!TextUtils.isEmpty(newsDetailEntity.getImage())) {
                    setCover(newsDetailEntity.getImage());
                }
                else {
                    binding.appBar.getLayoutParams().height = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
                    setExpandEnabled(false);
                    //binding.toolbar.setBackgroundColor(Color.parseColor("#2e3542"));
                }

            }
        });
    }

    private void setActionBarTitle(int type) {
        String title = "";
        switch (type) {
            case 1:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE1);
                break;
            case 2:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE2);
                break;
            case 3:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE3);
                break;
            case 4:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE4);
                break;
            case 5:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE5);
                break;
            case 6:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE6);
                break;
            case 7:
                title = getString(R.string.TXT_LOGIN_NEWS_TYPE7);
                break;

        }
        getSupportActionBar().setTitle(title);
    }

    private void setExpandEnabled(boolean enabled) {
        binding.appBar.setExpanded(enabled, false);
        binding.appBar.setActivated(enabled);
        final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.collapsingToolbar.getLayoutParams();
        if (enabled)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        binding.collapsingToolbar.setLayoutParams(params);
    }


    private void setToolbar() {
        /** Burada collapsingToolbarLayout ile ilgili düzenlemeleri yaptım
         * CollapsingToolbarLayout'un buton renklerini style'ına styles dosyasındaki 'ToolbarColoredBackArrow' satırlarını vererek Ev */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            /** Toolbar text*/
            //binding.collapsingToolbar.setTitle(header);

            binding.collapsingToolbar.setTitleEnabled(false);

            binding.toolbar.setTitleTextColor(Color.WHITE);

            /** Toolbar text size ve text rengini styles dosyasını kullanarak ayarladım */
            binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

            /** Toolbar text fontu */
            final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DaimlerCA-Regular.ttf");
            binding.collapsingToolbar.setCollapsedTitleTypeface(tf);
            binding.collapsingToolbar.setExpandedTitleTypeface(tf);
        }
    }

    private void setCover(String imageLink) {
      /*  if (imageLink != null) {
            Picasso.with(this).load(imageLink).fit().centerCrop().placeholder(R.drawable.placeholder_event).into(binding.imageEventDetail);
        } else binding.imageEventDetail.setImageResource(R.drawable.placeholder_event);*/

        //binding.imageNewsDetail.setBackgroundColor(Color.BLUE);
        Picasso.get().load(imageLink).fit().centerCrop().into(binding.imageNewsDetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_info:
                AboutActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
