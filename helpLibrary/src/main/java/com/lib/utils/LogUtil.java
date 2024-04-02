package com.lib.utils;

import android.util.Log;

import static com.lib.BuildConfig.IS_DEBUG;

public class LogUtil {

    public static void logInfo(String tag, String message) {
        if (IS_DEBUG) Log.i(tag, message);
    }

    public static void logError(String tag, String message) {
        if (IS_DEBUG) Log.e(tag, message);
    }

}
