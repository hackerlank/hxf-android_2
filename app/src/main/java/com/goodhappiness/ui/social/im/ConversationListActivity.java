package com.goodhappiness.ui.social.im;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.social.FriendshipFragment;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by 电脑 on 2016/6/29.66666
 */
@ContentView(R.layout.conversationlist)
public class ConversationListActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.conversation_list_page));
        MobclickAgent.onResume(this);
        GoodHappinessApplication.isAhead = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(getString(R.string.conversation_list_page));
        MobclickAgent.onPause(this);
        GoodHappinessApplication.isAhead = false;
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.message);
        iv_right.setImageResource(R.mipmap.add);
        iv_right.setVisibility(View.VISIBLE);
        if (PreferencesUtil.getBooleanPreferences(this, FieldFinals.FIRST_SET_IM_CONFIG + String.valueOf(getUid()), true)) {
            PreferencesUtil.setPreferences(this, FieldFinals.FIRST_SET_IM_CONFIG + String.valueOf(getUid()), false);
            RongIMUtils.sendInsertMsgAndClear("3", getUid() + "");
            RongIMUtils.sendInsertMsgAndClear("2", getUid() + "");
            RongIMUtils.sendInsertMsgAndClear("1", getUid() + "");
        }
        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //rong_content 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }

    @Event({R.id.common_right,R.id.common_title})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right:
                IntentUtils.startToFriendship(this, 0, FriendshipFragment.FROM_TYPE_IM, getUid());
                break;
            case R.id.common_title:
                break;
        }
    }
}