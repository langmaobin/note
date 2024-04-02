package com.ywvision.wyvisionhelper.app;

import android.content.Context;

import com.google.gson.Gson;
import com.lib.retrofit.RetrofitError;
import com.lib.rx.RxSubscriber;
import com.lib.utils.ValidUtil;

import retrofit2.Response;

public abstract class AppRxSubscriber extends RxSubscriber<Response<AppBaseResponse>> {

    public AppRxSubscriber(Context context, String url, Gson gson) {
        super(context, url, gson);
    }

    @Override
    public void onSuccess(Response<AppBaseResponse> response) {
        AppBaseResponse baseResponse = response.body();
        if (baseResponse.success) {
            onSuccessResponse(baseResponse.data);
        } else {
            RetrofitError error = null;
            if (!ValidUtil.isEmpty(baseResponse.message)) {
                error = RetrofitError.createResponseError(url, baseResponse.message, baseResponse.statusCode);
            } else {
                error = RetrofitError.createDefaultResponseError(context, url);
            }
            onErrorResponse(error);
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        onErrorResponse(error);
    }

    public abstract void onSuccessResponse(String jsonData);

    public abstract void onErrorResponse(RetrofitError error);

}