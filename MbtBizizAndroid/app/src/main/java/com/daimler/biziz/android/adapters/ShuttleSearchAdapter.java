package com.daimler.biziz.android.adapters;

import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemSearchShuttleBinding;
import com.daimler.biziz.android.model.ShuttleAdapterModel;

import java.util.List;

public class ShuttleSearchAdapter extends DataBoundAdapter<ItemSearchShuttleBinding> {
    private List<ShuttleAdapterModel> shuttleAdapterModels;
    private ShuttleCallback shuttleCallback;

    public ShuttleSearchAdapter(List<ShuttleAdapterModel> shuttleAdapterModels, ShuttleCallback shuttleCallback) {
        super(R.layout.item_search_shuttle);
        this.shuttleAdapterModels = shuttleAdapterModels;
        this.shuttleCallback = shuttleCallback;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemSearchShuttleBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(shuttleAdapterModels.get(position));
        holder.binding.setStopcallback(shuttleCallback);
        if (position % 2 == 0) {
            holder.binding.imageBackground.setVisibility(View.VISIBLE);
        } else holder.binding.imageBackground.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return shuttleAdapterModels.size();
    }

    public interface ShuttleCallback {
        void onItemClick(ShuttleAdapterModel model);
    }
}
