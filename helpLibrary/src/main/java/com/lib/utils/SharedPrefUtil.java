package com.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPrefUtil {

    private Context context;
    private Gson gson;
    private SharedPreferences sharedPref;

    public SharedPrefUtil(Context context) {
        this(context, context.getPackageName() + ".sharedpref");
    }

    public SharedPrefUtil(Context context, String sharedPrefName) {
        this.context = context;
        this.gson = new Gson();
        this.sharedPref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    public <T> boolean set(String key, T value) {
        boolean success = false;
        try {
            String json = gson.toJson(new JsonWrapperUtil<>(value));
            sharedPref.edit().putString(key, json).apply();
            success = true;
        } catch (Exception e) {
            if (!(e instanceof NullPointerException)) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public <T> T get(String key) {
        try {
            String json = sharedPref.getString(key, "");
            Type type = new TypeToken<JsonWrapperUtil>() {
            }.getType();
            JsonWrapperUtil<T> wrapper = gson.fromJson(json, type);
            return wrapper.parse(gson);
        } catch (Exception e) {
            if (!(e instanceof NullPointerException)) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Use for primitive data return type as primitive data type does not accept null as default value
     * (e.g. int, double, float, long, short, byte, char, boolean)
     */
    public <T> T get(String key, T defaultValue) {
        T t = get(key);
        return t != null ? t : defaultValue;
    }

    public void remove(String key) {
        sharedPref.edit().remove(key).apply();
    }

    public void clear() {
        sharedPref.edit().clear().apply();
    }

}
