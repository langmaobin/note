package com.lib.widget;

import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class PasswordEditTextDrawable implements EditTextDrawable.OnTextViewDrawableListener {

    private EditTextDrawable editTextDrawable;
    private Drawable drawableVisible;
    private Drawable drawableInvisible;
    private TextWatcher errorTextWatcher;
    private boolean hasListener;

    public PasswordEditTextDrawable(EditTextDrawable editTextDrawable,
                                    Drawable drawableVisible,
                                    Drawable drawableInvisible,
                                    TextWatcher errorTextWatcher) {
        this.editTextDrawable = editTextDrawable;
        this.drawableVisible = drawableVisible;
        this.drawableInvisible = drawableInvisible;
        this.errorTextWatcher = errorTextWatcher;
    }

    @Override
    public void onRightDrawableClicked() {
        Drawable drawable;
        int cursorPosition = editTextDrawable.getSelectionEnd();
        if (errorTextWatcher != null) {
            editTextDrawable.removeTextChangedListener(errorTextWatcher);
        }
        if (editTextDrawable.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            editTextDrawable.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            drawable = drawableVisible;
        } else {
            editTextDrawable.setTransformationMethod(PasswordTransformationMethod.getInstance());
            drawable = drawableInvisible;
        }
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        drawable.setBounds(0, 0, w, h);
        editTextDrawable.setCompoundDrawableRight(drawable);
        editTextDrawable.setSelection(cursorPosition);
    }
}
