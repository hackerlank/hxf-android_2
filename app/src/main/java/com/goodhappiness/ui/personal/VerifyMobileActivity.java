package com.goodhappiness.ui.personal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.Sms;
import com.goodhappiness.bean.UserInfoResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_verify_mobile)
public class VerifyMobileActivity extends BaseActivity {
    @ViewInject(R.id.verify_mobile_et_phone)
    private EditText et_phone;
    @ViewInject(R.id.verify_mobile_et_mac)
    private EditText et_mac;
    @ViewInject(R.id.verify_mobile_tv_get_mac)
    private TextView tv_getMac;
    @ViewInject(R.id.verify_mobile_tv_tips)
    private TextView tv_Tips;
    private Timer timer;
    private boolean isCanClick = true;
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
    protected void setData() {
        initView();
        tv_title.setText(R.string.mobile_num);
        tv_right.setText(R.string.commit);
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));

    }

    @Override
    protected void reload() {

    }

    @Event({R.id.verify_mobile_tv_get_mac, R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.verify_mobile_tv_get_mac:
                if (!TextUtils.isEmpty(et_phone.getText().toString())) {
                    if (isCanClick) {
                        isCanClick = false;
                        if (et_phone.getText().toString().trim().length() != 11) {
                            showToast(R.string.number_limit);
                            return;
                        }
                        RequestParams params = new RequestParams(HttpFinal.SMS);
                        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                        params.addBodyParameter(FieldFinals.MOBILE, et_phone.getText().toString());
                        HttpUtils.post(this,params, new TypeToken<Result<Sms>>() {
                        }.getType(), new OnHttpRequest() {
                            @Override
                            public void onLoading(long total, long current, boolean isDownloading) {

                            }

                            @Override
                            public void onStarted() {
                                dialog.show();
                            }

                            @Override
                            public void onWaiting() {

                            }

                            @Override
                            public void onSuccess(Result result) {
                                Sms sms = (Sms) result.getData();
                                et_mac.setText(sms.getCode() + "");
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
                        tv_Tips.setVisibility(View.VISIBLE);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        }, 0, 1000);
                    }
                }
                break;
            case R.id.common_right_text:
                if (!TextUtils.isEmpty(et_phone.getText().toString()) && !TextUtils.isEmpty(et_mac.getText().toString())) {
                    RequestParams params = new RequestParams(HttpFinal.UPDATE_USER_INFO);
                    params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                    params.addBodyParameter(FieldFinals.SID, getSid());
                    params.addBodyParameter(FieldFinals.KEY, FieldFinals.MOBILE);
                    params.addBodyParameter(FieldFinals.VALUE, et_phone.getText().toString() + "-" + et_mac.getText().toString());
                    HttpUtils.post(this,params, new TypeToken<Result<UserInfoResult>>() {
                    }.getType(), new OnHttpRequest() {
                        @Override
                        public void onLoading(long total, long current, boolean isDownloading) {

                        }

                        @Override
                        public void onStarted() {
                            dialog.show();
                        }

                        @Override
                        public void onWaiting() {

                        }

                        @Override
                        public void onSuccess(Result result) {
                            UserInfoResult userInfoResult = (UserInfoResult) result.getData();
                            PreferencesUtil.saveUserInfo(VerifyMobileActivity.this, userInfoResult.getUserInfo());
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
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (count > 0) {
                        count--;
                        tv_Tips.setText(String.format(getString(R.string.format_second_retry,count)));//count + "秒后重试");
                    } else {
                        timer.cancel();
                        isCanClick = true;
                        tv_Tips.setVisibility(View.GONE);
                        count = 60;
                    }
                    break;
            }
        }
    };
}
