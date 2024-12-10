package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.ItemFoodCalorieBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList;

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
