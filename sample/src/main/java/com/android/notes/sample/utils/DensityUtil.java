package com.android.notes.sample.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtil {

    public static int dp2px(Context ctx, float dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);// + 0.5f是为了让结果四舍五入
    }
    public static int getScreenWidth(Context context) {
        float  scale = context.getResources().getDisplayMetrics().density;
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static float dp2px(float paramFloat) {
        return TypedValue.applyDimension(1, paramFloat, Resources.getSystem().getDisplayMetrics());
    }
}
