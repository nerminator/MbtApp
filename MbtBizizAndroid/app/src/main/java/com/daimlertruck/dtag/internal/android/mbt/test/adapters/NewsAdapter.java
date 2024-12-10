package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.test.BR;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.MultiTypeDataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.news.News;

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
