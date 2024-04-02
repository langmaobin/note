package com.ywvision.wyvisionhelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ywvision.wyvisionhelper.R;

public class IconTextView extends AppCompatTextView {

    private boolean revampVersion;

    public void setRevampVersion(boolean revampVersion) {
        this.revampVersion = revampVersion;
        Typeface tf;
        if (revampVersion) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts2.ttf");
        } else {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts.ttf");
        }
        setTypeface(tf);
        invalidate();
    }

    public IconTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.IconTextView);
            revampVersion = attributes.getBoolean(R.styleable.IconTextView_itv_revampVersion, false);
            attributes.recycle();
        }
    }

    @Override
    public void setTypeface(Typeface tf) {
        if (revampVersion) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts2.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts.ttf"));
        }
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (revampVersion) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts2.ttf"), style);
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IconFonts.ttf"), style);
        }
    }

}