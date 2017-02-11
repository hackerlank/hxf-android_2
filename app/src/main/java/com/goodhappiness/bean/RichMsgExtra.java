package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/7/15.
 */
public class RichMsgExtra implements Serializable{

    private static final long serialVersionUID = -2044220594588018572L;

    private int type;
    private String androidUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }
}
