package com.goodhappiness.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.goodhappiness.utils.IntentUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by 电脑 on 2016/7/1.
 */
public class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {

        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    /**
     * 长按会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 长按时的会话条目。
     * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationLongClick(final Context context, View view, final UIConversation uiConversation) {
        if (Long.valueOf(uiConversation.getConversationTargetId()) < 100) {
        } else {
            String[] names = {uiConversation.isTop() ? "取消置顶" : "置顶该会话", "从会话列表中移除"};
            new AlertDialog.Builder(context).setTitle(uiConversation.getUIConversationTitle())
                    .setItems(names, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            switch (which) {
                                case 0:
                                    RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {

                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case 1:
                                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {

                                                }

                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {

                                        }
                                    });
                                    break;
                            }

                        }
                    }).setNegativeButton("取消", null).show();

        }
        return true;
    }

    /**
     * 点击会话列表中的 item 时执行。
     *
     * @param context        上下文。
     * @param view           触发点击的 View。
     * @param uiConversation 会话条目。
     * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        Log.e("r_","onConversationClick:"+uiConversation.getConversationType().getName());
        if (uiConversation.getConversationTargetId().equals("2")) {
            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "2", new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            IntentUtils.startToCommentToMe(context);
            return true;
        } else if (uiConversation.getConversationTargetId().equals("3")) {
            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "3", new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            IntentUtils.startToLikeToMe(context);
            return true;
        } else {
            return false;
        }
//        if(isChange){
//            return true;
//        }
//        Log.e("q_SenderId",uiConversation.getConversationSenderId());
//        Log.e("q_TargetId",uiConversation.getConversationTargetId());
//        Log.e("q_Content", uiConversation.getConversationContent().toString());
//        return false;
    }
}
