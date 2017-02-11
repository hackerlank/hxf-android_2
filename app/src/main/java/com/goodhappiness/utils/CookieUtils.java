package com.goodhappiness.utils;

import android.content.Context;
import android.util.Log;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.base.BaseFragmentActivity;

import org.xutils.http.cookie.DbCookieStore;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by 电脑 on 2016/5/16.
                                        */
                                        public class CookieUtils {
                                            public static String getCookies(final Context context) {
                                                DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
                                                List<HttpCookie> cookies = dbCookieStore.getCookies();
                                                StringBuffer s = new StringBuffer("");
                                                for(final HttpCookie cookie:cookies){
                                                    s.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
                                                    if(cookie.getValue().equals("")||cookie.getValue().equals("deleted")){
//                if (entity.getUri().contains(HttpFinal.LOGOUT)) {
                                                        if (context instanceof BaseActivity) {
                                                            ((BaseActivity) context).mHandler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ((BaseActivity) context).runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
                                                                            if (dbCookieStore.remove(null, new HttpCookie(cookie.getName(), ""))) {
                                                                                Log.e("coo" + cookie.getName(), "true");
                                                                            } else {
                                                                                Log.e("coo" + cookie.getName(), "false");
                                                                            }
                                    }
                                });
                            }
                        },2000);
            }
                    if (context instanceof BaseFragmentActivity) {
                        ((BaseFragmentActivity) context).mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((BaseFragmentActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
                                        if (dbCookieStore.remove(null, new HttpCookie(cookie.getName(), ""))) {
                                            Log.e("coo" + cookie.getName(), "true");
                                        } else {
                                            Log.e("coo" + cookie.getName(), "false");
                                        }
                                    }
                                });
                            }
                        },2000);
                    }
//                }
            }
        }
        PreferencesUtil.setPreferences(GoodHappinessApplication.getContext(), FieldFinals.COOKIE, s.toString());
        Log.e("cookie", s.toString());
        return s.toString();
    }
}
