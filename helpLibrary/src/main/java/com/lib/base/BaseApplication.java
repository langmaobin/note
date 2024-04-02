package com.lib.base;

import android.graphics.Typeface;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import static com.lib.BuildConfig.IS_DEBUG;
import static com.lib.base.BaseConstants.FONT_PATH;
import static com.lib.base.BaseConstants.HAS_FONT_STYLE;

public class BaseApplication extends MultiDexApplication {

    public static Gson gson;
    public static Typeface fontTypeface;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        if (IS_DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        if (HAS_FONT_STYLE) {
            fontTypeface = Typeface.createFromAsset(getAssets(), FONT_PATH);
//            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                    .setDefaultFontPath(FONT_PATH)
//                    .setFontAttrId(R.attr.fontPath)
//                    .build());
        }
    }

}
