package com.daimler.biziz.android.adapters;

import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemStopBinding;
import com.daimler.biziz.android.network.entity.transportation.StopListEntity;

import java.util.ArrayList;
import java.util.List;

public class StopListAdapter extends DataBoundAdapter<ItemStopBinding> {
    private List<StopListEntity> stopList = new ArrayList<>();

    public StopListAdapter(List<StopListEntity> stopList) {
        super(R.layout.item_stop);
        this.stopList = stopList;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemStopBinding> holder, int position, List<Object> payloads) {
        ItemStopBinding sbinding = holder.binding;
        StopListEntity stop = stopList.get(position);
        sbinding.setData(stop);
        if (position == stopList.size() - 1) {
            sbinding.dividerStop.setVisibility(View.GONE);
            sbinding.imageStopDot.setImageResource(R.drawable.ic_icn_destination);
        } else {
            if (stop.getTargetStop() != null && stop.getTargetStop())
                sbinding.imageStopDot.setImageResource(R.drawable.stop_round_white);
            else sbinding.imageStopDot.setImageResource(R.drawable.stop_round);
            sbinding.dividerStop.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stopList.size();
    }
}
