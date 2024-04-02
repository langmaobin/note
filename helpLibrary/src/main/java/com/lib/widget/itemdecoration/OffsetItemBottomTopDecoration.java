package com.lib.widget.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OffsetItemBottomTopDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public OffsetItemBottomTopDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public OffsetItemBottomTopDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, mItemOffset, 0, mItemOffset);
    }

}