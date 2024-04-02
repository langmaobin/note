package com.lib.widget.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

public class AlphaInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Only keep characters that are alphabet
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isLetter(c) || Character.isSpaceChar(c)) {
                builder.append(c);
            }
        }
        // If all characters are valid, return null, otherwise only return the filtered characters
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}
