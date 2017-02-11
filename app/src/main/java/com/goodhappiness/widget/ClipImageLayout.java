package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;


/**
* @ClassName: ClipImageLayout
* @Description: 选择头像自定义布局
* @author 肖邦
* @date 2015-1-26 下午5:39:04
*
 */
public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private Drawable defaultDrawable;
	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		this(context, attrs, null,0,0);
	}

	public ClipImageLayout(Context context, AttributeSet attrs, Drawable drawable,int padding,int borderStyle) {
		super(context, attrs);
		mHorizontalPadding = padding;
		defaultDrawable = getResources().getDrawable(R.mipmap.loading_default);
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context,null,0,borderStyle);
		android.view.ViewGroup.LayoutParams lp;
		if(padding==0){
			lp = new LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
		}else{
			lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		}
		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
		if (drawable != null) {
			mZoomImageView.setImageDrawable(drawable);
		} else {
			mZoomImageView.setImageDrawable(defaultDrawable);
		}

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

	public void setImage(Drawable drawable) {
		this.defaultDrawable = drawable;
		invalidate();
	}

}
