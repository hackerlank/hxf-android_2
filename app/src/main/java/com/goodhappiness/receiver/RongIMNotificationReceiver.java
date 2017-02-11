package com.goodhappiness.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.WelcomeActivity;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

    public class RongIMNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        Log.e("q_", message.getPushContent());
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.putExtra(FieldFinals.PUSH_MSG, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }
}
