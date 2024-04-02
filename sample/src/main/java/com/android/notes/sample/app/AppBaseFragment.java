package com.android.notes.sample.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.lib.base.BaseFragment;
import com.lib.utils.LogUtil;
import com.lib.utils.ValidUtil;
import com.lib.widget.errorlabellayout.ErrorLabelLayout;
import com.android.notes.sample.R;
import com.android.notes.sample.BuildConfig;
import com.android.notes.sample.manager.BaseManager;
import com.ywvision.wyvisionhelper.app.AppBaseApplication;

public class AppBaseFragment extends BaseFragment {
    protected boolean isLoading;
    protected BaseManager baseManager;
    protected Gson gson;
    protected AlertDialog updateRequiredDialog;

    /**
     * Setup layout view
     * <p>
     * (Mandatory) void setLayoutView(int layoutId)
     * -> For setup main layout by passing layout id.
     * <p>
     * (Optional) void setFrameLayoutView(int frameLayoutId)
     * -> For inflate fragment by passing frame layout id.
     * <p>
     * (Optional) void setHasOptionsMenu(boolean showMenu, int menuResId)
     * -> For setup menu items by passing true (default is false) with menu resource id.
     * <p>
     * (Optional) void setIsSetupToolbarEnabled(boolean isSetupToolbarEnabled)
     * -> For setup toolbar (default is false)
     */
    @Override
    public void onSetupView() {
    }

    /**
     * Setup toolbar
     * <p>
     * (Optional) void setToolbarTitle(String title)
     * -> For setting toolbar title.
     * <p>
     * (Optional) void setToolbarNavigationIcon(Drawable icon)
     * -> For setting toolbar navigation icon.
     */
    @Override
    public void onSetupToolbar() {
    }

    @Override
    public void onCreateToolbar(Toolbar toolbar) {
        super.onCreateToolbar(toolbar);
//        if (!isToolbarTransparentType(toolbar)) {
//            setToolbarBackgroundColor(getCurrentThemeColor());
//        }
    }

    @Override
    public void onClickToolbarNavigation() {
        onBackPressed();
    }

    protected Drawable getBackIconDrawable() {
        return getIconDrawable(R.layout.view_menu_back);
    }

    private boolean isToolbarTransparentType(Toolbar toolbar) {
        try {
            return toolbar.getTag().toString().equals("toolbar_transparent");
        } catch (Exception e) {
        }
        return false;
    }

//    @Override
//    public void onCreateProgressbar(ProgressBar progressBar, AVLoadingIndicatorView avLoadingIndicatorView) {
//        super.onCreateProgressbar(progressBar, avLoadingIndicatorView);
//        int progressbarColor;
//        progressbarColor = getCurrentThemeColor();
//        if (progressBar != null) {
//            progressBar.getIndeterminateDrawable().setColorFilter(
//                    ContextCompat.getColor(getContext(), progressbarColor),
//                    android.graphics.PorterDuff.Mode.SRC_IN
//            );
//        }
//        if (avLoadingIndicatorView != null) {
//            avLoadingIndicatorView.setIndicatorColor(ContextCompat.getColor(getContext(), progressbarColor));
//        }
//    }

    @Override
    public void onCreateExceptionLayout(ImageView ivExceptionIcon, TextView tvExceptionText, TextView tvExceptionButton) {
        super.onCreateExceptionLayout(ivExceptionIcon, tvExceptionText, tvExceptionButton);
        if (tvExceptionButton != null) {
            tvExceptionButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ripple_btn_white));
        }
    }


    /**
     * Do main stuffs here
     */
    @Override
    public void onBindData() {
        if (!ValidUtil.isEmpty(getCurrentFragment())) {
            LogUtil.logError("MyFragment", getCurrentFragment().getClass().getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (baseManager != null) {
            baseManager.unregisterAllSubscriptions();
        }
        if (baseManager != null) {
            baseManager.removeView();
        }
    }

    @Override
    public void onInitData() {
        super.onInitData();
        gson = AppBaseApplication.gson != null ? AppBaseApplication.gson : new Gson();
    }

    @Override
    public void onPermissionsGrantedFailed(String[] permissions) {
        super.onPermissionsGrantedFailed(permissions);
        if (getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).showRequestPermissionDialog(this, permissions);
        }
    }

    protected int getCurrentThemeColor() {
        if (getActivity() instanceof AppBaseActivity) {
            return ((AppBaseActivity) getActivity()).getCurrentThemeColor();
        }
        return -1;
    }

    public void onErrorSessionExpired() {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorSessionExpired();
        }
    }

    public void onErrorForceLogout() {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorForceLogout();
        }
    }

    public void onErrorAccountSuspended() {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorAccountSuspended();
        }
    }

    public void onErrorEmailReported() {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorEmailReported();
        }
    }

    public void onErrorEmailUpdated() {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorEmailUpdated();
        }
    }

    public void onErrorAppVersionUpdate(String message) {
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).onErrorAppVersionUpdate(message);
        }
    }
//
//    public void onSuccessAppVersionUpdate(GetAppVersionResponse response) {
//        if (isVisible() && getActivity() instanceof AppBaseActivity) {
//            ((AppBaseActivity) getActivity()).onSuccessAppVersionUpdate(response);
//        }
//    }

    protected void showErrorField(EditText editText, ErrorLabelLayout errorLabelLayout, String errorMessage) {
        if (editText.isFocusable()) {
            showKeyboard();
            editText.requestFocus();
        }
        errorLabelLayout.setError(errorMessage);
    }


    /**
     * Dialog settings
     */

    protected AlertDialog twoButtonsDialog;

    protected void showTwoButtonsDialog(String title, String message,
                                        String positiveButton, String negativeButton,
                                        View.OnClickListener positiveOnClickListener,
                                        View.OnClickListener negativeOnClickListener,
                                        boolean isCancel) {

        if (twoButtonsDialog != null && twoButtonsDialog.isShowing()) {
            return;
        }
        if (isVisible() && getActivity() instanceof AppBaseActivity) {
            twoButtonsDialog = ((AppBaseActivity) getActivity()).createOneButtonDialog(
                    title, message,
                    positiveButton, negativeButton,
                    positiveOnClickListener, negativeOnClickListener);

            twoButtonsDialog.show();
            twoButtonsDialog.setCancelable(isCancel);
            twoButtonsDialog.setCanceledOnTouchOutside(isCancel);
        }
    }

    public void showTwoButtonsDialog(String title, String message,
                                     String positiveButton, String negativeButton,
                                     View.OnClickListener positiveOnClickListener,
                                     View.OnClickListener negativeOnClickListener) {

        showTwoButtonsDialog(title,
                message,
                positiveButton,
                negativeButton,
                positiveOnClickListener,
                negativeOnClickListener,
                true);
    }

    public void showOneButtonsDialog(String message) {
        showTwoButtonsDialog("",
                message,
                "",
                getString(R.string.__t_global_text_ok),
                null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissTwoButtonsDialog();
                    }
                });

    }

    public void showOneButtonsDialog(String message, View.OnClickListener positiveOnClickListener) {
        showTwoButtonsDialog("",
                message,
                "",
                getString(R.string.__t_global_text_ok),
                null,
                positiveOnClickListener);
    }

    public void showOneButtonsDialog(String message, View.OnClickListener positiveOnClickListener, boolean isCancel) {
        showTwoButtonsDialog("",
                message,
                "",
                getString(R.string.__t_global_text_ok),
                null,
                positiveOnClickListener,
                isCancel);
    }


    protected void dismissTwoButtonsDialog() {
        if (twoButtonsDialog != null && twoButtonsDialog.isShowing()) {
            twoButtonsDialog.dismiss();
        }
    }

    /**
     * change language =
     * zh-chines simplified,
     * tw-chines tantalisation ,
     * en-english,
     * tm-tamil
     */

    public void changeLanguage(String languageCode) {
        if (getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).changeLanguage(languageCode);
        }
    }

    protected String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    protected String getVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    public void initUpdateRequiredDialog() {
        baseManager = new BaseManager(getAppContext());
//        AndroidModel androidModel = baseManager.getAndroidVersionModel();
//        if (!ValidUtil.isEmpty(androidModel)) {
//            updateRequiredDialog = ((AppBaseActivity) getActivity()).createOneButtonDialog(
//                    androidModel.getTitle(),
//                    androidModel.getTitle(),
//                    getString(R.string.__t_application_alert_new_version_available_download_button),
//                    androidModel.getIsConstraint() <= 0 ? getString(R.string.__t_application_alert_new_version_available_Skip_update_button) : "",
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            updateRequiredDialog.dismiss();
//                            if (SYSTEM_STATUS.equalsIgnoreCase("android-googleplay")
//                            ) {
//                                ((AppBaseActivity) getActivity()).naviToGooglePlayStore();
//                                ((AppBaseActivity) getActivity()).finish();
//                            } else {
//                                downApkCheckPermission();
//                                showProgress();
//                                if (androidModel.getIsConstraint() > 0) {
//                                    ((AppBaseActivity) getActivity()).showDownloadingText(getString(R.string.__t_application_alert_new_version_available_downloading_latest_apk));
//                                }
//                            }
//                        }
//                    }, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            hideProgress();
//                            updateRequiredDialog.dismiss();
//                        }
//                    });
//            updateRequiredDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    dialog.dismiss();
//                }
//            });
//            updateRequiredDialog.setCancelable(androidModel.getIsConstraint() <= 0);
//            updateRequiredDialog.setCanceledOnTouchOutside(androidModel.getIsConstraint() <= 0);
//        }
    }

    protected void showUpdateRequiredDialog() {
        if (updateRequiredDialog != null && !((AppBaseActivity) getActivity()).isDialogShowing()) {
            updateRequiredDialog.show();
        }
    }

    public void downApkCheckPermission() {

    }

    public void logout() {
        if (getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).logout();
        }
    }
    public void switchLoginActivity() {
        if (getActivity() instanceof AppBaseActivity) {
            ((AppBaseActivity) getActivity()).switchLoginActivity();
        }
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
//        showKeyboard();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
