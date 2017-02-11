package com.goodhappiness.dao;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.goodhappiness.bean.RichMsgExtra;
import com.goodhappiness.utils.IntentUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;

/**
 * Created by 电脑 on 2016/7/14.
 */
public class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

    /**
     * 当点击用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserId())&&!userInfo.getUserId().equals("1")) {
            IntentUtils.startToPerson(context, Long.valueOf(userInfo.getUserId()));
        }
        return false;
    }

    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getTargetId().equals("1") && message.getContent() instanceof RichContentMessage) {
            RichContentMessage richContentMessage = (RichContentMessage) message.getContent();
            Gson gson = new Gson();
            RichMsgExtra richMsgExtra = gson.fromJson(richContentMessage.getExtra(), new TypeToken<RichMsgExtra>() {
            }.getType());
            if (!IntentUtils.checkURL(context, richMsgExtra.getAndroidUrl())) {
                IntentUtils.startToWebView(context, richMsgExtra.getAndroidUrl());
            }
            return true;
        }
        return false;
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

//        if(message.getTargetId().equals("1")){
//            return true;
//        }
        return false;
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }
}