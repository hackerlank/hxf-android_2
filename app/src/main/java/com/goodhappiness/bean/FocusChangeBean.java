package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/6/28.
 */
public class FocusChangeBean implements Serializable {

    private static final long serialVersionUID = -6101506685833212272L;
    private long focusCount;

    public FocusChangeBean(long focusCount) {
        this.focusCount = focusCount;
    }

    public long getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(long focusCount) {
        this.focusCount = focusCount;
    }

}
