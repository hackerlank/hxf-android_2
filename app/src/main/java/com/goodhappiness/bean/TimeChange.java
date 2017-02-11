package com.goodhappiness.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeChange {
    /**
     * 转换刷新时间格式
     */
    public static String getCurrentTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);

    }

    public static String getSeconds() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //设置时间
    public static String setTime(long total) {
        long longHour = total / (1000 * 60 * 60);
        long letf1 = total % (1000 * 60 * 60);
        long longMinute = letf1 / (1000 * 60);
        long left2 = letf1 % (1000 * 60);
        String hour = String.valueOf(longHour);
        hour = longHour < 10 ? "0" + longHour : hour;
        String min = String.valueOf(longMinute);
        min = longMinute < 10 ? "0" + longMinute : min;
        String second = String.valueOf((left2 / 1000)<10?"0"+(left2 / 1000):left2 / 1000);
        if (longHour != 0) {
            return hour + ":" + min + ":" + second;
        } else if (longHour == 0) {
            return min + ":" + second;
        }
        return hour + ":" + min + ":" + second;
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }
}
