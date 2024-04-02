package com.ywvision.wyvisionhelper.api;

import android.content.Context;

import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.R;

public class ApiError {

    public static final String API_STATUS_ALERT_SUCCESS = "100";
    public static final String API_STATUS_ALERT_INVALID_SIGNATURE = "300";
    public static final String API_STATUS_ALERT_INVALID_HOST_IP = "301";
    public static final String API_STATUS_ALERT_INVALID_LOGIN_TOKEN = "1007";
    public static final String API_STATUS_ALERT_INVALID_WS_ID = "303";
    public static final String API_STATUS_ALERT_ACCOUNT_SUSPENDED = "304";
    public static final String API_STATUS_ALERT_EMAIL_REPORTED = "305";
    public static final String API_STATUS_ALERT_EMAIL_UPDATED = "306";
    public static final String API_STATUS_ALERT_SERVER_MAINTENANCE = "500";
    public static final String API_STATUS_ALERT_SERVER_ERROR = "501";
    public static final String API_STATUS_ALERT_TOKEN_EXPIRE= "40101";
    public static final String API_STATUS_ALERT_TOKEN_INVALID = "1008";
    public static final String API_STATUS_ALERT_TOKEN_MISSING= "1006";
    public static final String API_STATUS_ALERT_FAUL_REFERSH= "1010";
    public static final String API_STATUS_ALERT_ACCOUNT_BLOCK= "2002";
    public static final String API_STATUS_ALERT_FORCE_LOGOUT= "2021";
    public static final String API_STATUS_ALERT_UNKNOWN_SYSTEM_ERROR = "0x00000000";
    public static final String API_STATUS_ALERT_APP_UPDATE = "1003";

    public static String getMessage(Context context, String url, String statusCode) {
        if (!ValidUtil.isEmpty(url) && !ValidUtil.isEmpty(statusCode)) {
            switch (statusCode) {
                case API_STATUS_ALERT_SUCCESS:
                    return context.getString(R.string.__t_api_status_alert_success);
                case API_STATUS_ALERT_INVALID_SIGNATURE:
                    return context.getString(R.string.__t_api_status_alert_invalid_signature);
                case API_STATUS_ALERT_INVALID_HOST_IP:
                    return context.getString(R.string.__t_api_status_alert_invalid_host_ip);
                case API_STATUS_ALERT_INVALID_LOGIN_TOKEN:
                    return context.getString(R.string.__t_api_status_alert_invalid_login_token);
                case API_STATUS_ALERT_INVALID_WS_ID:
                    return context.getString(R.string.__t_api_status_alert_invalid_ws_id);
                case API_STATUS_ALERT_ACCOUNT_SUSPENDED:
                    return context.getString(R.string.__t_api_status_alert_account_suspended);
                case API_STATUS_ALERT_EMAIL_REPORTED:
                    return context.getString(R.string.__t_api_status_alert_email_reported);
                case API_STATUS_ALERT_EMAIL_UPDATED:
                    return context.getString(R.string.__t_api_status_alert_email_updated);
                case API_STATUS_ALERT_SERVER_MAINTENANCE:
                    return context.getString(R.string.__t_api_status_alert_server_maintenance);
                case API_STATUS_ALERT_SERVER_ERROR:
                    return context.getString(R.string.__t_api_status_alert_server_error);
                case API_STATUS_ALERT_UNKNOWN_SYSTEM_ERROR:
                    return context.getString(R.string.__t_api_status_alert_unknown_system_error);
            }
        }
        return context.getString(R.string.__t_api_status_alert_unknown_system_error);
    }

}
