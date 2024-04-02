package com.lib.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BasePresenter {

    public Context context;
    private List<Disposable> disposables;

    public BasePresenter(Context context) {
        this.context = context;
        this.disposables = new ArrayList<>();
    }
    public void registerSubscription(Disposable disposable) {
        disposables.add(disposable);
    }

    public void unregisterSubscription(Disposable disposable) {
        if (isUnsubscribed(disposable)) {
            disposable.dispose();
            disposables.remove(disposable);
        }
    }

    public void unregisterAllSubscriptions() {
        if (disposables != null) {
            for (Disposable disposable : disposables) {
                if (isUnsubscribed(disposable)) {
                    disposable.dispose();
                }
            }
            disposables.clear();
        }
    }

    private boolean isUnsubscribed(Disposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

}