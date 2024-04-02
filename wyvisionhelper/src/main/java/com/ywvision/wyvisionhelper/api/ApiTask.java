package com.ywvision.wyvisionhelper.api;

import android.content.Context;

import com.lib.retrofit.RetrofitClient;
import com.lib.utils.LogUtil;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.app.AppBaseResponse;
import com.ywvision.wyvisionhelper.app.AppBaseResponseConverter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

import static com.lib.BuildConfig.IS_DEBUG;
import static com.lib.BuildConfig.IS_MOCK;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_APP_CHANNEL;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_APP_VERSION;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_DEVICE_TYPE;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_IMEI;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_LANGUAGE;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_SUFFIX;
import static com.lib.base.BaseConstants.HttpHeaderFields.CONTENT_TYPE;
import static com.lib.base.BaseConstants.HttpHeaderFields.TOKEN;
import static com.lib.base.BaseConstants.MediaTypes.MEDIA_TYPE_JSON;
import static com.lib.base.BaseConstants.Tag.TAG_URL;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_CH;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_EG;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_FA;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_SIMPLIFIED;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_TRADITIONAL;
import static com.ywvision.wyvisionhelper.app.AppConstants.LANGUAGE_CODE.LANGUAGE_CODE_TW;

public class ApiTask {

    private static RetrofitClient retrofitClient;

    public static ApiService getService(Context context, String channel, String versionCode, String languageCode,String token, String urlEndPoint, String publicKey) {

        String baseUrl;
        boolean isMock;

        switch (urlEndPoint) {

            case ApiMethod.API_GET_COUNT_PAIPAN_ALL:
                baseUrl = "https://xilin-api.hyahm.com/";
                isMock = IS_MOCK;
                break;
//            case ApiMethod.API_GET_MERCHANTS:
//                baseUrl = SERVER_BASE_URL;
//                isMock = true;
//                break;
            //for local json
            case ApiMethod.API_POST_APP_LOGIN:
            case ApiMethod.API_POST_APP_LOGOUT:
            case ApiMethod.API_POST_APP_FORGETPASSWORD:
            case ApiMethod.API_POST_APP_CHANGEPASSWORD:
            case ApiMethod.API_POST_APP_SMSCODE:
            case ApiMethod.API_POSTT_APP_VERSION:
            case ApiMethod.API_GET_USER_PROFILE:
            case ApiMethod.API_UPDATE_USER_PROFILE:
            case ApiMethod.API_GET_ADDRESS:
            case ApiMethod.API_CREATE_ADDRESS:
            case ApiMethod.API_GET_COUNTRY:
            case ApiMethod.API_DELETE_ADDRESS:
            case ApiMethod.API_GET_ADDRESS_DETAIL:
            case ApiMethod.API_UPDATE_ADDRESS:
            case ApiMethod.API_SECURE_PASS:
            case ApiMethod.API_FEEDBACK:
            case ApiMethod.API_POLICY:
            case ApiMethod.API_FAQ:
            case ApiMethod.API_FAQ_DETAIL:
            case ApiMethod.API_FAQ_VOTE:
            case ApiMethod.API_FAQ_SEARCH:
            case ApiMethod.API_PHONE_CHANGE_FIRST:
            case ApiMethod.API_PHONE_CHANGE_CONFIRM:
            case ApiMethod.API_POSTT_APP_OTHER:
            case ApiMethod.API_POST_APP_LOGIN_PHONE:
                baseUrl = ApiUrl.getCurrentBaseUrl(apiLink);
                isMock = IS_MOCK;
                break;
            default:
                baseUrl = ApiUrl.getCurrentBaseUrl(apiLink);
                isMock = IS_MOCK;
                break;
        }

        LogUtil.logInfo(TAG_URL, baseUrl + urlEndPoint);

        Map<String, String> headers = new HashMap<>();
//            headers.put(USER_AGENT, CryptoUtil.getUserAgent(context));
        headers.put(CONTENT_TYPE, MEDIA_TYPE_JSON);
        headers.put(CONTENT_DEVICE_TYPE, "1");
        headers.put(CONTENT_APP_CHANNEL, channel);
        headers.put(CONTENT_APP_VERSION, "user");
        headers.put(CONTENT_SUFFIX, publicKey);
        headers.put(CONTENT_IMEI, SettingsUtil.getDeviceImei(context));
        headers.put(CONTENT_LANGUAGE, getLanguage(languageCode));
        headers.put(TOKEN, token);

        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient()
                    .isDebugMode(IS_DEBUG)
                    .headers(headers)
                    .registerTypeAdapter(AppBaseResponse.class, new AppBaseResponseConverter());
        }

        return retrofitClient
                .headers(headers)
                .baseUrl(baseUrl)
                .isMock(isMock)
                .buildRetrofit(context)
                .create(ApiService.class);

    }

    private static String getLanguage(String language) {
        if (!ValidUtil.isEmpty(language)) {
            if (language.equalsIgnoreCase(LANGUAGE_CODE_SIMPLIFIED)) {
                return LANGUAGE_CODE_CH;
            } else if (language.equalsIgnoreCase(LANGUAGE_CODE_TRADITIONAL)) {
                return LANGUAGE_CODE_TW;
            } else if (language.equalsIgnoreCase(LANGUAGE_CODE_FA)) {
                return LANGUAGE_CODE_FA;
            }
        }

        return LANGUAGE_CODE_EG;
    }

    private static int apiLink = ApiUrl.DEVELOPMENT;

    public interface ApiService {

        @POST("/jayson")
        Observable<Response<AppBaseResponse>> postJson(@Url String url,
                                                       @Body HashMap<String, Object> body);

        @POST
        Observable<Response<AppBaseResponse>> callApi(@Url String url,
                                                      @Body Map<String, Object> Object);

        @POST
        Observable<Response<AppBaseResponse>> callApiWithEncrypt(@Url String url,
                                                                 @Body Map<String, Object> request);

        @POST
        @Multipart
        Observable<Response<AppBaseResponse>> callMultipartApi(@Url String url,
                                                               @PartMap Map<String, RequestBody> request,
                                                               @Part MultipartBody.Part... files);
    }


}