package com.daimler.biziz.android.uiControls;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.daimler.biziz.android.R;

import java.lang.reflect.Field;

public class ThemedNumberPicker extends NumberPicker {




    public ThemedNumberPicker(Context context) {
        this(context, null);
    }

    public ThemedNumberPicker(Context context, AttributeSet attrs) {
        // wrap the current context in the style we defined before
        super(new ContextThemeWrapper(context, R.style.AlertDialogCustomYear), attrs);
        init();
    }

    private void init() {
        setDividerColor();
    }

    public void setDividerColor() {
        try {
            Field fDividerDrawable = NumberPicker.class.getDeclaredField("mSelectionDivider");
            fDividerDrawable.setAccessible(true);
            Drawable d = (Drawable) fDividerDrawable.get(this);
            d.setAlpha(100);
            d.setColorFilter(getResources().getColor(R.color.tabUnSelectedColor), PorterDuff.Mode.SRC_ATOP);
            d.invalidateSelf();
            postInvalidate(); // Drawable is dirty
        } catch (Exception e) {

        }
    }
}
