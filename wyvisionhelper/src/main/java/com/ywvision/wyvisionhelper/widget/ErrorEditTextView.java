package com.ywvision.wyvisionhelper.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by SzeGuan on 3/7/2017.
 */

public class ErrorEditTextView extends AppCompatEditText {

    public ErrorEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
    }
}