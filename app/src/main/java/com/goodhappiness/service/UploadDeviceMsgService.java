package com.goodhappiness.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.utils.SystemUtils;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

public class UploadDeviceMsgService extends Service {
    public UploadDeviceMsgService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RequestParams params = new RequestParams("www.baidu.com");
        params.addBodyParameter("deviceId", SystemUtils.getDeviceId(this));
        params.addBodyParameter("deviceResolution", SystemUtils.getResolution(this));
        params.addBodyParameter("deviceSysVersion", SystemUtils.getSysVersion());
        params.addBodyParameter("deviceType", "android");
        //注册
        params.addBodyParameter("deviceIdentifier", "");
        params.addBodyParameter("mobile", "");
        params.addBodyParameter("password", "");
        params.addBodyParameter("code", "");
        //登录
        params.addBodyParameter("deviceIdentifier", "");
        params.addBodyParameter("mobile", "");
        params.addBodyParameter("password", "");
        //发送验证码
        params.addBodyParameter("deviceIdentifier", "");
        params.addBodyParameter("mobile", "");





        Type type = new TypeToken<Result<BaseBean>>() {
        }.getType();
//        HttpUtils.post(params, type, new OnHttpRequest() {
//            @Override
//            public void onSuccess(String result) {
//                stopSelf();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                stopSelf();
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//                stopSelf();
//            }
//
//            @Override
//            public void onFinished() {
//                stopSelf();
//            }
//        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
