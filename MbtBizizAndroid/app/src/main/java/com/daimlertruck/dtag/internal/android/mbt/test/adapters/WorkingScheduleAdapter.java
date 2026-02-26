package com.daimlertruck.dtag.internal.android.mbt.test.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;

import com.daimlertruck.dtag.internal.android.mbt.R;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.binding.DataBoundViewHolder;
import com.daimlertruck.dtag.internal.android.mbt.databinding.ItemWorkingScheduleBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.model.Day;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.TypeEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule.WorkingScheduleFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WorkingScheduleAdapter extends DataBoundAdapter<ItemWorkingScheduleBinding> {
    private List<Day> days = new ArrayList<>();
    private WorkingScheduleFragment.IWorkCalendar iWorkCalendar;

    public WorkingScheduleAdapter(List<Day> days, WorkingScheduleFragment.IWorkCalendar iWorkCalendar) {
        super(R.layout.item_working_schedule);
        this.iWorkCalendar = iWorkCalendar;
        this.days = days;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ItemWorkingScheduleBinding> holder, int position, List<Object> payloads) {
        Day day = days.get(position);
        holder.binding.setData(day);
        holder.binding.setCallback(iWorkCalendar);

        if (day.getTypeEntityList() != null && day.getTypeEntityList().size()>0) {
            for (int i = 0; i < day.getTypeEntityList().size(); i++) {
                float marginPx = Utils.dp2px(holder.binding.getRoot().getResources(), 2);
                float scalePx = Utils.dp2px(holder.binding.getRoot().getResources(), 6);
                int mPx = Math.round(marginPx);
                int sPx = Math.round(scalePx);
                TypeEntity typeEntity = day.getTypeEntityList().get(i);
                View view = new View(holder.binding.linearWorkingTypes.getContext());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.OVAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sPx, sPx);
                params.setMargins(mPx, mPx, mPx, mPx);
                view.setLayoutParams(params);
                shape.setColor(Color.parseColor("#" + typeEntity.getTypeColor()));
                view.setBackground(shape);
                holder.binding.linearWorkingTypes.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
