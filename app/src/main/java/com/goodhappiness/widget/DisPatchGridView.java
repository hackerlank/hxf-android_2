package com.goodhappiness.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by 电脑 on 2016/5/4.
 */
public class DisPatchGridView extends GridView {
    public DisPatchGridView(Context context) {
        this(context,null);
    }

    public DisPatchGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisPatchGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
