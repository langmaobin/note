package com.lib.widget.currencyedittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.Currency;
import java.util.Locale;

@SuppressWarnings("unused")
public class CurrencyTextWatcher implements TextWatcher {

    private CurrencyEditText editText;
    private Locale defaultLocale;

    private boolean ignoreIteration;
    private boolean showCurrencySymbol;
    private int lastCursorPosition;
    private int lastTextLength;
    private String lastGoodInput;

    private double CURRENCY_DECIMAL_DIVISOR;
    private final int CURSOR_SPACING_COMPENSATION = 2;

    // Setting a max length because after this length, java represents doubles in scientific notation which breaks the formatter
    private final int MAX_RAW_INPUT_LENGTH = 15;

    /**
     * A specialized TextWatcher designed specifically for converting EditText values to a pretty-print string currency value.
     *
     * @param textBox The EditText box to which this TextWatcher is being applied.
     *                Used for replacing user-entered text with formatted text as well as handling cursor position for inputting monetary values
     */
    public CurrencyTextWatcher(CurrencyEditText textBox) {
        this(textBox, Locale.US);
    }

    /**
     * A specialized TextWatcher designed specifically for converting EditText values to a pretty-print string currency value.
     *
     * @param textBox       The EditText box to which this TextWatcher is being applied.
     *                      Used for replacing user-entered text with formatted text as well as handling cursor position for inputting monetary values
     * @param defaultLocale optional locale to default to in the event that the provided CurrencyEditText locale fails due to being unsupported
     */
    public CurrencyTextWatcher(CurrencyEditText textBox, Locale defaultLocale) {
        editText = textBox;
        lastGoodInput = "";
        ignoreIteration = false;
        this.defaultLocale = defaultLocale;
        // Different countries use different fractional values for denominations (0.999 <x> vs. 0.99 cents),
        // therefore this must be defined at runtime
        try {
            CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, Currency.getInstance(editText.getLocale()).getDefaultFractionDigits());
        } catch (IllegalArgumentException e) {
            Log.e("CurrencyTextWatcher", "Unsupported locale provided, defaulting to Locale.US. Error: " + e.getMessage());
            CURRENCY_DECIMAL_DIVISOR = (int) Math.pow(10, Currency.getInstance(defaultLocale).getDefaultFractionDigits());
        }
    }

    /**
     * After each letter is typed, this method will take in the current text, process it, and take the resulting
     * formatted string and place it back in the EditText box the TextWatcher is applied to
     *
     * @param editable text to be transformed
     */
    @Override
    public void afterTextChanged(Editable editable) {
        // Use the ignoreIteration flag to stop our edits to the text field from triggering an endlessly recursive call to afterTextChanged
        if (!ignoreIteration) {
//            int initialCursorPosition = editText.getSelectionEnd();
            int startingCursorPosition = editText.getSelectionEnd();
            ignoreIteration = true;
            // Start by converting the editable to something easier to work with, then remove all non-digit characters
            String newText = editable.toString();
            String textToDisplay;
            newText = (editText.areNegativeValuesAllowed()) ? newText.replaceAll("[^0-9/-]", "") : newText.replaceAll("[^0-9]", "");
            if (!newText.equals("") && newText.length() < MAX_RAW_INPUT_LENGTH && !newText.equals("-")) {
                // Store a copy of the raw input to be retrieved later by getRawValue
                editText.setValueInLowestDenom(Long.valueOf(newText));
            }
            try {
                textToDisplay = CurrencyTextFormatter.formatText(newText, editText.getCurrency(), editText.getLocale(), defaultLocale, showCurrencySymbol);
            } catch (IllegalArgumentException exception) {
                textToDisplay = lastGoodInput;
            }
            int textToDisplayLength = textToDisplay.length();
            int lastGoodInputLength = lastGoodInput.length();
            int diff = textToDisplayLength - lastGoodInputLength;
            if (showCurrencySymbol) {
                // If cursor position is within 'RM '
                if (startingCursorPosition <= 3) {
                    if (startingCursorPosition == 3) {
                        if (lastCursorPosition == 2) {
                            startingCursorPosition = 4;
                        }
                    } else if (startingCursorPosition < 3 && lastCursorPosition > startingCursorPosition) {
                        startingCursorPosition = lastCursorPosition;
                    } else if (newText.length() <= 3) {
                        if (Integer.parseInt(newText) == 0) {
                            startingCursorPosition = 3;
                        }
                    } else {
                        startingCursorPosition = 4;
                    }
                }
                // If cursor position is beyond 'RM '
                if (startingCursorPosition > 3) {
                    if (diff == 2) {
                        ++startingCursorPosition;
                    } else if (diff == -2) {
                        --startingCursorPosition;
                    }
                }
                // If text to display is 'RM 0.00'
                if (textToDisplayLength == 7) {
                    if (lastCursorPosition != 7 && startingCursorPosition > 3) {
                        if (lastCursorPosition > startingCursorPosition) {
                            startingCursorPosition = lastCursorPosition;
                        } else if (lastCursorPosition < startingCursorPosition) {
                            startingCursorPosition = lastCursorPosition;
                        }
                    } else if (lastCursorPosition == 7 && lastTextLength == 7) {
                        startingCursorPosition = lastCursorPosition;
                    }
                }
            } else {
                if (diff == 2) {
                    ++startingCursorPosition;
                } else if (diff == -2) {
                    --startingCursorPosition;
                }
                // If text to display is '0.00'
                if (textToDisplayLength == 4) {
                    if (lastCursorPosition != 4) {
                        if (lastCursorPosition > startingCursorPosition) {
                            // If cursor position is after '0.'
                            if (startingCursorPosition > 1) {
                                startingCursorPosition = lastCursorPosition;
                            }
                        } else if (lastCursorPosition < startingCursorPosition) {
                            startingCursorPosition = lastCursorPosition;
                        }
                    } else if (lastTextLength == 4) {
                        startingCursorPosition = lastCursorPosition;
                    }
                }
            }
            editText.setText(textToDisplay);
            // Store the last known good input so if there are any issues with new input later, we can fall back gracefully.
            lastGoodInput = textToDisplay;
            // Locate the position to move the cursor to. The CURSOR_SPACING_COMPENSATION constant is to account for locales where the Euro is displayed as " €" (2 characters).
            // A more robust cursor strategy will be implemented at a later date.
            int cursorPosition = editText.getText().length();
            if (textToDisplay.length() > 0 && Character.isDigit(textToDisplay.charAt(0)) && showCurrencySymbol) {
                cursorPosition -= CURSOR_SPACING_COMPENSATION;
            }
            if (newText.length() == MAX_RAW_INPUT_LENGTH) {
                startingCursorPosition = lastCursorPosition - 1;
            }
            if (startingCursorPosition > cursorPosition) {
                startingCursorPosition = cursorPosition;
            }
            if (startingCursorPosition < 0) {
                startingCursorPosition = 0;
            }
            // Move the cursor to the end of the numerical value to enter the next number in a right-to-left fashion, like you would on a calculator.
            editText.setSelection(startingCursorPosition);
        } else {
            ignoreIteration = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        try {
            char ch = editText.getText().toString().charAt(start);
            if (ch == '.' || ch == ',') {
                editText.setSelection(start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastCursorPosition = editText.getSelectionEnd();
        lastTextLength = editText.length();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    void setShowCurrencySymbol(boolean showCurrencySymbol) {
        this.showCurrencySymbol = showCurrencySymbol;
    }

}