package com.goodhappiness.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.goodhappiness.R;

/**
 * Created by 电脑 on 2016/4/20.
 */
public class TimeAxis extends View {
    private int position = 2;
    private int status = 3;
    public static final int POSITION_TOP = 1;
    public static final int POSITION_NORMAL = 2;
    public static final int POSITION_BOTTOM = 3;
    public static final int STATUS_PAST = 1;
    public static final int STATUS_NOW = 2;
    public static final int STATUS_FUTURE = 3;
    private boolean isFirstStep = false;

    public TimeAxis(Context context) {
        this(context, null);
    }

    public TimeAxis(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeAxis(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeAxis, defStyleAttr, 0);
        isFirstStep = a.getBoolean(R.styleable.TimeAxis_isFirstStep,false);
        position = a.getInteger(R.styleable.TimeAxis_time_position,POSITION_NORMAL);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setStatus(int status){
        this.status = status;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.black_333_text));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
        switch (position) {
            case POSITION_TOP:
                canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight(), paint);
                break;
            case POSITION_NORMAL:
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
                break;
            case POSITION_BOTTOM:
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() / 2, paint);
                break;
        }

        Paint mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        Bitmap bitmap = null;
        switch (status) {
            case STATUS_PAST:
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_point_past);
                break;
            case STATUS_NOW:
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_point_now);
                break;
            case STATUS_FUTURE:
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_point_futura);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_point_past);
                break;
        }
        if(isFirstStep){
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, 30, mBitPaint);
        }else{
            canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, mBitPaint);
        }
    }
}
