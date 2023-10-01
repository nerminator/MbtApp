package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemBirthdayBinding;
import com.daimler.biziz.android.network.entity.birthday.BirthdayListEntity;

import java.util.ArrayList;
import java.util.List;

public class BirthdayAdapter extends DataBoundAdapter<ItemBirthdayBinding> {
    ArrayList<BirthdayListEntity> list = new ArrayList<>();

    public BirthdayAdapter(ArrayList<BirthdayListEntity> list) {
        super(R.layout.item_birthday);
        this.list = list;
    }



    @Override
    protected void bindItem(DataBoundViewHolder<ItemBirthdayBinding> holder, int position, List<Object> payloads) {
        if (position%2 ==0){
            holder.binding.container.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.flexible_second_item));
        }else {
            holder.binding.container.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.flexible_first_item));
        }

        holder.binding.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
