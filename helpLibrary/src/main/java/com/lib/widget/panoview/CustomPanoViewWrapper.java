package com.lib.widget.panoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Build;

import com.lib.widget.panoview.constant.PanoMode;
import com.lib.widget.panoview.filters.vr.AbsHotspot;
import com.lib.widget.panoview.utils.BitmapUtils;
import com.lib.widget.panoview.utils.StatusHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * References: https://github.com/Martin20150405/Pano360
 */
public class CustomPanoViewWrapper {

    private PanoRender renderer;
    private GLSurfaceView glSurfaceView;

    private Context context;
    private String filePath;
    private List<AbsHotspot> hotspotList = new ArrayList<>();

    private CustomPanoViewWrapper(Context context, String filePath, GLSurfaceView glSurfaceView) {
        this.context = context;
        this.filePath = filePath;
        this.glSurfaceView = glSurfaceView;
        init();
    }

    private void init() {

        StatusHelper statusHelper = new StatusHelper(context);
        statusHelper.setPanoDisPlayMode(PanoMode.SINGLE_SCREEN);
        statusHelper.setPanoInteractiveMode(PanoMode.MOTION);

        Bitmap bitmap = BitmapUtils.loadBitmapFromAssets(context, filePath);

        renderer = PanoRender.newInstance()
                .setStatusHelper(statusHelper)
                .setImageMode(true)
                .setPlaneMode(false)
                .setBitmap(bitmap)
                .setFilterMode(PanoRender.FILTER_MODE_AFTER_PROJECTION)
                .init();
        renderer.getSpherePlugin().setHotspotList(hotspotList);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        glSurfaceView.setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            glSurfaceView.setPreserveEGLContextOnPause(true);
        }

    }

    public void onPause() {
        glSurfaceView.onPause();
    }

    public void onResume() {
        glSurfaceView.onResume();
    }

    public void releaseResources() {
        if (renderer.getSpherePlugin() != null) {
            renderer.getSpherePlugin().getSensorEventHandler().releaseResources();
        }
    }

    public static CustomPanoViewWrapper build(Context context, String filePath, GLSurfaceView glSurfaceView) {
        return new CustomPanoViewWrapper(context, filePath, glSurfaceView);
    }

}