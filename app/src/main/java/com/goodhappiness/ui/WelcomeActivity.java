package com.goodhappiness.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.DeviceInfo;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.service.ListenNetStateService;
import com.goodhappiness.service.RunService;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.BitmapUtil;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PermissionHelper;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.SystemUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
    @ViewInject(R.id.common_iv_measure1)
    private View v_measure;
    @ViewInject(R.id.common_rl_measure)
    private RelativeLayout rl_measure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.welcome_activity));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.welcome_activity));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setData() {
        initView();
        PermissionHelper permissionHelper = new PermissionHelper(this);
        if (permissionHelper.checkPermission("android.permission.READ_PHONE_STATE")) {
            Message message = handler.obtainMessage();
            message.what = 101;
            handler.sendMessageDelayed(message, 1500);
        } else {
            permissionHelper.permissionsCheck("android.permission.READ_PHONE_STATE", 12);
        }
    }

    @Override
    protected void reload() {
        init();
    }

//    @SuppressLint("NewApi")
//    public static boolean checkPermission(Context context, String permission) {
//        boolean result = false;
//        PackageManager pm = context.getPackageManager();
//        if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//            result = true;
//        }
//        return result;
//    }
    //友盟测试
//    public static String getDeviceInfo(Context context) {
//        try {
//            org.json.JSONObject json = new org.json.JSONObject();
//            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            String device_id = null;
//            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//                device_id = tm.getDeviceId();
//            }
//            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
//                    .getSystemService(Context.WIFI_SERVICE);
//            String mac = wifi.getConnectionInfo().getMacAddress();
//            json.put("mac", mac);
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = mac;
//            }
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
//                        android.provider.Settings.Secure.ANDROID_ID);
//            }
//            json.put("device_id", device_id);
//            return json.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    void init() {
        RequestParams params = new RequestParams(HttpFinal.INIT);
        params.addBodyParameter(FieldFinals.DEVICEID, SystemUtils.getDeviceId(this));
        params.addBodyParameter(FieldFinals.DEVICERESOLUTION, SystemUtils.getResolution(this));
        params.addBodyParameter(FieldFinals.DEVICESYSVERSION, SystemUtils.getSysVersion());
        params.addBodyParameter(FieldFinals.DEVICETYPE, FieldFinals.ANDROID);
        params.addBodyParameter(FieldFinals.APP_VERSION, SystemUtils.getVersionCode(this) + "");
        params.addBodyParameter(FieldFinals.PUSH_TOKEN, "");
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(this, params, new TypeToken<Result<DeviceInfo>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                DeviceInfo deviceInfo = (DeviceInfo) result.getData();

                if (deviceInfo != null && deviceInfo.getUpdateVersionInfo() != null) {
                    PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.UPDATE_INFO, deviceInfo.getUpdateVersionInfo());
                    PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.CHAT_TOKEN, deviceInfo.getChatToken());
                    PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.NEW_VERSION_CODE, deviceInfo.getUpdateVersionInfo().getVersionCode());
                    PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.BANNERS, deviceInfo.getBanners());
//                    GoodHappinessApplication.checkType = 1;
                    if (deviceInfo.getBanners() != null && deviceInfo.getBanners().size() > 0 && !TextUtils.isEmpty(deviceInfo.getBanners().get(0).getImgUrl())) {
                        BitmapUtil.loadImage(deviceInfo.getBanners().get(0).getImgUrl());
                    }
                    if (!TextUtils.isEmpty(deviceInfo.getChatToken())) {
                        RongIMUtils.connect(WelcomeActivity.this, deviceInfo.getChatToken());
                        RongIMUtils.initRong(WelcomeActivity.this);
                        RongIMUtils.setUserInfoProvider(new io.rong.imlib.model.UserInfo(String.valueOf(getUid()), String.valueOf(PreferencesUtil.getStringPreferences(WelcomeActivity.this, FieldFinals.NICKNAME)), Uri.parse(PreferencesUtil.getStringPreferences(WelcomeActivity.this, FieldFinals.AVATAR))));
//                        RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(getUid()), String.valueOf(PreferencesUtil.getStringPreferences(WelcomeActivity.this, FieldFinals.NICKNAME)), Uri.parse(PreferencesUtil.getStringPreferences(WelcomeActivity.this, FieldFinals.AVATAR))));
//                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                    }
                } else if (deviceInfo != null && deviceInfo.getUpdateVersionInfo() == null) {
                    PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.UPDATE_INFO, null);
                }
                PreferencesUtil.setPreferences(WelcomeActivity.this, FieldFinals.DEVICE_IDENTIFIER, deviceInfo.getDeviceIdentifier());
                Message message2 = handler.obtainMessage();
                message2.what = 102;
                handler.sendMessage(message2);
                startService(new Intent(WelcomeActivity.this, ListenNetStateService.class));
                startService(new Intent(WelcomeActivity.this, RunService.class));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!isFinishing()) {
                    DialogFactory.createDefaultDialog(WelcomeActivity.this, getString(R.string.init_fail), new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            SystemUtils.endApp();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    GoodHappinessApplication.w = w = rl_measure.getWidth();
                    GoodHappinessApplication.h = h = rl_measure.getHeight();
                    GoodHappinessApplication.per = (float) w / 1080;
                    GoodHappinessApplication.perHeight = (float) v_measure.getHeight() / 80;
                    Log.e("q_","per:"+GoodHappinessApplication.per+"    perHeight"+ GoodHappinessApplication.perHeight);
                    init();
                    break;
                case 102:
                    if (!start()) {
                        HomepageActivity.type = HomepageActivity.SHOP;
                        Intent intent = new Intent(WelcomeActivity.this, HomepageActivity.class);
                        intent.putExtra(FieldFinals.PUSH_MSG, getIntent().getParcelableExtra(FieldFinals.PUSH_MSG));
                        startTheActivity(intent);
                        finishActivity();
                    }
                    break;
            }
        }
    };

    private boolean start() {
        if (PreferencesUtil.getBooleanPreferences(WelcomeActivity.this, FieldFinals.FIRST_IN, true)) {
            startTheActivity(new Intent(WelcomeActivity.this, StartActivity.class));
        }
        return PreferencesUtil.getBooleanPreferences(WelcomeActivity.this, FieldFinals.FIRST_IN, true);
    }
}
