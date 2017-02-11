package com.goodhappiness.dao;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by 电脑 on 2016/5/16.
 */
public class onHorizontalGestureListener extends GestureDetector.SimpleOnGestureListener
{
    private OnGestureScrollListener listener;
    public onHorizontalGestureListener(OnGestureScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e)
    {
        // TODO Auto-generated method stub
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        // TODO Auto-generated method stub
        return super.onDown(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY)
    {
        Log.e("e_","x"+velocityX);
        if(Math.abs(velocityX)>1000){
            if(listener!=null){
                if(velocityX>0){//下滑
                    listener.onLeftScroll();
                }else{//上滑
                    listener.onRightScroll();
                }
            }
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY)
    {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        return super.onSingleTapUp(e);
    }

}