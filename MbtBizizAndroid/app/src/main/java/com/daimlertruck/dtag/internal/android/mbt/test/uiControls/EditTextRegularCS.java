package com.daimlertruck.dtag.internal.android.mbt.test.uiControls;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by atakankersit on 06/06/2017.
 */

public class EditTextRegularCS extends AppCompatEditText {
    public EditTextRegularCS(Context context) {
        super(context);
        init(context);
    }

    public EditTextRegularCS(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/DaimlerCS-Regular.ttf");
        setTypeface(customFont);
    }
}
