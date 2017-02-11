package com.goodhappiness.ui.register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.DeviceInfo;
import com.goodhappiness.bean.Register;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.Sms;
import com.goodhappiness.bean.ThirdLoginResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.SHA1;
import com.goodhappiness.widget.ClearEditText;
import com.goodhappiness.widget.StretchPanel;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 电脑 on 2016/8/25.
 */
@ContentView(R.layout.activity_register_v4)
public class RegisterActivity extends BaseActivity {
    public static final int REGISTER_TYPE_NORMAL = 1;
    public static final int REGISTER_TYPE_BIND = 2;
    @ViewInject(R.id.iv_adv)
    private ImageView imageView;
    @ViewInject(R.id.register_mobile_num)
    private ClearEditText cet_mobile;
    @ViewInject(R.id.register_psw)
    private ClearEditText cet_psw;
    @ViewInject(R.id.register_sn_code)
    private ClearEditText cet_mac_code;
    @ViewInject(R.id.register_sn_code_pic)
    private ClearEditText cet_mac_code_pic;
    @ViewInject(R.id.tv_register)
    private TextView tv_register;
    @ViewInject(R.id.ll_register_msg)
    private LinearLayout ll_registerMsg;
    @ViewInject(R.id.ll_piv_get)
    private LinearLayout ll_PicGet;
    @ViewInject(R.id.tv_get_mac)
    private TextView tv_getMac;
    @ViewInject(R.id.iv_get_mac)
    private ImageView iv_getMac;
    @ViewInject(R.id.submit_order_sp)
    private StretchPanel sp;
    private EditText et_inviteCode;
    private View contentView, stretchView;
    private ImageView arrowView;
    private boolean isCanReSend = true;
    private Timer timer;
    private int count = 60;
    private int type = 1;


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.newcomer_register));
        MobclickAgent.onResume(this);
        setIvMac();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.newcomer_register));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();

        type = getIntent().getIntExtra(FieldFinals.ACTION, 1);
        if (type != 1) {
            ll_registerMsg.setVisibility(View.GONE);
            tv_title.setText(getString(R.string.complete_msg));
            tv_register.setText(R.string.complete_msg);
        }else{
            tv_title.setText(getString(R.string.newcomer_register));
        }
        initSp();
    }

    private void setIvMac() {
        RequestParams params = new RequestParams(HttpFinal.GET_IMG_CODE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER,getDid());
        params.addBodyParameter(FieldFinals.TIME,System.currentTimeMillis()+"");
        params.setUseCookie(true);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                if(result!=null){
                    iv_getMac.setImageBitmap(BitmapFactory.decodeFile(result.getAbsolutePath()));
                    Log.e("q_",result.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("q_",ex.getMessage());
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

    private void initSp() {
        if (PreferencesUtil.getPreferences(this, FieldFinals.BANNERS) != null) {
            final List<DeviceInfo.Banners> banners = PreferencesUtil.getPreferences(this, FieldFinals.BANNERS);
            if (banners != null && banners.size() > 0) {
                ImageLoader.getInstance().loadImage(banners.get(0).getImgUrl(), GoodHappinessApplication.options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        params.height = (int) (((float) bitmap.getHeight() * GoodHappinessApplication.w) / bitmap.getWidth());
                        imageView.setLayoutParams(params);
                        imageView.setImageBitmap(bitmap);
                        if (!TextUtils.isEmpty(banners.get(0).getAppUrl())) {
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!IntentUtils.checkURL(RegisterActivity.this, banners.get(0).getAppUrl())) {
                                        IntentUtils.startToWebView(RegisterActivity.this, banners.get(0).getAppUrl());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            }
        }
        contentView = View.inflate(this, R.layout.layout_stretch_register_invite_head, null);
        stretchView = View.inflate(this, R.layout.layout_stretch_register_invite, null);
        et_inviteCode = (EditText) stretchView.findViewById(R.id.register_invite_code);
        arrowView = (ImageView) contentView.findViewById(R.id.layout_stretch_submit_order_head_iv_arrow);
        contentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (sp.isStretchViewOpened()) {
                    sp.closeStretchView();
                } else {
                    sp.openStretchView();
                }
            }
        });
        sp.setStretchView(stretchView);
        sp.setContentView(contentView);
        sp.setOnStretchListener(new StretchPanel.OnStretchListener() {
            @Override
            public void onStretchFinished(boolean isOpened) {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.arrowrote);
                if (isOpened) {
                    arrowView.setImageResource(R.mipmap.btn_main_middle_dn);
                    arrowView.startAnimation(animation);
                } else {
                    arrowView.setImageResource(R.mipmap.register_arrow_up);
                    arrowView.startAnimation(animation);
                }
            }
        });
        sp.openStretchView();
    }

    @Override
    protected void reload() {

    }


    @Event({R.id.iv_get_mac,R.id.tv_get_mac, R.id.tv_register,R.id.tv_protocol,R.id.tv_login})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.iv_get_mac:
                setIvMac();
                break;
            case R.id.tv_login:
                break;
            case R.id.tv_protocol:
                IntentUtils.startToLogin(this);
                break;
            case R.id.tv_get_mac:
                if (TextUtils.isEmpty(cet_mobile.getText().toString())) {
                    showToast(R.string.please_input_all_msg);
                    return;
                }
                if (TextUtils.isEmpty(cet_mac_code_pic.getText().toString())) {
                    showToast("请先输入图形验证码");
                    return;
                }
                if(isCanReSend){
                    getSms(cet_mac_code_pic.getText().toString());
                }
                break;
            case R.id.tv_register:
                if (TextUtils.isEmpty(cet_mac_code.getText().toString()) || TextUtils.isEmpty(cet_mobile.getText().toString()) || TextUtils.isEmpty(cet_psw.getText().toString())) {
                    showToast(R.string.please_input_all_msg);
                    return;
                }
                RequestParams params = new RequestParams(getIntent().getIntExtra(FieldFinals.ACTION, 1) == RegisterActivity.REGISTER_TYPE_NORMAL ? HttpFinal.REGISTER : HttpFinal.BIND_USER_MOBILE);
                params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                params.addBodyParameter(FieldFinals.MOBILE, cet_mobile.getText().toString());
                params.addBodyParameter(FieldFinals.PASSWORD, new SHA1().getDigestOfString(cet_psw.getText().toString().getBytes()).toUpperCase());
                params.addBodyParameter(FieldFinals.CODE, cet_mac_code.getText().toString());
                if (!TextUtils.isEmpty(et_inviteCode.getText().toString())) {
                    params.addBodyParameter(FieldFinals.INVITE_CODE, et_inviteCode.getText().toString());
                }
                if (getIntent().getIntExtra(FieldFinals.ACTION, 1) == RegisterActivity.REGISTER_TYPE_BIND) {
                    params.addBodyParameter(FieldFinals.SID, getSid());
                }
                HttpUtils.post(this, params, getIntent().getIntExtra(FieldFinals.ACTION, 1) == RegisterActivity.REGISTER_TYPE_NORMAL ? new TypeToken<Result<Register>>() {
                }.getType() : new TypeToken<Result<ThirdLoginResult>>() {
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
                        String uid = "";
                        PreferencesUtil.setPreferences(RegisterActivity.this, FieldFinals.FOCUS_REFRESH, true);
                        PreferencesUtil.setPreferences(RegisterActivity.this, FieldFinals.WORLD_REFRESH, true);
                        GoodHappinessApplication.isNeedRefresh = true;
                        HomepageActivity.type = HomepageActivity.SHOP;
                        if (getIntent().getIntExtra(FieldFinals.ACTION, 1) == RegisterActivity.REGISTER_TYPE_NORMAL) {
                            PreferencesUtil.saveUserInfo(RegisterActivity.this, (Register) result.getData());
                            uid = String.valueOf(((Register) result.getData()).getUserInfo().getUid());
                            RongIMUtils.initRong(RegisterActivity.this);
                            new SetIMConfigTask().execute(uid);
                        } else {
                            PreferencesUtil.saveUserInfo(RegisterActivity.this, ((ThirdLoginResult) result.getData()).getUserInfo());
                            PreferencesUtil.setPreferences(RegisterActivity.this, FieldFinals.SID, ((ThirdLoginResult) result.getData()).getSid());
                            AppManager.getAppManager().finishActivity(LoginActivity.class);
                            finishActivity();
                        }

                        showToast(R.string.register_success);
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
                break;
        }
    }

    class SetIMConfigTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RongIMUtils.refreshRongPic(RegisterActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppManager.getAppManager().finishActivity(LoginActivity.class);
            finishActivity();
        }
    }

    private void getSms(String picMac) {
        RequestParams params = new RequestParams(HttpFinal.SMS);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.MOBILE, cet_mobile.getText().toString());
        params.addBodyParameter(FieldFinals.IMAGE_CODE,picMac);
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
                } else {
                    isCanReSend = true;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isCanReSend = true;
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
            tv_getMac.setText(count + "");
            if (count <= 0) {
                tv_getMac.setText(getString(R.string.resend));
                isCanReSend = true;
                timer.cancel();
            }
        }
    };
}
