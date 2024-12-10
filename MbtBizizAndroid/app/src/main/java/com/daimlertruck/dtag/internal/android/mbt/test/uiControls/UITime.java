package com.daimlertruck.dtag.internal.android.mbt.test.uiControls;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.food.TimeList;

public class UITime extends RelativeLayout {

    private TimeList timeList;

    public UITime(Context context, TimeList timeList) {
        super(context);
        this.timeList = timeList;
        init(context);
    }

    public UITime(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    private void init(Context context) {
        View view = inflate(context, R.layout.item_time, this);
        ((TextView) view.findViewById(R.id.tvName)).setText(timeList.getName());
        ((TextView) view.findViewById(R.id.tvValue)).setText(timeList.getTimeText());
        //ItemTimeBinding binding = DataBindingUtil.bind(view);
        //binding.setData(timeList);
    }
}
