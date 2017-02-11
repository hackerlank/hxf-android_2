package com.goodhappiness.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/11/27.
 */
public class SystemUtils {

    public static void endApp(){
        System.exit(0);
    }

    public static void systemBrowser(Context context, String URL) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(URL);
        intent.setData(url);
        context.startActivity(intent);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if(tm.getDeviceId()==null){
            return System.currentTimeMillis()+"";
        }
        return tm.getDeviceId();
    }
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int widthPixels= dm.widthPixels;
        int heightPixels= dm.heightPixels;
        return  widthPixels+"*"+heightPixels;
    }
    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
