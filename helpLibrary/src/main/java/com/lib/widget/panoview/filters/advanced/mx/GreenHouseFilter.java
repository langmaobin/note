package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class GreenHouseFilter extends SimpleFragmentShaderFilter {
    public GreenHouseFilter(Context context) {
        super(context, "filter/fsh/mx_green_house.glsl");
    }
}
