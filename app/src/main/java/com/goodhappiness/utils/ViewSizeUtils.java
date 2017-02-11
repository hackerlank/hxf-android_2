package com.goodhappiness.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;

/**
 * Created by 电脑 on 2016/4/5.
 */
public class ViewSizeUtils {

    public static void setVisibility(View v,int a){
        v.setVisibility(a);
    }

    public static void setLinearLayoutViewWithFullWidth(int w, int h, View v,int padding) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.height = (int)((float)(GoodHappinessApplication.w-padding*GoodHappinessApplication.perHeight)*h/w);
        v.setLayoutParams(params);
    }
    public static void setRelativeLayoutViewWithFullWidth(int w, int h, View v,int padding) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.height = (int)((float)(GoodHappinessApplication.w-padding*GoodHappinessApplication.perHeight)*h/w);
        v.setLayoutParams(params);
    }

    public static void setLinearLayoutViewWithFullWidth(View v) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.height = GoodHappinessApplication.w;
        v.setLayoutParams(params);
    }
    public static void setLinearLayoutView(int w, int h, View v) {
        int width = (int) (w * GoodHappinessApplication.per);
        int height = (int) (h * GoodHappinessApplication.per);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.width = width;
        params.height = height;
        v.setLayoutParams(params);
    }

    public static void setRelativeLayoutView(int w, int h, View v) {
        int width = (int) (w * GoodHappinessApplication.per);
        int height = (int) (h * GoodHappinessApplication.per);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.width = width;
        params.height = height;
        v.setLayoutParams(params);
    }
}
