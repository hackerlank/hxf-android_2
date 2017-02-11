package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.goodhappiness.R;
import com.goodhappiness.utils.IntentUtils;


/**
 * 常用Dialog
 */
public class SendFlowerSuccessDialog extends Dialog implements View.OnClickListener {
    private Context context;

    public SendFlowerSuccessDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SendFlowerSuccessDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_flower_success);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_cls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startToFlower(context,1);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(mCancelable){
//
//            }
//            setCancelable();
//            DefaultDialog.this.dismiss();
//            return false;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
