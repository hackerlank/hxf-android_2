package com.goodhappiness.widget.social;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

/**
 * Created by 电脑 on 2016/5/24.
 */
public class MyImageView extends ImageView implements FilterImageViewInterface {
    public MyImageView(Context context) {
        this(context,null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImage(Bitmap bitmap) {

    }

    @Override
    public void setImageBackgroundColor(int i) {

    }

    @Override
    public void setFilterWrap(FilterWrap filterWrap) {

    }

    @Override
    public void enableTouchForOrigin() {

    }

    @Override
    public void disableTouchForOrigin() {

    }

    @Override
    public void requestRender() {

    }
}
