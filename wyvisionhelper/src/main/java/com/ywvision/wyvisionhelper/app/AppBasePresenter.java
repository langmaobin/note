package com.ywvision.wyvisionhelper.app;

import android.content.Context;

import com.google.gson.Gson;
import com.lib.base.BaseApplication;
import com.lib.base.BasePresenter;
import com.lib.base.BaseView;
import com.lib.retrofit.RetrofitError;
import com.lib.utils.LibUtil;
import com.lib.utils.LogUtil;
import com.ywvision.wyvisionhelper.R;
import com.ywvision.wyvisionhelper.api.ApiError;
import com.ywvision.wyvisionhelper.api.ApiTask;
import com.ywvision.wyvisionhelper.utils.CryptoUtil;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_SUFFIX;
import static com.lib.base.BaseConstants.Tag.TAG_BODY;
import static com.lib.base.BaseConstants.Tag.TAG_ERROR;

public abstract class AppBasePresenter<T extends BaseView> extends BasePresenter {

    protected T view;
    protected Gson gson;
    protected AppSharedPref sharedPref;
    protected Disposable disposable;

    public AppBasePresenter(Context context, T view) {
        super(context);
        this.view = view;
        this.gson = BaseApplication.gson != null ? BaseApplication.gson : new Gson();
        this.sharedPref = new AppSharedPref(context);
    }

    public <Q extends AppBaseRequest> void callApi(final String versionCode,
                                                   final String channel,
                                                   final String languageCode,
                                                   final String token,
                                                   final String urlEndPoint,
                                                   final Q request,
                                                   final Class responseType,
                                                   final Object... params) {
        String publicKey = request.putRequestPublicKey();
        Map<String, Object> requestMap = request.getRequestMap();


        String value = CryptoUtil.encrypt(request.getGson().toJson(requestMap), publicKey);
        String finalPublicKey = String.valueOf(publicKey);
        LogUtil.logInfo(TAG_BODY, value.replaceAll("\\n", ""));
        LogUtil.logInfo("publicKey", finalPublicKey);
        LogUtil.logInfo("json", request.getGson().toJson(requestMap));
        Map<String, Object> requestMapFinal = new TreeMap<>();
        requestMapFinal.put("post-data", value.trim().replaceAll("\\n", ""));
        requestMapFinal.put(CONTENT_SUFFIX, finalPublicKey);

        ApiTask.getService(context, channel, versionCode, languageCode,token, urlEndPoint, finalPublicKey)
                .callApiWithEncrypt(urlEndPoint, requestMapFinal)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppRxSubscriber(context, urlEndPoint, gson) {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                        registerSubscription(d);
                    }

                    @Override
                    public void onSuccessResponse(final String jsonData) {
                        responseSuccess(urlEndPoint, responseType, jsonData, params);
                    }

                    @Override
                    public void onErrorResponse(final RetrofitError error) {
                        error.setParams(params);
                        responseError(error, params);
                    }

                });
    }

    public <Q extends AppBaseRequest> void callApiNormal(final String versionCode,
                                                         final String channel,
                                                         final String languageCode,
                                                         final String token,
                                                         final String urlEndPoint,
                                                         final Q request,
                                                         final Class responseType,
                                                         final Object... params) {
        String publicKey = request.putRequestPublicKey();
        Map<String, Object> requestMap = request.getRequestMap();
        LogUtil.logInfo("publicKey", publicKey);
        LogUtil.logInfo("json", request.getGson().toJson(requestMap));

        ApiTask.getService(context, channel, versionCode, languageCode,token, urlEndPoint, publicKey)
                .callApi(urlEndPoint, requestMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppRxSubscriber(context, urlEndPoint, gson) {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                        registerSubscription(d);
                    }

                    @Override
                    public void onSuccessResponse(final String jsonData) {
                        responseSuccess(urlEndPoint, responseType, jsonData, params);
                    }

                    @Override
                    public void onErrorResponse(final RetrofitError error) {
                        error.setParams(params);
                        responseError(error, params);
                    }

                });
    }


//    public <Q extends AppBaseRequest> void callApiCart(final String versionCode,
//                                                   final String channel,
//                                                   final String languageCode,
//                                                   final String urlEndPoint,
//                                                   final Q request,
//                                                   final Class responseType,
//                                                   final String data,
//                                                   final Object... params) {
//        String publicKey = request.putRequestPublicKey();
//        //Map<String, Object> requestMap = request.getRequestMap();
//
//
//        String value = CryptoUtil.encrypt(data, publicKey);
//        String finalPublicKey = String.valueOf(publicKey);
//        LogUtil.logInfo(TAG_BODY, value.replaceAll("\\n", ""));
//
//        Map<String, Object> requestMapFinal = new TreeMap<>();
//        requestMapFinal.put("post-data", value.trim().replaceAll("\\n", ""));
//        requestMapFinal.put(CONTENT_SUFFIX, finalPublicKey);
//
//        ApiTask.getService(context, channel, versionCode, languageCode, urlEndPoint, finalPublicKey)
//                .callApiWithEncrypt(urlEndPoint, requestMapFinal)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new AppRxSubscriber(context, urlEndPoint, gson) {
//
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                        registerSubscription(d);
//                    }
//
//                    @Override
//                    public void onSuccessResponse(final String jsonData) {
//                        responseSuccess(urlEndPoint, responseType, jsonData, params);
//                    }
//
//                    @Override
//                    public void onErrorResponse(final RetrofitError error) {
//                        error.setParams(params);
//                        responseError(error, params);
//                    }
//
//                });
//    }

    private void responseSuccess(String urlEndPoint, Class clazz, String jsonData, Object... params) {
        try {
            Object data = gson.fromJson(jsonData, clazz);
            onResponse(urlEndPoint, data, params);
        } catch (Exception e) {
            e.printStackTrace();
            responseError(RetrofitError.createDefaultResponseError(urlEndPoint, context.getString(R.string.__t_api_status_alert_unknown_system_error)), params);
        }
    }

    private boolean isErrorForceLogout(String statusCode) {
        return statusCode.equals(ApiError.API_STATUS_ALERT_INVALID_LOGIN_TOKEN)
                || statusCode.equals(ApiError.API_STATUS_ALERT_ACCOUNT_SUSPENDED)
                || statusCode.equals(ApiError.API_STATUS_ALERT_TOKEN_EXPIRE)
                || statusCode.equals(ApiError.API_STATUS_ALERT_FORCE_LOGOUT)
                || statusCode.equals(ApiError.API_STATUS_ALERT_TOKEN_INVALID)
                || statusCode.equals(ApiError.API_STATUS_ALERT_EMAIL_REPORTED)
                || statusCode.equals(ApiError.API_STATUS_ALERT_TOKEN_MISSING)
                || statusCode.equals(ApiError.API_STATUS_ALERT_FAUL_REFERSH)//
                || statusCode.equals(ApiError.API_STATUS_ALERT_APP_UPDATE)
                || statusCode.equals(ApiError.API_STATUS_ALERT_ACCOUNT_BLOCK);
    }

    public void responseError(RetrofitError error, Object... params) {
        if (view != null) {
            String statusCode = error.getStatusCode();
            if (statusCode != null && isErrorForceLogout(statusCode)) {
                switch (statusCode) {
                    case ApiError.API_STATUS_ALERT_FORCE_LOGOUT:
                        view.onErrorForceLogout();
                        break;
                    case ApiError.API_STATUS_ALERT_FAUL_REFERSH:
                    case ApiError.API_STATUS_ALERT_INVALID_LOGIN_TOKEN:
                    case ApiError.API_STATUS_ALERT_TOKEN_MISSING:
                    case ApiError.API_STATUS_ALERT_TOKEN_EXPIRE:
                    case ApiError.API_STATUS_ALERT_TOKEN_INVALID:
                        view.onErrorSessionExpired();
                        break;
                    case ApiError.API_STATUS_ALERT_ACCOUNT_SUSPENDED:
                    case ApiError.API_STATUS_ALERT_ACCOUNT_BLOCK:
                        view.onErrorAccountSuspended();
                        break;
                    case ApiError.API_STATUS_ALERT_EMAIL_REPORTED:
                        view.onErrorEmailReported();
                        break;
                    case ApiError.API_STATUS_ALERT_EMAIL_UPDATED:
                        view.onErrorEmailUpdated();
                        break;
                    case ApiError.API_STATUS_ALERT_APP_UPDATE:
                        view.onErrorAppVersionUpdate(error.getMessage());
                        break;
                }
            } else {
                view.onErrorResponse(error);
            }
        }
        LibUtil.printPrettyJson(TAG_ERROR, gson.toJson(error));
    }

    public abstract void onResponse(String urlEndPoint, Object data, Object... params);

}
