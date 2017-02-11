package com.goodhappiness.ui.register;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.widget.ClearEditText;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_register_step_one)
public class RegisterStepOneActivity extends BaseActivity {
    @ViewInject(R.id.register_one_cet)
    private EditText et;
    @ViewInject(R.id.register_one_ll)
    private LinearLayout ll;
    @ViewInject(R.id.register_one_tv_get)
    private TextView tv;
    @ViewInject(R.id.register_one_tv_protocol)
    private TextView tv_protocol;
    @ViewInject(R.id.register_one_iv_protocol)
    private ImageView iv;
    @ViewInject(R.id.register_sn_code_pic)
    private ClearEditText cet_mac_code_pic;
    @ViewInject(R.id.iv_get_mac)
    private ImageView iv_getMac;
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final int ACTIVITY_FORGET_PASSWORD = 1;
    public static final int ACTIVITY_REGISTER = 2;
    private boolean isCanReSend = true;
    private Timer timer;
    private int count = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setData() {
        initView();
        if (ACTIVITY_FORGET_PASSWORD == getIntent().getIntExtra(ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.forget_pwd);
            ll.setVisibility(View.GONE);
            tv.setText(R.string.next_step);
        } else if (ACTIVITY_REGISTER == getIntent().getIntExtra(ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.register);
        }
        tv_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startToWebView(RegisterStepOneActivity.this, HttpFinal.SERVE_CLAUSE);
            }
        });
//        SpannableString ss = new SpannableString(getString(R.string.protocol));
//        //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
//        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.black_333_text)), 0, 4,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.advert_blue_text)), 4, 8,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.black_333_text)), 8, 11,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.advert_blue_text)), 11, 29,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.black_333_text)), 29, 30,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv_protocol.setText(ss);
        setIvMac();
    }

    private void setIvMac() {
        RequestParams params = new RequestParams(HttpFinal.GET_IMG_CODE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.TIME, System.currentTimeMillis() + "");
        params.setUseCookie(true);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                if (result != null) {
                    iv_getMac.setImageBitmap(BitmapFactory.decodeFile(result.getAbsolutePath()));
                    Log.e("q_", result.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("q_", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }

    @Override
    protected void reload() {

    }

    private void getSms(String picMac) {
        RequestParams params = new RequestParams(HttpFinal.SMS);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.MOBILE, et.getText().toString());
        params.addBodyParameter(FieldFinals.IMAGE_CODE, picMac);
        if (RegisterStepOneActivity.ACTIVITY_FORGET_PASSWORD == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            params.addBodyParameter(FieldFinals.ACTION, FieldFinals.RESET);
        } else {
            params.addBodyParameter(FieldFinals.ACTION, FieldFinals.REGISTER);
        }
        HttpUtils.post(this, params, new TypeToken<Result<Sms>>() {
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
//                showToast("验证码：" + ((Sms) result.getData()).getCode());
                if (result.getData() != null && ((Sms) result.getData()).getCode() != 0) {
                    initTimer();
                    Intent intent = new Intent(RegisterStepOneActivity.this, RegisterStepTwoActivity.class);
                    intent.putExtra(ACTIVITY_TYPE, getIntent().getIntExtra(ACTIVITY_TYPE, 0));
                    intent.putExtra(FieldFinals.MOBILE, et.getText().toString());
                    startTheActivity(intent);
                } else {
                    isCanReSend = true;
                }
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
            tv.setText(count + "");
            if (count <= 0) {
                tv.setText(getString(R.string.resend));
                isCanReSend = true;
                timer.cancel();
            }
        }
    };

    @Event({R.id.iv_get_mac, R.id.register_one_tv_get})
    private void onEventOnclick(View v) {
        switch (v.getId()) {
            case R.id.iv_get_mac:
                setIvMac();
                break;
            case R.id.register_one_tv_get:
                if (TextUtils.isEmpty(et.getText().toString())) {
                    showToast(R.string.please_input_all_msg);
                    return;
                }
                if (TextUtils.isEmpty(cet_mac_code_pic.getText().toString())) {
                    showToast("请先输入图形验证码");
                    return;
                }
                if (isCanReSend) {
                    getSms(cet_mac_code_pic.getText().toString());
                }
                break;
        }
    }
}
