package com.goodhappiness.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodhappiness.R;


/**
 * Created by baidu on 15/8/31.
 */
public class XProgressDialog extends AlertDialog {

    // theme类型
    public static final int THEME_CIRCLE_PROGRESS = 2;

    protected ImageView progressBar;
    protected TextView message;
    protected String messageText = "正在加载中...";
    protected int theme = THEME_CIRCLE_PROGRESS;
    private RotateAnimation rotateAnimation;
    public XProgressDialog(Context context) {
        super(context);
    }

    public XProgressDialog(Context context, String message) {
        super(context);
        messageText = message;
    }

    public XProgressDialog(Context context, int theme) {
        super(context);
        this.theme = theme;
    }

    public XProgressDialog(Context context, String message, int theme) {
        super(context);
        messageText = message;
        this.theme = theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_xprogressdialog_circle_progress);
        message = (TextView) findViewById(R.id.message);
        progressBar = (ImageView) findViewById(R.id.loading_view_iv);
        rotateAnimation = new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        progressBar.startAnimation(rotateAnimation);
        if (message != null && !TextUtils.isEmpty(messageText)) {
            message.setText(messageText);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    /**
     * 获取显示文本控件
     *
     * @return
     */
    protected TextView getMessageView() {
        return message;
    }

    /**
     * 获取显示进度的控件
     *
     * @return
     */
    protected View getProgressView() {
        return progressBar;
    }

    @Override
    public void show() {
        super.show();
    }

}
