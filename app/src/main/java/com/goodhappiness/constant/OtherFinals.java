package com.goodhappiness.constant;

import android.os.Environment;

/**
 * Created by 电脑 on 2016/3/25.
 */
public class OtherFinals {
    public static final String GOOD_HAPPINESS_HEAD = "GoodHappiness";
    public static final String PATH_SD = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/"+GOOD_HAPPINESS_HEAD+"/";
    public static final String DIR_IMG = PATH_SD + "扑多相册/";
    public static final String DIR_IMG2 = PATH_SD + "image/";
    public static final String DIR_CACHE = PATH_SD + "cache/";
    public static final String DIR_APK = PATH_SD + "apk/";
    public static final String APK_NAME = "GoodHappiness.apk";
    public static final String APK = Environment.getExternalStorageDirectory() + "/"+APK_NAME;
}
