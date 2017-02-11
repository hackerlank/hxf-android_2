package com.goodhappiness.widget.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import io.rong.imkit.R;


public class EmojiTextView extends TextView {
    private int emojiSize;
    private int length = 5;
    private boolean isEllipsize;
    private float alpha = 1;
    public EmojiTextView(final Context context) {
        super(context);
        init(null);
    }

    public EmojiTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable final AttributeSet attrs) {
        if (attrs == null) {
            emojiSize = (int) getTextSize();
        } else {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.emoji);

            try {
                emojiSize = (int) a.getDimension(R.styleable.emoji_emojiSize, getTextSize());
                isEllipsize = a.getBoolean(R.styleable.emoji_isEllipsize, false);
                length = a.getInteger(R.styleable.emoji_length, 5);
            } finally {
                a.recycle();
            }
        }

        setText(getText());
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        this.alpha = alpha;
        invalidate();
    }

    @Override
    public void setText(final CharSequence rawText, final BufferType type) {
        final CharSequence text = rawText == null ? "" : rawText;
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        EmojiHandler.addAlphaEmojis(getContext(), spannableStringBuilder, emojiSize,alpha);
        if(isEllipsize&&text.length()>=length){
            CharSequence mText = text.subSequence(0,length);
            SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(mText);
            mSpannableStringBuilder.append("...");
            super.setText(mSpannableStringBuilder, type);
        }else{
            super.setText(spannableStringBuilder, type);
        }
    }

    public void setEmojiSize(final int pixels) {
        emojiSize = pixels;
    }
}
