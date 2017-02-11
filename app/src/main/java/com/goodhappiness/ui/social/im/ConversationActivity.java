package com.goodhappiness.ui.social.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.social.FriendActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.RongIMUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.MessageListFragment;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

@ContentView(R.layout.activity_conversation)
public class ConversationActivity extends BaseFragmentActivity {
    private String mTargetId;
    private int blackStatus;
    private String title;
    private Conversation.ConversationType mConversationType;
    @ViewInject(R.id.no_data)
    private RelativeLayout rl_noData;
//    private UriFragment lastUriFragment;
//    private FragmentTransaction lastFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter2 = new IntentFilter(StringFinal.BROADCAST_FLOWER_SEND);
        sendFlowerSuccessBroadcastReceiver = new SendFlowerSuccessBroadcastReceiver();
        registerReceiver(sendFlowerSuccessBroadcastReceiver, filter2);
    }

    private SendFlowerSuccessBroadcastReceiver sendFlowerSuccessBroadcastReceiver;

    private class SendFlowerSuccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FLOWER_SEND) && (intent.getStringExtra(FieldFinals.ACTION).contains("ConversationActivity"))) {
                if(intent.getBooleanExtra(FieldFinals.RESULT,false)){
                    RongIMUtils.sendFlower(intent.getIntExtra(FieldFinals.COUNT, 0), title, mTargetId,
                            new RongIMClient.SendMessageCallback() {
                                @Override
                                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {

                                }
                            }, new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    DialogFactory.createFlowerSendDialog(ConversationActivity.this);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }else{
                    DialogFactory.createDefaultDialog(ConversationActivity.this,getString(R.string.send_flower_fail));
                }
            }
        }
    }

    @Override
    protected void setData() {
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                AppManager.getAppManager().finishActivity(FriendActivity.class);
                finishActivity();
            }
        };
        initView();
        Intent intent = getIntent();
        setData(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.conversation_page));
        MobclickAgent.onResume(this);
        GoodHappinessApplication.isAhead = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.conversation_page));
        MobclickAgent.onPause(this);
        GoodHappinessApplication.isAhead = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendFlowerSuccessBroadcastReceiver);
    }

    private void setData(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        Log.e("r_", "mTargetId::" + mTargetId);
        title = intent.getData().getQueryParameter("title");
        tv_title.setText(intent.getData().getQueryParameter("title"));
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        enterFragment(mConversationType, mTargetId);
        if (!mTargetId.equals("1")) {
            if(RongIM.getInstance().getCurrentConnectionStatus().getValue()==0){
                tv_right.setTextColor(getTheColor(R.color.gray_999_text));
                RongIM.getInstance().getBlacklistStatus(mTargetId, new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {
                    @Override
                    public void onSuccess(RongIMClient.BlacklistStatus blacklistStatus) {
                        blackStatus = blacklistStatus.getValue();
                        if (blacklistStatus.getValue() == 0) {//在黑名单
                            tv_right.setText(R.string.have_add_to_black_list);
                        } else {
                            tv_right.setText(R.string.add_to_black_list);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }
        }else{
            RongIM.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, "1", 10, new RongIMClient.ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
                    if(messages==null||messages.size()==0){
                        rl_noData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        if (mTargetId.equals("2")) {
            IntentUtils.startToCommentToMe(this);
            finishActivity();
        }
        if (mTargetId.equals("3")) {
            IntentUtils.startToLikeToMe(this);
            finishActivity();
        }
        UriFragment fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mTargetId.equals("1") || mTargetId.equals("2") || mTargetId.equals("3")) {
            fragment = new MessageListFragment();
        } else {
            fragment = new ConversationFragment();
        }
        RongIMUtils.closeNotify(this, Integer.valueOf(mTargetId));
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
//        lastUriFragment = fragment;
//        lastFragmentTransaction = transaction;

        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }

    @Event({R.id.common_right_text, R.id.common_title})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right_text:
                if (blackStatus == 0) {//恢复
                    DialogFactory.createSelectDialog(this, getString(R.string.relieve_to_black_tip1), getString(R.string.relieve_to_black_tip2), new OnSelectListener() {
                        @Override
                        public void onSelected(boolean isSelected) {
                            if (isSelected) {
                                newDialog().show();
                                RongIM.getInstance().removeFromBlacklist(mTargetId, new RongIMClient.OperationCallback() {
                                    @Override
                                    public void onSuccess() {
                                        blackStatus = 1;
                                        dialog.dismiss();
                                        tv_right.setText(R.string.add_to_black_list);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        showToast(R.string.add_to_black_list_fail);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                } else {//拉黑
                    DialogFactory.createSelectDialog(this, getString(R.string.add_to_black_tip1), getString(R.string.add_to_black_tip2), new OnSelectListener() {
                        @Override
                        public void onSelected(boolean isSelected) {
                            if (isSelected) {
                                newDialog().show();
                                RongIM.getInstance().addToBlacklist(mTargetId, new RongIMClient.OperationCallback() {
                                    @Override
                                    public void onSuccess() {
                                        blackStatus = 0;
                                        tv_right.setText(R.string.have_add_to_black_list);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        dialog.dismiss();
                                        showToast(R.string.have_add_to_black_list_fail);
                                    }
                                });
                            }
                        }
                    });
                }
                break;
            case R.id.common_title:
                break;
        }
    }
}
