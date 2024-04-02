package com.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SettingsUtil {

    public static boolean hasPermission(Context context, String... permissions) {
        boolean isAllPermissionGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionGranted = false;
                break;
            }
        }
        return isAllPermissionGranted;
    }

    @SuppressLint("HardwareIds")
    public static String getUuId(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = String.valueOf(tm.getDeviceId());
            String serialNumber = String.valueOf(tm.getSimSerialNumber());
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID uuid = new UUID(androidId.hashCode(), ((long) deviceId.hashCode() << 32) | serialNumber.hashCode());
            return uuid.toString();
        } catch (Exception e) {
            return getCurrentTimeInMs();
        }
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return !ValidUtil.isEmpty(androidId) ? androidId : getCurrentTimeInMs();
        } catch (Exception e) {
            return getCurrentTimeInMs();
        }
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceImei(Context context) {
        try {
            if (Build.VERSION.SDK_INT>= 29){
                return getAndroidId(context);
            }else{
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String id = telephonyManager.getDeviceId();
                if (id == null || id.equalsIgnoreCase("")) {
                    String macAddress = getDeviceMac();
                    return macAddress.equalsIgnoreCase("0") ? getAndroidId(context) : macAddress;
                } else {
                    return id;
                }
            }
        } catch (Exception e) {
            return getAndroidId(context);
        }
    }

    public static String getDeviceSerialNum() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            serial = c.getMethod("get", String.class).invoke(c, "ro.serialno") + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !ValidUtil.isEmpty(serial) ? serial : String.valueOf(new Date().getTime());
    }

    private static String getCurrentTimeInMs() {
        return String.valueOf(new Date().getTime());
    }

    private static String getDeviceMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * @param lngCode language code (e.g. zh, fa, en)
     */
    public static void changeLanguage(Context baseContext, String lngCode) {
        if (baseContext != null && lngCode != null) {
            Locale myLocale = new Locale(lngCode);
            Resources res = baseContext.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            if (Build.VERSION.SDK_INT >= 17) {
                conf.setLocale(myLocale);
            } else {
                conf.locale = myLocale;
            }
            res.updateConfiguration(conf, dm);
        }
    }
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
