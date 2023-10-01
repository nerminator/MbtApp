package com.daimler.biziz.android.uiControls.CustomScrollView;

public interface ScrollViewListener {
    void onScrollChanged(ScrollViewCustom scrollView,
                         int x, int y, int oldx, int oldy);
}