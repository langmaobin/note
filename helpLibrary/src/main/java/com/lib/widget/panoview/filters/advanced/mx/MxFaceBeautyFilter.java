package com.lib.widget.panoview.filters.advanced.mx;

import android.content.Context;
import android.opengl.GLES20;

import com.lib.widget.panoview.filters.base.SimpleFragmentShaderFilter;
import com.lib.widget.panoview.utils.TextureUtils;

public class MxFaceBeautyFilter extends SimpleFragmentShaderFilter {

    public MxFaceBeautyFilter(Context context) {
        super(context, "filter/fsh/mx_face_beauty.glsl");
    }

    @Override
    public void onDrawFrame(int textureId) {
        onPreDrawElements();
        setUniform1f(glSimpleProgram.getProgramId(), "stepSizeX", 1.0f / surfaceWidth);
        setUniform1f(glSimpleProgram.getProgramId(), "stepSizeY", 1.0f / surfaceHeight);
        TextureUtils.bindTexture2D(textureId, GLES20.GL_TEXTURE0, glSimpleProgram.getTextureSamplerHandle(), 0);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
        plane.draw();
    }

}
