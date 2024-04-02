package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.lib.R;

public class PasswordTextInputLayout extends TextInputLayout {

    public PasswordTextInputLayout(Context context) {
        super(context);
    }

    public PasswordTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View passwordToggle = findViewById(R.id.text_input_password_toggle);
        if (passwordToggle != null) {
            passwordToggle.setBackground(null);
            passwordToggle.setPadding(0, 0, 0, 0);
        }
    }

}