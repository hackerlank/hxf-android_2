package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 电脑 on 2016/6/24.
 */
public class LimitTextView extends TextView {
    public LimitTextView(Context context) {
        this(context, null);
    }

    public LimitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getText().toString().length()>10){
            String a = getText().toString().substring(0,9)+"...";
            setText(a);
        }

    }
}
