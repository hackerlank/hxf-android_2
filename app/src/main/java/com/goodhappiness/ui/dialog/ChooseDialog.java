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
 * Created by 电脑 on 2016/4/19.
 */
public class ChooseDialog extends Dialog implements View.OnClickListener {
    private Button btnSubmit;
    private Button btnCancel;
    private TextView tvTips;
    private TextView tvTitle;
    private Context context;
    private String tips;
    private String title;
    private int bg;
    private OnSelectListener onSelectListener;

    public ChooseDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ChooseDialog(Context context, int theme, String title,String tips, OnSelectListener onSelectListener,int bg) {
        super(context, theme);
        this.context = context;
        this.tips = tips;
        this.title = title;
        this.onSelectListener = onSelectListener;
        this.bg = bg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose);
        initView();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.dialog_default_ok);
        btnCancel = (Button) findViewById(R.id.dialog_default_cancel);
        tvTips = (TextView) findViewById(R.id.dialog_default_tips);
        tvTitle = (TextView) findViewById(R.id.dialog_default_title);
//        if(title.equals(getContext().getString(R.string.version_update))){
//            tvTips.setGravity(Gravity.LEFT);
//        }
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        if(bg==R.drawable.shape_for_black_trans){
            findViewById(R.id.ll_bg).setBackgroundResource(bg);
            tvTitle.setTextColor(getContext().getResources().getColor(R.color.white));
            btnSubmit.setTextColor(getContext().getResources().getColor(R.color.white));
            btnCancel.setTextColor(getContext().getResources().getColor(R.color.theme_color));
        }

        if(!TextUtils.isEmpty(tips)){
            tvTips.setText(tips);
        }

        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if(onSelectListener!=null)
        if(v==btnSubmit){
            onSelectListener.onSelected(true);
            this.dismiss();
        }
        if(v==btnCancel){
            onSelectListener.onSelected(false);
            this.dismiss();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(onSelectListener!=null)
            onSelectListener.onSelected(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
