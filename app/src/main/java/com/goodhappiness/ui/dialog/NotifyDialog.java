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
import com.goodhappiness.dao.OnSelectListener;

/**
 * Created by 电脑 on 2016/5/30.
 */
public class NotifyDialog extends Dialog implements View.OnClickListener {
    private Button btnSubmit;
    private TextView tvTips;
    private TextView tvTitle;
    private Context context;
    private String tips;
    private String title;
    private OnSelectListener onSelectListener;

    public NotifyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public NotifyDialog(Context context, int theme, String title, String tips,OnSelectListener onSelectListener) {
        super(context, theme);
        this.context = context;
        this.tips = tips;
        this.title = title;
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notify);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.dialog_default_ok);
        btnSubmit.setOnClickListener(this);
        tvTips = (TextView) findViewById(R.id.dialog_default_tips);
        if (!TextUtils.isEmpty(tips)) {
            tvTips.setText(tips);
        }
        tvTitle = (TextView) findViewById(R.id.dialog_default_title);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if (onSelectListener != null)
            if (v == btnSubmit) {
                onSelectListener.onSelected(true);
                this.dismiss();
            }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onSelectListener != null)
                onSelectListener.onSelected(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
