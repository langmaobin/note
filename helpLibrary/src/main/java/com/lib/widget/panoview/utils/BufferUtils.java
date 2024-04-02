package com.lib.widget.panoview.utils;

import com.lib.widget.panoview.constant.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BufferUtils {
    public static FloatBuffer getFloatBuffer(final float[] array, int offset) {
        FloatBuffer bb = ByteBuffer.allocateDirect(
                array.length * Constants.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(array);
        bb.position(offset);
        return bb;
    }
}
