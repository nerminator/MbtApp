package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemRecentShuttleSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchsAdapter extends DataBoundAdapter<ItemRecentShuttleSearchBinding> {
    private List<String> recentSearchs = new ArrayList<>();
    private RecentSearchCallback recentSearchCallback;

    public RecentSearchsAdapter(List<String> recentSearchs, RecentSearchCallback recentSearchCallback) {
        super(R.layout.item_recent_shuttle_search);
        this.recentSearchs = recentSearchs;
        this.recentSearchCallback = recentSearchCallback;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemRecentShuttleSearchBinding> holder, int position, List<Object> payloads) {
        holder.binding.setName(recentSearchs.get(position));
        holder.binding.setCallback(recentSearchCallback);
    }

    @Override
    public int getItemCount() {
        return recentSearchs.size();
    }

    public interface RecentSearchCallback {
        void onItemClick(String model);
    }
}
