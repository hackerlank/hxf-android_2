package com.goodhappiness.dao;

import com.goodhappiness.widget.ObservableScrollView;

/**
 * Created by 电脑 on 2016/5/11.
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
