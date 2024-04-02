package com.lib.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.lib.base.BaseConstants.CharSets.UTF_8;

public class StreamUtil {

    public static String encodeToUtf8(String value) {
        try {
            return URLEncoder.encode(value, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String decodeFromUtf8(String value) {
        try {
            return URLDecoder.decode(value, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
