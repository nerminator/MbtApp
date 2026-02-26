package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.graphics.Color;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.MultiTypeDataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemFoodHeaderBinding;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemFoodMenuBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.DetailList;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodInfo;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.FoodList;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuAdapter extends MultiTypeDataBoundAdapter<ItemFoodMenuBinding> {

    private ArrayList<FoodList> lists = new ArrayList<>();
    private FoodInfo foodInfo;

    public FoodMenuAdapter( FoodInfo foodInfo) {
        foodInfo.getFoodList().add(0,null);
        this.lists = (ArrayList<FoodList>) foodInfo.getFoodList();
        this.foodInfo = foodInfo;
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
//        super.bindItem(holder, position, payloads);
        if (position != 0) {

            if (position % 2 == 0) {
                //holder.binding.container.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.flexible_first_item));
            } else {
                //holder.binding.container.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.flexible_second_item));
                ((ItemFoodMenuBinding) holder.binding).container.setBackgroundColor(Color.parseColor("#08f4f4f4"));

            }
            ((ItemFoodMenuBinding) holder.binding).setData(lists.get(position));

            setChieldRecyclerView(holder, lists.get(position).getDetailList());


            //Set Click Listeners
            ((ItemFoodMenuBinding) holder.binding).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((ItemFoodMenuBinding) holder.binding).chieldGroup.getVisibility() == View.VISIBLE) {
                        ((ItemFoodMenuBinding) holder.binding).chieldGroup.setVisibility(View.GONE);
                        ((ItemFoodMenuBinding) holder.binding).imgArrow.animate().setInterpolator(new OvershootInterpolator()).rotation(0).setDuration(200).start();
                    } else {
                        ((ItemFoodMenuBinding) holder.binding).chieldGroup.setVisibility(View.VISIBLE);
                        ((ItemFoodMenuBinding) holder.binding).imgArrow.animate().setInterpolator(new OvershootInterpolator()).rotation(180).setDuration(200).start();
                    }
                }
            });
        }
        else {
            ((ItemFoodHeaderBinding) holder.binding).setDatainfo(foodInfo);
        }

    }


    private void setChieldRecyclerView(DataBoundViewHolder<ItemFoodMenuBinding> holder, List<DetailList> detailList) {
        if (detailList != null && detailList.size() > 0) {
            CalorieAdapter calorieAdapter = new CalorieAdapter((ArrayList<DetailList>) detailList);
            ((ItemFoodMenuBinding) holder.binding).recyclerViewCalorie.setAdapter(calorieAdapter);
        } else {
            ((ItemFoodMenuBinding) holder.binding).recyclerViewCalorie.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemLayoutId(int position) {
        if (position == 0) {
            return R.layout.item_food_header;
        } else {
            return R.layout.item_food_menu;
        }

    }
}
