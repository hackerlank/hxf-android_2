package com.goodhappiness.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期转化工具
 *
 * @author lanyan
 */
public class DateUtil {

    /**
     * 将long型格式转化为所需格式的日期，以字符串返回
     *
     * @param time    需要转化的时间，单位是毫秒
     * @param pattern 待转化的格式
     * @return
     */
    public static String formatDate(long time, String pattern) {
        if (time < 0 || TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    /**
     * <<<<<<< .mine 将long型格式转化为所需格式的日期，以字符串返回
     *
     * @param time    需要转化的时间，单位是毫秒
     * @param pattern 待转化的格式
     * @return
     */
    public static String format(long time, String pattern) {
        if (time < 0 || TextUtils.isEmpty(pattern)) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(calendar.getTime());
    }

    /**
     * 将string型格式转化为所需格式的日期，以字符串返回
     *
     * @param time     需要转化的时间
     * @param previous 转化前的格式
     * @param pattern  待转化的格式
     * @return
     */
    public static String format(String time, String previous, String pattern) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(previous, Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            Date dt = sdf1.parse(time);
            return sdf2.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取与当前时间的间隔毫秒数
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long getIntervalTime(String time, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            Date date = df.parse(time);
            return date.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将毫秒数转成天 小时 分 秒
     *
     * @param time
     * @return
     */
    public static String calculateTimeBySec(long time) {
        if (time < 0) {
            return null;
        }
        int days = (int) (time / 86400000);
        int hour = (int) ((time - days * 86400000) / 3600000);
        int minute = (int) ((time - days * 86400000 - hour * 3600000) / 60000);
        int second = (int) ((time - days * 86400000 - hour * 3600000 - minute * 60000) / 1000);
        if (days < 1) {
            if (hour < 1) {
                if (minute < 1) {
                    return second + "秒";
                } else {
                    return minute + "分" + second + "秒";
                }
            } else {
                return hour + "小时" + minute + "分" + second + "秒";
            }
        } else {
            return days + "天" + hour + "小时" + minute + "分" + second + "秒";
        }
    }

    public static String getPublishTime(long created) {
        long time = System.currentTimeMillis() - created*1000;
        if (time < 0) {
            return "";
        }
        int days = (int) (time / 86400000);
        int hour = (int) ((time - days * 86400000) / 3600000);
        int minute = (int) ((time - days * 86400000 - hour * 3600000) / 60000);
        int second = (int) ((time - days * 86400000 - hour * 3600000 - minute * 60000) / 1000);
        if (days < 1) {
            if (hour < 1) {
                if (minute < 1) {
                    return "刚刚";
                } else {
                    return minute + "分钟前";
                }
            } else {
                return hour + "小时前";
            }
        } else {
            if (days <= 7) {
                return days + "天前";
            } else {
                return dateFormat(created,"");
            }
        }
    }

    /**
     * 比较2个时间的大小
     *
     * @param time1   第一个时间
     * @param format1 第一个时间的时间个时
     * @param time2   第二个时间
     * @param format2 第二个时间的时间格式
     * @return 1 第一个时间迟;-1 第二个时间迟;0 相等
     */
    public static int compareDates(String time1, String format1, String time2,
                                   String format2) {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)
                || TextUtils.isEmpty(format1) || TextUtils.isEmpty(format2)) {
            return 0;
        }
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format1, Locale.CHINA);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.CHINA);
            Date d1 = sdf1.parse(time1);
            Date d2 = sdf2.parse(time2);
            if (d1.after(d2)) {
                return 1;
            } else if (d1.before(d2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs) {
        long totalSeconds = timeMs / 1000;

        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        StringBuilder mFormatBuilder = new StringBuilder();
        @SuppressWarnings("resource")
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.ENGLISH);
        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
                .toString();
    }

    /**
     * 将长时间格式字符串转换为字符串,默认为yyyy-MM-dd HH:mm:ss
     *
     * @param dataFormat 需要返回的时间格式，例如： yyyy-MM-dd， yyyy-MM-dd HH:mm:ss
     * @return dataFormat格式的时间结果字符串
     */
    public static String dateFormat(long milliseconds, String dataFormat) {
        long tempTimestamp = milliseconds > 9999999999L ? milliseconds
                : milliseconds * 1000;
        if (TextUtils.isEmpty(dataFormat)) {
            dataFormat = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = new Date(tempTimestamp * 1l);
        SimpleDateFormat formatter = new SimpleDateFormat(dataFormat);
        return formatter.format(date);
    }
}
