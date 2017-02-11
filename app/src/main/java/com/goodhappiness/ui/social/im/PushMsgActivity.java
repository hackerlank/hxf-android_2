package com.goodhappiness.ui.social.im;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.goodhappiness.R;
import com.goodhappiness.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_push_msg)
public class PushMsgActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        getPushMessage(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPushMessage(intent);
    }

    /**
     * Android push 消息
     */
    private void getPushMessage(Intent intent) {

        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //该条推送消息的内容。
            String content = intent.getData().getQueryParameter("pushContent");
            //标识该推送消息的唯一 Id。
            String id = intent.getData().getQueryParameter("pushId");
            //用户自定义参数 json 格式，解析后用户可根据自己定义的 Key 、Value 值进行业务处理。
            String extra = intent.getData().getQueryParameter("extra");
            //统计通知栏点击事件.
            Log.d("TestPushActivity", "--content:" + content + "--id:" + id + "---extra:" + extra);
        }
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void reload() {

    }
}
