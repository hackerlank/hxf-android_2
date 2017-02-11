package com.goodhappiness.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by 电脑 on 2016/8/19.
 */
public class DisPatchRelativeLayout extends RelativeLayout{
    public DisPatchRelativeLayout(Context context) {
        this(context,null);
    }

    public DisPatchRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DisPatchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
