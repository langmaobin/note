package com.ywvision.wyvisionhelper.app;

import android.graphics.Typeface;

import com.lib.base.BaseApplication;

import static com.ywvision.wyvisionhelper.app.AppConstants.HAS_ICON_FONT_STYLE;
import static com.ywvision.wyvisionhelper.app.AppConstants.ICON_FONT_PATH;

public class AppBaseApplication extends BaseApplication {

    public static Typeface customTypeface;
    public static Typeface customTypeface2;
    public static Typeface fontRidleyGroteskRegularTypeface;
    public static Typeface fontRidleyGroteskBoldTypeface;
    public static Typeface fontPingFangMediumTypeface;
    public static Typeface fontPingFangRegularTypeface;

    @Override
    public void onCreate() {
        AppConstants.init(this);
        super.onCreate();
        if (HAS_ICON_FONT_STYLE) {
            customTypeface = Typeface.createFromAsset(getAssets(), ICON_FONT_PATH);
        }
        customTypeface2 = Typeface.createFromAsset(getAssets(), "fonts/IconFonts2.ttf");
        fontRidleyGroteskRegularTypeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat.ttf");
        fontRidleyGroteskBoldTypeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        fontPingFangMediumTypeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang-Medium.ttf");
        fontPingFangRegularTypeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang-Regular.ttf");
    }

}
