package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class SmoothNestedScrollView extends NestedScrollView {

    private int slop;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mState = RecyclerView.SCROLL_STATE_IDLE;
    private boolean enableScrolling = true;


    public SmoothNestedScrollView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration config = ViewConfiguration.get(context);
        slop = config.getScaledEdgeSlop();
    }

    public SmoothNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SmoothNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    private float xDistance, yDistance, lastX, lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            final float x = ev.getX();
            final float y = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xDistance = yDistance = 0f;
                    lastX = ev.getX();
                    lastY = ev.getY();
                    computeScroll();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float curX = ev.getX();
                    final float curY = ev.getY();
                    xDistance += Math.abs(curX - lastX);
                    yDistance += Math.abs(curY - lastY);
                    lastX = curX;
                    lastY = curY;
                    if (xDistance > yDistance) {
                        return false;
                    }
            }
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isEnableScrolling() && super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        // Grab the last child placed in the ScrollView, we need it to determinate the bottom position
        View view = getChildAt(getChildCount() - 1);
        // Calculate the scroll differences
        int diff = (view.getBottom() - (getHeight() + getScrollY()));
        // If differences is zero, then the bottom has been reached
        if (smoothNestedScrollViewInteractionListener != null) {
            if (diff <= 0) {
                // Notify that we have reached the bottom
                smoothNestedScrollViewInteractionListener.onScrollToBottom();
            } else {
//                smoothNestedScrollViewInteractionListener.onScrollToTop();
            }
            if (scrollY < oldScrollY) {
                // Vertical scrolling down
                smoothNestedScrollViewInteractionListener.onScrollUp();
            } else {
                // Vertical scrolling up
                smoothNestedScrollViewInteractionListener.onScrollDown();
            }
            if (scrollY == 0) {// 滚动到顶
                smoothNestedScrollViewInteractionListener.onScrollToTop();
            }
            smoothNestedScrollViewInteractionListener.calcFraction(scrollY-oldScrollY);
        }
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

    private OnSmoothNestedScrollViewInteractionListener smoothNestedScrollViewInteractionListener;

    public void setSmoothNestedScrollViewInteractionListener(OnSmoothNestedScrollViewInteractionListener smoothNestedScrollViewInteractionListener) {
        this.smoothNestedScrollViewInteractionListener = smoothNestedScrollViewInteractionListener;
    }

    public interface OnSmoothNestedScrollViewInteractionListener {

        void onScrollToBottom();

        void onScrollToTop();

        void onScrollDown();

        void onScrollUp();

        void calcFraction(int dy);

    }

}