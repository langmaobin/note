package com.lib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lib.utils.NetworkUtil;

public abstract class NetworkReceiver extends BroadcastReceiver {

    private boolean isRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtil.isNetworkConnected(context)) {
            connectToNetwork();
        } else {
            disconnectFromNetwork();
        }
    }

    /**
     * @param context activity/application context
     * @param filter intent filter
     */
    public Intent register(Context context, IntentFilter filter) {
        try {
            return !isRegistered
                    ? context.registerReceiver(this, filter)
                    : null;
        } finally {
            isRegistered = true;
        }
    }

    /**
     * @param context activity/application context
     */
    public boolean unregister(Context context) {
        return isRegistered && unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;
        return true;
    }

    public abstract void connectToNetwork();

    public abstract void disconnectFromNetwork();

}