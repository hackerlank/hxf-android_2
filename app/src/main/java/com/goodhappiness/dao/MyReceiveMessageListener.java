package com.goodhappiness.dao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.ui.social.LikeMeActivity;
import com.goodhappiness.ui.social.picture.CommentToMeActivity;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.RongIMUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

/**
 * Created by 电脑 on 2016/6/30.
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    private Context context;

    public MyReceiveMessageListener(Context context) {
        this.context = context;
    }

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(final Message message, int left) {
        IntentUtils.sendNewMsgBroadcast(context);
        RongIMUtils.setUserInfoProvider(message);
        Log.e("Msg","getObjectName:"+message.getObjectName()+"   getConversationType:"+message.getConversationType()+"   ts:"+message.toString());
        if(message.getContent() instanceof RichContentMessage){
            String extras = ((RichContentMessage) message.getContent()).getExtra();
            if(!TextUtils.isEmpty(extras)){
                if(extras.contains("\"type\":1")){
                    IntentUtils.sendFlowerSendBroadcast(context,extras);
                }
            }
        }
        if (!GoodHappinessApplication.isAhead) {
            RongIM.getInstance().getUnreadCount(message.getConversationType(), message.getTargetId(), new RongIMClient.ResultCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder;
                    builder = new Notification.Builder(
                            context);
                    builder.setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setTicker("您有一条未读消息").setContentText("").setWhen(System.currentTimeMillis())
                            .setOngoing(false).setContentTitle(message.getTargetId()).setAutoCancel(true);
                    if (message.getSenderUserId().equals("1")) {
                        builder.setContentTitle(context.getString(R.string.system_msg));
                        builder.setContentIntent(getDefaultIntent(context, message.getSenderUserId(), context.getString(R.string.system_msg)));
                        builder.setTicker("您有一条系统消息");
                    }
                    if (message.getObjectName().equals("RC:TxtMsg")) {
                        builder.setContentText(((TextMessage) message.getContent()).getContent());
                        if (message.getContent().getUserInfo() != null) {
                            builder.setContentTitle(message.getContent().getUserInfo().getName());
                            builder.setContentIntent(getDefaultIntent(context, message.getSenderUserId(), message.getContent().getUserInfo().getName()));
                            builder.setTicker(message.getContent().getUserInfo().getName() + ":" + ((TextMessage) message.getContent()).getContent());
                            if (integer > 1) {
                                builder.setContentText("[" + integer + "条]" + message.getContent().getUserInfo().getName() + ":" + ((TextMessage) message.getContent()).getContent());
                            }
                            setAvatar(builder, message);
                        }
                        if (message.getSenderUserId().equals("2")) {
                            builder.setContentTitle(context.getString(R.string.comment_to_me));
                            Intent commentIntent = new Intent(context, CommentToMeActivity.class);
                            commentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            builder.setContentIntent(PendingIntent.getActivity(context, 1, commentIntent, 0));
                        }
                        if (message.getSenderUserId().equals("3")) {
                            builder.setContentTitle(context.getString(R.string.like_to_me));
                            Intent likeIntent = new Intent(context, LikeMeActivity.class);
                            likeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            builder.setContentIntent(PendingIntent.getActivity(context, 1, likeIntent, 0));
                        }
                    } else if (message.getObjectName().equals("app:flower")) {
                        if (message.getContent().getUserInfo() != null) {
                            builder.setContentTitle(message.getContent().getUserInfo().getName());
                            builder.setContentIntent(getDefaultIntent(context, message.getSenderUserId(), message.getContent().getUserInfo().getName()));
                            builder.setContentText(message.getContent().getUserInfo().getName() + "给你送花了！");
                            if (integer > 1) {
                                builder.setContentText("[" + integer + "条]" + message.getContent().getUserInfo().getName() + "给你送花了！");
                            }
                            setAvatar(builder, message);
                        }
                    } else if (message.getObjectName().equals("RC:ImgMsg")) {
                        builder.setContentTitle(message.getContent().getUserInfo().getName());
                        builder.setContentIntent(getDefaultIntent(context, message.getSenderUserId(), message.getContent().getUserInfo().getName()));
                        builder.setContentText("[图片]");
                        if (integer > 1) {
                            builder.setContentText("[" + integer + "条]：[图片]");
                        }
                        setAvatar(builder, message);
                    } else if (message.getObjectName().equals("RC:VcMsg")) {
                        builder.setContentTitle(message.getContent().getUserInfo().getName());
                        builder.setContentIntent(getDefaultIntent(context, message.getSenderUserId(), message.getContent().getUserInfo().getName()));
                        builder.setContentText("[语音]");
                        if (integer > 1) {
                            builder.setContentText("[" + integer + "条]：[语音]");
                        }
                        setAvatar(builder, message);
                    }
                    Notification notification = builder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(Integer.valueOf(message.getTargetId()), notification);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            return true;
        } else {
            return false;
        }
        //开发者根据自己需求自行处理
    }

    private void setAvatar(Notification.Builder builder, Message message) {
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(message.getContent().getUserInfo().getPortraitUri().toString());
        if (bitmap == null) {
            if (!TextUtils.isEmpty(message.getContent().getUserInfo().getPortraitUri().toString())) {
                File f = ImageLoader.getInstance().getDiskCache().get(message.getContent().getUserInfo().getPortraitUri().toString());
                if (f == null) {
                    ImageLoader.getInstance().loadImage(message.getContent().getUserInfo().getPortraitUri().getQuery(), null);
                } else {
                    builder.setLargeIcon(BitmapFactory.decodeFile(f.getAbsolutePath()));
                }
            }
        } else {
            builder.setLargeIcon(bitmap);
        }
    }

    public PendingIntent getDefaultIntent(Context context, String targetUserId, String title) {
        Log.e("r_p", targetUserId);
        Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", targetUserId).appendQueryParameter("title", title).build();
        Intent realIntent = new Intent("android.intent.action.VIEW", uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        Intent intent = new Intent(context, NotificationClickReceiver.class);
//        intent.putExtra(FieldFinals.ACTION,realIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, realIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
