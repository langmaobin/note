package com.lib.retrofit;


import android.content.Context;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lib.utils.LibUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lib.base.BaseApplication.gson;
import static com.lib.base.BaseConstants.Tag.TAG_REQUEST_HEADER;

public class RetrofitClient {

    private final String DEFAULT_BASE_URL = "http://47.75.121.210/";
    private final int DEFAULT_MAX_RETRY = 2;
    private final int DEFAULT_WRITE_TIMEOUT = 15;
    private final int DEFAULT_READ_TIMEOUT = 15;
    private final int DEFAULT_CONNECT_TIMEOUT = 15;

    private boolean isMock;
    private boolean isDebugMode;
    private String baseUrl;
    private int maxRetry = -1;
    private int writeTimeout = -1;
    private int readTimeout = -1;
    private int connectTimeout = -1;
    private Map<String, String> headers;
    private GsonBuilder gsonBuilder;

    public RetrofitClient isMock(boolean isMock) {
        this.isMock = isMock;
        return this;
    }

    public RetrofitClient isDebugMode(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
        return this;
    }

    public RetrofitClient baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RetrofitClient maxRetry(int count) {
        this.maxRetry = count;
        return this;
    }

    public RetrofitClient writeTimeout(int seconds) {
        this.writeTimeout = seconds;
        return this;
    }

    public RetrofitClient readTimeout(int seconds) {
        this.readTimeout = seconds;
        return this;
    }

    public RetrofitClient connectTimeout(int seconds) {
        this.connectTimeout = seconds;
        return this;
    }

    public RetrofitClient headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RetrofitClient registerTypeAdapter(Type type, Object typeAdapter) {
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
        }
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        return this;
    }

    public Retrofit buildRetrofit(Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_BASE_URL;
        }
        if (maxRetry < 0) {
            maxRetry = DEFAULT_MAX_RETRY;
        }
        if (writeTimeout < 0) {
            writeTimeout = DEFAULT_WRITE_TIMEOUT;
        }
        if (readTimeout < 0) {
            readTimeout = DEFAULT_READ_TIMEOUT;
        }
        if (connectTimeout < 0) {
            connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        }
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
        }

        // Set request timeout in seconds
        httpClient.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        httpClient.readTimeout(readTimeout, TimeUnit.SECONDS);
        httpClient.connectTimeout(connectTimeout, TimeUnit.SECONDS);

        // Create a trust manager that validate certificate chains
        httpClient.sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault(), new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        });

        // Log request and response for the http call
        if (isDebugMode) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logInterceptor);
        }

        //test local api
//        if (isMock) {
//            httpClient.addInterceptor(new MockInterceptor(context));
//        }

        // Customize the process of the http request
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                if (headers != null) {
                    for (Map.Entry entry : headers.entrySet()) {
                        Object objKey = entry.getKey();
                        Object objValue = entry.getValue();
                        String headerName = objKey != null ? objKey.toString() : "";
                        String headerValue = objValue != null ? objValue.toString() : "";
                        if (!headerName.isEmpty() && !headerValue.isEmpty()) {
                            requestBuilder.header(headerName, headerValue);
                        }
                    }
                    if (gson != null) {
                        LibUtil.printPrettyJson(TAG_REQUEST_HEADER, gson.toJson(headers));
                    }
                }
                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
//                // Set up max retry attempt
//                Response response = null;
//                boolean success = false;
//                int retryCounter = 0;
//                while (!success && retryCounter < maxRetry) {
//                    try {
//                        response = chain.proceed(request);
//                        success = response.isSuccessful();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        if (e instanceof SocketTimeoutException) {
//                            break;
//                        }
//                    } finally {
//                        retryCounter++;
//                    }
//                }
//                // To prevent NullPointerException return by response object
//                if (response == null) {
//                    return chain.proceed(request);
//                }
//                return response;
            }

        });

        // Create retrofit instance
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

    }

}
