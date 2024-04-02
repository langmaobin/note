package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class NoAnimationRadioButton extends RadioButton {

    public NoAnimationRadioButton(Context context) {
        super(context);
        removeBackgroundDrawable();
    }

    public NoAnimationRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        removeBackgroundDrawable();
    }

    public NoAnimationRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeBackgroundDrawable();
    }

    private void removeBackgroundDrawable() {
        setBackground(null);
    }

}