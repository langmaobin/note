package com.ywvision.wyvisionhelper.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lib.utils.SharedPrefUtil;

public class AppSharedPref extends SharedPrefUtil {

    public enum Key {
       WORK,LIFE,HEALTH
    }

    public AppSharedPref(Context context) {
        super(context);
    }

    public AppSharedPref(Context context, String sharedPrefName) {
        super(context, sharedPrefName);
    }

    public <T> boolean set(Key key, T value) {
        return set(key.name(), value);
    }

    public <T> T get(Key key) {
        return get(key.name());
    }

    public <T> T get(Key key, T defaultValue) {
        return get(key.name(), defaultValue);
    }

    public void remove(Key key) {
        remove(key.name());
    }


    @SuppressLint("ApplySharedPref")
    public static void clearShare(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    @SuppressLint("ApplySharedPref")
    public static void setDefaultsBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    public static void setDefaultsString(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getDefaultsBoolean(String key, Context context, boolean t) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, t);
    }

    public static String getDefaultsString(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}
