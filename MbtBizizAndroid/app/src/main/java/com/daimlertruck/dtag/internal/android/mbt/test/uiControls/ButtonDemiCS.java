package com.daimlertruck.dtag.internal.android.mbt.test.uiControls;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by atakankersit on 06/06/2017.
 */

public class ButtonDemiCS extends AppCompatButton {
    public ButtonDemiCS(Context context) {
        super(context);
        init(context);
    }

    public ButtonDemiCS(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/DaimlerCS-Demi.ttf");
        setTypeface(customFont);
    }


}
