package com.daimler.biziz.android.adapters;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemWorkInfoBinding;
import com.daimler.biziz.android.interfaces.IWorkInfoClick;
import com.daimler.biziz.android.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.daimler.biziz.android.network.entity.flexibleWorkHours.FlexibleWorkEntity;

import java.util.ArrayList;
import java.util.List;

public class WorkInfoAdapter extends DataBoundAdapter<ItemWorkInfoBinding> {
    private ArrayList<FlexibleItemEntity> list = new ArrayList<>();
    private FlexibleWorkEntity textValues;
    private IWorkInfoClick iWorkInfoClick;


    public WorkInfoAdapter(ArrayList<FlexibleItemEntity> list, FlexibleWorkEntity textValues, IWorkInfoClick iWorkInfoClick) {
        super(R.layout.item_work_info);
        this.list = list;
        this.textValues = textValues;
        this.iWorkInfoClick = iWorkInfoClick;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemWorkInfoBinding> holder, int position, List<Object> payloads) {
        if (position % 2 != 0) {
            holder.binding.container.setBackgroundColor(Color.parseColor("#08f4f4f4"));
        } else {
            holder.binding.container.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), android.R.color.transparent));
        }

        holder.binding.setData(list.get(position));
        holder.binding.setTextdata(textValues);
        holder.binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iWorkInfoClick.click(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
