package com.ywvision.wyvisionhelper.api;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApiUrl {
    static final int DEVELOPMENT = 1;
    static final int STAGING = 2;
    static final int LIVE = 3;

    @IntDef({DEVELOPMENT, STAGING, LIVE})
    @Retention(RetentionPolicy.SOURCE)
    @interface ApiLink {
    }

//    public static String BASE_URL_LIVE = "http://8.210.230.236/master/";
    public static String BASE_URL_LIVE = "https://bazhihe.top/master/";
    public static String BASE_URL_DEVELOP = "http://47.115.224.200/master/";

    static String getCurrentBaseUrl(@ApiLink int apiLink) {
        return getApiLink(apiLink);
    }

    private static String getApiLink(@ApiLink int apiLink) {
        return BASE_URL_LIVE;
    }
}
