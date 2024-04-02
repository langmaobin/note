package com.lib.rx;

import android.content.Context;

import com.google.gson.Gson;
import com.lib.retrofit.RetrofitError;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import retrofit2.Response;

public abstract class RxSubscriber<T> implements Observer<T> {

    protected Context context;
    protected String url;
    protected Gson gson;

    public RxSubscriber(Context context, String url, Gson gson) {
        this.context = context;
        this.url = url;
        this.gson = gson;
    }

//    @Override
//    public void onSubscribe(Disposable d) {
//    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            onFailure(RetrofitError.createNetworkConnectionError(context, url));
        } else if (e instanceof SocketTimeoutException) {
            onFailure(RetrofitError.createConnectionTimeoutError(context, url));
        } else {
            onFailure(RetrofitError.createDefaultResponseError(context, url));
        }
    }

    @Override
    public void onNext(T t) {
        if (t != null) {
            if (t instanceof Response) {
                Response response = (Response) t;
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess(t);
                } else if (response.body() == null) {
                    onFailure(RetrofitError.createNullResponseBodyError(context, url));
                } else {
                    onFailure(RetrofitError.createGenericResponseError(url, response));
                }
            } else if (t instanceof Response[]) {
                onSuccess(t);
            }
        } else {
            onFailure(RetrofitError.createInvalidResponseError(context, url));
        }
    }

    public abstract void onSuccess(T response);

    public abstract void onFailure(RetrofitError error);

}