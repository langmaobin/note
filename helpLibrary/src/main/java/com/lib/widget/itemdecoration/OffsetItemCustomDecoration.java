package com.lib.widget.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OffsetItemCustomDecoration extends RecyclerView.ItemDecoration {

    private int offsetBottomTop;
    private int offsetLeftRight;

    public OffsetItemCustomDecoration(int offsetBottomTop, int offsetLeftRight) {
        this.offsetBottomTop = offsetBottomTop;
        this.offsetLeftRight = offsetLeftRight;
    }

    public OffsetItemCustomDecoration(@NonNull Context context, @DimenRes int offsetBottomTopId, @DimenRes int offsetLeftRightId) {
        this(
                context.getResources().getDimensionPixelSize(offsetBottomTopId),
                context.getResources().getDimensionPixelSize(offsetLeftRightId)
        );
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(offsetLeftRight, offsetBottomTop, offsetLeftRight, offsetBottomTop);
    }

}
