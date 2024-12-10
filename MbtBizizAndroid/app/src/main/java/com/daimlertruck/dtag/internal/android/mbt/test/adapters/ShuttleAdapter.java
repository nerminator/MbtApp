package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemShuttleBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.ShuttleList;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.TimeList;
import com.daimlertruck.dtag.internal.android.mbt.test.uiControls.UITime;

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
