package com.goodhappiness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.goodhappiness.wxapi.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by 电脑 on 2016/4/20.
 */
public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        api.registerApp(Constants.WX_APP_ID);
    }
}
