package com.daimler.biziz.android.adapters;

import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemShuttleBinding;
import com.daimler.biziz.android.network.entity.food.ShuttleList;
import com.daimler.biziz.android.network.entity.food.TimeList;
import com.daimler.biziz.android.uiControls.CustomScrollView.UITime;

import java.util.ArrayList;
import java.util.List;

public class ShuttleAdapter extends DataBoundAdapter<ItemShuttleBinding> {
    private ArrayList<ShuttleList> list = new ArrayList<>();

    public ShuttleAdapter(ArrayList<ShuttleList> list) {
        super(R.layout.item_shuttle);
        this.list = list;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemShuttleBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(list.get(position));
        for (TimeList timeList : list.get(position).getTimeList()) {
            UITime uiTime = new UITime(holder.itemView.getContext(),timeList);
            holder.binding.container.addView(uiTime);
        }
        if (list.size()-1 == position){
            holder.binding.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
