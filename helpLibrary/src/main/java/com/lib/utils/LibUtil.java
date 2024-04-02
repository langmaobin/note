package com.lib.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lib.R;

import java.util.Random;

public class LibUtil {

    public static String genRandomNumbers(int digit) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(digit);
        for (int i = 0; i < digit; i++) {
            sb.append((char) ('0' + rnd.nextInt(10)));
        }
        return sb.toString();
    }

    public static void printPrettyJson(String tag, String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = new JsonParser().parse(json);
        LogUtil.logInfo(tag, gson.toJson(jsonElement));
    }

    public static String appendName(Context context, String name, int index, int total) {
        if (total == 1) {
            return " <b>" + name + "</b>";
        } else if (total == 2) {
            if (index == 0) {
                return " <b>" + name + "</b>";
            } else if (index == 1) {
                return " " + context.getString(R.string.__T_global_text_and) + " <b>" + name + "</b>";
            }
        } else if (total == 3) {
            if (index == 0) {
                return " <b>" + name + "</b>";
            } else if (index == 1) {
                return ", " + " <b>" + name + "</b>";
            } else if (index == 2) {
                return " " + context.getString(R.string.__T_global_text_and) + " <b>" + name + "</b>";
            }
        } else if (total >= 4) {
            if (index == 0) {
                return " <b>" + name + "</b>";
            } else if (index == 1) {
                return ", " + "<b>" + name + "</b>";
            } else if (index == 2) {
                return ", " + "<b>" + name + "</b>";
            } else if (index == 3) {
                return " " + context.getString(R.string.__T_global_text_and) + " <b>" + (total - 3) + "</b> " + context.getString(R.string.__T_global_text_more);
            }
        }
        return "";
    }

}
