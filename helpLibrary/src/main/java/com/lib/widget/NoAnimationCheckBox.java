package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class NoAnimationCheckBox extends CheckBox {

    public NoAnimationCheckBox(Context context) {
        super(context);
        removeBackgroundDrawable();
    }

    public NoAnimationCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        removeBackgroundDrawable();
    }

    public NoAnimationCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeBackgroundDrawable();
    }

    private void removeBackgroundDrawable() {
        setBackground(null);
    }

}
