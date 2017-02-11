package com.goodhappiness.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.Sms;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_register_step_two)
public class RegisterStepTwoActivity extends BaseActivity {
    @ViewInject(R.id.register_two_tv_num)
    private TextView tv_num;
    @ViewInject(R.id.register_two_cet)
    private EditText et;
    @ViewInject(R.id.register_two_ll_second)
    private LinearLayout ll_countdown;
    @ViewInject(R.id.register_two_tv_second)
    private TextView tv_countdown;
    private boolean isCanReSend = false;
    private Timer timer;
    private int count = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        if (RegisterStepOneActivity.ACTIVITY_FORGET_PASSWORD == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.forget_pwd);
        } else if (RegisterStepOneActivity.ACTIVITY_REGISTER == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.register);
        }
        if (getIntent() != null) {
            tv_num.setText("您的手机号：" + getIntent().getStringExtra(FieldFinals.MOBILE));
        }
    }

    private void getSms() {
        RequestParams params = new RequestParams(HttpFinal.SMS);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.MOBILE, getIntent().getStringExtra(FieldFinals.MOBILE));
        if (RegisterStepOneActivity.ACTIVITY_FORGET_PASSWORD == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            params.addBodyParameter(FieldFinals.ACTION, FieldFinals.RESET);
        } else {
            params.addBodyParameter(FieldFinals.ACTION, FieldFinals.REGISTER);
        }
        HttpUtils.post(this,params, new TypeToken<Result<Sms>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                newDialog().show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                initTimer();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    private void initTimer() {
        ll_countdown.setVisibility(View.VISIBLE);
        timer = new Timer();
        count = 60;
        isCanReSend = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            tv_countdown.setText(count + "");
            if (count <= 0) {
                ll_countdown.setVisibility(View.GONE);
                isCanReSend = true;
                timer.cancel();
            }
        }
    };

    @Override
    protected void reload() {

    }

    @Event({R.id.register_two_tv_submit, R.id.register_two_tv_get})
    private void onEventOnclick(View v) {
        switch (v.getId()) {
            case R.id.register_two_tv_submit:
                if (!TextUtils.isEmpty(et.getText().toString())) {
                    Intent intent = new Intent(this, RegisterStepThreeActivity.class);
                    intent.putExtra(FieldFinals.CODE, et.getText().toString());
                    intent.putExtra(FieldFinals.MOBILE, getIntent().getStringExtra(FieldFinals.MOBILE));
                    intent.putExtra(RegisterStepOneActivity.ACTIVITY_TYPE, getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0));
                    startTheActivity(intent);
                    finishActivity();
                }
                break;
            case R.id.register_two_tv_get:
                finishActivity();
                break;
        }
    }
}
