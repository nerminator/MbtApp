package com.daimlertruck.dtag.internal.android.mbt.test.uiControls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareCoordinatorLayout extends RelativeLayout {
    public SquareCoordinatorLayout(Context context) {
        super(context);
    }

    public SquareCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
