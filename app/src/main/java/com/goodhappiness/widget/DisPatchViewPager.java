package com.goodhappiness.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 电脑 on 2016/5/13.
 */
public class DisPatchViewPager extends ViewPager {
    public DisPatchViewPager(Context context) {
        this(context,null);
    }

    public DisPatchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
