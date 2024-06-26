package com.lib.widget.panoview.filters.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.lib.widget.panoview.object.Plane;
import com.lib.widget.panoview.programs.GLOESProgram;
import com.lib.widget.panoview.textures.GLOESTexture;
import com.lib.widget.panoview.utils.MatrixUtils;
import com.lib.widget.panoview.utils.TextureUtils;

public class OESFilter extends AbsFilter {

    private GLOESTexture glOESTexture;
    private GLOESProgram glOESProgram;
    private Plane plane;

    private float[] mSTMatrix = new float[16];

    public OESFilter(Context context) {
        plane = new Plane(true);
        glOESProgram = new GLOESProgram(context);
        glOESTexture = new GLOESTexture();
        Matrix.setIdentityM(mSTMatrix, 0);
    }

    @Override
    public void init() {
        glOESProgram.create();
        glOESTexture.loadTexture();
    }

    @Override
    public void onPreDrawElements() {
        super.onPreDrawElements();
        glOESProgram.use();
        plane.uploadTexCoordinateBuffer(glOESProgram.getTextureCoordinateHandle());
        plane.uploadVerticesBuffer(glOESProgram.getPositionHandle());
        GLES20.glUniformMatrix4fv(glOESProgram.getMuSTMatrixHandle(), 1, false, mSTMatrix, 0);
        GLES20.glUniformMatrix4fv(glOESProgram.getMVPMatrixHandle(), 1, false, MatrixUtils.IDENTITY_MATRIX, 0);
    }

    @Override
    public void destroy() {
        glOESProgram.onDestroy();
        glOESTexture.deleteTexture();
    }

    @Override
    public void onDrawFrame(int textureId) {
        onPreDrawElements();
        TextureUtils.bindTextureOES(glOESTexture.getTextureId(), GLES20.GL_TEXTURE0, glOESProgram.getUTextureSamplerHandle(), 0);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
        plane.draw();
    }

    public GLOESTexture getGlOESTexture() {
        return glOESTexture;
    }

    public float[] getSTMatrix() {
        return mSTMatrix;
    }

}
