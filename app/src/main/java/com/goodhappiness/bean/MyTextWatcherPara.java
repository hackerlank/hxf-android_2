package com.goodhappiness.bean;

import android.widget.EditText;

/**
 * Created by 电脑 on 2016/6/6.
 */
public class MyTextWatcherPara {
    private EditText editText;
    private String tips;

    public MyTextWatcherPara(EditText editText) {
        this.editText = editText;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

}
