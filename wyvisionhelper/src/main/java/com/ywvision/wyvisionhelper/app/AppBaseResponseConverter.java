package com.ywvision.wyvisionhelper.app;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.lib.utils.LibUtil;
import com.lib.utils.LogUtil;
import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.utils.Base64Utils;
import com.ywvision.wyvisionhelper.utils.CryptoUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.lib.base.BaseConstants.Tag.TAG_RESPONSE;

public class AppBaseResponseConverter implements JsonDeserializer<AppBaseResponse> {

    @Override
    public AppBaseResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        printPrettyJson(json);

        AppBaseResponse baseResponse = new AppBaseResponse();
        JsonElement dataEncrytp = json.getAsJsonObject().get("data");
        JsonElement suffix = json.getAsJsonObject().get("suffix");

        if (!ValidUtil.isEmpty(dataEncrytp) && !ValidUtil.isEmpty(suffix)) {
            String encrypt = !dataEncrytp.isJsonNull() ? dataEncrytp.getAsString() : "";
            String publicKey = !suffix.isJsonNull() ? suffix.getAsString() : "";

            String dataResult = CryptoUtil.decrypt(encrypt, publicKey);

            JsonElement jsonElement = new JsonParser().parse(dataResult);
            LogUtil.logInfo("AppLogResult", String.valueOf(jsonElement));
            baseResponse = getResponse(jsonElement);
        } else {
            JsonElement data = json.getAsJsonObject().get("data");
            if (data.isJsonObject()) {
                JsonElement lbapi = data.getAsJsonObject().get("syapi");
                if (lbapi != null && !lbapi.isJsonNull()) {
                    baseResponse.success = true;
                    baseResponse.data = data != null && !data.isJsonNull() ? data.toString() : null;
                } else {
                    baseResponse.success = false;
                }
            } else {
                String string2 = "";
                try {
                    //我要获取当前的日期
                    Date date1 = new Date();
                    //设置要获取到什么样的时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createdate = sdf.format(date1);
                    //获取当前时间对应的格林时间
                    String gtime = LocalToGTM(createdate);
                    LogUtil.logInfo("gtime:", gtime);
                    //将格林时间时分秒变0，获取格林时间0时的时间戳
                    String [] t=gtime.split(" ");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf2.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                    Date date2 = sdf2.parse(t[0]+" 00:00:00");
                    long timeMilli = date2.getTime() / 1000;

//                    Calendar day = Calendar.getInstance();
//                    day.set(Calendar.MILLISECOND, 0);
//                    day.set(Calendar.SECOND, 0);
//                    day.set(Calendar.MINUTE, 0);
//                    day.set(Calendar.HOUR_OF_DAY, 0);
//                    Date date = day.getTime();
//                    long timeMilli = date.getTime() / 1000;
                    String midnightEncode = Base64Utils.encode(String.valueOf(timeMilli).getBytes());
                    String str1 = midnightEncode;
                    String str2 = String.valueOf(data);
                    byte[] dByte1 = getStringByte(str1);
                    byte[] dByte2 = Base64Utils.decode(str2);
                    byte[] dByte3 = new byte[dByte2.length];
                    for (int a = 0; a < dByte2.length; a++) {
                        int temp1 = dByte1[a % dByte1.length] & 0xFF;
                        int temp2 = dByte2[a] & 0xFF;
                        int r = temp2 - temp1;
                        if (r < 0) {
                            r = r + 256;
                        }
                        dByte3[a] = (byte) r;
                    }
                    String string1 = new String(dByte3);
                    byte[] dByte4 = Base64Utils.decode(string1);
                    string2 = new String(dByte4);
                } catch (Exception e) {
                }
                baseResponse.success = true;
                baseResponse.data = string2;
            }
        }
        printPrettyJson(baseResponse.data);
        return baseResponse;

    }


    public byte[] getStringByte(String a) {
        try {
            return a.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
    }

    private AppBaseResponse getResponse(JsonElement json) {
        AppBaseResponse baseResponse = new AppBaseResponse();

        JsonElement jsonStatusCode = json.getAsJsonObject().get("code");
        baseResponse.statusCode = jsonStatusCode != null && !jsonStatusCode.isJsonNull() ? jsonStatusCode.getAsString() : "-1";

        baseResponse.success = baseResponse.statusCode.equals("200");

        JsonElement jsonStatusMessage = json.getAsJsonObject().get("msg");
        baseResponse.message = jsonStatusMessage != null && !jsonStatusMessage.isJsonNull() ? jsonStatusMessage.getAsString() : "";

        if (baseResponse.success) {
            JsonElement jsonData = json.getAsJsonObject().get("data");
            baseResponse.data = jsonData != null && !jsonData.isJsonNull() ? jsonData.toString() : null;
        } else {
            baseResponse.data = null;
        }
        return baseResponse;
    }


    private void printPrettyJson(JsonElement jsonElement) {
        if (jsonElement != null && !jsonElement.isJsonNull()) {
            LibUtil.printPrettyJson(TAG_RESPONSE, jsonElement.toString());
        }
    }

    private void printPrettyJson(String jsonData) {
        if (jsonData != null) {
            LogUtil.logInfo(TAG_RESPONSE + "Crypt", jsonData);
            LibUtil.printPrettyJson(TAG_RESPONSE + "Crypt", jsonData);
        }
    }

    private String LocalToGTM(String LocalDate) {
        SimpleDateFormat format;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date result_date;
        long result_time = 0;
        if (null == LocalDate) {
            return LocalDate;
        } else {
            try {
                format.setTimeZone(TimeZone.getDefault());
                result_date = format.parse(LocalDate);
                result_time = result_date.getTime();
                format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
                return format.format(result_time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return LocalDate;
    }

}