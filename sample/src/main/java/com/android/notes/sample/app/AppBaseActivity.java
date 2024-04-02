package com.android.notes.sample.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.notes.sample.R;
import com.android.notes.sample.manager.BaseManager;

import com.google.gson.Gson;
import com.lib.base.BaseActivity;
import com.lib.retrofit.RetrofitError;
import com.lib.utils.LogUtil;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.app.AppBaseApplication;
import com.ywvision.wyvisionhelper.app.AppSharedPref;

import static com.lib.base.BaseConstants.LibReqCode.REQ_CODE_CHECK_PERMISSION_SETTINGS;

@SuppressLint("Registered")
public class AppBaseActivity extends BaseActivity implements CheckAppVersionView {


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected int currentThemeColor = -1;
    protected AppSharedPref sharedPref;
    protected BaseManager baseManager;
    protected BaseManager baseManagerOther;
    protected Gson gson;
    protected AlertDialog updateRequiredDialog;
    protected AlertDialog whatNewDialog;
    protected AlertDialog sessionExpiredDialog;
    protected AlertDialog forceDialog;
    protected AlertDialog accountSuspendedDialog;
    protected AlertDialog emailReportedDialog;
    protected AlertDialog emailUpdatedDialog;
    protected AlertDialog requestPermissionDialog;
    protected AlertDialog announcementDialog;
    protected View dialogRequestPermissionView;

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

    /**
     * Do main stuffs here
     */
    @Override
    public void onBindData() {
        initUpdateRequiredDialog();
        initWhatNewDialog();
        initSessionExpiredDialog();
        initForceDialog();
        initAccountSuspendedDialog();
        initEmailReportedDialog();
        initEmailUpdatedDialog();
        initRequestPermissionDialog();
        baseManagerOther = new BaseManager<CheckAppVersionView>(this, this, true);
    }

    @Override
    public void onInitData() {
        super.onInitData();
        sharedPref = new AppSharedPref(this);
        gson = AppBaseApplication.gson != null ? AppBaseApplication.gson : new Gson();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baseManager != null) {
            baseManager.removeView();
        }
    }

    @Override
    public void onPermissionsGrantedFailed(String[] permissions) {
        super.onPermissionsGrantedFailed(permissions);
        showRequestPermissionDialog(permissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_CHECK_PERMISSION_SETTINGS:
                if (permissions != null) {
                    if (!SettingsUtil.hasPermission(this, permissions)) {
                        if (requestPermissionDialog != null) {
                            requestPermissionDialog.dismiss();
                        }
                        showRequestPermissionDialog(permissions);
                    } else if (fragment != null && fragment instanceof AppBaseFragment) {
                        if (requestPermissionDialog != null) {
                            requestPermissionDialog.dismiss();
                        }
//                        ((AppBaseFragment) fragment).onAllPermissionsGranted(permissions);
                    } else {
                        if (requestPermissionDialog != null) {
                            requestPermissionDialog.dismiss();
                        }
//                        onAllPermissionsGranted(permissions);
                    }
                }
                break;
            default:
                if (getCurrentFragment() != null) {
                    (getCurrentFragment()).onActivityResult(requestCode, resultCode, data);
                }
        }
    }

    /**
     * Toast settings
     */

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

//        CafeBar cafeBar = new CafeBar.Builder(this)
//                .customView(R.layout.view_app_snackbar)
//                .floating(true)
//                .autoDismiss(true)
//                .swipeToDismiss(true)
//                .duration(1800)
//                .build();
//
//        View view = cafeBar.getCafeBarView();
//
//        TextView textViewMessage = view.findViewById(R.id.text_view_message);
//        TextView textViewClose = view.findViewById(R.id.text_view_close);
//        TextView textViewRetry = view.findViewById(R.id.text_view_retry);
//
//        textViewMessage.setText(message);
//        textViewClose.setVisibility(View.INVISIBLE);
//        textViewClose.setMinHeight(1);
//        textViewClose.setMaxHeight(1);
//        textViewClose.setMaxWidth(1);
//
//        textViewRetry.setVisibility(View.INVISIBLE);
//        textViewRetry.setMinHeight(1);
//        textViewRetry.setMaxHeight(1);
//        textViewRetry.setMaxWidth(1);
//
//        View rootView = view.getRootView();
//        if (rootView != null) {
//            rootView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
//            if (rootView instanceof Snackbar.SnackbarLayout) {
//                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) rootView;
//                int childCount = snackbarLayout.getChildCount();
//                for (int i = 0; i < childCount; i++) {
//                    View childView = snackbarLayout.getChildAt(i);
//                    childView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
//                }
//            }
//        }
//
//        cafeBar.show();

    }

    /**
     * default API error
     */

    public void onErrorSessionExpired() {
        showSessionExpiredDialog();
    }

    public void onErrorForceLogout() {
        showFourceLogoutDialog();
    }

    public void onErrorAccountSuspended() {
        showAccountSuspendedDialog();
    }

    public void onErrorEmailReported() {
        showEmailReportedDialog();
    }

    public void onErrorEmailUpdated() {
        showEmailUpdatedDialog();
    }

    public void onErrorAppVersionUpdate(String message) {
        showProgress();
//        baseManagerOther.getAppVersion(new GetAppVersionRequest.Builder()
//                .channel(App.SYSTEM_STATUS)
//                .context(getAppContext())
//                .build());
    }

    @Override
    public void onErrorResponse(RetrofitError error) {
        LogUtil.logError("testingaaaa", error.getMessage());
    }

    public void onSuccessAppVersionUpdate() {
//        baseManagerOther.setAppVersion(getAppVersionResponse.getVersion());
//        baseManagerOther.setVersionUrl(getAppVersionResponse.getVersionUrl());
//        baseManagerOther.setForceUpdate(getAppVersionResponse.isVersionForceUpdate());
//        initUpdateRequiredDialog();
//        showUpdateRequiredDialog();
    }

    /**
     * Double press to exit
     */

    private Handler handler = new Handler();
    private boolean isDoubleBackToExitPressedOnce;

    protected void doublePressToExit(Activity activity) {
//        if (isDoubleBackToExitPressedOnce) {
//            if (activity instanceof AuthActivity) {
//                Intent intent = new Intent(activity, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                activity.finish();
//            } else {
//                finishAffinity();
//            }
//            return;
//        }
//        isDoubleBackToExitPressedOnce = true;
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isDoubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//        displayToast(getString(R.string.__t_global_text_click_back_again_to_exit));
    }

    /**
     * Other settings
     */

    protected Drawable getBackIconDrawable() {
        return getIconDrawable(R.layout.view_menu_back);
    }

    protected void logout() {
//        DbManager.clear(this);
//        sharedPref.clear();
//        Intent intent = new Intent(this, AuthActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finishAffinity();
    }

    /**
     * Dialog settings
     */

    private Fragment fragment;
    private String[] permissions;

    public void showRequestPermissionDialog(Fragment fragment, String[] permissions) {
        this.fragment = fragment;
        showRequestPermissionDialog(permissions);
    }

    protected void showUpdateRequiredDialog() {
        if (!isFinishing()) {
            if (updateRequiredDialog != null && !isDialogShowing()) {
                updateRequiredDialog.show();
            }
        }
    }

    protected void showWhatNewDialog() {
        if (!isFinishing()) {
            if (whatNewDialog != null && !isDialogShowing()) {
                whatNewDialog.show();
            }
        }
    }

    protected void showSessionExpiredDialog() {
        if (!isFinishing()) {
            if (sessionExpiredDialog != null && !isDialogShowing()) {
                sessionExpiredDialog.show();
            }
        }
    }

    protected void showFourceLogoutDialog() {
        if (!isFinishing()) {
            if (forceDialog != null && !isDialogShowing()) {
                forceDialog.show();
            }
        }
    }

    protected void showAccountSuspendedDialog() {
        if (!isFinishing()) {
            if (accountSuspendedDialog != null && !isDialogShowing()) {
                accountSuspendedDialog.show();
            }
        }
    }

    protected void showEmailReportedDialog() {
        if (!isFinishing()) {
            if (emailReportedDialog != null && !isDialogShowing()) {
                emailReportedDialog.show();
            }
        }
    }

    protected void showEmailUpdatedDialog() {
        if (!isFinishing()) {
            if (emailUpdatedDialog != null && !isDialogShowing()) {
                emailUpdatedDialog.show();
            }
        }
    }

    protected void showRequestPermissionDialog(String[] permissions) {
        this.permissions = permissions;
        if (dialogRequestPermissionView != null && requestPermissionDialog != null && !isDialogShowing()) {
            TextView textViewMessage = dialogRequestPermissionView.findViewById(R.id.text_view_message);
            textViewMessage.setText(getRequestPermissionMessage(permissions));
            requestPermissionDialog.setView(dialogRequestPermissionView);
            requestPermissionDialog.show();
        }
    }

    private String getRequestPermissionMessage(String[] permissions) {
        String message = "";
        boolean permissionCamera = false;
        boolean permissionStorage = false;
        boolean permissionContacts = false;
        boolean permissionFineLocation = false;
        boolean permissionCoarseLocation = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        permissionCamera = true;
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        permissionStorage = true;
                        break;
                    case Manifest.permission.READ_PHONE_STATE:
                        permissionContacts = true;
                        break;
                    case Manifest.permission.ACCESS_FINE_LOCATION:
                        permissionFineLocation = true;
                        break;
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        permissionCoarseLocation = true;
                        break;
                }
            }
        }
        String appName = getString(R.string.app_name);
        if (permissionCamera && permissionStorage) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_camera_storage, appName);
        } else if (permissionCamera) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_camera, appName);
        } else if (permissionStorage) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_storage, appName);
        } else if (permissionContacts) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_contacts, appName);
        } else if (permissionFineLocation) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_fine_location, appName);
        } else if (permissionCoarseLocation) {
            message = getString(R.string.__t_application_alert_request_permission_message_access_fine_location, appName);
        }
        return message;
    }


    public AlertDialog createOneButtonDialog(String title, String message,
                                             String positiveButton,
                                             View.OnClickListener positiveOnClickListener) {
        return createOneButtonDialog(title, message, positiveButton, "", positiveOnClickListener, null);
    }

    public AlertDialog createOneButtonDialog(String title, String message,
                                             String positiveButton,
                                             String negativeButton,
                                             View.OnClickListener positiveOnClickListener,
                                             View.OnClickListener negativeOnClickListener) {
        return createOneButtonDialog(title, message, positiveButton, negativeButton, positiveOnClickListener, negativeOnClickListener, R.drawable.ic_dialog_bg_normal);

    }

    public AlertDialog createOneButtonDialog(String title, String message,
                                             String positiveButton,
                                             String negativeButton,
                                             View.OnClickListener positiveOnClickListener,
                                             View.OnClickListener negativeOnClickListener,
                                             int bg) {

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_two_buttons, null);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setView(dialogView);

        AppCompatTextView textViewTitle = dialogView.findViewById(R.id.text_view_title);
        TextView textViewMessage = dialogView.findViewById(R.id.text_view_message);
        AppCompatTextView textViewButtonOne = dialogView.findViewById(R.id.text_view_button_one);
        AppCompatTextView textViewButtonTwo = dialogView.findViewById(R.id.text_view_button_two);
        RelativeLayout relativeLayoutBg = dialogView.findViewById(R.id.relative_layout_bg);
        relativeLayoutBg.setBackgroundResource(bg);
        if (!ValidUtil.isEmpty(title)) {
            textViewTitle.setVisibility(View.VISIBLE);
        } else {
            textViewTitle.setVisibility(View.INVISIBLE);
        }
        textViewTitle.setText(title);
        textViewMessage.setText(message);
        if (ValidUtil.isEmpty(negativeButton)) {
            textViewButtonOne.setVisibility(View.GONE);
        } else {
            textViewButtonOne.setVisibility(View.VISIBLE);
            textViewButtonOne.setText(negativeButton);
            textViewButtonOne.setOnClickListener(negativeOnClickListener);
        }
        if (ValidUtil.isEmpty(positiveButton)) {
            textViewButtonTwo.setVisibility(View.GONE);
        } else {
            textViewButtonTwo.setVisibility(View.VISIBLE);
            textViewButtonTwo.setText(positiveButton);
            textViewButtonTwo.setOnClickListener(positiveOnClickListener);
        }
//        if (!ValidUtil.isEmpty(skinBean)) {
//            SkinSelectorUtils.setTextViewColor(textViewButtonTwo, skinBean.getColors().getTc1().getColor());
//        }
        try {
//            dialogView.findViewById(R.id.linear_layout_title).setBackgroundColor(ContextCompat.getColor(this, getCurrentThemeColor()));
            textViewButtonTwo.setTextColor(ContextCompat.getColor(this, getCurrentThemeColor()));
            textViewButtonOne.setTextColor(ContextCompat.getColor(this, getCurrentThemeColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;

    }

    public void initUpdateRequiredDialog() {
//        baseManager = new BaseManager(getAppContext());
//        AndroidModel androidModel = baseManager.getAndroidVersionModel();
//        if (!ValidUtil.isEmpty(androidModel)) {
//            updateRequiredDialog = createOneButtonDialog(
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
//                                naviToGooglePlayStore();
//                                finish();
//                            } else {
//                                downApkCheckPermission();
//                                showProgress();
//                                if (androidModel.getIsConstraint() > 0) {
//                                    showDownloadingText(getString(R.string.__t_application_alert_new_version_available_downloading_latest_apk));
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

    private void initWhatNewDialog() {
        baseManager = new BaseManager(getAppContext());
//        AndroidModel androidModel = baseManager.getAndroidVersionModel();
//        if (!ValidUtil.isEmpty(androidModel)) {
//            whatNewDialog = createOneButtonDialog(
//                    getString(R.string.__t_application_alert_what_new_title),
//                    androidModel.getContent(),
//                    getString(R.string.__t_application_alert_session_expired_ok_button),
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //vpnChecking();
//                            whatNewDialog.dismiss();
//                        }
//                    });
//            whatNewDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    dialog.dismiss();
//                }
//            });
//            whatNewDialog.setCancelable(true);
//            whatNewDialog.setCanceledOnTouchOutside(true);
//        }
    }

    private void initSessionExpiredDialog() {
//        sessionExpiredDialog = createOneButtonDialog(
//                getString(R.string.__t_application_alert_session_expired_title),
//                getString(R.string.__t_application_alert_session_expired_message),
//                getString(R.string.__t_application_alert_session_expired_ok_button),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        sessionExpiredDialog.dismiss();
//                        hideProgress();
//                        logout();
//                    }
//                });
//        sessionExpiredDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//                hideProgress();
//                logout();
//            }
//        });
//        sessionExpiredDialog.setCancelable(false);
//        sessionExpiredDialog.setCanceledOnTouchOutside(false);
    }

    private void initForceDialog() {
//        forceDialog = createOneButtonDialog(
//                getString(R.string.__t_application_alert_force_logout_title),
//                getString(R.string.__t_application_alert_force_logout_message),
//                getString(R.string.__t_application_alert_session_expired_ok_button),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        forceDialog.dismiss();
//                    }
//                });
//        forceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        forceDialog.setCancelable(false);
//        forceDialog.setCanceledOnTouchOutside(false);
    }

    private void initAccountSuspendedDialog() {
//        accountSuspendedDialog = createOneButtonDialog(
//                getString(R.string.__t_application_alert_account_suspended_title),
//                getString(R.string.__t_application_alert_account_suspended_message),
//                getString(R.string.__t_application_alert_account_suspended_ok_button),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        accountSuspendedDialog.dismiss();
//                    }
//                });
//        accountSuspendedDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        accountSuspendedDialog.setCancelable(false);
//        accountSuspendedDialog.setCanceledOnTouchOutside(false);
    }

    private void initEmailReportedDialog() {
//        emailReportedDialog = createOneButtonDialog(
//                getString(R.string.__t_application_alert_email_reported_title),
//                getString(R.string.__t_application_alert_email_reported_message),
//                getString(R.string.__t_application_alert_email_reported_ok_button),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        emailReportedDialog.dismiss();
//                    }
//                });
//        emailReportedDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        emailReportedDialog.setCancelable(false);
//        emailReportedDialog.setCanceledOnTouchOutside(false);
    }

    private void initEmailUpdatedDialog() {
//        emailUpdatedDialog = createOneButtonDialog(
//                getString(R.string.__t_application_alert_email_updated_title),
//                getString(R.string.__t_application_alert_email_updated_message),
//                getString(R.string.__t_application_alert_email_updated_ok_button),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        emailUpdatedDialog.dismiss();
//                    }
//                });
//        emailUpdatedDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        emailUpdatedDialog.setCancelable(false);
//        emailUpdatedDialog.setCanceledOnTouchOutside(false);
    }

    private void initRequestPermissionDialog() {

//        dialogRequestPermissionView = LayoutInflater.from(this).inflate(R.layout.dialog_request_permission, null);
//        requestPermissionDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
//        if (requestPermissionDialog.getWindow() != null) {
//            requestPermissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        requestPermissionDialog.setView(dialogRequestPermissionView);
//
//        TextView textViewNotNow = dialogRequestPermissionView.findViewById(R.id.text_view_not_now);
//        textViewNotNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestPermissionDialog.dismiss();
//            }
//        });
//
//        TextView textViewSettings = dialogRequestPermissionView.findViewById(R.id.text_view_settings);
//        textViewSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToSettings();
//            }
//        });
//
//        try {
//            dialogRequestPermissionView.findViewById(R.id.frame_layout_top).setBackgroundColor(ContextCompat.getColor(this, getCurrentThemeColor()));
//            textViewNotNow.setTextColor(ContextCompat.getColor(this, getCurrentThemeColor()));
//            textViewSettings.setTextColor(ContextCompat.getColor(this, getCurrentThemeColor()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public boolean isDialogShowing() {
        boolean isUpdateRequiredDialogShowing = updateRequiredDialog != null && updateRequiredDialog.isShowing();
        boolean isSessionExpiredDialogShowing = sessionExpiredDialog != null && sessionExpiredDialog.isShowing();
        boolean isForceLogoutDialogShowing = forceDialog != null && forceDialog.isShowing();
        boolean isAccountSuspendedDialogShowing = accountSuspendedDialog != null && accountSuspendedDialog.isShowing();
        boolean isEmailReportedDialogShowing = emailReportedDialog != null && emailReportedDialog.isShowing();
        boolean isEmailUpdatedDialogShowing = emailUpdatedDialog != null && emailUpdatedDialog.isShowing();
        boolean isRequestPermissionDialogShowing = requestPermissionDialog != null && requestPermissionDialog.isShowing();
        return isUpdateRequiredDialogShowing
                || isSessionExpiredDialogShowing
                || isForceLogoutDialogShowing
                || isAccountSuspendedDialogShowing
                || isEmailReportedDialogShowing
                || isEmailUpdatedDialogShowing
                || isRequestPermissionDialogShowing;
    }

    private void navigateToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQ_CODE_CHECK_PERMISSION_SETTINGS);
    }

    private View.OnClickListener permissionPositiveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navigateToSettings();
        }
    };


    public void setCurrentThemeColor(int currentThemeColor) {
        this.currentThemeColor = currentThemeColor;
    }

    public int getCurrentThemeColor() {
        return R.color.color_app_main;
//        return currentThemeColor != -1 ? currentThemeColor : R.color.color_app_shadow;
    }

    public void changeLanguage(String languageCode) {
//        SettingsUtil.changeLanguage(getBaseContext(), languageCode);
//        Intent refresh = new Intent(this, MainActivity.class);
//        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(refresh);
//        finish();
    }

    public void setStatusBarColor(int statusBarColor) {
        Window w = getWindow();
        w.setStatusBarColor(ContextCompat.getColor(getAppContext(), statusBarColor));
    }

    public void statusBarControl(boolean isTransparent) {
        Window window = getWindow();
        if (!isTransparent) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void downApkCheckPermission() {

    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    public void switchLoginActivity() {
//        Intent intent;
//        intent = new Intent(this, AuthActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }
}
