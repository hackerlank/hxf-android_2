package com.goodhappiness.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by 电脑 on 2016/8/19.
 */
public class DisPatchLinearLayout extends LinearLayout{
    public DisPatchLinearLayout(Context context) {
        this(context,null);
    }

    public DisPatchLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DisPatchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
