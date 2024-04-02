package com.lib.widget.errorlabellayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.R;

public class ErrorLabelLayout extends LinearLayout implements ViewGroup.OnHierarchyChangeListener {

    private static final int ERROR_LABEL_PADDING = 4;

    protected TextView errorLabel;
    protected Drawable errorDrawable;

    private int textColor;
    private int errorColor;
    private float errorLabelTextSize;

    public ErrorLabelLayout(Context context) {
        super(context);
        initView();
    }

    public ErrorLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ErrorLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        errorLabelTextSize = getResources().getDimension(R.dimen._8ssp);
        setOnHierarchyChangeListener(this);
        setOrientation(VERTICAL);
        errorColor = Color.parseColor("#F04E4E");
        initErrorLabel();
    }

    protected void initErrorLabel() {
        errorLabel = new TextView(getContext());
        errorLabel.setFocusable(true);
        errorLabel.setFocusableInTouchMode(true);
        errorLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorLabelTextSize);
        errorLabel.setTextColor(errorColor);
        errorLabel.setPadding(dipsToPix(ERROR_LABEL_PADDING), 0, dipsToPix(ERROR_LABEL_PADDING), dipsToPix(ERROR_LABEL_PADDING));
        errorLabel.setVisibility(GONE);
    }

    public void setErrorLabelTextSize(float errorLabelTextSize) {
        this.errorLabelTextSize = errorLabelTextSize;
        errorLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorLabelTextSize);
    }

    public void setErrorColor(int color) {
        errorColor = color;
        errorLabel.setTextColor(errorColor);
    }

    public void clearError() {
        errorLabel.setVisibility(GONE);
        if (errorDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                errorDrawable.clearColorFilter();
            } else {
                errorDrawable.setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void setError(String text) {
        errorLabel.setVisibility(VISIBLE);
        errorLabel.setText(text);
        // changing focus from EditText to error label, necessary for Android L only
        // EditText background Drawable is not tinted, until EditText remains focus
        // errorLabel.requestFocus();
        // tint drawable
        if (errorDrawable != null) {
            errorDrawable.setColorFilter(errorColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        int childCount = getChildCount();
        if (childCount == 1) {
            View childView = getChildAt(0);
            Drawable background = childView.getBackground();
            if (childView instanceof EditText) {
                textColor = ((EditText) childView).getTextColors().getDefaultColor();
            }
//            if (background != null) {
//                errorDrawable = background.mutate();
//            }
            addView(errorLabel);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dps, getResources().getDisplayMetrics());
    }

}