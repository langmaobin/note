package com.lib.widget.panoview.utils;

import android.content.Context;

import com.lib.widget.panoview.constant.PanoMode;
import com.lib.widget.panoview.constant.PanoStatus;

public class StatusHelper {

    private PanoStatus panoStatus;
    private PanoMode panoDisPlayMode;
    private PanoMode panoInteractiveMode;
    private Context context;

    public StatusHelper(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public PanoStatus getPanoStatus() {
        return panoStatus;
    }

    public void setPanoStatus(PanoStatus panoStatus) {
        this.panoStatus = panoStatus;
    }

    public PanoMode getPanoDisPlayMode() {
        return panoDisPlayMode;
    }

    public void setPanoDisPlayMode(PanoMode panoDisPlayMode) {
        this.panoDisPlayMode = panoDisPlayMode;
    }

    public PanoMode getPanoInteractiveMode() {
        return panoInteractiveMode;
    }

    public void setPanoInteractiveMode(PanoMode panoInteractiveMode) {
        this.panoInteractiveMode = panoInteractiveMode;
    }

}
