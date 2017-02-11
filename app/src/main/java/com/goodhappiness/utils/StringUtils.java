package com.goodhappiness.utils;

import android.text.TextUtils;

import com.goodhappiness.bean.BannerData;
import com.goodhappiness.bean.BaseBean;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by 电脑 on 2016/6/6.
 */
public class StringUtils {

    public static BaseBean toGsonBean(String s, Type type){
        if(TextUtils.isEmpty(s)){
            return null;
        }
        if(type==null){
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(s,type);
    }

    public static String[] getWinner(BannerData.WinnersBean winner){
        String[] s = new String[4];
        s[0] = "恭喜";
        s[1] = winner.getNickname();
        s[2] = DateUtil.getPublishTime(winner.getLotteryTimestamp())+"获得";
        s[3] = winner.getName();
        return s;
    }
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || input.equals("null"))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }

        return true;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }
}
