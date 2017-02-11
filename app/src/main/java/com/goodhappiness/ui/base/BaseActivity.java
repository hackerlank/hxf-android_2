package com.goodhappiness.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.BaseShareObject;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.ui.register.LoginActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.ShareUtil;
import com.goodhappiness.widget.XProgressDialog;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.constant.WBConstants;

import org.xutils.x;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public abstract class BaseActivity extends Activity implements IWeiboHandler.Response {
    protected TextView tv_title, tv_right;
    protected ImageView iv_back, iv_right;
    protected LinearLayout ll_empty;
    protected RelativeLayout rl_head;
    protected TextView tv_reload;
    protected int w, h;
    protected Toast mToast;
    protected Animation rotateAnimation;
    protected OnBackKeyDownClickListener onBackKeyDownClickListener;
    protected XProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    /**
     * 微博微博分享接口实例
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        AppManager.getAppManager().addActivity(this);
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            GoodHappinessApplication.mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
        w = GoodHappinessApplication.w;
        h = GoodHappinessApplication.h;
//        WindowManager manage = getWindowManager();
//        Display display = manage.getDefaultDisplay();
        dialog = new XProgressDialog(this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
        setData();
//        w = display.getWidth();
//        h = display.getHeight();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    public Dialog newDialog() {
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        dialog = new XProgressDialog(this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
        return dialog;
    }

    public Dialog newDialog(boolean isCanClose) {
        dialog = new XProgressDialog(this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
        dialog.setCancelable(isCanClose);
        dialog.setCanceledOnTouchOutside(isCanClose);
        return dialog;
    }

    protected abstract void setData();

    protected abstract void reload();

    protected void showEmptyView(boolean isShow) {
        if (ll_empty != null) {
            if (isShow) {
                ll_empty.setVisibility(View.VISIBLE);
            } else {
                ll_empty.setVisibility(View.GONE);
            }
        }
    }

    protected void initView() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        tv_title = (EmojiTextView) findViewById(R.id.common_title);
        tv_right = (TextView) findViewById(R.id.common_right_text);
        rl_head = (RelativeLayout) findViewById(R.id.common_rl_head);
        iv_back = (ImageView) findViewById(R.id.common_left);
        iv_right = (ImageView) findViewById(R.id.common_right);
        if (iv_back != null) {
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackKeyDownClickListener != null) {
                        onBackKeyDownClickListener.onBackKeyClick();
                    } else {
                        finishActivity();
                    }
                }
            });
        }
        ll_empty = (LinearLayout) findViewById(R.id.empty_view);
        tv_reload = (TextView) findViewById(R.id.empty_view_tv_reload);
        if (tv_reload != null) {
//            tv_reload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showEmptyView(false);
//                    reload();
//                }
//            });
            RxView.clicks(tv_reload).throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            showEmptyView(false);
                            reload();
                        }
                    });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        GoodHappinessApplication.mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @param baseResp 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    showToast(R.string.share_success);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    showToast(R.string.share_cancel);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    showToast(getString(R.string.share_fail) + "Error Message: " + baseResp.errMsg);
                    break;
            }
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FieldFinals.SHARE_FLAG:
                    sendSingleMessage((BaseShareObject) msg.obj);
                    break;
            }
        }
    };

    public void sendSingleMessage(BaseShareObject baseShareObject) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = ShareUtil.getTextObj(baseShareObject);
        weiboMessage.imageObject = ShareUtil.getImageObj(this, baseShareObject);
        weiboMessage.mediaObject = ShareUtil.getWebpageObj(this, baseShareObject);
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        GoodHappinessApplication.mWeiboShareAPI.sendRequest(this, request);
    }

    protected boolean isUserLogined() {
        if (!"".equals(PreferencesUtil.getStringPreferences(this, "sid"))) {
            return true;
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
    }

    protected int getTheColor(int color) {
        return getResources().getColor(color);
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg) {
        try {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            if (mToast == null) {
                mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
            }
            mToast.setText(msg);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示提示信息
     *
     * @param stringId
     */
    public void showToast(int stringId) {
        try {
            String msg = getResources().getString(stringId);
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            if (mToast == null) {
                mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
            }
            mToast.setText(msg);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, GoodHappinessApplication.options);
    }
    public void displayImage(ImageView imageView, String url, ImageSize imageSize) {
        ImageLoader.getInstance().displayImage(url, imageView, imageSize);
    }

    protected void startTheActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    protected String getSid() {
        return PreferencesUtil.getStringPreferences(getApplicationContext(), FieldFinals.SID);
    }

    protected String getNickName() {
        return PreferencesUtil.getStringPreferences(getApplicationContext(), FieldFinals.NICKNAME);
    }

    protected long getUid() {
        if (!TextUtils.isEmpty(getSid())) {
            return PreferencesUtil.getLongPreferences(getApplicationContext(), FieldFinals.UID, 10000000000L);
        } else {
            return -1;
        }
    }

    protected String getDid() {
        return PreferencesUtil.getStringPreferences(getApplicationContext(), FieldFinals.DEVICE_IDENTIFIER);
    }

    protected void finishActivity() {
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (onBackKeyDownClickListener != null) {
                onBackKeyDownClickListener.onBackKeyClick();
            } else {
                finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showKeyBroad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }




    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Base Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.goodhappiness.ui.base/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Base Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.goodhappiness.ui.base/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
}
