package com.lib.base;

import com.lib.retrofit.RetrofitError;

public interface BaseView {

    void onErrorSessionExpired();

    void onErrorForceLogout();

    void onErrorAccountSuspended();

    void onErrorEmailReported();

    void onErrorEmailUpdated();

    void onErrorAppVersionUpdate(String message);

    void onErrorResponse(RetrofitError error);

}
