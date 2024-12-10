package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemBirthdayBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.birthday.BirthdayListEntity;

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
