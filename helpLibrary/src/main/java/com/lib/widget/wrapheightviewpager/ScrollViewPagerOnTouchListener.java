package com.lib.widget.wrapheightviewpager;

import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.lib.widget.SmoothNestedScrollView;

/**
 * Resolve scrolling issue for viewpager within scrollview
 * use along with {@link WrapContentHeightViewPager}
 */
public class ScrollViewPagerOnTouchListener implements View.OnTouchListener {

    private SmoothNestedScrollView scrollView;
    private ViewPager viewPager;

    public ScrollViewPagerOnTouchListener(SmoothNestedScrollView scrollView, ViewPager viewPager) {
        this.scrollView = scrollView;
        this.viewPager = viewPager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int dragthreshold = 30;
        int downX = 0;
        int downY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) event.getRawX() - downX);
                int distanceY = Math.abs((int) event.getRawY() - downY);
                if (distanceY > distanceX && distanceY > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }

}
