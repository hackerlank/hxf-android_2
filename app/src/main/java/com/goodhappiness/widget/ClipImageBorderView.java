package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author 肖邦
 * @ClassName: ClipImageBorderView
 * @Description: 选择头像矩形自定义矩形控件
 * @date 2015-1-26 下午5:38:30
 */
public class ClipImageBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 绘制的边框
     */
    private int bordar_type;
    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Bitmap dstBitmap;

    private Paint mPaint;
    public static final int BORDER_STYLE_CIRCLE = 0;
    public static final int BORDER_STYLE_RECTANGLE = 1;
    private int borderStyle = BORDER_STYLE_CIRCLE;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle, int borderStyle) {
        super(context, attrs, defStyle);
        this.borderStyle = borderStyle;
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(borderStyle==BORDER_STYLE_CIRCLE) {
            // 计算矩形区域的宽度
            mWidth = getWidth() - 2 * mHorizontalPadding;
            // 计算距离屏幕垂直边界 的边距
            mVerticalPadding = (getHeight() - mWidth) / 2;
            mPaint.setColor(Color.parseColor("#ffffffff"));
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setStyle(Paint.Style.STROKE);

            int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#ffffffff"));
            canvas.drawOval(new RectF(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding, getHeight() - mVerticalPadding), paint);
            paint.setColor(Color.parseColor("#aa000000"));
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            paint.setXfermode(null);
//		// 还原画布
            canvas.restoreToCount(sc);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mWidth / 2, mPaint);
        }else if(borderStyle==BORDER_STYLE_RECTANGLE){
            int l = (int)((float)getWidth()/3);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.WHITE);
            Path path = new Path();
            PathEffect effects = new DashPathEffect(new float[]{2,2,2,2},1);
            paint.setPathEffect(effects);
            path.moveTo(0, l);
            path.lineTo(getWidth(),l);
            canvas.drawPath(path, paint);
            path.moveTo(0, 2*l);
            path.lineTo(getWidth(),2*l);
            canvas.drawPath(path, paint);
            path.moveTo(l, 0);
            path.lineTo(l,getHeight());
            canvas.drawPath(path, paint);
            path.moveTo(2*l, 0);
            path.lineTo(2*l,getHeight());
            canvas.drawPath(path, paint);
//            // 计算矩形区域的宽度
//            mWidth = getWidth() - 2 * mHorizontalPadding;
//            // 计算距离屏幕垂直边界 的边距
//            mVerticalPadding = (getHeight() - mWidth) / 2;
//            mPaint.setColor(Color.parseColor("#aa000000"));
//            mPaint.setStyle(Paint.Style.FILL);
//            // 绘制左边1
//            canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
//            // 绘制右边2
//            canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(), getHeight(), mPaint);
//            // 绘制上边3
//            canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding, mVerticalPadding, mPaint);
//            // 绘制下边4
//            canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding, getWidth() - mHorizontalPadding, getHeight(), mPaint);
//            // 绘制外边框
//            mPaint.setColor(mBorderColor);
//            mPaint.setStrokeWidth(mBorderWidth);
//            mPaint.setStyle(Paint.Style.STROKE);
//            canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

        }
    }

    //创建一个圆形bitmap，作为dst图
    private Bitmap makeDst(int r) {
        Bitmap bm = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0x00FFCC44);
        c.drawOval(new RectF(0, 0, r, r), p);
        return bm;
    }

    private Bitmap drawBackground(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xaa000000);
        c.drawRect(0, 0, w, h, p);
        return bm;
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;

    }

}
