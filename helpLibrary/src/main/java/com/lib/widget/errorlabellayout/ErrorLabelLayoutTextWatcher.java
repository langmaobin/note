package com.lib.widget.errorlabellayout;

import android.text.Editable;
import android.text.TextWatcher;

public class ErrorLabelLayoutTextWatcher implements TextWatcher {

    private ErrorLabelLayout errorLabelLayout;

    public ErrorLabelLayoutTextWatcher(ErrorLabelLayout errorLabelLayout) {
        this.errorLabelLayout = errorLabelLayout;
        clearError();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        clearError();
    }

    private void clearError() {
        if (errorLabelLayout != null) {
            errorLabelLayout.clearError();
        }
    }

}
