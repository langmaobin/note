package com.ywvision.wyvisionhelper.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import androidx.multidex.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lib.utils.LogUtil;
import com.lib.utils.SettingsUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.lib.base.BaseConstants.Cryptography.AES;
import static com.lib.base.BaseConstants.Cryptography.MD5;
import static com.lib.base.BaseConstants.Cryptography.SHA_256;
import static com.ywvision.wyvisionhelper.app.AppConstants.CryptoFields.CIPHER_METHOD;
import static com.ywvision.wyvisionhelper.app.AppConstants.CryptoFields.IV_KEY;
import static com.ywvision.wyvisionhelper.app.AppConstants.CryptoFields.SECRET_KEY;

public class CryptoUtil {

    public static String encrypt(String value, String publicKey) {
        try {
            String iv = IV_KEY + publicKey;
            Cipher cipher = Cipher.getInstance(CIPHER_METHOD);
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
            AlgorithmParameterSpec spec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return new String(Base64.encode(encrypted, Base64.DEFAULT)).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String decrypt(String cryptedText, String publicKey) {
        try {

            LogUtil.logInfo("AppLogEncrypt", cryptedText);
            LogUtil.logInfo("AppLogSuffix", IV_KEY + publicKey);

            String iv = IV_KEY + publicKey;
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding"); // Cipher.getInstance(CIPHER_METHOD);
            AlgorithmParameterSpec spec = new IvParameterSpec(iv.getBytes());
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(bytes);
            return new String(decrypted, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cryptedText;
    }


    public static String md5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(value.getBytes());
            return createHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String md5File(String filePath) {
        int bufferSize = 8192;
        try {
            FileInputStream fs = new FileInputStream(filePath);
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            byte[] buffer = new byte[bufferSize];
            int bytes;
            do {
                bytes = fs.read(buffer, 0, bufferSize);
                if (bytes > 0) messageDigest.update(buffer, 0, bytes);
            } while (bytes > 0);
            return createHexString(messageDigest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String createHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : bytes) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2) h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }

    public static String getEncryptionValue(String publicKey, Map<String, Object> requestMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = new JsonParser().parse(gson.toJson(requestMap));
        return CryptoUtil.encrypt(gson.toJson(jsonElement), publicKey);
    }

    private static final String RANDOM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String getSecureUrlParams(Context context) {
        return "?" + getSecureUrlParams(context, "d", "m");
    }

    /**
     * @param fieldOne temporarily set to 'd'
     * @param fieldTwo temporarily set to 'm'
     */
    public static String getSecureUrlParams(Context context, String fieldOne, String fieldTwo) {
        String salt = generateRandomChars(20);
        String firstParam = fieldOne + "=" + getEncodedAppInfo(getAppInfo(context));
        String secondParam = fieldTwo + "=" + getHash(getEncodedAppInfo(getAppInfo(context)), salt) + "-" + salt;
        return firstParam + "&" + secondParam;
    }

    public static String getUserAgent(Context context) {
        return "apps;" + getAppInfo(context);
    }

    public static String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            md.update(value.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%0" + (digest.length * 2) + 'x', new BigInteger(1, digest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAppInfo(Context context) {
        return "android;"
                + Build.VERSION.SDK_INT + ";"
                + BuildConfig.VERSION_CODE + ";"
                + SettingsUtil.getDeviceImei(context) + ";"
                + Build.MANUFACTURER + ";"
                + getDeviceName();
    }

    private static String getEncodedAppInfo(String appInfo) {
        byte[] data = new byte[0];
        data = appInfo.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        base64 = base64.replaceAll("([/])", "_");
        base64 = base64.replaceAll("([+])", "-");
        base64 = base64.replaceAll("([=])", "");
        return base64.trim();
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            model = model.split(manufacturer)[0];
            return capitalize(model);
        }
        return model;
    }

    public static String convertToBase64(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    private static String generateRandomChars(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(RANDOM.charAt(rnd.nextInt(RANDOM.length())));
        return sb.toString();
    }

    private static String getHash(String value, String salt) {
        return sha256(value + SECRET_KEY + salt);
    }

}