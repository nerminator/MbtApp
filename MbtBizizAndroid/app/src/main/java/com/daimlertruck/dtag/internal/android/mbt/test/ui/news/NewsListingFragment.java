package com.daimlertruck.dtag.internal.android.mbt.test.ui.news;


import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.NewsAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.databinding.FragmentNewsListingBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.NewsEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.birthday.BirthdayActivity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.dialogs.bottomSheetDialog.DiscountTypeBottomSheet;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListingFragment extends BaseFragment implements DiscountTypeBottomSheet.DiscountSelectCallback, NewsAdapter.NewsItemCallback {


    private static final String KEY_TYPE = "type";
    private static final String LOCATION_ID = "locationId";
    public static final Integer TYPE_SOCIAL_CLUBS = 10;

    FragmentNewsListingBinding binding;
    private com.daimlertruck.dtag.internal.android.mbt.test.ui.news.VMNews vmNews;
    private List<Object> newsList = new ArrayList<Object>() {};
    private int type;
    private int page = 0;
    private Integer discountType = null;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 10;
    NewsAdapter adapter;
    private boolean isFinish;

    public static NewsListingFragment newInstance(Integer type) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        NewsListingFragment fragment = new NewsListingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static NewsListingFragment newInstance(Integer type, @Nullable Integer locationId) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putInt(LOCATION_ID, locationId);
        NewsListingFragment fragment = new NewsListingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public NewsListingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (vmNews == null)
            vmNews = ViewModelProviders.of(this).get(com.daimlertruck.dtag.internal.android.mbt.test.ui.news.VMNews.class);
        this.type = getArguments().getInt(KEY_TYPE);
        discountType = type == 3 ? 1 : null;
        if (newsList.isEmpty())
            if (type == TYPE_SOCIAL_CLUBS) {
                vmNews.getNewsWithLocationId(type, page, getArguments().getInt(LOCATION_ID));
            } else vmNews.getNews(type, page, discountType);
        observe();
        initUi();
        listenRecycler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_listing, container, false);

        return binding.getRoot();

    }

    private void listenRecycler() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.newsRecyler
                .getLayoutManager();

        binding.newsRecyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isFinish) return;

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!vmNews.getIsLoading().getValue()
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    page++;
                    vmNews.getNews(type, page, discountType);
                }
            }
        });
    }

    private void initUi() {
        binding.swipeReflesh.setOnRefreshListener(() -> {
            page = 0;
            vmNews.getNews(type, page, discountType);
        });
        binding.selectCategory.setVisibility(type == 3 ? View.VISIBLE : View.GONE);
        if (type == 3)
            binding.selectCategory.setOnClickListener(v -> DiscountTypeBottomSheet.newInstance(NewsListingFragment.this, discountType).show(getFragmentManager(), DiscountTypeBottomSheet.class.getCanonicalName()));
    }

    private void observe() {
        vmNews.getData().observe(getViewLifecycleOwner(), newsEntity -> {
            binding.swipeReflesh.setRefreshing(false);
            if (newsEntity.getNews() == null || newsEntity.getNews().size() < 10) {
                isFinish = true;
            }
            if (page == 0) {
                newsList.clear();
            }
            if (newsEntity != null)
                setAdapter(newsEntity);

            binding.swipeReflesh.setRefreshing(false);
        });
    }

    private void setAdapter(NewsEntity newsEntity) {

        if (newsEntity.getBirthdayCount() != null && newsEntity.getBirthdayCount() > 0 && type == 1) {
            newsList.add(newsEntity.getBirthdayCount());
        }
        for (News nw : newsEntity.getNews()) {
            if (!newsList.contains(nw.getMonthName())) {
                newsList.add(nw.getMonthName());
            }
            newsList.add(nw);
        }
        if (adapter == null) {
            adapter = new NewsAdapter(newsList, type == 1, this);
            binding.newsRecyler.setAdapter(adapter);
        } else if (binding.newsRecyler.getAdapter() == null) {
            binding.newsRecyler.setAdapter(adapter);
        } else adapter.notifyDataSetChanged();
    }

    @Override
    public void onDiscountSelected(int id, String catName) {
        if (id != discountType) {
            isFinish = false;
            page = 0;
            discountType = id;
            vmNews.getNews(type, page, discountType);
            binding.tvCategoryName.setText(catName);
        }
    }

    @Override
    public void onNewsClick(News news) {
        if (news.getType() == News.TYPE_LINKS) {
            if (!TextUtils.isEmpty(news.getUrl())) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(news.getUrl()));
                startActivity(i);
            }
        }
        else if (news.getType() == News.TYPE_CONTACTS) {
            if (!TextUtils.isEmpty(news.getPhone())) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", news.getPhone(), null));
                startActivity(i);
            }
        }

        else {
            NewsDetailActivity.start(getContext(), news.getId().toString());
        }
    }

    @Override
    public void openBirthday() {
        BirthdayActivity.start(getContext());
    }
}
