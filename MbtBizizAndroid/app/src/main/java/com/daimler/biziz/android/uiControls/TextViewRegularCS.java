package com.daimler.biziz.android.uiControls;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by atakankersit on 06/06/2017.
 */

public class TextViewRegularCS extends AppCompatTextView {
    public TextViewRegularCS(Context context) {
        super(context);
        init(context);
    }

    public TextViewRegularCS(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/DaimlerCS-Regular.ttf");
        setTypeface(customFont);
    }
}
