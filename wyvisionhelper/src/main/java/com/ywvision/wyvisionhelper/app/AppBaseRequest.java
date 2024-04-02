package com.ywvision.wyvisionhelper.app;

import android.content.Context;

import com.google.gson.Gson;
import com.lib.utils.LibUtil;
import com.lib.utils.LogUtil;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.utils.CryptoUtil;

import java.util.Map;
import java.util.TreeMap;

import static android.provider.Settings.Global.DEVICE_NAME;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_IMEI;
import static com.lib.base.BaseConstants.Tag.TAG_REQUEST;
import static com.lib.base.BaseConstants.Tag.TAG_SIGNATURE;

public class AppBaseRequest {
    protected final String TAG_MULTIPART = "[multipart]";
    private final String PARAM_SIGNATURE = "encode_sign";
    private final String PARAM_TIME_STAMP = "timestamp";
    private final String PARAM_WSID = "wsid";
    private final String ENCODE_SIGN = "b|s.e%avo`&!6c'u8]:/4t={~}(,>7+13#m@";

    private Gson gson = new Gson();
    public Context context;

    public Gson getGson() {
        return gson;
    }

    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public String putRequestPublicKey() {
        this.publicKey = LibUtil.genRandomNumbers(6);
        return publicKey;
    }

    /**
     * Normal request mapping
     */

    private Map<String, Object> requestMap = new TreeMap<>();

    public Map<String, Object> getRequestMap() {
        putRequestMapToken();
        logRequest();
        return requestMap;
    }

    public void putRequestMap(String key, Object obj) {
        if (key != null && !ValidUtil.isEmpty(obj)) {
            if (obj instanceof String) {
                if (((String) obj).length() > 0) {
                    requestMap.put(key, obj.toString().trim());
                }
            } else {
                requestMap.put(key, obj);
            }
        }
    }

    public void putRequestMapToken() {
        putRequestMap(CONTENT_IMEI, SettingsUtil.getDeviceImei(context));
        putRequestMap(DEVICE_NAME, SettingsUtil.getDeviceBrand() + ":" + SettingsUtil.getSystemModel());
        putRequestMap(PARAM_TIME_STAMP, System.currentTimeMillis());
        putRequestMap(PARAM_SIGNATURE, generateSignature());
    }

    public String getEncryptedField(String value) {
        if (value != null) {
            value = value.trim();
        }
        return AppConstants.IS_ENCRYPTION_ENABLED ? CryptoUtil.encrypt(value, publicKey) : value;
    }

    private String generateSignature() {
        StringBuilder signatureBuilder = new StringBuilder();
        for (Map.Entry entry : requestMap.entrySet()) {
            if (entry.getValue() != null) {
                signatureBuilder.append(entry.getKey() + "=");
                signatureBuilder.append(entry.getValue() + "&");
            }
        }
        signatureBuilder.append(ENCODE_SIGN);
        String signature = signatureBuilder.toString();
        LogUtil.logInfo(TAG_SIGNATURE, signature);
        return CryptoUtil.md5(signature);
    }

    private void logRequest() {
        LibUtil.printPrettyJson(TAG_REQUEST, gson.toJson(requestMap));
    }
}