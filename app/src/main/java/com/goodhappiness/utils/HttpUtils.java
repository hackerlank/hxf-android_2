package com.goodhappiness.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.HttpCookie;
import java.net.SocketTimeoutException;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by 电脑 on 2016/3/28.
 */

public class HttpUtils {
    public static void synCookies(Context context, String url) {
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
            List<HttpCookie> cookies = dbCookieStore.getCookies();
            for (HttpCookie cookie : cookies) {

                cookieManager.setCookie(url, cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain() + "; path=" + cookie.getPath());
                Log.e("coo_1", cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain() + "; path=" + cookie.getPath());
            }
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
        }
    }

    public static Result post(String url, List<NameValuePair> params, final Type type) {
        HttpPost httpRequest = new HttpPost(url);
        Result result = null;
        try {
            // 设置字符集
            HttpEntity httpentity = new UrlEncodedFormEntity(params, "gb2312");
            // 请求httpRequest
            httpRequest.setEntity(httpentity);
            // 取得默认的HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();
            // 取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            int a = httpResponse.getStatusLine().getStatusCode();
            // HttpStatus.SC_OK表示连接成功
            {
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)

                {
                    // 取得返回的字符串
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    List<Cookie> cookies = httpclient.getCookieStore().getCookies();
                    if (cookies.size() > 1) {
                        String oldCookie =
                                cookies.get(0).getName() + "=" + cookies.get(0).getValue() + ";" +
                                        cookies.get(1).getName() + "=" + cookies.get(1).getValue() + ";";
                        PreferencesUtil.setPreferences(GoodHappinessApplication.getContext(), FieldFinals.COOKIE, oldCookie);
                    }
                    Gson mGson = new Gson();
                    result = mGson.fromJson(strResult, type);
                    return result;
                } else
                    return result;
            }
        } catch (Exception e) {
            return result;
        }
    }

    public static void get(RequestParams entity, final Type type, final OnHttpRequest onHttpRequest) {
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Gson mGson = new Gson();
                    Result res = mGson.fromJson(result, type);
                    if (res != null) {
                        Log.e("t", res.toString());
                        if (onHttpRequest != null) {
                            onHttpRequest.onSuccess(res);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                if (onHttpRequest != null) {
                    onHttpRequest.onError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), cex.getMessage(), Toast.LENGTH_LONG).show();
                if (onHttpRequest != null) {
                    onHttpRequest.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (onHttpRequest != null) {
                    onHttpRequest.onFinished();
                }
            }
        });
    }

    public static void post(final Context context, final RequestParams entity, final Type type, final OnHttpRequest onHttpRequest) {
        addSign(entity);
        Log.e("k_params_" + entity.getUri(), entity.toString());
        if (entity.getUri().contains(HttpFinal.LOGOUT)) {
            RongIM.getInstance().logout();
        }
        entity.setUseCookie(true);
        entity.setConnectTimeout(30000);
        x.http().post(entity, new Callback.ProgressCallback<String>() {

            @Override
            public void onWaiting() {
                if (onHttpRequest != null) {
                    onHttpRequest.onWaiting();
                }
            }

            @Override
            public void onStarted() {
                if (onHttpRequest != null) {
                    onHttpRequest.onStarted();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (onHttpRequest != null) {
                    onHttpRequest.onLoading(total, current, isDownloading);
                }
            }

            @Override
            public void onSuccess(String result) {
                Log.e("k_result_" + entity.getUri(), result);
                CookieUtils.getCookies(context);
                if (!TextUtils.isEmpty(result)) {
                    Gson mGson = new Gson();
                    Result res = mGson.fromJson(result, type);
                    if (res != null) {
                        if (res.getCode() == 51005) {//&&TextUtils.isEmpty(PreferencesUtil.getStringPreferences(context,FieldFinals.SID))){
                            logout(context);
                            return;
                        }
                        if ("success".equals(res.getStatus())) {
                            Log.e("t", res.toString());
                                if (onHttpRequest != null) {
                                onHttpRequest.onSuccess(res);
                            }
                        } else {
                            BaseBean bean = (BaseBean) res.getData();
                            Toast.makeText(x.app(), bean.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof SocketTimeoutException) {
                    Toast.makeText(x.app(), "访问超时，请重新访问", Toast.LENGTH_LONG).show();
                }
                if (onHttpRequest != null) {
                    onHttpRequest.onError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (onHttpRequest != null) {
                    onHttpRequest.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (onHttpRequest != null) {
                    onHttpRequest.onFinished();
                }
            }
        });
    }

    public static void logout(final Context context) {
//        DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
//        List<HttpCookie> cookies = dbCookieStore.getCookies();
//        for (HttpCookie cookie : cookies) {
//            if (cookie.getName().contains(FieldFinals.SID)) {
//                dbCookieStore.remove(null,cookie);
//                break;
//            }
//        }
        RequestParams params = new RequestParams(HttpFinal.LOGOUT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(context, FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter(FieldFinals.DEVICETYPE, FieldFinals.ANDROID);
        params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getStringPreferences(context, FieldFinals.SID));
        HttpUtils.post(context, params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {

            @Override
            public void onSuccess(Result result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                PreferencesUtil.clearUserInfo(context);
                HomepageActivity.type = HomepageActivity.SHOP;
                RongIM.getInstance().logout();
                Intent intent = new Intent(context, HomepageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }

    private static void addSign(RequestParams entity) {
        entity.addBodyParameter(FieldFinals.TIME, System.currentTimeMillis() + "");
        List<KeyValue> keyValues = entity.getQueryStringParams();
        StringBuffer sign = new StringBuffer();
        for (KeyValue keyValue : keyValues) {
            sign.append(keyValue.key).append(keyValue.value);
        }
        sign.append(HttpFinal.AUTH_KEY);
        SHA1 sha1 = new SHA1();
        entity.addBodyParameter(FieldFinals.SIGN, sha1.getDigestOfString(sign.toString().getBytes()).toUpperCase());
    }

    public static Object postSync(RequestParams entity, final Type type) {
        String result;
        try {
            result = x.http().postSync(entity, String.class);
        } catch (Throwable throwable) {
            result = "";
        }
        if (!TextUtils.isEmpty(result)) {
            Log.e("k_", result);
            Gson mGson = new Gson();
            Result res = mGson.fromJson(result, type);
            if (res != null) {
                if ("success".equals(res.getStatus())) {
                    return res.getData();
                } else {
                    BaseBean bean = (BaseBean) res.getData();
                    return null;
                }
            }
        }
        return null;
    }

    public static Object postSyncFastJson(RequestParams entity, Object o) {
        String result;
        try {
            addSign(entity);
            result = x.http().postSync(entity, String.class);
        } catch (Throwable throwable) {
            result = "";
        }
        if (!TextUtils.isEmpty(result)) {
            Log.e("k_", result);
            Gson gson = new Gson();
            Result<BaseBean> beanResult = gson.fromJson(result, new TypeToken<Result<BaseBean>>() {
            }.getType());
//            Result<BaseBean> beanResult = JSON.parseObject(result, Result.class);
            if (beanResult != null) {
                Object re = JSON.parseObject(JSON.toJSONString(beanResult.getData()), o.getClass());
                if (re != null) {
                    if ("success".equals(beanResult.getStatus())) {
                        return re;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
