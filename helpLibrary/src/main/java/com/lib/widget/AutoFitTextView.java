package com.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class AutoFitTextView extends AppCompatTextView {

    private float mDefaultTextSize;
    private Paint mTextPaint;

    public AutoFitTextView(Context context) {
        this(context, null);
    }

    private void initAttr() {
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.set(getPaint());
        }
        mDefaultTextSize = getTextSize();
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        refitText(text.toString(), getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refitText(getText().toString(), getMeasuredWidth());
    }

    public void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableTextWidth = textWidth - getPaddingLeft() - getPaddingRight();
            float tsTextSize = mDefaultTextSize;
            mTextPaint.setTextSize(tsTextSize);
            float length = mTextPaint.measureText(text);
            while (length > availableTextWidth) {
                tsTextSize--;
                mTextPaint.setTextSize(tsTextSize);
                length = mTextPaint.measureText(text);
            }
            setTextSize(TypedValue.COMPLEX_UNIT_PX, tsTextSize);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setTypeface(Typeface tf) {
        setPaintWithTypeface(tf);
        super.setTypeface(tf);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        setPaintWithTypeface(tf);
        super.setTypeface(tf, style);
    }

    private void setPaintWithTypeface(Typeface tf) {
        TextPaint mPaint = new TextPaint(getPaint());
        mPaint.setTypeface(tf);
        mTextPaint = new Paint();
        mTextPaint.set(mPaint);
    }

}