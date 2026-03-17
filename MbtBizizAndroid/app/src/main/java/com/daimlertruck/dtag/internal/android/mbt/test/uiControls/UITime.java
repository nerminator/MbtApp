package com.daimlertruck.dtag.internal.android.mbt.test.uiControls;

import android.content.Context;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimlertruck.dtag.internal.android.mbt.R;
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

        TextView tvName  = view.findViewById(R.id.tvName);
        TextView tvValue = view.findViewById(R.id.tvValue);

        String name = timeList != null && timeList.getName() != null ? timeList.getName() : "";

        // Match Turkish + English (and a plain-ascii variant just in case)
        boolean isDinner =
                "Akşam Yemeği".equalsIgnoreCase(name) ||
                        "Aksam Yemegi".equalsIgnoreCase(name) ||
                        "Dinner".equalsIgnoreCase(name);

        if (isDinner) {
            String text = name + "*";
            SpannableString s = new SpannableString(text);
            int start = text.length() - 1;
            s.setSpan(new SuperscriptSpan(), start, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(0.7f), start, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvName.setText(s);
        } else {
            tvName.setText(name);
        }

        tvValue.setText(timeList != null ? timeList.getTimeText() : "");
    }
}
