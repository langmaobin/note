package com.ywvision.wyvisionhelper.app;

import android.content.Context;
import android.os.Environment;

import com.lib.base.BaseConstants;
import com.paypal.android.sdk.payments.PayPalConfiguration;

public class AppConstants extends BaseConstants {

    //tencent
    public static final String CHAT_INFO = "chatInfo";
    public static final String CALL_MODEL = "callModel";
    public static final String IS_OFFLINE_PUSH_JUMP = "is_offline_push";


    public static void init(Context context) {
        new Builder()
                .packageName(context.getPackageName())
                .paypalConfiguration(PayPalConfiguration.ENVIRONMENT_PRODUCTION, "AS2UsdqfXQYnEjfosC1VsMTZ-pXNEpb3lRRRXNtQOFbMEWT6fNDBqbfsCQWJQXXXtsyw34CwxZTj3vON")
                .fontStyle(true, "fonts/Montserrat.ttf")
                .iconFontStyle(true, "fonts/IconFonts.ttf")
                .isEncryptionEnabled(true)
                .isTokenEnabled(true)
                .cryptoFields("AES/CBC/PKCS5Padding", "2wbqyo863z", "ctv530nokij4qg7ly21zwfsbradh89xe")
                .withExtras(context);
    }

    public static String DEVICE_IMEI;
    public static String PACKAGE_NAME;
    public static String PAYPAL_ENVIRONMENT;
    public static String PAYPAL_CLIENT_ID;
    public static String SP_LANGUAGE;
    public static String ICON_FONT_PATH;
    public static boolean HAS_ICON_FONT_STYLE;
    public static boolean IS_ENCRYPTION_ENABLED;
    public static boolean IS_TOKEN_ENABLED;

    public static class CryptoFields {
        public static String CIPHER_METHOD;
        public static String IV_KEY;
        public static String SECRET_KEY;
    }

    public static final class ReqCode {
        public static final int REQ_CODE_PAYPAL_PAYMENT = 3;
    }

    public static final class Args {
        public static final String ARG_PAYMENT_AMOUNT = "ARG_PAYMENT_AMOUNT";
        public static final String ARG_PAYMENT_INVOICE = "ARG_PAYMENT_INVOICE";
        public static final String ARG_PAYPAL_INVOICE = "ARG_PAYPAL_INVOICE";
        public static final String ARG_PAYPAL_PAYID = "ARG_PAYPAL_PAYID";
        public static final String ARG_COUNTRY = "ARG_COUNTRY";
        public static final String ARG_TO_ACTIVITY = "ARG_TO_ACTIVITY";
        public static final String ARG_CHART_INFO = "ARG_CHART_INFO";
        public static final String ARG_CURRENT_TIME = "ARG_CURRENT_TIME";
        public static final String ARG_ZHI_WEI= "ARG_ZHI_WEI";
        public static final String ARG_MING_ZHU = "ARG_MING_ZHU";
        public static final String ARG_COURSE_MODEL = "ARG_COURSE_MODEL";
        public static final String ARG_USER_ORDER_MODEL = "ARG_USER_ORDER_MODEL";
        public static final String ARG_COURSE_MODEL_COMMUNITY = "ARG_COURSE_MODEL_COMMUNITY";
        public static final String ARG_COMMUNITY_MODEL = "ARG_COMMUNITY_MODEL";
        public static final String ARG_CUSTOMER_MODEL = "ARG_CUSTOMER_MODEL";
    }


    public static final class OTHER_ACTIVITY {
        public static final String OTHER_TITLE = "OTHER_TITLE";
        public static final String OTHER_SETTING = "OTHER_SETTING";
        public static final String OTHER_EMPTY = "OTHER_EMPTY";
        public static final String OTHER_LANGUAGE = "OTHER_LANGUAGE";

    }
    public static final class LANGUAGE_CODE {
        public static final String LANGUAGE_CODE_EG = "en";
        public static final String LANGUAGE_CODE_TW = "zh-tw";
        public static final String LANGUAGE_CODE_CH = "zh-cn";
        public static final String LANGUAGE_CODE_SIMPLIFIED = "zh";
        public static final String LANGUAGE_CODE_TRADITIONAL = "tw";
        public static final String LANGUAGE_CODE_FA = "fa";
    }
    public static final class COMMUNITY_TYPE {
        public static final String COMMUNITY_TYPE_HOT = "1";
        public static final String COMMUNITY_TYPE_MY_Q = "2";
        public static final String COMMUNITY_TYPE_MY_R = "3";
    }

    private static final class Builder {

        Builder packageName(String packageName) {
            PACKAGE_NAME = packageName;
            return this;
        }

        Builder paypalConfiguration(String paypalEnvironment, String paypalClientId) {
            PAYPAL_ENVIRONMENT = paypalEnvironment;
            PAYPAL_CLIENT_ID = paypalClientId;
            return this;
        }

        Builder fontStyle(boolean hasFontStyle, String fontStyleFile) {
            HAS_FONT_STYLE = hasFontStyle;
            FONT_PATH = fontStyleFile;
            return this;
        }

        Builder iconFontStyle(boolean hasIconFontStyle, String iconFontStyleFile) {
            HAS_ICON_FONT_STYLE = hasIconFontStyle;
            ICON_FONT_PATH = iconFontStyleFile;
            return this;
        }

        Builder isEncryptionEnabled(boolean isEncryptionEnabled) {
            IS_ENCRYPTION_ENABLED = isEncryptionEnabled;
            return this;
        }

        Builder isTokenEnabled(boolean isTokenEnabled) {
            IS_TOKEN_ENABLED = isTokenEnabled;
            return this;
        }

        Builder cryptoFields(String cryptoEncryptionMethod, String cryptoIvKey, String cryptoSecretKey) {
            CryptoFields.CIPHER_METHOD = cryptoEncryptionMethod;
            CryptoFields.IV_KEY = cryptoIvKey;
            CryptoFields.SECRET_KEY = cryptoSecretKey;
            return this;
        }

        Builder withExtras(Context context) {
            SP_LANGUAGE = PACKAGE_NAME + ".sharedpref_language";
            EXT_DIR_IMG_PATH = Environment.getExternalStorageDirectory() + Separator.BACK_SLASH
                    + PACKAGE_NAME + Separator.BACK_SLASH
                    + "images" + Separator.BACK_SLASH;
            return this;
        }

    }

}