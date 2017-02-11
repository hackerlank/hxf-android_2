package com.goodhappiness.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by 电脑 on 2016/5/13.
 */
public class DisPatchListView extends ListView {
    public DisPatchListView(Context context) {
        this(context,null);
    }

    public DisPatchListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisPatchListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
