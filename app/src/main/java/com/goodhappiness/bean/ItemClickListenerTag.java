package com.goodhappiness.bean;

import android.view.View;

/**
 * Created by 电脑 on 2016/4/22.
 */
public class ItemClickListenerTag {
    private View.OnClickListener onClickListener;
    private int position;

    public ItemClickListenerTag(View.OnClickListener onClickListener, int position) {
        this.onClickListener = onClickListener;
        this.position = position;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
