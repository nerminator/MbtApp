package com.daimler.biziz.android.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.daimler.biziz.android.BR;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.binding.MultiTypeDataBoundAdapter;
import com.daimler.biziz.android.network.entity.news.News;

import java.util.List;

public class NewsAdapter extends MultiTypeDataBoundAdapter {


    private final boolean isAll;
    private NewsItemCallback callback;

    public NewsAdapter(List items, boolean isAll, NewsItemCallback callback) {
        super(items);
        this.isAll = isAll;
        this.callback = callback;
    }

    @Override
    public int getItemLayoutId(int position) {
        if (mItems.get(position) instanceof String) {
            return R.layout.item_news_header;
        }
        if (isAll) {
            if (mItems.get(position) instanceof Integer) {
                return R.layout.item_news_birthday;
            }
            return R.layout.item_news_common;
        } else {
            News news = (News) mItems.get(position);
            switch (news.getType()) {
                case News.TYPE_EVENT:
                    return R.layout.item_news_event;
                case News.TYPE_DISCOUNT:
                    return R.layout.item_news_discount;
                case News.TYPE_LINKS:
                    return R.layout.item_news_links_and_contacts;
                case News.TYPE_CONTACTS:
                    return R.layout.item_news_links_and_contacts;
                default:
                    return R.layout.item_news_event;
            }

        }

    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        super.bindItem(holder, position, payloads);
        holder.binding.setVariable(BR.data, mItems.get(position));
        holder.binding.setVariable(BR.callback, callback);
    }

    public interface NewsItemCallback {

        void onNewsClick(News news);

        void openBirthday();
    }
}
