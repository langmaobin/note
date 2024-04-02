package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;

public class ShiftColorFilter extends SimpleFragmentShaderFilter {
    public ShiftColorFilter(Context context) {
        super(context, "filter/fsh/mx_shift_color.glsl");
    }
}
