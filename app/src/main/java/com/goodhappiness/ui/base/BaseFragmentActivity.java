package com.goodhappiness.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.constant.WBConstants;

import org.xutils.x;

/**
 * Created by 电脑 on 2016/5/11.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements IWeiboHandler.Response {
    protected TextView tv_title, tv_right;
    protected ImageView iv_back, iv_right,iv_mid;
    protected int w, h;
    protected Toast mToast;
    protected Animation rotateAnimation;
    protected OnBackKeyDownClickListener onBackKeyDownClickListener;
    public XProgressDialog dialog;

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
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        dialog = new XProgressDialog(this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
        setData();
        w = GoodHappinessApplication.w;
        h = GoodHappinessApplication.h;

    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FieldFinals.SHARE_FLAG:
                    sendSingleMessage((BaseShareObject) msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        GoodHappinessApplication.mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
    public Dialog newDialog() {
        dialog = new XProgressDialog(this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
        return dialog;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
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
                    showToast(getString(R.string.share_cancel));
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    showToast(getString(R.string.share_fail) + "Error Message: " + baseResp.errMsg);
                    break;
            }
        }
    }

    protected abstract void setData();

    protected void initView() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        tv_title = (EmojiTextView) findViewById(R.id.common_title);
        tv_right = (TextView) findViewById(R.id.common_right_text);
        iv_back = (ImageView) findViewById(R.id.common_left);
        iv_right = (ImageView) findViewById(R.id.common_right);
        iv_mid = (ImageView) findViewById(R.id.common_mid);
        if(iv_back!=null)
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

    protected void startTheActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    public String getSid() {
        return PreferencesUtil.getStringPreferences(getApplicationContext(), "sid");
    }

    public String getDid() {
            return PreferencesUtil.getStringPreferences(getApplicationContext(), FieldFinals.DEVICE_IDENTIFIER);
    }

    protected long getUid() {
        if(!TextUtils.isEmpty(getSid())){
            return PreferencesUtil.getLongPreferences(getApplicationContext(), FieldFinals.UID, 10000000000L);
        }else{
            return -1;
        }
    }

    public void finishActivity() {
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

    protected boolean isKeyBroadShow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//isOpen若返回true，则表示输入法打开
    }
}
