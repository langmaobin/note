package com.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.lib.R;
import com.lib.base.BaseApplication;

public class TextViewProgressBar extends ProgressBar {

    private final int DEFAULT_TEXT_SIZE = 12;
    private final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private int textSize = sp2px(DEFAULT_TEXT_SIZE);
    private int textColor = DEFAULT_TEXT_COLOR;
    private float textSizeInFloat;
    private String text = "";
    private Paint textPaint = new Paint();
    private Rect bounds = new Rect();

    public TextViewProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewProgress);
        textColor = a.getColor(R.styleable.TextViewProgress_tvp_progressTextColor, textColor);
        textSize = (int) a.getDimension(R.styleable.TextViewProgress_tvp_progressTextSize, textSize);
        textSizeInFloat = a.getDimension(R.styleable.TextViewProgress_tvp_progressTextSize, textSize);
        a.recycle();
//        textPaint.setTextSize(textSize);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen._11ssp));
        textPaint.setColor(textColor);
        textPaint.setTypeface(BaseApplication.fontTypeface);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(text, x, y, textPaint);
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
        drawableStateChanged();
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

}