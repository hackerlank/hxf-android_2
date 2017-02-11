package com.goodhappiness.utils;

import android.content.Context;

/**
 * Created by 电脑 on 2016/4/23.
 */
public class CarUtils {
    public static final String CAR_COUNT="carCount";
    public static void set(Context context,int count){
        PreferencesUtil.setPreferences(context,CAR_COUNT,count);
    }
    public static int get(Context context){
        return PreferencesUtil.getIntPreferences(context,CAR_COUNT,0);
    }
    public static void clear(Context context){
        PreferencesUtil.setPreferences(context,CAR_COUNT,0);
    }
}
