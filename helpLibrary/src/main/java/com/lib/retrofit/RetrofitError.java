package com.lib.retrofit;

import android.content.Context;

import com.lib.R;

import java.util.Arrays;

import retrofit2.Response;

public class RetrofitError {

    public static final String STATUS_CODE_DEFAULT_RESPONSE_ERROR = "status.code.999";
    public static final String STATUS_CODE_INVALID_RESPONSE_ERROR = "status.code.998";
    public static final String STATUS_CODE_NULL_RESPONSE_BODY_ERROR = "status.code.997";
    public static final String STATUS_CODE_NETWORK_CONNECTION_ERROR = "status.code.996";
    public static final String STATUS_CODE_CONNECTION_TIMEOUT_ERROR = "status.code.995";

    private String url;
    private String statusCode;
    private String[] statusCodes;
    private String message;
    private String[] messages;
    private Object[] params;
    private String data;

    private RetrofitError(Builder builder) {
        setUrl(builder.url);
        setStatusCode(builder.statusCode);
        setStatusCodes(builder.statusCodes);
        setMessage(builder.message);
        setMessages(builder.messages);
        setData(builder.data);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String[] getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(String[] statusCodes) {
        this.statusCodes = statusCodes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Url: " + url
                + "\nStatus code: " + statusCode
                + "\nStatus codes: " + Arrays.toString(statusCodes)
                + "\nMessage: " + message
                + "\nMessages: " + Arrays.toString(messages);
    }

    public static RetrofitError createResponseError(String url, String[] messages, String[] codes) {
        return new Builder()
                .url(url)
                .statusCode(codes[0])
                .statusCodes(codes)
                .message(messages[0])
                .messages(messages)
                .build();
    }

    public static RetrofitError createResponseError(String url, String message, String code) {
        return new Builder()
                .url(url)
                .statusCode(code)
                .message(message)
                .build();
    }

    public static RetrofitError createGenericResponseError(String url, Response response) {
        return new Builder()
                .url(url)
                .statusCode(String.valueOf(response.code()))
                .message(response.message())
                .build();
    }

    public static RetrofitError createDefaultResponseError(String url, String errorMessage) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_DEFAULT_RESPONSE_ERROR)
                .message(errorMessage)
                .build();
    }

    public static RetrofitError createDefaultResponseError(Context context, String url) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_DEFAULT_RESPONSE_ERROR)
                .message(getStringRes(context, R.string.__t_global_text_invalid_response_return))
                .build();
    }

    public static RetrofitError createInvalidResponseError(Context context, String url) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_INVALID_RESPONSE_ERROR)
                .message(getStringRes(context, R.string.__t_global_text_invalid_response_return))
                .build();
    }

    public static RetrofitError createNullResponseBodyError(Context context, String url) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_NULL_RESPONSE_BODY_ERROR)
                .message(getStringRes(context, R.string.__t_global_text_no_response_return))
                .build();
    }

    public static RetrofitError createNetworkConnectionError(Context context, String url) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_NETWORK_CONNECTION_ERROR)
                .message(getStringRes(context, R.string.__t_global_text_network_connection_error))
                .build();
    }

    public static RetrofitError createConnectionTimeoutError(Context context, String url) {
        return new Builder()
                .url(url)
                .statusCode(STATUS_CODE_CONNECTION_TIMEOUT_ERROR)
                .message(getStringRes(context, R.string.__t_global_text_connection_timeout))
                .build();
    }

    private static String getStringRes(Context context, int resId) {
        try {
            return context.getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private static final class Builder {

        private String url;
        private String statusCode;
        private String[] statusCodes;
        private String message;
        private String[] messages;
        private String data;

        Builder() {
        }

        Builder url(String val) {
            url = val;
            return this;
        }

        public Builder statusCode(String val) {
            statusCode = val;
            return this;
        }

        public Builder statusCodes(String[] val) {
            statusCodes = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public Builder messages(String[] val) {
            messages = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public RetrofitError build() {
            return new RetrofitError(this);
        }

    }

}