package com.goodhappiness.ui.register;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.Register;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.SHA1;
import com.goodhappiness.wxapi.Constants;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_register_step_three)
public class RegisterStepThreeActivity extends BaseActivity {
    @ViewInject(R.id.register_three_cet)
    private EditText et;
    @ViewInject(R.id.register_three_cet2)
    private EditText et2;
    @ViewInject(R.id.register_three_tv_over)
    private TextView tv;
    @ViewInject(R.id.register_three_iv_yee)
    private ImageView iv_yee;
    @ViewInject(R.id.register_three_ll_forget)
    private LinearLayout ll;

    private boolean isHide = true;

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
        if (RegisterStepOneActivity.ACTIVITY_FORGET_PASSWORD == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.forget_pwd);
            tv.setText(R.string.finish_modify);
            ll.setVisibility(View.VISIBLE);
        } else if (RegisterStepOneActivity.ACTIVITY_REGISTER == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
            tv_title.setText(R.string.register);
        }
    }

    @Override
    protected void reload() {

    }

    @Event({R.id.register_three_tv_over, R.id.register_three_iv_yee})
    private void onEventOnclick(View v) {
        switch (v.getId()) {
            case R.id.register_three_tv_over:
                if(RegisterStepOneActivity.ACTIVITY_REGISTER == getIntent().getIntExtra(RegisterStepOneActivity.ACTIVITY_TYPE, 0)) {
                    if (!TextUtils.isEmpty(et.getText().toString())) {
                        if (et.getText().toString().length() < 6 || et.getText().toString().length() > 16) {
                            showToast(R.string.please_input_right_pwd_length);
                            return;
                        }
                        RequestParams params = new RequestParams(HttpFinal.REGISTER);
                        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                        params.addBodyParameter(FieldFinals.MOBILE, getIntent().getStringExtra(FieldFinals.MOBILE));
                        params.addBodyParameter(FieldFinals.PASSWORD, new SHA1().getDigestOfString(et.getText().toString().getBytes()).toUpperCase());
                        params.addBodyParameter(FieldFinals.CODE, getIntent().getStringExtra(FieldFinals.CODE));
                        HttpUtils.post(this,params, new TypeToken<Result<Register>>() {
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
                                PreferencesUtil.saveUserInfo(RegisterStepThreeActivity.this, (Register) result.getData());
                                showToast(R.string.register_success);
                                AppManager.getAppManager().finishActivities(new Class[]{LoginActivity.class,RegisterStepTwoActivity.class
                                ,RegisterStepOneActivity.class});
                                finishActivity();
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
                    }else{
                        showToast(R.string.please_input_pwd);
                    }
                }else{
                    if (!TextUtils.isEmpty(et.getText().toString())&&!TextUtils.isEmpty(et.getText().toString())) {
                        if (et.getText().toString().length() < Constants.MIN_PWD_LENGTH || et.getText().toString().length() > Constants.MAX_PWD_LENGTH||et2.getText().toString().length() < Constants.MIN_PWD_LENGTH || et2.getText().toString().length() > Constants.MAX_PWD_LENGTH) {
                            showToast(R.string.please_input_right_pwd_length);
                            return;
                        }
                        if(et.getText().toString().length()!=et2.getText().toString().length()||!et.getText().toString().equals(et2.getText().toString())){
                            showToast(R.string.two_input_pwd_difference);
                            return;
                        }
                        RequestParams params = new RequestParams(HttpFinal.PASSWORD_RESET);
                        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                        params.addBodyParameter(FieldFinals.MOBILE, getIntent().getStringExtra(FieldFinals.MOBILE));
                        params.addBodyParameter(FieldFinals.PASSWORD, new SHA1().getDigestOfString(et.getText().toString().getBytes()).toUpperCase());
                        params.addBodyParameter(FieldFinals.CODE, getIntent().getStringExtra(FieldFinals.CODE));
                        HttpUtils.post(this,params, new TypeToken<Result<Register>>() {
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
                                showToast(R.string.finish_modify);
//                                AppManager.getAppManager().finishActivity(LoginActivity.class);
//                                AppManager.getAppManager().finishActivity(RegisterStepTwoActivity.class);
//                                AppManager.getAppManager().finishActivity(RegisterStepOneActivity.class);
                                AppManager.getAppManager().finishActivities(new Class[]{LoginActivity.class, RegisterStepTwoActivity.class
                                        , RegisterStepOneActivity.class});
                                finishActivity();
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
                    }else{
                        showToast(R.string.please_input_pwd);
                    }
                }
                break;
            case R.id.register_three_iv_yee:
                isHide = !isHide;
                if (isHide) {
                    iv_yee.setImageResource(R.mipmap.ico_show);
                    et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    iv_yee.setImageResource(R.mipmap.ico_hide);
                    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }
}
