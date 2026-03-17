package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemSearchShuttleBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.model.ShuttleAdapterModel;

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
