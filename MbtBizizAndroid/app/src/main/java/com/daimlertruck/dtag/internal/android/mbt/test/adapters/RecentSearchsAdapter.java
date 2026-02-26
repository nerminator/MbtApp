package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemRecentShuttleSearchBinding;

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
