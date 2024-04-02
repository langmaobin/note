package com.lib.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.lib.R;
import com.lib.utils.KeyboardUtil;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ViewUtil;

import java.util.Stack;

import butterknife.ButterKnife;

import static com.lib.base.BaseConstants.LibReqCode.REQ_CODE_CHECK_PERMISSION;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onSetupView();
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        ButterKnife.bind(this);
        onInitData();
        initialize();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onInitData() {
        fragmentManager = getSupportFragmentManager();
    }

    private void initialize() {
        initAnimations();
        getBundleExtras();
        setupToolbar();
        setupProgressBar();
        setupExceptionLayout();
        onBindData();
    }

    private void getBundleExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            onGetBundleData(getIntent().getExtras());
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() ==  MotionEvent.ACTION_DOWN ) {
//           hideKeyboard();
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    /**
     * Application context settings
     */

    private Context appContext;

    public Context getAppContext() {
        return appContext == null ? getApplicationContext() : appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * Layout view settings
     */

    private int layoutId;

    public void setLayoutView(int layoutId) {
        this.layoutId = layoutId;
    }

    private int frameLayoutId;

    public void setFrameLayoutView(int frameLayoutId) {
        this.frameLayoutId = frameLayoutId;
    }

    public void displayToast(String message) {
        ViewUtil.displayToast(this, message);
    }

    /**
     * Soft keyboard settings
     */

    public void hideKeyboard() {
        View currentFocusView = getCurrentFocus();
        if (currentFocusView != null) {
            KeyboardUtil.hideKeyboard(this, currentFocusView);
        }
    }

    public void showKeyboard() {
        KeyboardUtil.showKeyboard(this);
    }

    /**
     * Menu item settings
     */

    private boolean showMenu;
    private int menuResId;

    /**
     * Call this method during onCreate to determine whether to show menu item
     *
     * @param showMenu  true if menu item should be visible, false otherwise
     * @param menuResId the menu resource to be inflate into this layout
     */
    public void setHasOptionsMenu(boolean showMenu, int menuResId) {
        this.showMenu = showMenu;
        this.menuResId = menuResId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu) {
            menu.clear();
            getMenuInflater().inflate(menuResId, menu);
            onCreateMenuItem(menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Toolbar settings
     */

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Must place after set support action bar
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (baseInteractionListener != null) {
                        baseInteractionListener.onClickToolbarNavigation();
                    } else {
                        onClickToolbarNavigation();
                    }
                }
            });
            setToolbarTitle("");
            setToolbarNavigationIcon(null);
            onSetupToolbar();
            onCreateToolbar(toolbar);
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        if (toolbar != null) {
            TextView tvToolbarTitle = toolbar.findViewById(R.id.text_view_toolbar_title);
            if (tvToolbarTitle != null) {
                tvToolbarTitle.setText(title);
            }
        }
    }

    public void setToolbarBackgroundColor(int resId) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, resId));
        }
    }

    public void setToolbarNavigationIcon(Drawable icon) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(icon);
        }
    }

    public void setupSupportActionBar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
            }
        }
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Progress bar settings
     */

    protected boolean isInProgress;
    private View progressbarLayout;
    private View relativeBg;
    private TextView textViewDownloading;
    //    private AVLoadingIndicatorView avLoadingIndicatorView;
    private AppCompatImageView imgProgress;

    private void setupProgressBar() {
        progressbarLayout = findViewById(R.id.rl_progressbar);
        textViewDownloading = findViewById(R.id.text_view_downloading);
        relativeBg = findViewById(R.id.relative_bg);
        imgProgress = findViewById(R.id.img_progress);

//        avLoadingIndicatorView = findViewById(R.id.loading_indicator_view);
//        ProgressBar progressBar = findViewById(R.id.pb_loading);
//        if (progressBar != null) {
//            onCreateProgressbar(progressBar);
//        }
        initProgressBar();
        setRadiusAndColor(relativeBg, "000000", 10);
        setAlpha(relativeBg, "0.15");
    }

    public void showDownloadingText(String text) {
        if (textViewDownloading != null) {
            textViewDownloading.setVisibility(View.VISIBLE);
            textViewDownloading.setText(text);
        }
    }

    public void hideProgress() {
        if (progressbarLayout != null) {
//            avLoadingIndicatorView.smoothToHide();
            progressbarLayout.setVisibility(View.GONE);
            imgProgress.setVisibility(View.GONE);
        }
    }

    public void showProgress() {
        if (progressbarLayout != null) {
//            avLoadingIndicatorView.smoothToShow();
            progressbarLayout.setVisibility(View.VISIBLE);
            imgProgress.setVisibility(View.VISIBLE);
        }
    }

    public void initProgressBar() {
        if (imgProgress != null) {
            AnimationDrawable animation = new AnimationDrawable();
//            animation.addFrame(getResources().getDrawable(R.drawable.ic_progress1), 150);
//            animation.addFrame(getResources().getDrawable(R.drawable.ic_progress2), 150);
//            animation.addFrame(getResources().getDrawable(R.drawable.ic_progress3), 150);
//            animation.addFrame(getResources().getDrawable(R.drawable.ic_progress4), 150);
            animation.addFrame(getResources().getDrawable(R.drawable.anime_loading_1), 150);
            animation.addFrame(getResources().getDrawable(R.drawable.anime_loading_2), 150);
            animation.addFrame(getResources().getDrawable(R.drawable.anime_loading_3), 150);
            animation.addFrame(getResources().getDrawable(R.drawable.anime_loading_4), 150);
            animation.setOneShot(false);
            imgProgress.setBackgroundDrawable(animation);
            animation.start();
        }
    }

    /**
     * 设置四角统一圆角
     *
     * @paramcolorVale颜色值
     */
    public void setRadiusAndColor(View view, String colorVale, int radius) {

        if (view != null) {
            int roundRadius = radius;
            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(Color.parseColor("#" + colorVale));//内部填充颜色
            gd.setCornerRadius(roundRadius);

            view.setBackgroundDrawable(gd);
        }
    }

    /**
     * 设置view透明度
     *
     * @paramcolorVale颜色值
     */
    public void setAlpha(View view, String alphaVale) {
        if (view != null) {
            Double dvale = Double.valueOf(alphaVale) * 255;
            int vale = dvale.intValue();
            view.getBackground().mutate().setAlpha(vale);
        }
    }

    /**
     * Error layout settings
     */

    private View exceptionContainer;
    private ImageView ivExceptionIcon;
    private TextView tvExceptionText;
    private TextView tvExceptionButton;

    private void setupExceptionLayout() {
        exceptionContainer = findViewById(R.id.ll_exception_container);
        ivExceptionIcon = findViewById(R.id.iv_exception_icon);
        tvExceptionText = findViewById(R.id.tv_exception_text);
        tvExceptionButton = findViewById(R.id.tv_exception_button);
        if (tvExceptionButton != null) {
            tvExceptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickButtonRetry(view.getTag());
                }
            });
        }
    }

    protected void hideExceptionLayout() {
        if (exceptionContainer != null) {
            exceptionContainer.setVisibility(View.GONE);
        }
    }

    protected void showExceptionLayout() {
        showExceptionLayout(-1, R.string.__t_global_text_error_while_loading, false);
    }

    protected void showEmptyLayout() {
        showExceptionLayout(-1, R.string.__t_global_text_no_result_shows, false);
    }

    protected void showExceptionLayout(int iconResId, int msgResId, boolean hideButton) {
        showExceptionLayout(iconResId, msgResId, R.string.__t_global_text_tap_to_retry, hideButton);
    }

    protected void showExceptionLayout(String message) {
        try {
            showExceptionLayout(null, message, R.string.__t_global_text_tap_to_retry, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showExceptionLayout(int iconResId, int msgResId, int btnMsgResId, boolean hideButton) {
        try {
            showExceptionLayout(ViewUtil.getDrawable(this, iconResId), getString(msgResId), btnMsgResId, hideButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExceptionLayout(Drawable icon, String message, int btnMsgResId, boolean hideButton) throws Exception {
        if (exceptionContainer != null) {
            exceptionContainer.setVisibility(View.VISIBLE);
            if (ivExceptionIcon != null) {
                if (icon != null) {
                    ivExceptionIcon.setImageDrawable(icon);
                    ivExceptionIcon.setVisibility(View.VISIBLE);
                } else {
                    ivExceptionIcon.setVisibility(View.GONE);
                }
            }
            if (tvExceptionText != null) {
                tvExceptionText.setText(message);
            }
            if (tvExceptionButton != null) {
                String msg = getString(btnMsgResId);
                tvExceptionButton.setVisibility(hideButton ? View.GONE : View.VISIBLE);
                tvExceptionButton.setText(msg);
                tvExceptionButton.setTag(msg);
            }
        }
    }

    /**
     * Handle fragment switching and onBackPressed functions
     */

    private boolean enabledFragmentTransition;

    private FragmentManager fragmentManager;
    private Stack<Fragment> fragmentStack = new Stack<>();

    public void setEnabledFragmentTransition(boolean enabledFragmentTransition) {
        this.enabledFragmentTransition = enabledFragmentTransition;
    }

    private boolean enabledSlideFragmentTransition;

    public void setEnabledSlideFragmentTransition(boolean enabledSlideFragmentTransition) {
        this.enabledSlideFragmentTransition = enabledSlideFragmentTransition;
    }

    private boolean enabledSlideOverFragmentTransition;

    public void setEnabledSlideOverFragmentTransition(boolean enabledSlideOverFragmentTransition) {
        this.enabledSlideOverFragmentTransition = enabledSlideOverFragmentTransition;
    }

    private boolean enabledSlideOverFromBottomFragmentTransition;

    public void setEnabledSlideOverFromBottomFragmentTransition(boolean enabledSlideOverFromBottomFragmentTransition) {
        this.enabledSlideOverFromBottomFragmentTransition = enabledSlideOverFromBottomFragmentTransition;
    }

    public Stack<Fragment> getFragmentStack() {
        return fragmentStack;
    }

    private void popFragment() {
        if (!fragmentStack.isEmpty()) {
            fragmentStack.pop();
        }
    }

    public void onBack() {
        onBack(null);
    }

    public void onBack(Bundle args) {
        if (baseInteractionListener != null && args == null) {
            baseInteractionListener.onBackPressed();
        } else {
            onBackPressed(args);
        }
    }

    public void onBackPressed(Bundle args) {
        hideKeyboard();
        setBaseInteractionListener(null);
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof BaseFragment
                && ((BaseFragment) fragment).isBackAllowed()
                && getFragmentCount() > 1) {
            fragmentManager.popBackStack();
            popFragment();
            try {
                previousFragment = getCurrentFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Fragment previousFragment = getFragmentAt(getFragmentCount() - 2);
            if (previousFragment != null) {
                isFragmentMatched(previousFragment.getClass(), args, previousFragment);
            }
        } else {
            onLastFragmentBackPressed();
        }
    }

//    public void clearFragmentStack() {
//        clearFragmentStack(null, null);
//    }

//    public void clearFragmentStack(Class fragmentClass, Bundle args) {
//        hideKeyboard();
//        setBaseInteractionListener(null);
//        int entryCount = getFragmentCount();
//        Fragment fragment = null;
//        try {
//            previousFragment = getCurrentFragment();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Retain first fragment
//        while (entryCount-- > 1) {
//            fragmentManager.popBackStack();
//            popFragment();
//            fragment = getFragmentAt(entryCount - 1);
//            if (fragment != null && isFragmentMatched(fragmentClass, args, fragment)) {
//                break;
//            }
//        }
//        fragmentToResume = fragment;
//        fragmentManager.executePendingTransactions();
//    }

    public void clearFragmentStack(Bundle args, Class... fragmentClasses) {
        hideKeyboard();
        setBaseInteractionListener(null);
        int entryCount = getFragmentCount();
        Fragment fragment = null;
        try {
            previousFragment = getCurrentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retain first fragment
        outerloop:
        while (entryCount-- > 1) {
            fragmentManager.popBackStack();
            popFragment();
            fragment = getFragmentAt(entryCount - 1);
            if (fragment != null && fragmentClasses != null && fragment instanceof BaseFragment) {
                for (Class clazz : fragmentClasses) {
                    if (isFragmentMatched(clazz, args, fragment)) {
                        break outerloop;
                    }
                }
            }
        }
        fragmentToResume = fragment;
        fragmentManager.executePendingTransactions();
    }

    public void clearFragmentStackIncluded(Class... fragmentClasses) {
        clearFragmentStackIncluded(null, fragmentClasses);
    }

    public void clearFragmentStackIncluded(Bundle args, Class... fragmentClasses) {
        hideKeyboard();
        int entryCount = getFragmentCount();
        Fragment fragment = null;
        try {
            previousFragment = getCurrentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retain first fragment
        while (entryCount-- > 1) {
            fragmentManager.popBackStack();
            popFragment();
            fragment = getFragmentAt(entryCount - 1);
            if (!isFragmentIncluded(fragment, fragmentClasses)) {
                if (fragment != null && args != null) {
                    ((BaseFragment) fragment).onUpdateFragment(args);
                }
                break;
            }
        }
        fragmentToResume = fragment;
        fragmentManager.executePendingTransactions();
    }

    public void switchFragment(Fragment fragment) {
        switchFragment(true, fragment, false);
    }

    public void switchFragment2(Fragment fragment) {
        switchFragmentWithoutInstances(null, fragment);
    }

    public void switchFragment(boolean remainInstances, Fragment fragment, boolean disabledFragmentTransition) {
        Fragment src = getCurrentFragment();
        if (src != null && src.getClass() != fragment.getClass()) {
            if (remainInstances) {
                switchFragmentWithInstances(src, fragment, disabledFragmentTransition);
            } else {
                switchFragmentWithoutInstances(src, fragment);
                // switchFragmentWithoutInstances(src, fragment, false);
            }
        } else if (src == null) {
            switchFragmentWithoutInstances(null, fragment);
        }
    }

    public void switchFragmentWithSameClazz(boolean remainInstances, Fragment fragment) {
        Fragment src = getCurrentFragment();
        if (src != null) {
            if (remainInstances) {
                switchFragmentWithInstances(src, fragment, false);
            } else {
                switchFragmentWithoutInstances(src, fragment);
            }
        } else {
            switchFragmentWithoutInstances(null, fragment);
        }
    }

    private void switchFragmentWithInstances(Fragment src, Fragment dest, boolean disabledFragmentTransition) {
        FragmentTransaction transaction = startFragmentTransaction(src, disabledFragmentTransition);
        if (src instanceof BaseFragment && (((BaseFragment) src).isShowFragmentBottomTopAnimation() || showBottomTopAnimation)) {
            transaction.add(frameLayoutId, dest, String.valueOf(getFragmentCount()));
        } else {
            transaction.add(frameLayoutId, dest, String.valueOf(getFragmentCount())).hide(src);
        }
        commitFragmentTransaction(transaction);
        fragmentStack.push(dest);
    }

//    private void switchFragmentWithoutInstances(Fragment src, Fragment dest, boolean disabledFragmentTransition) {
//        FragmentTransaction transaction = startFragmentTransaction(src, disabledFragmentTransition);
//        transaction.add(frameLayoutId, dest, String.valueOf(getFragmentCount())).remove(src);
//        commitFragmentTransaction(transaction);
//        fragmentStack.push(dest);
//    }

    private void switchFragmentWithoutInstances(Fragment src, Fragment dest) {
        FragmentTransaction transaction = startFragmentTransaction(src, false); // true for disabled
        transaction.replace(frameLayoutId, dest, String.valueOf(getFragmentCount()));
        commitFragmentTransaction(transaction);
        fragmentStack.push(dest);
    }

    private FragmentTransaction startFragmentTransaction(Fragment src, boolean disabledFragmentTransition) {
        previousFragment = src;
        fragmentManager.executePendingTransactions();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (disabledFragmentTransition) {
//            transaction.setCustomAnimations(
//                    R.anim.anim_fade_in_fast,
//                    R.anim.anim_fade_out_fast,
//                    R.anim.anim_enter_from_left,
//                    R.anim.anim_exit_to_right);
//        } else
        if (enabledSlideOverFromBottomFragmentTransition && src != null) {
            transaction.setCustomAnimations(
                    R.anim.anim_slide_up_animation_2,
                    R.anim.anim_fade_out_with_dark_bg,
                    R.anim.anim_fade_in_with_dark_bg,
                    R.anim.anim_slide_down_animation_2);
            enabledSlideOverFromBottomFragmentTransition = false;
        } else if (src instanceof BaseFragment && (((BaseFragment) src).isShowFragmentBottomTopAnimation() || showBottomTopAnimation)) {
            transaction.setCustomAnimations(
                    R.anim.anim_slide_up_animation,
                    0,
                    0,
                    R.anim.anim_slide_down_animation);
        } else if (enabledFragmentTransition) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        } else if (enabledSlideFragmentTransition && src != null) {
            transaction.setCustomAnimations(
                    R.anim.anim_slide_up_animation_2,
                    R.anim.anim_fade_out_with_dark_bg,
                    R.anim.anim_fade_in_with_dark_bg,
                    R.anim.anim_slide_down_animation_2);
        } else if (enabledSlideOverFragmentTransition && src != null) {
            transaction.setCustomAnimations(
                    R.anim.anim_slide_right_animation,
                    R.anim.anim_fade_out_with_dark_bg,
                    R.anim.anim_fade_in_with_dark_bg,
                    R.anim.anim_slide_left_animation);
        }
        return transaction;
    }

    private void commitFragmentTransaction(FragmentTransaction transaction) {
        transaction.addToBackStack(null).commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private boolean isFragmentMatched(Class fragmentClass, Bundle args, Fragment fragment) {
        if (fragmentClass != null
                && fragmentClass.isInstance(fragment)
                && fragment instanceof BaseFragment) {
            if (args != null) {
                ((BaseFragment) fragment).onUpdateFragment(args);
            }
            return true;
        }
        return false;
    }

    private boolean isFragmentIncluded(Fragment fragment, Class... fragmentClasses) {
        boolean isIncluded = false;
        if (fragment != null && fragmentClasses != null && fragment instanceof BaseFragment) {
            for (Class clazz : fragmentClasses) {
                if (clazz.isInstance(fragment)) {
                    isIncluded = true;
                    break;
                }
            }
        }
        return isIncluded;
    }

    private Fragment fragmentToResume, previousFragment;

    protected Fragment getFragmentToResume() {
        return fragmentToResume;
    }

    public Fragment getPreviousFragment() {
        return previousFragment;
    }

    protected void resetFragmentToResume() {
        fragmentToResume = null;
    }

    protected Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount() - 1);
    }

    protected int getFragmentCount() {
        return fragmentManager.getBackStackEntryCount();
    }

    private Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? fragmentManager.findFragmentByTag(String.valueOf(index)) : null;
    }

    /**
     * Show fragment BottomTop Animation layout settings
     */
    private boolean showBottomTopAnimation;

    public void showBottomTopAnimation(boolean showBottomTopAnimation) {
        this.showBottomTopAnimation = showBottomTopAnimation;
    }

    /**
     * Request permission settings
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_CHECK_PERMISSION:
                boolean isAllPermissionGranted = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isAllPermissionGranted = false;
                        break;
                    }
                }
                if (isAllPermissionGranted) {
                    onAllPermissionsGranted(permissions);
                } else {
                    onPermissionsGrantedFailed(permissions);
                }
                break;
        }
    }

    /**
     * @param permissions list of permission to check in string array
     *                    (e.g. new String[] {Manifest.permission.xxx})
     */
    public void checkPermissions(String[] permissions) {
        if (SettingsUtil.hasPermission(this, permissions)) {
            onAllPermissionsGranted(permissions);
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE_CHECK_PERMISSION);
        }
    }

    /**
     * Other settings
     */

    public Drawable getIconDrawable(int layoutId) {
        return ViewUtil.getIconDrawable(this, layoutId);
    }

    public void naviToGooglePlayStore() {
        final String packageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    /**
     * Animation settings
     */

    private Animation slideUpAnimation, slideDownAnimation;

    private void initAnimations() {
        if (slideUpAnimation == null) {
            slideUpAnimation = AnimationUtils.loadAnimation(getAppContext(), R.anim.anim_slide_up);
            slideUpAnimation.setDuration(200);
        }
        if (slideDownAnimation == null) {
            slideDownAnimation = AnimationUtils.loadAnimation(getAppContext(), R.anim.anim_slide_down);
            slideDownAnimation.setDuration(200);
        }
    }

    private void startAnimation(View view, Animation animation, int visibility) {
        view.startAnimation(animation);
        view.setVisibility(visibility);
    }

    public void slideUp(View view, int visibility) {
        startAnimation(view, slideUpAnimation, visibility);
    }

    public void slideDown(View view, int visibility) {
        startAnimation(view, slideDownAnimation, visibility);
    }

    /**
     * BaseInteractionListener
     */

    private BaseInteractionListener baseInteractionListener;

    public void setBaseInteractionListener(BaseInteractionListener baseInteractionListener) {
        this.baseInteractionListener = baseInteractionListener;
    }

    /**
     * Override this method to perform last fragment back pressed action
     */
    public void onLastFragmentBackPressed() {
        this.finish();
    }

    /**
     * Override this method to perform the action after all permissions have been granted
     */
    public void onAllPermissionsGranted(String[] permissions) {
    }

    /**
     * Override this method to perform the action if there is permission not granted
     */
    public void onPermissionsGrantedFailed(String[] permissions) {
    }

    /**
     * Override this method to get list of bundle data
     */
    public void onGetBundleData(Bundle bundle) {
    }

    /**
     * Override this method to setup own toolbar menu items
     */
    public void onCreateMenuItem(Menu menu) {
    }

    /**
     * Override this method to setup own toolbar settings
     */
    public void onCreateToolbar(Toolbar toolbar) {
    }

    /**
     * Override this method to setup own progress bar settings
     */
//    public void onCreateProgressbar(ProgressBar progressBar) {
//    }

    /**
     * Override this method to perform button retry action
     */
    public void onClickButtonRetry(Object tag) {
    }

    /**
     * Override this method to perform own toolbar navigation action
     */
    public void onClickToolbarNavigation() {
        finish();
    }

    /**
     * Called before onCreate method to initialize main view id
     */
    public abstract void onSetupView();

    /**
     * Called after onCreate method to setup toolbar layout
     */
    public abstract void onSetupToolbar();

    /**
     * Used to initialize or bind other data to the view
     */
    public abstract void onBindData();

}