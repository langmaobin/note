package com.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetworkUtil {

    private enum NetworkType {
        NOT_CONNECTED, WIFI, MOBILE
    }

    public static boolean isNetworkConnected(Context context) {
        return getConnectivityType(context) != NetworkType.NOT_CONNECTED;
    }

    public static boolean isWifiConnected(Context context) {
        return getConnectivityType(context) == NetworkType.WIFI;
    }

    public static boolean isMobileConnected(Context context) {
        return getConnectivityType(context) == NetworkType.MOBILE;
    }

    private static NetworkType getConnectivityType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetworkType.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return NetworkType.MOBILE;
            }
        }
        return NetworkType.NOT_CONNECTED;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isNetworkConnected = isNetworkConnected(context);
        if (isNetworkConnected) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                return exitValue == 0;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isNetworkConnected;
    }

}
