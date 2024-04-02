package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class VignetteFilter extends SimpleFragmentShaderFilter {
    public VignetteFilter(Context context) {
        super(context, "filter/fsh/mx_vignette.glsl");
    }
}
