package com.lib.widget.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

public class MobileInputFilter implements InputFilter {

    private int indexWithSpace = -1;

    public MobileInputFilter() {
    }

    public MobileInputFilter(int indexWithSpace) {
        this.indexWithSpace = indexWithSpace;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isDigit(c)
                    || (i == 0 && c == '+')
                    || (indexWithSpace > 0 && i == indexWithSpace && c == ' ')) {
                builder.append(c);
            }
        }
        // If all characters are valid, return null, otherwise only return the filtered characters
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}
