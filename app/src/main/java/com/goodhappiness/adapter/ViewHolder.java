package com.goodhappiness.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.widget.PercentageBar;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.goodhappiness.widget.timer.CountdownView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setEmojiText(int viewId, String text) {
        EmojiTextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setEmojiText(int viewId, CharSequence text) {
        EmojiTextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolder setVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }
    public ViewHolder setOnclickListener(int viewId, View.OnClickListener onClickListener) {
            getView(viewId).setOnClickListener(onClickListener);
        return this;
    }
    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param id
     * @return
     */
    public ViewHolder setText(int viewId, int id) {
        TextView view = getView(viewId);
        view.setText(id);
        return this;
    }

    /**
     * RatingBar 设置星级
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setRating(int viewId, float text) {
        RatingBar view = getView(viewId);
        view.setRating(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    public ViewHolder setTimer(int viewId, long time, CountdownView.OnCountdownEndListener onCountdownEndListener, int position) {
        CountdownView view = getView(viewId);
        view.setTag(position);
        view.setOnCountdownEndListener(onCountdownEndListener);
        view.start(time);
        return this;
    }

    public ImageView loadImage(int viewId, String url) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().displayImage(url, view, GoodHappinessApplication.options);
        return view;
    }
    public ImageView loadImage(int viewId, String url, ImageSize imageSize) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().displayImage(url, view, imageSize);
        return view;
    }
    public ViewHolder loadImage(ImageView imageView, String url, ImageSize imageSize) {
        ImageLoader.getInstance().displayImage(url, imageView,  imageSize);
        return this;
    }
    public ViewHolder setPercentage(int id, String p) {
        PercentageBar percentageBar = getView(id);
        try {
            percentageBar.setPercentage(Float.valueOf(p));
        } catch (Exception e) {

        }
        return this;
    }

    public ViewHolder loadImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, GoodHappinessApplication.options);
        return this;
    }

    public Bitmap loadImageSync(String url) {
        return ImageLoader.getInstance().loadImageSync(url, GoodHappinessApplication.options);
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable)

    {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}