package com.lib.widget.panoview.filters.base;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.lib.widget.panoview.constant.PanoMode;
import com.lib.widget.panoview.object.Plane;
import com.lib.widget.panoview.programs.GLPassThroughProgram;
import com.lib.widget.panoview.utils.MatrixUtils;
import com.lib.widget.panoview.utils.StatusHelper;
import com.lib.widget.panoview.utils.TextureUtils;

public class OrthoFilter extends AbsFilter {

    private int adjustingMode;

    private GLPassThroughProgram glPassThroughProgram;
    private Plane plane;

    private float[] projectionMatrix = new float[16];

    private int videoWidth, videoHeight;

    private StatusHelper statusHelper;

    public OrthoFilter(StatusHelper statusHelper, int adjustingMode) {
        this.statusHelper = statusHelper;
        glPassThroughProgram = new GLPassThroughProgram(statusHelper.getContext());
        plane = new Plane(true);
        Matrix.setIdentityM(projectionMatrix, 0);
        this.adjustingMode = adjustingMode;
    }

    @Override
    public void init() {
        glPassThroughProgram.create();
    }

    @Override
    public void onPreDrawElements() {
        super.onPreDrawElements();
        int targetSurfaceWidth = surfaceWidth;
        if (statusHelper.getPanoDisPlayMode() == PanoMode.DUAL_SCREEN) {
            targetSurfaceWidth /= 2;
        }
        MatrixUtils.updateProjection(
                videoWidth,
                videoHeight,
                targetSurfaceWidth,
                surfaceHeight,
                adjustingMode,
                projectionMatrix);
        glPassThroughProgram.use();
        plane.uploadTexCoordinateBuffer(glPassThroughProgram.getTextureCoordinateHandle());
        plane.uploadVerticesBuffer(glPassThroughProgram.getPositionHandle());
        GLES20.glUniformMatrix4fv(glPassThroughProgram.getMVPMatrixHandle(), 1, false, projectionMatrix, 0);
    }

    @Override
    public void destroy() {
        glPassThroughProgram.onDestroy();
    }

    @Override
    public void onDrawFrame(int textureId) {
        onPreDrawElements();
        TextureUtils.bindTexture2D(textureId, GLES20.GL_TEXTURE0, glPassThroughProgram.getTextureSamplerHandle(), 0);
        if (statusHelper.getPanoDisPlayMode() == PanoMode.DUAL_SCREEN) {
            GLES20.glViewport(0, 0, surfaceWidth / 2, surfaceHeight);
            plane.draw();
            GLES20.glViewport(surfaceWidth / 2, 0, surfaceWidth - surfaceWidth / 2, surfaceHeight);
            plane.draw();
        } else {
            GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
            plane.draw();
        }
    }

    public void updateProjection(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
    }

    @Override
    public void onFilterChanged(int width, int height) {
        super.onFilterChanged(width, height);
        updateProjection(videoWidth, videoHeight);
    }

}
