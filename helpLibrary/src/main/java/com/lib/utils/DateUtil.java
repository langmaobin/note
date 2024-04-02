package com.lib.utils;

import android.content.Context;

import com.lib.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final String DATE_FORMAT_DISPLAY = "yyyy-MM-dd HH:mm"; // "dd MMM yyyy, hh:mmaa";
    private static final String DATE_FORMAT_DISPLAY_ZH = "yyyy'年'MM'月'dd'日', HH:mm";
    private static final String DATE_FORMAT_SERVER = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_DISPLAY_ZH2 = "yyyy'年'MM'月'dd'日'";

    private static final String DATE_FORMAT_SHORT_TIME = "h:mm a";
    private static final String DATE_FORMAT_SHORT_DAY_AND_TIME = "EEEE, h:mma";
    private static final String DATE_FORMAT_SHORT_DATE_AND_TIME = "MMM d, h:mma";
    private static final String DATE_FORMAT_SHORT_DATE_AND_TIME_ZH = "'MM'月'dd'日', h:mma";
    private static final String DATE_FORMAT_LONG_DATE_AND_TIME = "yyyy MMM d, h:mma";
    private static final String DATE_FORMAT_LONG_DATE_AND_TIME_ZH = "yyyy'年'MM'月'dd'日', h:mma";

    private static final String DATE_FORMAT_SHORT_DATE_NRIC = "yyMMdd";
    private static final String DATE_FORMAT_SHORT_DATE = "yyyy-MM-dd";
    private static final String DATE_FORMAT_SHORT_DATE_REVERSE = "yyyy-MM-dd";
    private static final String DATE_FORMAT_LONG_DATE_REVERSE = "yyyy-MM-dd, hh:mm:ss a";

    private static final String DATE_FORMAT_SHORT_DATE_MONTH_YEAR = "dd/MM";
    private static final String DATE_FORMAT_LONG_DATE = "yyyy-MM-dd-HH-mm-ss";
    private static final String DATE_FORMAT_SHORT_TIME_24HRS = "HH:mm:ss";
    private static final String DATE_FORMAT_SHORT_DATE_DD_MM_YYYY = "dd MMM yyyy";
    private static final String DATE_FORMAT_SHORT_DATE_DD_MM_YYYY_ZH = "yyyy'年'MM'月'dd'日'";
    private static final String DATE_FORMAT_MMM_DD_YYYY = "MMMdd ,yyyy";
    private static final String DATE_FORMAT_FILL_FORMATTED = "dd MMM yyyy, h:mm a";
    private static final String DATE_FORMAT_FILL_FORMATTED_ZH = "yyyy'年'MM'月'dd'日, h:mm a";
    private static final String DATE_FORMAT_START_WITH_DAY = "EEE, yyyy-MM-dd, HH:mm:ss";

    private static final String DATE_FORMAT_MONTH = "MMMM_yyyy";
    private static final String DATE_FORMAT_MONTH_ZH = "MM'月'_yyyy";

    public static final String DATE_FORMATTED_1 = "DATE_FORMAT_DISPLAY [dd-MM-yyyy HH:mm]";
    public static final String DATE_FORMATTED_2 = "DATE_FORMAT_MONTH [MMMM_yyyy]";
    public static final String DATE_FORMATTED_3 = "DATE_FORMAT_SHORT_DATE_DD_MM_YYYY [dd MMM yyyy]";
    public static final String DATE_FORMATTED_4 = "DATE_FORMAT_SHORT_DATE [dd-MM-yyyy]";
    public static final String DATE_FORMATTED_5 = "DATE_FORMAT_MONTH [MMMM yyyy]";
    public static final String DATE_FORMATTED_6 = "DATE_FORMAT_FILL_FORMATTED [dd MMM yyyy, h:mm a]";
    public static final String DATE_FORMATTED_7 = "DATE_FORMAT_SERVER [yyyy-MM-dd HH:mm:ss]";
    public static final String DATE_FORMATTED_8 = "DATE_FORMAT_START_WITH_DAY [EEE, yyyy-MM-dd, HH:mm:ss]";
    public static final String DATE_FORMATTED_9 = "DATE_FORMAT_SHORT_DATE_REVERSE [yyyy-MM-dd]";
    public static final String DATE_FORMATTED_10 = "DATE_FORMAT_LONG_DATE_REVERSE [yyyy-MM-dd, hh:mm:ss a]";

    public static Date getLocalDateNow() {
        return Calendar.getInstance(Locale.ENGLISH).getTime();
    }

    public static String getDateStringForDisplay(int year, int month, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, days);
        return getStringFromDate(getLocaleDateFormatDisplay(), calendar.getTime());
    }

    public static String getDateStringForServer(int year, int month, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, days);
        return getStringFromDate(DATE_FORMAT_SERVER, calendar.getTime());
    }

    public static String getDateStringForShortDate(int year, int month, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, days);
        return getStringFromDate(DATE_FORMAT_SHORT_DATE, calendar.getTime());
    }

    public static String checkMiniSecond(long longDate, String dateFormatted) {
        if (longDate > 0L) {
            switch (dateFormatted) {
                case DATE_FORMATTED_1:
                    return getDateStringDisplay(new Date(longDate));
                case DATE_FORMATTED_2:
                    return getDateWithFullMonth(new Date(longDate));
                case DATE_FORMATTED_3:
                    return getDateInShortDateDDMMYYY(new Date(longDate));
                case DATE_FORMATTED_4:
                    return getDateStringShortDate(new Date(longDate));
                case DATE_FORMATTED_5:
                    return getDateWithFullMonth(new Date(longDate)).replace("_", " ");
                case DATE_FORMATTED_6:
                    return getDateStringLongDateFullFormatted(new Date(longDate));
                case DATE_FORMATTED_7:
                    return getDateStringServer(new Date(longDate));
                case DATE_FORMATTED_8:
                    return getDateStringLongDateStartFromDay(new Date(longDate));
                case DATE_FORMATTED_9:
                    return getDateStringShortReverseDate(new Date(longDate));
                case DATE_FORMATTED_10:
                    return getDateStringLongReverseDate(new Date(longDate));
                default:
                    return "-";
            }
        } else {
            return "-";
        }
    }

    /**
     * Get date format based on locale
     */

    private static String getLocaleDateFormatDisplay() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_DISPLAY_ZH;
        }
        return DATE_FORMAT_DISPLAY;
    }

    private static String getLocaleShortDateAndTime() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_SHORT_DATE_AND_TIME_ZH;
        }
        return DATE_FORMAT_SHORT_DATE_AND_TIME;
    }

    private static String getLocaleLongDateAndTime() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_LONG_DATE_AND_TIME_ZH;
        }
        return DATE_FORMAT_LONG_DATE_AND_TIME;
    }

    private static String getLocaleLongMonth() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_MONTH_ZH;
        }
        return DATE_FORMAT_MONTH;
    }

    private static String getLocaleShortDateDDMMYYYY() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_SHORT_DATE_DD_MM_YYYY_ZH;
        }
        return DATE_FORMAT_SHORT_DATE_DD_MM_YYYY;
    }

    private static String getLocaleShortDateFullFormatted() {
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.CHINESE)) {
            return DATE_FORMAT_FILL_FORMATTED_ZH;
        }
        return DATE_FORMAT_FILL_FORMATTED;
    }

    /**
     * Get string from date
     */

    public static String getDateStringDisplay(Date date) {
        return getStringFromDate(getLocaleDateFormatDisplay(), date);
    }

    public static String getDateStringServer(Date date) {
        return getStringFromDate(DATE_FORMAT_SERVER, date);
    }

    public static String getDateStringShortTime(Date date) {
        return getStringFromDate(DATE_FORMAT_SHORT_TIME, date);
    }

    public static String getDateStringShortDayAndTime(Date date) {
        return getStringFromDate(DATE_FORMAT_SHORT_DAY_AND_TIME, date);
    }

    public static String getDateStringShortDateAndTime(Date date) {
        return getStringFromDate(getLocaleShortDateAndTime(), date);
    }

    public static String getDateStringLongDateAndTime(Date date) {
        return getStringFromDate(getLocaleLongDateAndTime(), date);
    }

    public static String getDateStringLongDateFullFormatted(Date date) {
        return getStringFromDate(getLocaleShortDateFullFormatted(), date);
    }
    public static String getDateStringLongDateStartFromDay(Date date) {
        return getStringFromDate(DATE_FORMAT_START_WITH_DAY, date);
    }

    public static String getDateStringShortTime24Hrs(Date date) {
        return getStringFromDate(DATE_FORMAT_SHORT_TIME_24HRS, date);
    }

    public static String getDateStringShortDate(Date date) {
        return getStringFromDate(DATE_FORMAT_SHORT_DATE, date);
    }

    public static String getDateStringShortReverseDate(Date date) {
        return getStringFromDate(DATE_FORMAT_SHORT_DATE_REVERSE, date);
    }

    public static String getDateStringLongReverseDate(Date date) {
        return getStringFromDate(DATE_FORMAT_LONG_DATE_REVERSE, date);
    }
    public static String getDateStringLongDate(Date date) {
        return getStringFromDate(DATE_FORMAT_LONG_DATE, date);
    }

    public static String getDateWithFullMonth(Date date) {
        return getStringFromDate(getLocaleLongMonth(), date);
    }

    public static String getDateInShortDateDDMMYYY(Date date) {
        return getStringFromDate(getLocaleShortDateDDMMYYYY(), date);
    }

    public static String getDateMMDDYYYDisplay(Date date) {
        return getStringFromDate(DATE_FORMAT_MMM_DD_YYYY, date);
    }

    public static String getStringFromDate(String dateFormat, Date date) {
        return getStringFromDate(dateFormat, date, TimeZone.getDefault());
    }

    public static String getCurrentHour() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static String getCurrentYearAndMonth() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static String getCurrentYearAndMonth2() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getStringFromDate(String dateFormat, Date date, TimeZone timezone) {
        try {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"am", "pm"});
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            simpleDateFormat.setDateFormatSymbols(symbols);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.setTimeZone(timezone);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get date from string
     */

    public static Date getDateInDisplayFormat(String dateString) {
        return getDateFromString(getLocaleDateFormatDisplay(), dateString);
    }

    public static Date getDateInServerFormat(String dateString) {
        return getDateFromString(DATE_FORMAT_SERVER, dateString);
    }

    public static Date getDateInShortTime(String dateString) {
        return getDateFromString(DATE_FORMAT_SHORT_TIME, dateString);
    }

    public static Date getDateInShortDayAndTime(String dateString) {
        return getDateFromString(DATE_FORMAT_SHORT_DAY_AND_TIME, dateString);
    }

    public static Date getDateInShortDateAndTime(String dateString) {
        return getDateFromString(getLocaleShortDateAndTime(), dateString);
    }

    public static Date getDateInLongDateAndTime(String dateString) {
        return getDateFromString(getLocaleLongDateAndTime(), dateString);
    }

    public static Date getDateInShortDate(String dateString) {
        return getDateFromString(DATE_FORMAT_SHORT_DATE, dateString);
    }

    public static Date getDateDDMMYYYY(String dateString) {
        return getDateFromString(DATE_FORMAT_DISPLAY_ZH2, dateString);
    }

    public static Date getDateFromString(String dateFormat, String dateString) {
        return getDateFromString(dateFormat, dateString, TimeZone.getDefault());
    }

    public static Date getDateFromString(String dateFormat, String dateString, TimeZone timezone) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.setTimeZone(timezone);
            return simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String-to-string conversion
     */

    public static String getDateStringFromString(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SERVER, DATE_FORMAT_DISPLAY_ZH2);
    }

    public static String getDateStringFromShortDateToShortDateReverse(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SHORT_DATE, DATE_FORMAT_SHORT_DATE_REVERSE);
    }

    public static String getDateStringFromShortDateReverseToShortDate(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SHORT_DATE_REVERSE, DATE_FORMAT_SHORT_DATE);
    }

    public static String getDateStringFromShortDateNricToShortDate(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SHORT_DATE_NRIC, DATE_FORMAT_SHORT_DATE);
    }

    public static String getDateStringFromShortDateNricToShortDateWithYearCheck(String dateString) {
        String date = getStringToStringDateConversion(dateString, DATE_FORMAT_SHORT_DATE_NRIC, DATE_FORMAT_SHORT_DATE_REVERSE);
        if (date != null) {
            int year = ParseUtil.parseInt(date.substring(0, 4));
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            while (year >= currentYear) {
                year -= 100;
            }
            date = year + date.substring(4);
        }
        return date;
    }

    public static String getDateStringFromServerToDisplay(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SERVER, getLocaleDateFormatDisplay());
    }

    public static String getDateStringFromServerToShortDate(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SERVER, DATE_FORMAT_SHORT_DATE);
    }

    public static String getDateStringFromShortDateToShortMonthYear(String dateString) {
        return getStringToStringDateConversion(dateString, DATE_FORMAT_SHORT_DATE_REVERSE, DATE_FORMAT_SHORT_DATE_MONTH_YEAR);
    }

    public static String getStringToStringDateConversion(String dateString, String fromDateFormat, String toDateFormat) {
        Date date = getDateFromString(fromDateFormat, dateString);
        return getStringFromDate(toDateFormat, date);
    }

    /**
     * Get time ago
     */

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 7 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 365 * DAY_MILLIS;

    public static String getTimeAgo(Context context, String dateString) {
        return getTimeAgo(context, DATE_FORMAT_SERVER, dateString);
    }

    public static String getTimeAgo(Context context, String dateFormat, String dateString) {
        Date date = getDateFromString(dateFormat, dateString);
        if (context != null && date != null) {
            return getTimeAgo(context, date.getTime());
        }
        return dateString;
    }

    private static String getTimeAgo(Context context, long time) {

        long timeNow = new Date().getTime();
        long timeDiff = timeNow - time;

        if (time < 1000000000000L) {
            time *= 1000;
        }

        if (timeDiff < MINUTE_MILLIS) {
            return context.getString(R.string.__T_time_text_just_now);
        } else if (timeDiff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.__T_time_text_min_ago);
        } else if (timeDiff < HOUR_MILLIS) {
            return context.getString(R.string.__T_time_text_mins_ago, timeDiff / MINUTE_MILLIS);
        } else if (timeDiff < 120 * MINUTE_MILLIS) {
            return context.getString(R.string.__T_time_text_hr_ago);
        } else if (timeDiff < DAY_MILLIS) {
            return context.getString(R.string.__T_time_text_hrs_ago, timeDiff / HOUR_MILLIS);
        } else if (timeDiff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.__T_time_text_yesterday);
        } else if (timeDiff < WEEK_MILLIS) {
            return getDateStringShortDayAndTime(new Date(time));
        } else if (timeDiff < YEAR_MILLIS) {
            return getDateStringShortDateAndTime(new Date(time));
        }
        return getDateStringLongDateAndTime(new Date(time));

    }

    /**
     * Get days, hours, minutes format for countdown
     */

    public static String formatSeconds(long timeInSeconds) {

        int days = (int) TimeUnit.SECONDS.toDays(timeInSeconds);
        long hours = TimeUnit.SECONDS.toHours(timeInSeconds) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds) - (TimeUnit.SECONDS.toHours(timeInSeconds) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(timeInSeconds) - (TimeUnit.SECONDS.toMinutes(timeInSeconds) * 60);

        String formattedTime = "";

        if (days > 0) {
            if (days < 10) formattedTime += "0";
            formattedTime += days + " " + (days == 1 ? "day" : "days") + " ";
        } else {
            if (hours < 10) formattedTime += "0";
            formattedTime += hours + ":";

            if (minutes < 10) formattedTime += "0";
            formattedTime += minutes + ":";

            if (seconds < 10) formattedTime += "0";
            formattedTime += seconds;
        }

        return formattedTime;

    }

    public static String formatSecondsToMinSec(long timeInSeconds) {

        long minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds) - (TimeUnit.SECONDS.toHours(timeInSeconds) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(timeInSeconds) - (TimeUnit.SECONDS.toMinutes(timeInSeconds) * 60);

        String formattedTime = "";

        if (minutes < 10) formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10) formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;

    }

}
