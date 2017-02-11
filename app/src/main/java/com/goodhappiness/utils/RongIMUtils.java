package com.goodhappiness.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.im.FlowerMessage;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.MyConnectionStatusListener;
import com.goodhappiness.dao.MyConversationBehaviorListener;
import com.goodhappiness.dao.MyConversationListBehaviorListener;
import com.goodhappiness.dao.MyReceiveMessageListener;
import com.goodhappiness.dao.MySendMessageListener;
import com.goodhappiness.provider.FlowerExtendProvider;
import com.goodhappiness.provider.FlowerMessageItemProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by 电脑 on 2016/6/30.
 */
public class RongIMUtils {

    public static void initRong(Activity activity) {
        RongIMUtils.refreshRongPic(activity);
        RongIM.registerMessageType(FlowerMessage.class);
        RongIM.getInstance().registerMessageTemplate(new FlowerMessageItemProvider());
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()),//地理位置
                new FlowerExtendProvider(RongContext.getInstance())
        };

        if (RongIM.getInstance() != null) {
            /**
             * 设置连接状态变化的监听器.
             */
            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(activity));
            RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
            RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(GoodHappinessApplication.getContext()));
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        }
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
    }

    public static void refreshRongPic(Activity context) {
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_SYSTEM, context.getString(R.string.system_msg), Uri.parse(StringFinal.IMG_URI_HEAD+FileUtils.saveResourceToFile(context,R.drawable.ico_list_pd,FieldFinals.SYSTEM_MSG))));
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_COMMENT, context.getString(R.string.comment_to_me), Uri.parse(StringFinal.IMG_URI_HEAD+FileUtils.saveResourceToFile(context,R.drawable.ico_list_mg,FieldFinals.COMMENT_TO_ME))));
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_LIKE, context.getString(R.string.like_to_me), Uri.parse(StringFinal.IMG_URI_HEAD+FileUtils.saveResourceToFile(context,R.drawable.ico_list_z,FieldFinals.LIKE_TO_ME))));
//        if (TextUtils.isEmpty(PreferencesUtil.getStringPreferences(context, FieldFinals.SYSTEM_MSG))) {
//            String systemMsgPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ico_list_pd), null, null);
//            PreferencesUtil.setPreferences(context, FieldFinals.SYSTEM_MSG, systemMsgPath);
//            Log.e("r_", systemMsgPath);
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_SYSTEM, context.getString(R.string.system_msg), Uri.parse(systemMsgPath)));
//        } else {
//            Log.e("r_", PreferencesUtil.getStringPreferences(context, FieldFinals.SYSTEM_MSG));
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_SYSTEM, context.getString(R.string.system_msg), Uri.parse(PreferencesUtil.getStringPreferences(context, FieldFinals.SYSTEM_MSG))));
//        }
//        if (TextUtils.isEmpty(PreferencesUtil.getStringPreferences(context, FieldFinals.COMMENT_TO_ME))) {
//            String commentPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ico_list_mg), null, null);
//            PreferencesUtil.setPreferences(context, FieldFinals.COMMENT_TO_ME, commentPath);
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_COMMENT, context.getString(R.string.comment_to_me), Uri.parse(commentPath)));
//        } else {
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_COMMENT, context.getString(R.string.comment_to_me), Uri.parse(PreferencesUtil.getStringPreferences(context, FieldFinals.COMMENT_TO_ME))));
//        }
//        if (TextUtils.isEmpty(PreferencesUtil.getStringPreferences(context, FieldFinals.LIKE_TO_ME))) {
//            String likePath = MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.ico_list_z), null, null);
//            PreferencesUtil.setPreferences(context, FieldFinals.LIKE_TO_ME, likePath);
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_LIKE, context.getString(R.string.like_to_me), Uri.parse(likePath)));
//        } else {
//            RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringFinal.MSG_TYPE_LIKE, context.getString(R.string.like_to_me), Uri.parse(PreferencesUtil.getStringPreferences(context, FieldFinals.LIKE_TO_ME))));
//        }
    }

    public static void closeNotify(Context context, int targetId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(targetId);
    }

    public static void sendFlower(int count, String name, final String targetId, RongIMClient.SendMessageCallback sendMessageCallback, RongIMClient.ResultCallback<Message> messageResultCallback) {
        FlowerMessage flowerMessage = FlowerMessage.obtain(count, targetId);
        flowerMessage.setUserInfo(RongIMUtils.getUserInfo());
        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, flowerMessage,
                flowerMessage.getUserInfo().getName() + "给你送花了", "", sendMessageCallback, messageResultCallback);
    }

    public static UserInfo getUserInfo() {
        return new UserInfo(String.valueOf(PreferencesUtil.getLongPreferences(GoodHappinessApplication.getContext(), FieldFinals.UID, 10000000000L)),
                PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(), FieldFinals.NICKNAME),
                Uri.parse(PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(), FieldFinals.AVATAR)));
    }

    public static void sendInsertMsgAndClear(final String targetId, final String senderUserId) {
        RongIM.getInstance().insertMessage(Conversation.ConversationType.PRIVATE, targetId, senderUserId, TextMessage.obtain("."), new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                Log.e("r_", "sendInsertMsgAndClear" + targetId);
                RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.e("r_", "clearMessages" + targetId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("r_", "ResultCallback onError" + targetId);
            }
        });
    }


    public static void sendMsg(String targetId) {
        RongIM.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, TextMessage.obtain("系统消息"), "", "",
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                        Log.e("r_", "SendMessageCallback onError");
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e("r_", "SendMessageCallback onSuccess");
                    }

                }, new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e("r_", "ResultCallback onError");
                    }

                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        Log.e("r_", "ResultCallback onSuccess");
                    }

                });
    }

    public static boolean isCanChat(Context context, int relation) {
        if(relation==3){
            return true;
        }else{
            Toast.makeText(context,R.string.focus_each_other_can_chat,Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void startPrivate(Context context, long userId, String title) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(context, String.valueOf(userId), title);
        }
    }

    public static void connect(final Context context, String token) {
        Log.e("LoginActivity", "--connect");
        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
                Log.e("LoginActivity", "--onTokenIncorrect");
            }
            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {
                Log.e("LoginActivity", "--onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("LoginActivity", "--onError" + errorCode);
            }
        });
    }

    public static void setUserInfoProvider(final UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        RongIM.getInstance().refreshUserInfoCache(userInfo);
        DBUtils.setUserInfoToDataBase(userInfo);
        ImageLoader.getInstance().loadImage(userInfo.getPortraitUri().toString(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }


    public static void setUserInfoProvider(Message message) {
        if (message != null && message.getContent() != null && message.getContent().getUserInfo() != null) {
            if (getUserInfo().getUserId() == "1" || getUserInfo().getUserId() == "2" || getUserInfo().getUserId() == "3") {
                return;
            }
            setUserInfoProvider(message.getContent().getUserInfo());
        }
    }
}
