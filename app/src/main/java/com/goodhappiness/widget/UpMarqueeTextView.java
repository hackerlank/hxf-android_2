package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 跑马灯效果<向上方向>的TextView
 * Created by jayuchou on 15/7/9.
 */
public class UpMarqueeTextView extends LinearLayout implements Animator.AnimatorListener {

    private static final String TAG = "UpMarqueeTextView";

    private static final int ANIMATION_DURATION = 200;
    private float height;
    private int position;
    private AnimatorSet mAnimatorStartSet;
    private AnimatorSet mAnimatorEndSet;
    private String[] mText;
    private OnClickListener onNameClickListener;
    private OnClickListener onPeriodClickListener;
    public UpMarqueeTextView(Context context) {
        super(context);
    }

    public UpMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.w(TAG, "--- onDraw ---");
        height = GoodHappinessApplication.perHeight*40;// 确保view的高度
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**--- 初始化向上脱离屏幕的动画效果 ---*/
    private void initStartAnimation() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", 0, -height);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mAnimatorStartSet = new AnimatorSet();
        mAnimatorStartSet.play(translate).with(alpha);
        mAnimatorStartSet.setDuration(ANIMATION_DURATION);
        mAnimatorStartSet.addListener(this);
    }

    /**--- 初始化从屏幕下面向上的动画效果 ---*/
    private void initEndAnimation() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", height, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mAnimatorEndSet = new AnimatorSet();
        mAnimatorEndSet.play(translate).with(alpha);
        mAnimatorEndSet.setDuration(ANIMATION_DURATION);
    }

    public void setOnNameClickListener(OnClickListener onNameClickListener){
        this.onNameClickListener = onNameClickListener;
    }
    public void setOnPeriosClickListener(OnClickListener onPeriodClickListener){
        this.onPeriodClickListener = onPeriodClickListener;
    }

    /**--- 设置内容 ---**/
    public void setText(String... text) {
        if(height==0){
            height = GoodHappinessApplication.perHeight*40;

        }
        if (text.length<=0) {
            Log.e(TAG, "--- 请确保text不为空 ---");
            return;
        }
        mText = text;
        if (null == mAnimatorStartSet) {
            initStartAnimation();
        }
        mAnimatorStartSet.start();
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        removeAllViews();
        int i = 0;
        for(String s:mText){
            TextView textView = new EmojiTextView(getContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setText(s);
            textView.setTextSize(12);
            textView.setTextColor(getContext().getResources().getColor(R.color.gray_999_text));
            switch (i++){
                case 1:
                    textView.setTextColor(getContext().getResources().getColor(R.color.advert_blue_text));
                    textView.setPadding(10, 0, 10, 0);
                    textView.setMaxEms(8);
                    textView.setSingleLine();
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    if(onNameClickListener!=null){
                        textView.setOnClickListener(onNameClickListener);
                    }
                    break;
                case 3:
                    textView.setTextColor(getContext().getResources().getColor(R.color.gray_666_text));
                    textView.setPadding(10, 0, 10, 0);
                    if(onNameClickListener!=null){
                        textView.setOnClickListener(onPeriodClickListener);
                    }
                    break;
            }
            addView(textView);
        }
        if (null == mAnimatorEndSet) {
            initEndAnimation();
        }
        mAnimatorEndSet.start();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
