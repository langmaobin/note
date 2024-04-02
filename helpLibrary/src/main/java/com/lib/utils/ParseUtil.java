package com.lib.utils;

public class ParseUtil {

    public static int parseInt(String value) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intValue;
    }

    public static float parseFloat(String value) {
        float floatValue = 0.0f;
        try {
            floatValue = Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return floatValue;
    }

    public static double parseDouble(String value) {
        double doubleValue = 0.0;
        try {
            doubleValue = Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doubleValue;
    }

    public static long parseLong(String value) {
        long longValue = 0;
        try {
            longValue = Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longValue;
    }

}
