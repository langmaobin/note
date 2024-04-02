package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class MoonLightFilter extends SimpleFragmentShaderFilter {
    public MoonLightFilter(Context context) {
        super(context, "filter/fsh/mx_moon_light.glsl");
    }
}
