package com.goodhappiness.dao;

import com.goodhappiness.widget.ObservableHorizontalScrollView;

/**
 * Created by 电脑 on 2016/5/11.
 */
public interface HorizontalScrollViewListener {
    void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
}
