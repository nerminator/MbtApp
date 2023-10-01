package com.daimler.biziz.android.uiControls.CustomScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * Created by atakankersit on 24/06/2017.
 */

public class ScrollViewCustom extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public ScrollViewCustom(Context context) {
        super(context);
    }

    public ScrollViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollViewCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ScrollViewListener getScrollViewListener() {
        return scrollViewListener;
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
