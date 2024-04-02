package com.lib.utils;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.lib.R;

public class AnimationUtil {

    private static final String ROTATION = "rotation";
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";

    public static PropertyValuesHolder rotation(float... values) {
        return PropertyValuesHolder.ofFloat(ROTATION, values);
    }

    public static PropertyValuesHolder translationX(float... values) {
        return PropertyValuesHolder.ofFloat(TRANSLATION_X, values);
    }

    public static PropertyValuesHolder translationY(float... values) {
        return PropertyValuesHolder.ofFloat(TRANSLATION_Y, values);
    }

    public static PropertyValuesHolder scaleX(float... values) {
        return PropertyValuesHolder.ofFloat(SCALE_X, values);
    }

    public static PropertyValuesHolder scaleY(float... values) {
        return PropertyValuesHolder.ofFloat(SCALE_Y, values);
    }

    public static void slideFromTop(Context context, final View view, boolean show) {
        try {
            ValueAnimator animator;
            if (show) {
                view.setVisibility(View.VISIBLE);
                View v = view.getRootView();
                final int widthSpec = View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.AT_MOST);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(widthSpec, heightSpec);
                int height = view.getMeasuredHeight() <= 0 ? view.getHeight() : view.getMeasuredHeight();
                animator = ValueAnimator.ofInt(0, height);
            } else {
                int finalHeight = view.getHeight();
                animator = ValueAnimator.ofInt(finalHeight, 0);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.getLayoutParams().height = (int) animation.getAnimatedValue();
                    view.requestLayout();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                animator.setInterpolator(show ? new DecelerateInterpolator() : new AccelerateInterpolator());
            }
            animator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
            animator.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) {
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }
    }

    public static void slideFromTopWithListener(Context context, final View view, boolean show, Animator.AnimatorListener animatorListener) {
        try {
            ValueAnimator animator = null;
            if (show) {
                view.setVisibility(View.VISIBLE);
                View v = view.getRootView();
                final int widthSpec = View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.AT_MOST);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(widthSpec, heightSpec);
                animator = ValueAnimator.ofInt(0, view.getMeasuredHeight());
            } else {
                int finalHeight = view.getHeight();
                animator = ValueAnimator.ofInt(finalHeight, 0);
            }
            animator.addListener(animatorListener);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.getLayoutParams().height = (int) animation.getAnimatedValue();
                    view.requestLayout();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                animator.setInterpolator(show ? new DecelerateInterpolator() : new AccelerateInterpolator());
            }
            animator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
            animator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void slideFromBottom(Context context, final View view, boolean show) {
        try {
            ValueAnimator animator = null;
            if (show) {
                view.setVisibility(View.VISIBLE);
                View v = view.getRootView();
                final int widthSpec = View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.AT_MOST);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(widthSpec, heightSpec);
                animator = ValueAnimator.ofInt(view.getMeasuredHeight(), 0);
            } else {
                int finalHeight = view.getHeight();
                animator = ValueAnimator.ofInt(0, finalHeight);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.getLayoutParams().height = (int) animation.getAnimatedValue();
                    view.requestLayout();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                animator.setInterpolator(show ? new DecelerateInterpolator() : new AccelerateInterpolator());
            }
            animator.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
            animator.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (view != null) {
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }
    }

    public static void slideInBottom(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_slide_in_bottom, View.VISIBLE);
    }

    public static void slideOutBottom(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_slide_out_bottom, View.GONE);
    }

    public static void slideInTop(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_slide_in_top, View.VISIBLE);
    }

    public static void slideOutTop(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_slide_out_top, View.GONE);
    }

    public static void fadeIn(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_fade_in, View.VISIBLE);
    }

    public static void fadeOut(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_fade_out, View.GONE);
    }

    public static void fadeOutWithInvisible(Context context, View targetView) {
        animateView(context, targetView, R.anim.abc_fade_out, View.INVISIBLE);
    }

    private static void animateView(Context context, final View targetView, int anim, int visibility) {
        try {
            Animation animate = AnimationUtils.loadAnimation(context, anim);
            // Set fill after to false to prevent view from staying on screen after visibility set to gone
            animate.setFillAfter(false);
            targetView.startAnimation(animate);
            targetView.setVisibility(visibility);
        } catch (Exception e) {
            e.printStackTrace();
            if (targetView != null) {
                targetView.setVisibility(visibility);
            }
        }
    }

}
