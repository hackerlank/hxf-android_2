package com.goodhappiness.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.DisplayMetrics;

/**
 * Created by 电脑 on 2016/9/8.
 */
public class PhoneUtil {
    /**
     * get resolution
     *
     * @param context
     * @return
     */
    public static int[] getResolution(Context context) {
        int resolution[] = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        PhoneManager.getWindowManger(context).getDefaultDisplay()
                .getMetrics(dm);
        resolution[0] = dm.widthPixels;
        resolution[1] = dm.heightPixels;
        return resolution;
    }
    public static double[] getGpsData(Context context) {
        double[] long_lat = new double[2];
        LocationManager locationManager = null;
        try {
            // A LocationManager for controlling location (e.g., GPS) updates.
            locationManager = PhoneManager.getLocationManager(context);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
            criteria.setAltitudeRequired(false);// 不要求海拔
            criteria.setBearingRequired(false);// 不要求方位
            criteria.setCostAllowed(true);// 允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
            // 从可用的位置提供器中，匹配以上标准的最佳提供器
            String provider = locationManager.getBestProvider(criteria, true);

            // 获得最后一次变化的位置
            Location location = locationManager.getLastKnownLocation(provider);
            if (null == location) {
                return null;
            }
            long_lat[0] = location.getLongitude();
            long_lat[1] = location.getLatitude();

        } catch (Exception e) {
            return null;
        }

        return long_lat;
    }
}
