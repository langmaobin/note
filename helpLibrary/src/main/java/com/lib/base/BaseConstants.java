package com.lib.base;

public class BaseConstants {

    public static boolean HAS_FONT_STYLE;
    public static String FONT_PATH;
    public static String EXT_DIR_IMG_PATH;

    private static final String TAG_PREFIX = "AppLog";
    public static final String TMP_IMG_NAME = "tmp.png";

    public static final class Separator {
        public static final String BACK_SLASH = "/";
        public static final String COMMA = ",";
        public static final String DOT = ".";
    }

    public static final class Tag {
        public static final String TAG_ERROR = TAG_PREFIX + "Error";
        public static final String TAG_REQUEST = TAG_PREFIX + "Request";
        public static final String TAG_BODY = TAG_PREFIX + "BODY";
        public static final String TAG_RESPONSE = TAG_PREFIX + "Response";
        public static final String TAG_URL = TAG_PREFIX + "Url";
        public static final String TAG_SIGNATURE = TAG_PREFIX + "Signature";
        public static final String TAG_REQUEST_FILE = TAG_PREFIX + "Files";
        public static final String TAG_REQUEST_HEADER = TAG_PREFIX + "Headers";
    }

    public static final class CharSets {
        public static final String UTF_8 = "UTF-8";
    }

    public static final class Cryptography {
        public static final String MD5 = "MD5";
        public static final String AES = "AES";
        public static final String SHA_256 = "SHA-256";
    }

    public static final class MediaTypes {
        public static final String MEDIA_TYPE_MULTIPART = "multipart/form-data";
        public static final String MEDIA_TYPE_IMAGE = "image/*";
        public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain";
        public static final String MEDIA_TYPE_TEXT = "text/plain; charset=UTF-8";
        public static final String MEDIA_TYPE_JSON = "application/json; charset=UTF-8";
        public static final String MEDIA_TYPE_JSON2 = "application/x-www-form-urlencoded:";
        public static final String DATA_UPLOADED_FILES = "uploaded_file[]";
        public static final String DATA_UPLOADED_FILE = "uploaded_file";
    }

    public static final class HttpHeaderFields {
        public static final String USER_AGENT = "User-Agent";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String CONTENT_DEVICE_TYPE = "system";
        public static final String CONTENT_APP_VERSION = "version";
        public static final String CONTENT_APP_CHANNEL = "channel";
        public static final String CONTENT_SUFFIX = "suffix";
        public static final String CONTENT_IMEI = "imei";
        public static final String CONTENT_TYPE_JSON = "text/plain";
        public static final String CONTENT_LANGUAGE = "lang";
        public static final String DEVICE_NAME = "device_name";
        public static final String TOKEN = "token";
    }

    public static final class LibReqCode {
        public static final int REQ_CODE_CHECK_PERMISSION = 1000;
        public static final int REQ_CODE_CHECK_PERMISSION_SETTINGS = 2000;
    }

}