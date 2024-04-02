package com.lib.widget.panoview.filters.advanced;

import android.content.Context;
import android.opengl.GLES20;

import com.lib.widget.panoview.filters.base.AbsFilter;
import com.lib.widget.panoview.object.Plane;
import com.lib.widget.panoview.programs.GLTwoInputProgram;
import com.lib.widget.panoview.textures.BitmapTexture;
import com.lib.widget.panoview.utils.BufferUtils;
import com.lib.widget.panoview.utils.PlaneTextureRotationUtils;
import com.lib.widget.panoview.utils.TextureUtils;

import java.nio.FloatBuffer;

/**
 * Using frameBuffer will rotate the image 180 degree around the Y-axis
 * so I rotate the texture before render
 * If you don't have enough filter, use Simpler filter to take position
 * Again,the order of filters matters ! ! !
 */

abstract class MixBlendFilter extends AbsFilter {

    private Plane plane;
    private GLTwoInputProgram twoInputProgram;
    private FloatBuffer mTexCoordinateBuffer2;
    private BitmapTexture bitmapTexture;
    private int textureHandle2;
    private Context context;

    private int mMixLocation;
    private float mMixturePercent;

    public MixBlendFilter(Context context, final String fragmentShaderPath, float mMixturePercent) {
        this.context = context;
        plane = new Plane(true);
        twoInputProgram = new GLTwoInputProgram(context, "filter/vsh/two_input.glsl", fragmentShaderPath);
        mTexCoordinateBuffer2 = BufferUtils.getFloatBuffer(PlaneTextureRotationUtils.TEXTURE_NO_ROTATION, 0);
        this.mMixturePercent = mMixturePercent;
        bitmapTexture = new BitmapTexture();
    }

    @Override
    public void init() {
        twoInputProgram.create();
        textureHandle2 = GLES20.glGetUniformLocation(twoInputProgram.getProgramId(), "sTexture2");
        mMixLocation = GLES20.glGetUniformLocation(twoInputProgram.getProgramId(), "mixturePercent");
        bitmapTexture.loadWithAssetFile(context, "filter/imgs/texture_360_n.jpg");
    }

    @Override
    public void onPreDrawElements() {
        mTexCoordinateBuffer2.position(0);
        GLES20.glVertexAttribPointer(twoInputProgram.getMaTexture2Handle(), 2, GLES20.GL_FLOAT, false, 0, mTexCoordinateBuffer2);
        GLES20.glEnableVertexAttribArray(twoInputProgram.getMaTexture2Handle());
        plane.uploadTexCoordinateBuffer(twoInputProgram.getTextureCoordinateHandle());
        plane.uploadVerticesBuffer(twoInputProgram.getPositionHandle());
        GLES20.glUniform1f(mMixLocation, mMixturePercent);
    }

    @Override
    public void destroy() {
        twoInputProgram.onDestroy();
        bitmapTexture.destroy();
    }

    @Override
    public void onDrawFrame(int textureId) {
        twoInputProgram.use();
        onPreDrawElements();
        TextureUtils.bindTexture2D(textureId, GLES20.GL_TEXTURE0, twoInputProgram.getuTextureSamplerHandle(), 0);
        TextureUtils.bindTexture2D(bitmapTexture.getImageTextureId(), GLES20.GL_TEXTURE1, textureHandle2, 1);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
        plane.draw();
    }

    public void setMixturePercent(float mMixturePercent) {
        this.mMixturePercent = mMixturePercent;
    }

}
