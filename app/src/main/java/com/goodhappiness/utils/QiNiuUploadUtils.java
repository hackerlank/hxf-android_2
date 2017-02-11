package com.goodhappiness.utils;

import android.content.Context;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.bean.QINIUToken;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

/**
 * Created by 电脑 on 2016/5/9.
 */
public class QiNiuUploadUtils {

    public static String appendKey(String... keys) {
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append("[");
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                stringBuffer.append('"').append(keys[i]).append('"').append(",");
            } else {
                stringBuffer.append(keys[i]);
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public static void getToken(Context context,final onGetTokenListener onGetTokenListener) {
        RequestParams params = new RequestParams(HttpFinal.QINIU_TOKEN);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(), FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(), FieldFinals.SID));
        HttpUtils.post(context,params, new TypeToken<Result<QINIUToken>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }
            @Override
            public void onSuccess(Result result) {
                QINIUToken qiniuToken = (QINIUToken) result.getData();
                onGetTokenListener.onGetToken(qiniuToken);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onGetTokenListener.onGetToken(null);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface onGetTokenListener {
        void onGetToken(QINIUToken qiniuToken);
    }
}
