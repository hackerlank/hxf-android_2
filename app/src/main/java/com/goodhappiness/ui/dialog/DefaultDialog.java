package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goodhappiness.R;


/**
 * 常用Dialog
 */
public class DefaultDialog extends Dialog implements View.OnClickListener {
    private Button btnSubmit;
    private TextView tvTips;
    private Context context;
    private String tips;
    private OnCancelListener cancelListener;

    public DefaultDialog(Context context) {
        super(context);
        this.context = context;
    }

    public DefaultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public DefaultDialog(Context context, int theme, String tips) {
        super(context, theme);
        this.context = context;
        this.tips = tips;
    }
    public DefaultDialog(Context context, int theme, String tips, OnCancelListener cancelListener) {
        super(context, theme);
        this.context = context;
        this.tips = tips;
        this.cancelListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_default);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.dialog_default_ok);
        btnSubmit.setOnClickListener(this);
        tvTips = (TextView) findViewById(R.id.dialog_default_title);
        if(!TextUtils.isEmpty(tips)){
            tvTips.setText(tips);
        }
    }

    @Override
    public void onClick(View v) {
        if(cancelListener!=null){
            cancelListener.onCancel(this);
        }
        this.dismiss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(mCancelable){
//
//            }
//            setCancelable();
//            DefaultDialog.this.dismiss();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
