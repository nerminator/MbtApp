package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemCauseBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.model.Day;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.TypeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berkay on 11.06.2018.
 */

public class WorkCalendarCauseAdapter extends DataBoundAdapter<ItemCauseBinding> {

    private List<TypeEntity> typeEntityList;
    private Day day;

    public WorkCalendarCauseAdapter(Day day, List<TypeEntity> typeEntityList) {
        super(R.layout.item_cause);
        this.typeEntityList = typeEntityList;
        this.day = day;
        if (this.typeEntityList == null) this.typeEntityList = new ArrayList<>();
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemCauseBinding> holder, int position, List<Object> payloads) {
        TypeEntity typeEntity = typeEntityList.get(position);
        holder.binding.setType(typeEntity);

        View view = holder.binding.viewDot;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor("#" + typeEntity.getTypeColor()));
        view.setBackground(shape);

        if (day != null && day.getToday()!=null) {
            holder.binding.textToday.setVisibility(day.getToday() ? View.VISIBLE : View.GONE);
        }
        else holder.binding.textToday.setVisibility(View.GONE);

        int bgColor;
        if (position % 2 == 0) {
            bgColor = ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.item_cause_bg);
        } else
            bgColor = ContextCompat.getColor(holder.binding.getRoot().getContext(), android.R.color.transparent);

        holder.binding.getRoot().setBackgroundColor(bgColor);
    }


    @Override
    public int getItemCount() {
        return typeEntityList == null ? 0 : typeEntityList.size();
    }
}
