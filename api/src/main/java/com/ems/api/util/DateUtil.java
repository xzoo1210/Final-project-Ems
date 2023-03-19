package com.ems.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String DATETIME_FORMAT = "dd/MM/yyyy hh:mm:ss";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DIR = "yyyy/MM/dd";
    public static final String DEFAULT_TIMEZOE = "Asia/Ho_Chi_Minh";

    public static String formatDate(Date date, String format) {
        return formatDate(date,format,DEFAULT_TIMEZOE);
    }
    public static String formatDate(Date date) {
        return formatDate(date,DATETIME_FORMAT);
    }

    public static String formatDate(Date date, String format,String timezone) {
        if(date==null)return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(date);
    }

    public static String formatNow(String format) {
        return formatDate(new Date(), format);
    }
}
