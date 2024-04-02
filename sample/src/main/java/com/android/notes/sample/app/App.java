package com.android.notes.sample.app;

import android.content.Context;


import com.ywvision.wyvisionhelper.app.AppBaseApplication;


public class App extends AppBaseApplication {
    private static final String TAG = App.class.getSimpleName();

    public static Context appContext;
    public static String SYSTEM_STATUS;
    @Override
    public void onCreate() {
        super.onCreate();
        SYSTEM_STATUS = com.lib.BuildConfig.channel;
        appContext = getApplicationContext();

    }
    public static Context getAppContext() {
        return appContext;
    }
}
