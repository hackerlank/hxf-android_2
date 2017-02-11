package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/9/9.
 */
public class SearchTag {
    private String msg;
    private boolean isFocus = false;

    public SearchTag() {
    }

    public SearchTag(String msg, boolean isFocus) {
        this.msg = msg;
        this.isFocus = isFocus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
}
