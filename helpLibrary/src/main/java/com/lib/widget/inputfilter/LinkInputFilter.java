package com.lib.widget.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

public class LinkInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Only keep characters that are alphanumeric
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isLetterOrDigit(c)
                    || c == '-'
                    || c == '.'
                    || c == '_'
                    || c == '~'
                    || c == ':'
                    || c == '/'
                    || c == '?'
                    || c == '#'
                    || c == '['
                    || c == ']'
                    || c == '@'
                    || c == '!'
                    || c == '$'
                    || c == '&'
                    || c == '('
                    || c == ')'
                    || c == '*'
                    || c == '+'
                    || c == ','
                    || c == ';'
                    || c == '='
                    || c == '`'
                    || c == '\'') {
                builder.append(c);
            }
        }
        // If all characters are valid, return null, otherwise only return the filtered characters
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}
