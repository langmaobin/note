
package com.ywvision.wyvisionhelper.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ywvision.wyvisionhelper.R;

/**
 * References: https://github.com/Chen-Sir/ExpandableTextView
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 5;

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /* The default content text size*/
    private static final float DEFAULT_CONTENT_TEXT_SIZE = 16;
    private static final float DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER = 1.0f;

    private static final int STATE_TV_GRAVITY_LEFT = 0;
    private static final int STATE_TV_GRAVITY_CENTER = 1;
    private static final int STATE_TV_GRAVITY_RIGHT = 2;

    protected TextView textViewContent;
    protected IconTextView textViewExpandCollapse; // TextView to expand/collapse
    protected TextView textViewMore;
    protected LinearLayout linearLayoutViewMore;
    protected View viewTransparency;

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;

    private int mTextHeightWithMaxLines;

    private int mMaxCollapsedLines;

    private int mMarginBetweenTxtAndBottom;

//    private Drawable mExpandDrawable;
//
//    private Drawable mCollapseDrawable;

    private int mStateTvGravity;

    private String mCollapsedString;

    private String mExpandString;

    private int mAnimationDuration;

    private float mContentTextSize;
    private float mContentTextViewMoreSize;

    private int mContentTextColor;
    private int mContentExpandTextColor;

    private float mContentLineSpacingMultiplier;

    private int mStateTextColor;

    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;

    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mMarginBetweenTxtAndBottom = getHeight() - textViewContent.getHeight();
        }
    };

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {

        if (linearLayoutViewMore.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        textViewExpandCollapse.setText(mCollapsed ? getContext().getString(R.string.icon_movedown) : getContext().getString(R.string.icon_moveup2));

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }

        if (!mCollapsed) {
            viewTransparency.setBackgroundColor(Color.TRANSPARENT);
            textViewMore.setText(getContext().getString(R.string.__T_global_text_view_less));
        } else {
            viewTransparency.setBackgroundResource(R.drawable.layer_list_side_top_bottom_white);
            textViewMore.setText(getContext().getString(R.string.__T_global_text_view_more));
        }

        // mark that the animation is in progress
        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - textViewContent.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                // notify the listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(textViewContent, !mCollapsed);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });

        clearAnimation();
        startAnimation(animation);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating;
    }

    @Override
    protected void onFinishInflate() {
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        linearLayoutViewMore.setVisibility(View.GONE);
        textViewContent.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (textViewContent.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Saves the text height w/ max lines
        mTextHeightWithMaxLines = getRealTextViewHeight(textViewContent);

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            textViewContent.setMaxLines(mMaxCollapsedLines);
        }
        linearLayoutViewMore.setVisibility(View.VISIBLE);
        viewTransparency.setBackgroundResource(R.drawable.layer_list_side_top_bottom_white);
        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            textViewContent.post(mRunnable);
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = getMeasuredHeight();
        }

    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(@Nullable CharSequence text) {
        mRelayout = true;
        textViewContent.setText((String.valueOf(text)));
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        textViewExpandCollapse.setText(mCollapsed ? getContext().getString(R.string.icon_movedown) : getContext().getString(R.string.icon_moveup2));
//        textViewExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(mCollapsed ? mExpandDrawable : mCollapseDrawable, null, null, null);
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        if (textViewContent == null) {
            return "";
        }
        return textViewContent.getText();
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_expandable_textview, this, true);

        // enforces vertical orientation
        setOrientation(LinearLayout.VERTICAL);

        // default visibility is gone
        setVisibility(GONE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mContentTextSize = typedArray.getDimension(R.styleable.ExpandableTextView_contentTextSize, DEFAULT_CONTENT_TEXT_SIZE);
        mContentTextViewMoreSize = typedArray.getDimension(R.styleable.ExpandableTextView_contentViewMoreTextSize, DEFAULT_CONTENT_TEXT_SIZE);
        mContentLineSpacingMultiplier = typedArray.getFloat(R.styleable.ExpandableTextView_contentLineSpacingMultiplier, DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER);
        mContentTextColor = typedArray.getColor(R.styleable.ExpandableTextView_contentTextColor, Color.BLACK);
        mContentExpandTextColor = typedArray.getColor(R.styleable.ExpandableTextView_viewMoreTextColor, Color.BLACK);

//        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
//        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
        mStateTvGravity = typedArray.getInt(R.styleable.ExpandableTextView_DrawableAndTextGravity, STATE_TV_GRAVITY_RIGHT);
        mExpandString = typedArray.getString(R.styleable.ExpandableTextView_expandText);
//        mCollapsedString = typedArray.getString(R.styleable.ExpandableTextView_collapseText);
        mStateTextColor = typedArray.getColor(R.styleable.ExpandableTextView_expandCollapseTextColor, Color.BLACK);

//        if (mExpandDrawable == null) {
//            mExpandDrawable = getDrawable(getContext(), android.R.drawable.arrow_down_float);
//        }
//        if (mCollapseDrawable == null) {
//            mCollapseDrawable = getDrawable(getContext(), android.R.drawable.arrow_up_float);
//        }

        if (mExpandString == null) {
            mExpandString = "";
        }
        if (mCollapsedString == null) {
            mCollapsedString = "";
        }

        typedArray.recycle();

    }

    private void findViews() {

        textViewContent = findViewById(R.id.expandable_text);
        textViewContent.setTextColor(mContentTextColor);
//        textViewContent.setTextSize(mContentTextSize);
        textViewContent.setLineSpacing(0, mContentLineSpacingMultiplier);
        textViewContent.setOnClickListener(this);

        textViewExpandCollapse = findViewById(R.id.expand_collapse);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (mStateTvGravity == STATE_TV_GRAVITY_LEFT) {
            params.gravity = Gravity.START;
        } else if (mStateTvGravity == STATE_TV_GRAVITY_CENTER) {
            params.gravity = Gravity.CENTER_HORIZONTAL;
        } else if (mStateTvGravity == STATE_TV_GRAVITY_RIGHT) {
            params.gravity = Gravity.END;
        }

        textViewMore = findViewById(R.id.text_view_more);
        textViewMore.setTextColor(mContentExpandTextColor);
//        textViewMore.setTextSize(mContentTextViewMoreSize);
        linearLayoutViewMore = findViewById(R.id.view_more);
        linearLayoutViewMore.setOnClickListener(this);

        viewTransparency = findViewById(R.id.view_transparency);

        textViewExpandCollapse.setLayoutParams(params);
//        textViewExpandCollapse.setText(mCollapsed ? mExpandString : mCollapsedString);
        textViewExpandCollapse.setText(mCollapsed ? getContext().getString(R.string.icon_movedown) : getContext().getString(R.string.icon_moveup2));

        textViewExpandCollapse.setTextColor(mContentExpandTextColor);
//        textViewExpandCollapse.setCompoundDrawablesWithIntrinsicBounds(mCollapsed ? mExpandDrawable : mCollapseDrawable, null, null, null);
        textViewExpandCollapse.setCompoundDrawablePadding(10);
        textViewExpandCollapse.setOnClickListener(this);

    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {

        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            textViewContent.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }

}