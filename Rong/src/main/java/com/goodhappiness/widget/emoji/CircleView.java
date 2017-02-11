package com.goodhappiness.widget.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import io.rong.imkit.R;


/**
 * Created by 电脑 on 2016/5/11.
 */
public class CircleView extends View {
    private int color;
    private Paint paint;
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        color = getResources().getColor(R.color.theme_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
    }
    public void setColor(int color){
        this.color = color;
        invalidate();
    }
}
