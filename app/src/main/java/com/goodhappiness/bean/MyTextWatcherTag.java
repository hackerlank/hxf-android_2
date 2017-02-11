package com.goodhappiness.bean;

import com.goodhappiness.dao.MyTextWatcher;

/**
 * Created by 电脑 on 2016/5/23.
 */
public class MyTextWatcherTag {
    private MyTextWatcher myTextWatcher;
    private int position;
    private int code;

    public MyTextWatcherTag(MyTextWatcher myTextWatcher, int position,int code) {
        this.myTextWatcher = myTextWatcher;
        this.position = position;
        this.code = code;
    }

    public MyTextWatcher getMyTextWatcher() {
        return myTextWatcher;
    }

    public void setMyTextWatcher(MyTextWatcher myTextWatcher) {
        this.myTextWatcher = myTextWatcher;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
