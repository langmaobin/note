package com.ywvision.wyvisionhelper.api;

import com.lib.retrofit.RetrofitError;

public class ApiVerification {


    //authentication verify login
    public static boolean isForgotPasswordEmailError(RetrofitError error) {
        String url = error.getUrl();
        String statusCode = error.getStatusCode();
//        if (url != null && url.equals(ApiMethod.API_FORGOT_PASSWORD)) {
////            switch (statusCode) {
////                case ApiError.STATUS_ERR_FORGOT_PASSWORD_MISSING_EMAIL:
////                case ApiError.STATUS_ERR_FORGOT_PASSWORD_EMAIL_NOT_FOUND:
////                    return true;
////            }
//        }
        return false;
    }
}
