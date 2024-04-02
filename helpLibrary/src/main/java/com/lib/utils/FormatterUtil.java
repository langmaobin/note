package com.lib.utils;

import java.text.DecimalFormat;
import java.util.Locale;

public class FormatterUtil {

    /**
     * Number formatter
     */

    public static String getNumberFormatWithCommas(String value) {
        return getNumberFormatWithCommas(getNumberFormatWithoutCommas(value));
    }

    public static String getNumberFormatWithCommas(int value) {
        return String.format(Locale.ENGLISH, "%,d", value);
    }

    public static String getNumberFormatStringWithoutCommas(String value) {
        return value != null ? value.replaceAll("[^\\d]", "") : "0";
    }

    public static String getNumberFormatStringWithDotOnly(String value) {
        return value != null ? value.replaceAll("[^.\\d]", "") : "0";
    }

    public static int getNumberFormatWithoutCommas(String value) {
        return value != null ? ParseUtil.parseInt(value.replaceAll("[^\\d]", "")) : 0;
    }

    public static String getNumberFormatWithCommas(double value) {
        return new DecimalFormat("#,##0").format(value);
    }

    public static String getNumberFormatWithCommasAndDecimal(String value) {
        return getNumberFormatWithCommasAndDecimal(ParseUtil.parseDouble(value));
    }

    public static String getNumberFormatWithCommasAndDecimal(double value) {
        return new DecimalFormat("#,##0.00").format(value);
    }

    /**
     * Decimal formatter
     */

    public static String getDecimalFormatWithCommas(String value) {
        return getDecimalFormatWithCommas(ParseUtil.parseDouble(value));
    }

    public static String getDecimalFormatWithCommas(double value) {
        return new DecimalFormat("#,##0.00").format(value);
    }
    public static String getDecimalFormatWithOnePoint(String value) {
        return new DecimalFormat("#,##0.0").format(ParseUtil.parseDouble(value));
    }
//    public static double getDecimalFormatWithoutCommas(String value) {
//        return value != null ? ParseUtil.parseDouble(value.replaceAll("[^\\d.]", "")) : 0;
//    }
    public static String getDecimalFormatWithoutCommas(String value) {
        return new DecimalFormat("##0").format(ParseUtil.parseDouble(value));
    }

    public static String getDecimalFormatWithTwoPlaces(double value) {
        return new DecimalFormat("##0.00").format(value);
    }

    /**
     * Currency formatter
     */

    public static String getCurrencyValue(String currency, double value) {
        return String.format(Locale.ENGLISH, "%s %s", currency, getDecimalFormatWithCommas(value));
    }

    public static String getCurrencyValueWithoutDecimal(String currency, double value) {
        return String.format(Locale.ENGLISH, "%s %s", currency, getNumberFormatWithCommas(value));
    }

    private static final String MYR_CURRENCY = "RM";

    public static String getMyrCurrencyValue(double value) {
        return getCurrencyValue(MYR_CURRENCY, value);
    }

    public static String getMyrCurrencyValue(String value) {
        return getCurrencyValue(MYR_CURRENCY, ParseUtil.parseDouble(value));
    }

    public static String getMyrCurrencyValueConcat(String value) {
        return String.format(Locale.ENGLISH, "%s %s", MYR_CURRENCY, value);
    }

    public static String getMyrCurrencyValueWithoutDecimal(double value) {
        return getCurrencyValueWithoutDecimal(MYR_CURRENCY, value);
    }

    public static String getMyrCurrencyValueWithoutDecimal(String value) {
        return getCurrencyValueWithoutDecimal(MYR_CURRENCY, ParseUtil.parseDouble(value));
    }

    /**
     * Unit formatter
     */

    private static final String UNIT_KM = "KM";

    public static String getUnitKmValue(int value) {
        return String.format(Locale.ENGLISH, "%s %s", getNumberFormatWithCommas(value), UNIT_KM);
    }

    public static String getUnitKmValueWithoutCommas(int value) {
        return String.format(Locale.ENGLISH, "%s%s", String.valueOf(value), UNIT_KM);
    }

    private static final String UNIT_GB = "GB";

    public static String getUnitGbValue(int value) {
        return String.format(Locale.ENGLISH, "%s %s", getNumberFormatWithCommas(value), UNIT_GB);
    }

    public static String getUnitGbValueWithoutCommas(int value) {
        return String.format(Locale.ENGLISH, "%s%s", String.valueOf(value), UNIT_GB);
    }

    /**
     * Mobile formatter
     */

    public static String getMobileToDisplay(String mobile) {
        if (ValidUtil.isEmpty(mobile)) {
            return "";
        }
        mobile = mobile.replaceAll("[^\\d]", "");
        if (ValidUtil.isEmpty(mobile) || mobile.length() < 5) {
            return "";
        }
        String prefix = mobile.substring(0, 4);
        String mobileToDisplay = "+";
//        boolean is011 = prefix.startsWith("6011");
        if (mobile.startsWith(prefix)) {
            int mobileLength = mobile.length();
            int prefixLength = prefix.length();
            String front = mobile.substring(0, prefixLength);
            int middleEndLength = prefixLength + 4;
            if (middleEndLength > mobileLength) {
                return mobile;
            }
            String middle = mobile.substring(prefixLength, middleEndLength);
            String back = mobile.substring(middleEndLength, mobileLength);
            mobileToDisplay += front + " " + middle + " " + back;
        } else {
            return mobile;
        }
        return mobileToDisplay;
    }

    /**
     * Other formatter
     */

    public static String getValueWithFirstUppercaseOnly(String value) {
        if (!ValidUtil.isEmpty(value)) {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        return value;
    }

    /**
     * 将钱数转double类型
     * value
     */

    public static Double getValueWithDouble(String value) {
        double num=0.0;
        if (!ValidUtil.isEmpty(value)) {

            DecimalFormat format=new DecimalFormat("#0.00");
            num=Double.parseDouble(value);
            num=Double.parseDouble(format.format(num));
            return num;
        }
        return num;
    }

}
