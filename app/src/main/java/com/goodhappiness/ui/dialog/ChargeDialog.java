package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goodhappiness.R;
import com.goodhappiness.dao.OnSelectChargeCountListener;
import com.goodhappiness.widget.ClearEditText;


/**
 * 常用Dialog dialog_comment_lv
 */
public class ChargeDialog extends Dialog implements View.OnClickListener {
    private Button btnSubmit;
    private ClearEditText editText;
    private Context context;
    private OnSelectChargeCountListener onSelectChargeCountListener;
    private int defaultCount = 20;
    public ChargeDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ChargeDialog(Context context, int theme, OnSelectChargeCountListener onSelectChargeCountListener,int count) {
        super(context, theme);
        this.context = context;
        this.onSelectChargeCountListener = onSelectChargeCountListener;
        defaultCount = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_charge);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.dialog_default_ok);
        btnSubmit.setOnClickListener(this);
        editText = (ClearEditText) findViewById(R.id.dialog_charge_cet);
        editText.setText(defaultCount+"");
    }

    @Override
    public void onClick(View v) {
        if(editText.getText().toString().length()>8){
            Toast.makeText(getContext(),R.string.sum_too_large,Toast.LENGTH_SHORT).show();
            return;
        }
        if(editText.getText().toString().length()>0&&Integer.valueOf(editText.getText().toString())>0&&onSelectChargeCountListener!=null){
            onSelectChargeCountListener.onSelectChargeCount(Integer.valueOf(editText.getText().toString()));
        }else{
            onSelectChargeCountListener.onSelectChargeCount(defaultCount);
        }
        this.dismiss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(onSelectChargeCountListener!=null){
                onSelectChargeCountListener.onSelectChargeCount(defaultCount);
            }
            ChargeDialog.this.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

}
