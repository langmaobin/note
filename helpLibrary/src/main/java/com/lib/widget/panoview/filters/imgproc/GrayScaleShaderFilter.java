package com.lib.widget.panoview.filters.imgproc;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class GrayScaleShaderFilter extends SimpleFragmentShaderFilter {
    public GrayScaleShaderFilter(Context context) {
        super(context, "filter/fsh/gray_scale.glsl");
    }
}
