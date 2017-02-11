package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/8.
 */
public class BaseBean {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseBean() {
    }

    public BaseBean(String msg) {
        this.msg = msg;
    }
}
