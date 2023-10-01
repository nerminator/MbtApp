package com.daimler.biziz.android.adapters;

import com.daimler.biziz.android.R;
import com.daimler.biziz.android.binding.DataBoundAdapter;
import com.daimler.biziz.android.binding.DataBoundViewHolder;
import com.daimler.biziz.android.databinding.ItemFoodCalorieBinding;
import com.daimler.biziz.android.network.entity.food.DetailList;

import java.util.ArrayList;
import java.util.List;

public class CalorieAdapter extends DataBoundAdapter<ItemFoodCalorieBinding>{
    private ArrayList<DetailList> lists=new ArrayList<>();

    public CalorieAdapter(ArrayList<DetailList> lists) {
        super(R.layout.item_food_calorie);
        this.lists = lists;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemFoodCalorieBinding> holder, int position, List<Object> payloads) {
        holder.binding.setData(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
