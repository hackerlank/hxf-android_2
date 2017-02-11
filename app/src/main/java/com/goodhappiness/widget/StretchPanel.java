package com.goodhappiness.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.goodhappiness.R;


/**
 * Copyright 2014 soso_fy（flytopc@hotmail.com）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * StretchPanel可以设置始终展示的视图和拓展视图
 */
public class StretchPanel extends LinearLayout {

    private static final String TAG = StretchPanel.class.getSimpleName();
    
    private View mContentView;
    private View mStretchView;
    private int mContentViewLayoutId = -1;
    private int mStretchViewLayoutId = -1;
    private int mStretchHeight;
    private OnStretchListener mListener;
    private int mAnimationDuration = 300;
    private boolean isOpened = false;
    
    public interface OnStretchListener {
        /**
         * 动画结束监听
         * @param isOpened 当前的stretchView是否是打开的,
         */
        public void onStretchFinished(boolean isOpened);
    }
    
    
    public StretchPanel(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setOrientation(LinearLayout.VERTICAL);
    }
    
    public StretchPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setOrientation(LinearLayout.VERTICAL);
        init(context, attrs);
    }
    
    @SuppressLint("NewApi")
    public StretchPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        setOrientation(LinearLayout.VERTICAL);
        init(context, attrs);
    }

    // 获取自定义的属性值
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StretchPanel);
        mContentViewLayoutId = ta.getResourceId(R.styleable.StretchPanel_contentView, -1);
        mStretchViewLayoutId = ta.getResourceId(R.styleable.StretchPanel_stretchView, -1);
        if (mContentViewLayoutId > 0) {
            View view  = View.inflate(context, mContentViewLayoutId, null);
            setContentView(view);
        }
        if (mStretchViewLayoutId > 0) {
            View view = View.inflate(context, mStretchViewLayoutId, null);
            setStretchView(view);
        }
        ta.recycle();
    }
    
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (mStretchHeight == 0 && mStretchView != null) {
            mStretchView.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
            mStretchHeight = mStretchView.getMeasuredHeight();
            mStretchView.getLayoutParams().height = 0;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    
    /**
     * 获取当前的主View
     * @return
     */
    public View getContentView() {
        return this.mContentView;
    }
    
    /**
     * 获取当前的扩展View
     * @return
     */
    public View getStretchView() {
        return this.mStretchView;
    }
    
    /**
     * 设置主View
     * @param view
     */
    public void setContentView(View view) {
        if (view != null) {
            if (this.mContentView != null) {
                removeView(this.mContentView);
            }
            this.mContentView = view;
            addView(mContentView, 0);
        }
    }
    
    /**
     * 设置伸展View
     * @param view
     */
    public void setStretchView(View view) {
        if (view != null) {
            if (this.mStretchView != null) {
                removeView(this.mStretchView);
                // 在重新设置时，将该值置为0，否则新view将不能显示正确的高度
                this.mStretchHeight = 0;
            }
            this.mStretchView = view;
            addView(mStretchView);
        }
    }

    /**
     * 设置动画的监听
     * @param listener
     */
    public void setOnStretchListener(OnStretchListener listener) {
        this.mListener = listener;
    }
    
    /**
     * 当前的视图是否已经展开
     * @return
     */
    public boolean isStretchViewOpened() {
        return isOpened;
    }
    
    /**
     * 设置展开（或者收缩）动画的时间，默认300ms
     */
    public void setStretchAnimationDuration(int durationMs) {
        if (durationMs >= 0) {
            this.mAnimationDuration = durationMs;
        } else {
            throw new IllegalArgumentException("Animation duration cannot be negative");
        }
    }
    
    
    /**
     * 设置点击事件的处理view
     * <p>如果不设置，点击事件需要在外部处理；如果设置的话，该view之前的点击事件将被覆盖掉
     * @param view
     */
    public void setHandleClikeEventOnThis(View view) {
        if (view != null) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (isOpened) {
                        closeStretchView();
                    } else {
                        openStretchView();
                    }
                }
            });
        }
    }
    
    /**
     * 展开视图
     */
    public void openStretchView() {
        if (mStretchView != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    StretchAnimation animation = new StretchAnimation(0, mStretchHeight);
                    animation.setDuration(mAnimationDuration);
                    animation.setAnimationListener(animationListener);
                    mStretchView.startAnimation(animation);
                    invalidate();
                }
            });
        }
    }
    
    /**
     * 收起视图
     */
    public void closeStretchView() {
        if (mStretchView != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    StretchAnimation animation = new StretchAnimation(mStretchHeight, 0);
                    animation.setDuration(mAnimationDuration);
                    animation.setAnimationListener(animationListener);
                    mStretchView.startAnimation(animation);
                    invalidate();
                }
            });
        }
    }
    
    private AnimationListener animationListener = new AnimationListener() {
        
        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub  
        }
        
        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub        
        }
        
        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            isOpened = !isOpened;
            if (mListener != null) {
                mListener.onStretchFinished(isOpened);
            }
        }
    };

    /**
     * 伸缩动画
     */
    private class StretchAnimation extends Animation {
        private int startHeight;
        private int deltaHeight;
        
        public StretchAnimation(int startH, int endH) {
            this.startHeight = startH;
            this.deltaHeight = endH - startH;
        }
        
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // TODO Auto-generated method stub
            if (mStretchView != null) {
                LayoutParams params = (LayoutParams) mStretchView.getLayoutParams();
                params.height = (int) (startHeight + deltaHeight * interpolatedTime);
                mStretchView.setLayoutParams(params);
            }
        }
    }
    
}