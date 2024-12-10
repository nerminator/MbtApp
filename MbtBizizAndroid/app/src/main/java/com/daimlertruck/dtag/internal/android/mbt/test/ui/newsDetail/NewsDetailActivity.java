package com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail;

import android.annotation.SuppressLint;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.daimlertruck.dtag.internal.android.mbt.test.ui.fullscreenimage.FullscreenImageActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.recycler.NewsDetailPdfAdapter;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.appbar.AppBarLayout;

import androidx.core.widget.NestedScrollView;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ActivityNewsDetailBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.about.AboutActivity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends BaseActivity<ActivityNewsDetailBinding> {
    private static final String KEY_NEWS_DETAIL_ID = "KEY_NEWS_DETAIL_ID";
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.VMNewsDetail vmNewsDetail;

    public static void start(Context context, String newsDetailId) {
        Intent starter = new Intent(context, NewsDetailActivity.class);
        starter.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
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


        vmNewsDetail = ViewModelProviders.of(this, new com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.VMNewsDetail.Factory(
                getApplication(),
                getIntent().getStringExtra(KEY_NEWS_DETAIL_ID)
                //"200"
        )).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.VMNewsDetail.class);
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
                    i.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
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
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    vmNewsDetail.linkVisibility.postValue(false);
                } else {
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

        vmNewsDetail.newsDetail.observe(this, newsDetailEntity -> {
            setActionBarTitle(newsDetailEntity.getType());
            if (!TextUtils.isEmpty(newsDetailEntity.getImage()) || (newsDetailEntity.getImage() != null && !newsDetailEntity.getImage().isEmpty())) {
                setCover(newsDetailEntity.getImage(), newsDetailEntity.getImages());
            } else {
                binding.appBar.getLayoutParams().height = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
                setExpandEnabled(false);
                //binding.toolbar.setBackgroundColor(Color.parseColor("#2e3542"));
            }

        });

        vmNewsDetail.pdfListData.observe(this, pdfList -> {
            if (pdfList != null && !pdfList.isEmpty()) {
                binding.recyclerViewPdf.setVisibility(View.VISIBLE);
                binding.container.setPadding(dpToPx(20),dpToPx(20),dpToPx(20),dpToPx(120));
                NewsDetailPdfAdapter pdfAdapter = new NewsDetailPdfAdapter(pdfList, pdfUrl -> {
                    if (pdfUrl != null && !pdfUrl.isEmpty()) {
                        openPdfItem(pdfUrl);
                    }
                });

                binding.recyclerViewPdf.setAdapter(pdfAdapter);
            } else {
                binding.recyclerViewPdf.setVisibility(View.GONE);
                binding.container.setPadding(dpToPx(20),dpToPx(20),dpToPx(20),dpToPx(20));
            }
        });

        vmNewsDetail.discountCodeViewState.observe(this, discountCodeViewState -> {
            binding.setDiscountViewState(discountCodeViewState);
        });

        vmNewsDetail.discountCodeCopied.observe(this, discountCode -> {
            copyTextToClipboard(discountCode);
        });
    }

    private void openPdfItem(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.daimlertruck.dtag.internal.android.mbt.test");
        intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=" + pdfUrl), "text/html");
        startActivity(intent);
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

    private void setCover(String imageLink, List<String> images) {
        if (images != null && !images.isEmpty()) {
            binding.imageSlider.setVisibility(View.VISIBLE);
            binding.imageNewsDetail.setVisibility(View.GONE);
            ArrayList<SlideModel> imageList = new ArrayList<>();

            for (String image : images) {
                imageList.add(new SlideModel(image, ScaleTypes.CENTER_INSIDE));
            }
            binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE);

            binding.imageSlider.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int position) {
                    FullscreenImageActivity.start(NewsDetailActivity.this, imageList.get(position).getImageUrl());
                }

                @Override
                public void doubleClick(int i) {
                }
            });
        } else {
            binding.imageSlider.setVisibility(View.GONE);
            binding.imageNewsDetail.setVisibility(View.VISIBLE);
            Picasso.get().load(imageLink).fit().centerCrop().into(binding.imageNewsDetail);
        }
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


    private void copyTextToClipboard(String textToCopy) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("Copied Code", textToCopy);
            clipboardManager.setPrimaryClip(clipData);
            showSnackbar(getString(R.string.TXT_CODE_COPIED));
        }
    }

    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    private Integer dpToPx(Integer padding){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                padding,
                Resources.getSystem().getDisplayMetrics());
    }
}
