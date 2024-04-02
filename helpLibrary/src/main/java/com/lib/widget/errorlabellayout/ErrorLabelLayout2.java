package com.lib.widget.errorlabellayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lib.R;

public class ErrorLabelLayout2 extends ErrorLabelLayout {

    public ErrorLabelLayout2(Context context) {
        super(context);
    }

    public ErrorLabelLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorLabelLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initErrorLabel() {
        super.initErrorLabel();
        errorLabel.setPadding(0, getResources().getDimensionPixelSize(R.dimen._3sdp), 0, 0);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        if (child instanceof EditText || child instanceof RelativeLayout) {
            Drawable background = getChildAt(0).getBackground();
            if (background != null) {
                errorDrawable = background.mutate();
            }
            addView(errorLabel);
        }
    }

}