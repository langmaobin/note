package com.lib.utils;

import android.util.Patterns;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtil {

    private static final String REGEX_ALPHABET_SMALL_CASE = "[a-z]";
    private static final String REGEX_ALPHABET_UPPER_CASE = "[A-Z]";
    private static final String REGEX_ALPHABET_UPPER_AND_LOWER_CASE = "[a-zA-Z]";
    private static final String REGEX_NUMERIC = "[0-9]";
    private static final String REGEX_ALPHANUMERIC_STRICTLY = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+)[0-9a-zA-Z]*$";
    private static final String REGEX_ALPHANUMERIC = "^[a-zA-Z0-9]*$";
    private static final String REGEX_NON_ALPHANUMERIC = "[^a-zA-Z0-9]";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static final String REGEX_MSIA_PHONE_NUMBER = "^60[1-9][0-9]{8,10}$"; // "^[1-9][0-9]*${9,11}$"; // "^6?0\\d{9,11}$";

    public static String getString(String value) {
        return value != null ? value : "";
    }

    public static String getStringWithEmptyDash(String value) {
        return !isEmpty(value) ? value : "-";
    }

    public static String getUrlWithoutHttp(String value) {
        return value != null ? value.replaceFirst("^(http[s]?://)", "") : "";
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() <= 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() <= 0;
    }

    public static boolean isEmpty(Object[] arrays) {
        return arrays == null || arrays.length <= 0;
    }

    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static boolean isAlphabetOnly(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHABET_UPPER_AND_LOWER_CASE);
    }

    public static boolean isNumericOnly(String value) {
        return value != null && isMatchRegex(value, REGEX_NUMERIC);
    }

    public static boolean isContainUppercase(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHABET_UPPER_CASE);
    }

    public static boolean isContainLowercase(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHABET_SMALL_CASE);
    }

    public static boolean isContainUpperAndLowercase(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHABET_UPPER_AND_LOWER_CASE);
    }

    public static boolean isContainAlphanumericOnly(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHANUMERIC);
    }

    public static boolean isAlphanumeric(String value) {
        return value != null && isMatchRegex(value, REGEX_ALPHANUMERIC_STRICTLY);
    }

    public static boolean isValidEmail(String value) {
        return value != null && Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public static boolean isValidDomainName(String value) {
        return value != null && Patterns.DOMAIN_NAME.matcher(value).matches();
    }

    public static boolean isValidIpAddress(String value) {
        return value != null && Patterns.IP_ADDRESS.matcher(value).matches();
    }

    public static boolean isValidPhoneNumber(String value) {
        return value != null && Patterns.PHONE.matcher(value).matches();
    }

    public static boolean isValidWebUrl(String value) {
        return value != null && Patterns.WEB_URL.matcher(value).matches();
    }

    public static boolean isValidMalaysiaPhoneNumber(String value) {
        return value != null && isMatchRegex(value, REGEX_MSIA_PHONE_NUMBER);
    }

    public static boolean isValidPosition(int position, int maxPosition) {
        return position >= 0 && position < maxPosition;
    }

    public static boolean isDiff(String value1, String value2) {
        return value1 != null && value2 != null && !value1.equals(value2);
    }

    private static boolean isMatchRegex(String targetString, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(targetString);
        return matcher.find();
    }

}
