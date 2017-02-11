package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.goodhappiness.R;
import com.goodhappiness.dao.CheckoutResultListener;

/**
 * Created by 电脑 on 2016/4/19.
 */
public class CheckoutDialog extends Dialog implements View.OnClickListener {
    private Button btnSubmit;
    private Button btnCancel;
    private EditText etPsw;
    private Context context;
    CheckoutResultListener checkoutResultListener;

    public CheckoutDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CheckoutDialog(Context context, int theme, CheckoutResultListener checkoutResultListener) {
        super(context, theme);
        this.context = context;
        this.checkoutResultListener = checkoutResultListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkout);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.dialog_default_ok);
        btnCancel = (Button) findViewById(R.id.dialog_default_cancel);
        etPsw = (EditText) findViewById(R.id.dialog_default_psw);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            dismiss();
            checkoutResultListener.result(etPsw.getText().toString());
        } else if (v == btnCancel) {
            dismiss();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }
}
