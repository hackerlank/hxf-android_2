package com.goodhappiness.dao;

import android.content.Context;
import android.content.Intent;

import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.utils.IntentUtils;

import io.rong.imlib.RongIMClient;

/**
 * Created by 电脑 on 2016/7/5.
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
    private Context context;

    public MyConnectionStatusListener(Context context) {
                this.context = context;
        }

        @Override
        public void onChanged(ConnectionStatus connectionStatus) {

            switch (connectionStatus){

                case CONNECTED://连接成功。

                    break;
                case DISCONNECTED://断开连接。

                    break;
                case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                IntentUtils.sendOffLineBroadcast(context);
                HomepageActivity.type = HomepageActivity.LOTTERY;
                Intent intent = new Intent(context, HomepageActivity.class);
                intent.putExtra(FieldFinals.ACTION,FieldFinals.OFF_LINE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }
    }
}