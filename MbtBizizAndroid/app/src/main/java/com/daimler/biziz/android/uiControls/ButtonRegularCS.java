package com.daimler.biziz.android.uiControls;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by atakankersit on 06/06/2017.
 */

public class ButtonRegularCS extends AppCompatButton {
    public ButtonRegularCS(Context context) {
        super(context);
        init(context);
    }

    public ButtonRegularCS(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/DaimlerCS-Regular.ttf");
        setTypeface(customFont);
    }
}
