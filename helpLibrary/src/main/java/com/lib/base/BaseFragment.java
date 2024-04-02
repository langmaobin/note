package com.lib.base;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.lib.R;
import com.lib.receiver.NetworkReceiver;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ViewUtil;
import com.lib.widget.CustomSnackbar;

import java.util.Stack;

import butterknife.ButterKnife;

import static com.lib.base.BaseConstants.LibReqCode.REQ_CODE_CHECK_PERMISSION;

public abstract class BaseFragment extends Fragment implements BaseInteractionListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onSetupView();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);
        onInitData();
        initialize(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && (getFragmentToResume() == null || getFragmentToResume().getClass().isInstance(this))) {
            onLoadData();
            resetFragmentToResume();
            setBaseInteractionListener();
        }
    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(null);
        unregisterNetworkReceiver();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(null);
        unregisterNetworkReceiver();
        super.onDestroy();
    }

    public void onInitData() {
        registerNetworkReceiver();
    }

    private void initialize(View view) {
        getBundleExtras();
        setHasOptionsMenu(showMenu);
        setupToolbar(view);
        setupProgressBar(view);
        setupExceptionLayout(view);
        onBindView(view);
        onBindData();
    }

    private void getBundleExtras() {
        if (getArguments() != null) {
            onGetBundleData(getArguments());
        }
    }

    /**
     * Layout view settings
     */

    private int layoutId;

    public void setLayoutView(int layoutId) {
        this.layoutId = layoutId;
    }

    private int frameLayoutId;
    private FragmentManager fragmentManager;

    public void setFrameLayoutView(int frameLayoutId) {
        this.frameLayoutId = frameLayoutId;
        this.fragmentManager = getChildFragmentManager();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (showMenu) {
            menu.clear();
            inflater.inflate(menuResId, menu);
            onCreateMenuItem(menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Toolbar settings
     */

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setupSuppportActionBar();
            // Must place after set support action bar
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickToolbarNavigation();
                }
            });
            setToolbarTitle("");
            setToolbarNavigationIcon(null);
            onSetupToolbar();
            onCreateToolbar(toolbar);
        }
        setBaseInteractionListener();
        getActivity().invalidateOptionsMenu();
    }

    protected void setupSuppportActionBar() {
        ((BaseActivity) getActivity()).setupSupportActionBar(toolbar);
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            TextView tvToolbarTitle = toolbar.findViewById(R.id.text_view_toolbar_title);
            if (tvToolbarTitle != null) {
                tvToolbarTitle.setText(title);
            }
        }
    }

    public void setToolbarTitleColor(int resId) {
        if (toolbar != null) {
            TextView tvToolbarTitle = toolbar.findViewById(R.id.text_view_toolbar_title);
            if (tvToolbarTitle != null) {
                tvToolbarTitle.setTextColor(ContextCompat.getColor(getContext(), resId));
            }
            Drawable navigationIcon = toolbar.getNavigationIcon();
            if (navigationIcon != null) {
                navigationIcon.setColorFilter(ContextCompat.getColor(getContext(), resId), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public void setToolbarBackgroundColor(int resId) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setToolbarNavigationIcon(Drawable icon) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(icon);
        }
    }

    /**
     * Progress bar settings
     */

    private View progressbarLayout;
    private View relativeBg;
    private AppCompatImageView imgProgress;
//    private AVLoadingIndicatorView avLoadingIndicatorView;

    private void setupProgressBar(View view) {
        progressbarLayout = view.findViewById(R.id.rl_progressbar);
        relativeBg = view.findViewById(R.id.relative_bg);
        imgProgress = view.findViewById(R.id.img_progress);
//        avLoadingIndicatorView = view.findViewById(R.id.loading_indicator_view);
//        ProgressBar progressBar = view.findViewById(R.id.pb_loading);
        initProgressBar();
//        if (progressBar != null) {
//            onCreateProgressbar(progressBar, avLoadingIndicatorView);
//        }
        setRadiusAndColor(relativeBg, "000000", 10);
        setAlpha(relativeBg, "0.15");
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

    private void setupExceptionLayout(View view) {
        exceptionContainer = view.findViewById(R.id.ll_exception_container);
        ivExceptionIcon = view.findViewById(R.id.iv_exception_icon);
        tvExceptionText = view.findViewById(R.id.tv_exception_text);
        tvExceptionButton = view.findViewById(R.id.tv_exception_button);
        onCreateExceptionLayout(ivExceptionIcon, tvExceptionText, tvExceptionButton);
        if (tvExceptionButton != null) {
            tvExceptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideExceptionLayout();
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
        showExceptionLayout(-1, R.string.__t_global_text_no_result_shows, true);
    }

    protected void showEmptyLayout(String message) {
        try {
            showExceptionLayout(null, message, R.string.__t_global_text_tap_to_retry, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            showExceptionLayout(ViewUtil.getDrawable(getContext(), iconResId), getString(msgResId), btnMsgResId, hideButton);
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
     * Show fragment BottomTop Animation layout settings
     */
    private boolean ShowFragmentBottomTopAnimation;

    public void showFragmentBottomTopAnimation(boolean showFragmentBottomTopAnimation) {
        this.ShowFragmentBottomTopAnimation = showFragmentBottomTopAnimation;
    }

    /**
     * Snackbar layout settings
     */

    private View layoutContainerView;

    public void setLayoutContainerView(View layoutContainerView) {
        this.layoutContainerView = layoutContainerView;
    }

    private CustomSnackbar customSnackbar;

    public void hideSnackbar() {
        if (customSnackbar != null) {
            customSnackbar.hideSnackbar();
        }
    }

//    public void showSnackbar(String messageText) {
//        if (isVisible()) {
//            customSnackbar = ViewUtil.buildSnackbar(layoutContainerView, messageText);
//            customSnackbar.showSnackbar();
//        }
//    }
//
//    public void showSnackbarWithAction(String messageText, String actionText, View.OnClickListener onActionClickListener) {
//        if (isVisible()) {
//            customSnackbar = ViewUtil.buildSnackbarWithAction(layoutContainerView, messageText, actionText, onActionClickListener);
//            customSnackbar.showSnackbar();
//        }
//    }
//
//    public void showSnackbarWithActionColor(View layoutContainerView,
//                                            String messageText, String actionText, int actionTextColor,
//                                            View.OnClickListener onActionClickListener) {
//        if (isVisible()) {
//            customSnackbar = ViewUtil.buildSnackbarWithActionColor(layoutContainerView, messageText, actionText, actionTextColor, onActionClickListener);
//            customSnackbar.showSnackbar();
//        }
//    }
//
//    public void showSnackbarWithAction(View layoutContainerView,
//                                       String messageText, String actionText,
//                                       View.OnClickListener onActionClickListener) {
//        if (isVisible()) {
//            this.layoutContainerView = layoutContainerView;
//            showSnackbarWithAction(messageText, actionText, onActionClickListener);
//        }
//    }

    /**
     * Network broadcast receiver settings
     */

    private boolean enabledNetworkReceiver;

    public void setEnabledNetworkReceiver(boolean enabledNetworkReceiver) {
        this.enabledNetworkReceiver = enabledNetworkReceiver;
    }

    private void registerNetworkReceiver() {
        if (enabledNetworkReceiver) {
            networkReceiver.register(getAppContext(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void unregisterNetworkReceiver() {
        if (enabledNetworkReceiver) {
            networkReceiver.unregister(getAppContext());
        }
    }

    private NetworkReceiver networkReceiver = new NetworkReceiver() {

        @Override
        public void connectToNetwork() {
            onNetworkConnected();
        }

        @Override
        public void disconnectFromNetwork() {
            onNetworkDisconnected();
        }

    };

    /**
     * Other usage inherits from activity
     */

    public Context getAppContext() {
        return getActivity() instanceof BaseActivity
                ? ((BaseActivity) getActivity()).getAppContext()
                : getContext();
    }

    public void displayToast(String message) {
        if (isVisible() && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).displayToast(message);
        }
    }

    public void hideKeyboard() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideKeyboard();
        }
    }

    public void showKeyboard() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showKeyboard();
        }
    }

    protected void setBaseInteractionListener() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setBaseInteractionListener(this);
        }
    }

    private Fragment getFragmentToResume() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getFragmentToResume();
        }
        return null;
    }

    protected void resetFragmentToResume() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).resetFragmentToResume();
        }
    }

    public void backToPreviousFragment() {
        backToPreviousFragment(null);
    }

    public void backToPreviousFragment(Bundle args) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).onBackPressed(args);
        }
    }

    public Stack<Fragment> getFragmentStack() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getFragmentStack();
        }
        return null;
    }

    public void clearFragmentStack() {
        clearFragmentStack(null, null);
    }

    public void clearFragmentStack(Class fragmentClass, Bundle args) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearFragmentStack(args, fragmentClass);
        }
    }

    public void clearFragmentStack2(Bundle args, Class... fragmentClasses) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearFragmentStack(args, fragmentClasses);
        }
    }

    public void clearFragmentStackIncluded(Class... fragmentClasses) {
        clearFragmentStackIncluded(null, fragmentClasses);
    }

    public void clearFragmentStackIncluded(Bundle bundle, Class... fragmentClasses) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearFragmentStackIncluded(bundle, fragmentClasses);
        }
    }

    public void switchFragment(Fragment fragment) {
        switchFragment(true, fragment);
    }

    public void switchFragment(boolean remainInstances, Fragment fragment) {
        // hideSnackbar();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).switchFragment(remainInstances, fragment, false);
        }
    }

    public void switchFragmentWithSameClazz(Fragment fragment) {
        // hideSnackbar();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).switchFragmentWithSameClazz(true, fragment);
        }
    }

    public void switchInnerFragment(Fragment fragment) {
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(frameLayoutId, fragment)
                    .commitAllowingStateLoss();
        }
    }

    public Fragment getPreviousFragment() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getPreviousFragment();
        }
        return null;
    }

    public Fragment getCurrentFragment() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getCurrentFragment();
        }
        return null;
    }

    public void hideActivityProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgress();
        }
    }

    public void showActivityProgress() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgress();
        }
    }

    /**
     * Request permission settings
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_CHECK_PERMISSION: {
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
            }
        }
    }

    /**
     * @param permissions list of permission to check in string array
     *                    (e.g. new String[] {Manifest.permission.xxx})
     */
    public void checkPermissions(String[] permissions) {
        if (SettingsUtil.hasPermission(getAppContext(), permissions)) {
            onAllPermissionsGranted(permissions);
        } else {
            requestPermissions(permissions, REQ_CODE_CHECK_PERMISSION);
        }
    }

    /**
     * Other settings
     */

    protected Drawable getIconDrawable(int layoutId) {
        return ViewUtil.getIconDrawable(getContext(), layoutId);
    }

    /**
     * Handle fragment onBackPressed
     */

    private boolean isBackAllowed = true;

    public boolean isBackAllowed() {
        return isBackAllowed;
    }

    public void setBackAllowed(boolean backAllowed) {
        isBackAllowed = backAllowed;
    }

    /**
     * Override this method to bind more views on this fragment
     */
    public void onBindView(View view) {
    }

    /**
     * Override this method to load the data after the fragment become visible again
     * (Ignore this if remainInstances is set to false during switchFragment)
     */
    public void onLoadData() {
        ShowFragmentBottomTopAnimation = false;
    }

    /**
     * Override this method to update fragment with bundle data pass in
     */
    public void onUpdateFragment(Bundle args) {
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
//    public void onCreateProgressbar(ProgressBar progressBar, AVLoadingIndicatorView avLoadingIndicatorView) {
//    }

    /**
     * Override this method to setup own exception layout settings
     */
    public void onCreateExceptionLayout(ImageView ivExceptionIcon, TextView tvExceptionText, TextView tvExceptionButton) {
    }

    /**
     * Override this method to perform action after network connected
     */
    public void onNetworkConnected() {
    }

    /**
     * Override this method to perform action after network disconnected
     */
    public void onNetworkDisconnected() {
    }

    /**
     * Override this method to perform button retry action
     */
    public void onClickButtonRetry(Object tag) {
    }

    /**
     * Override this method to perform own toolbar navigation action
     */
    @Override
    public void onClickToolbarNavigation() {
    }

    /**
     * Override this method to perform back pressed function
     */
    @Override
    public void onBackPressed() {
        backToPreviousFragment();
    }


    /**
     * Override this method to show animation bottom to top
     */
    public boolean isShowFragmentBottomTopAnimation() {
        return ShowFragmentBottomTopAnimation;
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