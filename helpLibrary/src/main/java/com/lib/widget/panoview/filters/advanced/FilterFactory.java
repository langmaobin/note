package com.lib.widget.panoview.filters.advanced;

import android.content.Context;

import com.lib.widget.panoview.filters.advanced.mx.BlackWhiteFilter;
import com.lib.widget.panoview.filters.advanced.mx.BrightnessFilter;
import com.lib.widget.panoview.filters.advanced.mx.FillLightFilter;
import com.lib.widget.panoview.filters.advanced.mx.GreenHouseFilter;
import com.lib.widget.panoview.filters.advanced.mx.MoonLightFilter;
import com.lib.widget.panoview.filters.advanced.mx.MultiplyFilter;
import com.lib.widget.panoview.filters.advanced.mx.MxFaceBeautyFilter;
import com.lib.widget.panoview.filters.advanced.mx.MxLomoFilter;
import com.lib.widget.panoview.filters.advanced.mx.MxProFilter;
import com.lib.widget.panoview.filters.advanced.mx.PastTimeFilter;
import com.lib.widget.panoview.filters.advanced.mx.PrintingFilter;
import com.lib.widget.panoview.filters.advanced.mx.ReminiscenceFilter;
import com.lib.widget.panoview.filters.advanced.mx.ShiftColorFilter;
import com.lib.widget.panoview.filters.advanced.mx.SunnyFilter;
import com.lib.widget.panoview.filters.advanced.mx.ToyFilter;
import com.lib.widget.panoview.filters.advanced.mx.VignetteFilter;
import com.lib.widget.panoview.filters.base.AbsFilter;
import com.lib.widget.panoview.filters.base.PassThroughFilter;

public class FilterFactory {

    public static AbsFilter createFilter(FilterType filterType, Context context) {
        switch (filterType) {
            //Effects
            case FILL_LIGHT_FILTER:
                return new FillLightFilter(context);
            case GREEN_HOUSE_FILTER:
                return new GreenHouseFilter(context);
            case BLACK_WHITE_FILTER:
                return new BlackWhiteFilter(context);
            case PAST_TIME_FILTER:
                return new PastTimeFilter(context);
            case MOON_LIGHT_FILTER:
                return new MoonLightFilter(context);
            case PRINTING_FILTER:
                return new PrintingFilter(context);
            case TOY_FILTER:
                return new ToyFilter(context);
            case BRIGHTNESS_FILTER:
                return new BrightnessFilter(context);
            case VIGNETTE_FILTER:
                return new VignetteFilter(context);
            case MULTIPLY_FILTER:
                return new MultiplyFilter(context);
            case REMINISCENCE_FILTER:
                return new ReminiscenceFilter(context);
            case SUNNY_FILTER:
                return new SunnyFilter(context);
            case MX_LOMO_FILTER:
                return new MxLomoFilter(context);
            case SHIFT_COLOR_FILTER:
                return new ShiftColorFilter(context);
            case MX_FACE_BEAUTY_FILTER:
                return new MxFaceBeautyFilter(context);
            case MX_PRO_FILTER:
                return new MxProFilter(context);
            default:
                return new PassThroughFilter(context);
        }
    }

    public static AbsFilter randomlyCreateFilter(Context context) {
        int pos = ((int) (Math.random() * Integer.MAX_VALUE)) % FilterType.values().length;
        return createFilter(FilterType.values()[pos], context);
    }

}
