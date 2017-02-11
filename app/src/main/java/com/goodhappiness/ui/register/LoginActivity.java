package com.goodhappiness.ui.register;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.QQThirdLoginResult;
import com.goodhappiness.R;
import com.goodhappiness.bean.QQAuth;
import com.goodhappiness.bean.Register;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.ThirdLogin;
import com.goodhappiness.bean.ThirdLoginResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.MyConnectionStatusListener;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.SHA1;
import com.goodhappiness.wxapi.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Field;
import java.util.Map;

import io.rong.imkit.RongIM;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_et_name)
    private EditText et_name;
    @ViewInject(R.id.root_view)
    private RelativeLayout rootView;
    @ViewInject(R.id.login_et_password)
    private EditText et_password;
    private IUiListener iUiListener;
    Tencent mTencent;
    //    AuthInfo mAuthInfo;
    //    Oauth2AccessToken mAccessToken;
//    SsoHandler mSsoHandler;
    private SHARE_MEDIA media;
    private ThirdLogin thirdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                finishActivity();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.login));
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            thirdLogin = (ThirdLogin) intent.getSerializableExtra(FieldFinals.ACTION);
            thirdLogin();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.login));
        MobclickAgent.onPause(this);
    }

//    class AuthListener implements WeiboAuthListener {
//        @Override
//        public void onComplete(Bundle values) {
//            mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
//            if (mAccessToken.isSessionValid()) {
//                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken); //保存Token+
//                UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this, Constants.SINA_APP_KEY, mAccessToken);
//                mUsersAPI.show(Long.valueOf(mAccessToken.getUid()), new RequestListener() {
//                    @Override
//                    public void onComplete(String response) {
//                        showToast("onComplete" + response);
//                        if (!TextUtils.isEmpty(response)) {
//                            User user = User.parse(response);
//                            showToast(user.toString());
//                            try {
//                                CrashHandler.printMsg(user.toString());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onWeiboException(WeiboException e) {
//                        showToast("onWeiboException:" + e.toString());
//                    }
//                });
//            } else {
//                // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
//                String code = values.getString("code", "");
//            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//            showToast("onWeiboException:");
//        }
//
//        @Override
//        public void onCancel() {
//
//        }
//    }

    @Override
    protected void setData() {
        initView();
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
//        mAuthInfo = new AuthInfo(this, Constants.SINA_APP_KEY,
//                Constants.REDIRECT_URL, Constants.SINA_SCOPE);
        iUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
//                showToast(o.toString());
                JSONObject json = (JSONObject) o;
                Gson gson = new Gson();
                QQAuth qqAuth = gson.fromJson(json.toString(), new TypeToken<QQAuth>() {
                }.getType());
                thirdLogin = new ThirdLogin();
                thirdLogin.setOpenid(qqAuth.getOpenid());
                thirdLogin.setToken(qqAuth.getAccess_token());
                if (!TextUtils.isEmpty(qqAuth.getAccess_token()) && !TextUtils.isEmpty(String.valueOf(qqAuth.getExpires_in()))
                        && !TextUtils.isEmpty(qqAuth.getOpenid())) {
                    mTencent.setAccessToken(qqAuth.getAccess_token(), String.valueOf(qqAuth.getExpires_in()));
                    mTencent.setOpenId(qqAuth.getOpenid());
                    UserInfo info = new UserInfo(LoginActivity.this, mTencent.getQQToken());
                    info.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            JSONObject json = (JSONObject) o;
                            Gson gson = new Gson();
                            QQThirdLoginResult result = gson.fromJson(json.toString(), new TypeToken<QQThirdLoginResult>() {
                            }.getType());
                            thirdLogin.setAction("qq");
                            thirdLogin.setAvatar(result.getFigureurl_qq_2());
                            thirdLogin.setUsername(result.getNickname());
                            thirdLogin();
                        }

                        @Override
                        public void onError(UiError uiError) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };
        tv_title.setText(R.string.login);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                if (TextUtils.isEmpty(getSid())) {
                    HomepageActivity.type = HomepageActivity.SHOP;
                    GoodHappinessApplication.isNeedRefresh = true;
                }
                finishActivity();
            }
        };
        if (PreferencesUtil.getIntPreferences(this, FieldFinals.KEYBOARD_HEIGHT, 0) == 0) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                /**
                 * the result is pixels
                 */
                @Override
                public void onGlobalLayout() {
                    if (PreferencesUtil.getIntPreferences(LoginActivity.this, FieldFinals.KEYBOARD_HEIGHT, 0) == 0) {
                        Rect r = new Rect();
                        // r will be populated with the coordinates of your view that area still visible.
                        rootView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootView.getRootView().getHeight();
                        int heightDiff = screenHeight - (r.bottom - r.top);
                        if (heightDiff > 100) {
                            // if more than 100 pixels, its probably a keyboard
                            // get status bar height
                            int statusBarHeight = 0;
                            try {
                                Class<?> c = Class.forName("com.android.internal.R$dimen");
                                Object obj = c.newInstance();
                                Field field = c.getField("status_bar_height");
                                int x = Integer.parseInt(field.get(obj).toString());
                                statusBarHeight = getResources().getDimensionPixelSize(x);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int realKeyboardHeight = heightDiff - statusBarHeight;
                            PreferencesUtil.setPreferences(LoginActivity.this, FieldFinals.KEYBOARD_HEIGHT, realKeyboardHeight);
                            Log.e("q_", "keyboard height = " + realKeyboardHeight);
                        }
                    }
                }
            });
        }
    }

    private void thirdLogin() {
        if (thirdLogin == null) {
            return;
        }
        RequestParams params = new RequestParams(HttpFinal.APP_THIRD_LOGIN);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.ACTION, thirdLogin.getAction());
        params.addBodyParameter(FieldFinals.OPEN_ID, thirdLogin.getOpenid());
        params.addBodyParameter(FieldFinals.TOKEN, thirdLogin.getToken());
        params.addBodyParameter(FieldFinals.USERNAME, thirdLogin.getUsername());
        params.addBodyParameter(FieldFinals.AVATAR, thirdLogin.getAvatar());
        HttpUtils.post(this, params, new TypeToken<Result<ThirdLoginResult>>() {
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
                ThirdLoginResult loginResult = (ThirdLoginResult) result.getData();
                if (loginResult != null) {
//                    if(loginResult.getIsBind()==1){//已绑定
                    PreferencesUtil.saveUserInfo(LoginActivity.this, loginResult);
                    initData(loginResult.getUserInfo());
                    com.goodhappiness.bean.UserInfo info = loginResult.getUserInfo();
                    PreferencesUtil.setPreferences(LoginActivity.this, FieldFinals.IS_BIND, info.getIsBind());
                    RongIMUtils.connect(LoginActivity.this, loginResult.getChatToken());
                    RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(info.getUid()), String.valueOf(info.getNickname()), Uri.parse(info.getAvatar())));
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                    RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(GoodHappinessApplication.getContext()));
                    initData(info);
                    finishActivity();
//                    }else{
//                        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//                        intent.putExtra(FieldFinals.THIRD_LOGIN_RESULT,thirdLogin);
//                        intent.putExtra(FieldFinals.ACTION,RegisterActivity.REGISTER_TYPE_BIND);
//                        startActivity(intent);
//                    }
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

    @Override
    protected void reload() {

    }

    @Event({R.id.login_tv_login, R.id.login_tv_register, R.id.login_tv_forget, R.id.qq, R.id.wechat})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_login:
                if (!TextUtils.isEmpty(et_name.getText().toString()) && !TextUtils.isEmpty(et_password.getText().toString())) {
                    RequestParams params = new RequestParams(HttpFinal.LOGIN);
                    params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                    params.addBodyParameter(FieldFinals.MOBILE, et_name.getText().toString());
                    params.addBodyParameter(FieldFinals.PASSWORD, new SHA1().getDigestOfString(et_password.getText().toString().getBytes()).toUpperCase());
                    HttpUtils.post(this, params, new TypeToken<Result<Register>>() {
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
                            PreferencesUtil.saveUserInfo(LoginActivity.this, (Register) result.getData());
                            com.goodhappiness.bean.UserInfo info = ((Register) result.getData()).getUserInfo();
                            initData(info);
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
                } else {
                    showToast(R.string.please_input_all_msg);
                }
                break;
            case R.id.login_tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_NORMAL);
                startTheActivity(intent);
                break;
            case R.id.login_tv_forget:
                Intent intent2 = new Intent(this, RegisterStepOneActivity.class);
                intent2.putExtra(RegisterStepOneActivity.ACTIVITY_TYPE, RegisterStepOneActivity.ACTIVITY_FORGET_PASSWORD);
                startTheActivity(intent2);
                break;
            case R.id.qq:
                media = SHARE_MEDIA.QQ;
                mTencent.login(this, "all", iUiListener);
                break;
            case R.id.wechat:
                media = SHARE_MEDIA.WEIXIN;
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Toast.makeText(LoginActivity.this, "onComplete", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Toast.makeText(LoginActivity.this, "onError", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Toast.makeText(LoginActivity.this, "onCancel", Toast.LENGTH_LONG).show();
                    }
                });
                break;
//            case R.id.sina:
//                media = SHARE_MEDIA.SINA;
//                mSsoHandler.authorize(new AuthListener());
//                break;
        }
    }

    private void initData(com.goodhappiness.bean.UserInfo info) {
        RongIMUtils.initRong(LoginActivity.this);
        PreferencesUtil.setPreferences(LoginActivity.this, FieldFinals.FOCUS_REFRESH, true);
        PreferencesUtil.setPreferences(LoginActivity.this, FieldFinals.WORLD_REFRESH, true);
        GoodHappinessApplication.isNeedRefresh = true;
        HomepageActivity.type = HomepageActivity.SHOP;
        if (PreferencesUtil.getBooleanPreferences(LoginActivity.this, FieldFinals.FIRST_SET_IM_CONFIG + String.valueOf(info.getUid()), true)) {
            new SetIMConfigTask().execute(String.valueOf(info.getUid()));
        } else {
            checkURL();
            finishActivity();
        }
    }

    private void checkURL() {
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.URL))) {
            IntentUtils.startToWebView(this, getIntent().getStringExtra(FieldFinals.URL));
        }
    }

    class SetIMConfigTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RongIMUtils.refreshRongPic(LoginActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            checkURL();
            finishActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (media) {
            case QQ:
                mTencent.handleLoginData(data, iUiListener);
                break;
            case WEIXIN:
                break;
            case SINA:
//                if (mSsoHandler != null) {
//                    mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
