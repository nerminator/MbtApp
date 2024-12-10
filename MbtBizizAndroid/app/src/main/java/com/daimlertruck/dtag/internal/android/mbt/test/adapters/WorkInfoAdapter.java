package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemWorkInfoBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.interfaces.IWorkInfoClick;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleItemEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.flexibleWorkHours.FlexibleWorkEntity;

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
