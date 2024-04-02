package com.lib.widget.panoview.filters.base;

import android.content.Context;
import android.opengl.GLES20;

import com.lib.widget.panoview.object.Plane;
import com.lib.widget.panoview.programs.GLSimpleProgram;
import com.lib.widget.panoview.utils.TextureUtils;

public class SimpleFragmentShaderFilter extends AbsFilter {

    protected GLSimpleProgram glSimpleProgram;
    protected Plane plane;

    public SimpleFragmentShaderFilter(Context context, final String fragmentShaderPath) {
        glSimpleProgram = new GLSimpleProgram(context, "filter/vsh/simple.glsl", fragmentShaderPath);
        plane = new Plane(true);
    }

    @Override
    public void init() {
        glSimpleProgram.create();
    }

    @Override
    public void onPreDrawElements() {
        super.onPreDrawElements();
        glSimpleProgram.use();
        plane.uploadTexCoordinateBuffer(glSimpleProgram.getTextureCoordinateHandle());
        plane.uploadVerticesBuffer(glSimpleProgram.getPositionHandle());
    }

    @Override
    public void destroy() {
        glSimpleProgram.onDestroy();
    }

    @Override
    public void onDrawFrame(int textureId) {
        onPreDrawElements();
        TextureUtils.bindTexture2D(textureId, GLES20.GL_TEXTURE0, glSimpleProgram.getTextureSamplerHandle(), 0);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);
        plane.draw();
    }

}
