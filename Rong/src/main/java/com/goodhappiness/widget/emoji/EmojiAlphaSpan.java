package com.goodhappiness.widget.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.DynamicDrawableSpan;

final class EmojiAlphaSpan extends DynamicDrawableSpan {
    private final Context context;
    private final int     resourceId;
    private final int     size;
    private final float     alpha;

    private Drawable drawable;

    EmojiAlphaSpan(final Context context, final int resourceId, final int size,final float alpha) {
        this.context = context;
        this.resourceId = resourceId;
        this.size = size;
        this.alpha = alpha;
    }

    @Override
    public Drawable getDrawable() {
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, resourceId);
            drawable.setBounds(0, 0, size, size);
            drawable.setAlpha((int) (alpha*255));
        }

        return drawable;
    }
}
