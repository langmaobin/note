package com.lib.widget.panoview.filters.advanced;

import android.content.Context;

public class DissolveBlendFilter extends MixBlendFilter {
    public DissolveBlendFilter(Context context) {
        super(context, "filter/fsh/two_input_dissolve_blend.glsl", 0.5f);
    }
}
