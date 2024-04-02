package com.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.lib.R;

public class EditTextDrawable extends AppCompatEditText {

    private static final int DEFAULT_COMPOUND_DRAWABLE_SIZE = -1;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    private int compoundDrawableWidth;
    private int compoundDrawableHeight;

    public EditTextDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextDrawable);
        compoundDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.EditTextDrawable_etd_compoundDrawableWidth, DEFAULT_COMPOUND_DRAWABLE_SIZE);
        compoundDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.EditTextDrawable_etd_compoundDrawableHeight, DEFAULT_COMPOUND_DRAWABLE_SIZE);
        typedArray.recycle();
        resizeCompoundDrawables();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onClick) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (textViewDrawableListener != null) {
                                textViewDrawableListener.onRightDrawableClicked();
                            }
                            return true;
                        }
                    } else return event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width());
                    return false;
                } else {
                    return false;
                }
            }
        });
    }

    private void resizeCompoundDrawables() {
        Drawable[] drawables = getCompoundDrawables();
        if (compoundDrawableWidth > 0 || compoundDrawableHeight > 0) {
            for (Drawable drawable : drawables) {
                if (drawable == null) {
                    continue;
                }
                Rect realBounds = drawable.getBounds();
                float scaleFactor = realBounds.height() / (float) realBounds.width();
                float drawableWidth = realBounds.width();
                float drawableHeight = realBounds.height();
                if (this.compoundDrawableWidth > 0) {
                    if (drawableWidth > this.compoundDrawableWidth) {
                        drawableWidth = this.compoundDrawableWidth;
                        drawableHeight = drawableWidth * scaleFactor;
                    }
                }
                if (this.compoundDrawableHeight > 0) {
                    if (drawableHeight > this.compoundDrawableHeight) {
                        drawableHeight = this.compoundDrawableHeight;
                        drawableWidth = drawableHeight / scaleFactor;
                    }
                }
                realBounds.right = realBounds.left + Math.round(drawableWidth);
                realBounds.bottom = realBounds.top + Math.round(drawableHeight);
                drawable.setBounds(realBounds);
            }
        }
        super.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    public void setCompoundDrawableRight(Drawable drawableRight) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables[2] != null) {
            drawables[2] = drawableRight;
        }
        if (compoundDrawableWidth > 0 || compoundDrawableHeight > 0) {
            for (Drawable drawable : drawables) {
                if (drawable == null) {
                    continue;
                }
                Rect realBounds = drawable.getBounds();
                float scaleFactor = realBounds.height() / (float) realBounds.width();
                float drawableWidth = realBounds.width();
                float drawableHeight = realBounds.height();
                if (this.compoundDrawableWidth > 0) {
                    if (drawableWidth > this.compoundDrawableWidth) {
                        drawableWidth = this.compoundDrawableWidth;
                        drawableHeight = drawableWidth * scaleFactor;
                    }
                }
                if (this.compoundDrawableHeight > 0) {
                    if (drawableHeight > this.compoundDrawableHeight) {
                        drawableHeight = this.compoundDrawableHeight;
                        drawableWidth = drawableHeight / scaleFactor;
                    }
                }
                realBounds.right = realBounds.left + Math.round(drawableWidth);
                realBounds.bottom = realBounds.top + Math.round(drawableHeight);
                drawable.setBounds(realBounds);
            }
        }
        super.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    /**
     * Listener
     */

    private OnTextViewDrawableListener textViewDrawableListener;
    private boolean onClick;

    public void setTextViewDrawableListener(OnTextViewDrawableListener textViewDrawableListener) {
        this.textViewDrawableListener = textViewDrawableListener;
    }

    public void setTextViewDrawableListener(OnTextViewDrawableListener textViewDrawableListener, boolean onClick) {
        this.textViewDrawableListener = textViewDrawableListener;
        this.onClick = onClick;
    }

    public interface OnTextViewDrawableListener {
        void onRightDrawableClicked();
    }

}