package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.goodhappiness.R;


/**
 * Created by Administrator on 2016/3/9.
 */
public class PercentageBar extends View {
    private float percentage = 50;
    private Paint paint;

    public PercentageBar(Context context) {
        super(context);
    }

    public PercentageBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentageBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float wid = (percentage / 100)*width;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.theme_color));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, wid, height, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(getResources().getColor(R.color.black_333_text));
        canvas.drawLine(0, 0, width, 0, paint);//top
        canvas.drawLine(0, 0, 0, height, paint);//left
        canvas.drawLine(width, 0, width, height, paint);//right
        canvas.drawLine(0,height,width,height,paint);//bottom
    }
}
