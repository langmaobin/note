package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class BlackWhiteFilter extends SimpleFragmentShaderFilter {
    public BlackWhiteFilter(Context context) {
        super(context, "filter/fsh/mx_black_white.glsl");
    }
}
