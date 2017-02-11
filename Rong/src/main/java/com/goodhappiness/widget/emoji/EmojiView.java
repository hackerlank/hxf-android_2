package com.goodhappiness.widget.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.goodhappiness.widget.emoji.emoji.Cars;
import com.goodhappiness.widget.emoji.emoji.Electronics;
import com.goodhappiness.widget.emoji.emoji.Food;
import com.goodhappiness.widget.emoji.emoji.Nature;
import com.goodhappiness.widget.emoji.emoji.People;
import com.goodhappiness.widget.emoji.emoji.Sport;
import com.goodhappiness.widget.emoji.emoji.Symbols;
import com.goodhappiness.widget.emoji.listeners.OnEmojiBackspaceClickListener;
import com.goodhappiness.widget.emoji.listeners.OnEmojiClickedListener;
import com.goodhappiness.widget.emoji.listeners.RepeatListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.R;

@SuppressLint("ViewConstructor")
final class EmojiView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static final long INITIAL_INTERVAL = TimeUnit.SECONDS.toMillis(1) / 2;
    private static final int NORMAL_INTERVAL = 50;

    @ColorInt
    private final int themeAccentColor;
    @Nullable
    private OnEmojiBackspaceClickListener onEmojiBackspaceClickListener;

    private int emojiTabLastSelectedIndex = -1;

    private RecentEmojiGridView recentGridView;
    private LinearLayout linearLayout;
    private List<CircleView> pointView = new ArrayList<>();

    EmojiView(final Context context, final OnEmojiClickedListener onEmojiClickedListener, @NonNull final RecentEmoji recentEmoji) {
        super(context);

        View.inflate(context, R.layout.emoji_view, this);

        final ViewPager emojisPager = (ViewPager) findViewById(R.id.emojis_pager);
        emojisPager.addOnPageChangeListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.emojis_ll);

        final List<FrameLayout> views = getViews(context, onEmojiClickedListener, recentEmoji);

        for(int i = 0;i<views.size();i++){
            CircleView v = new CircleView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5,5);
            params.width = (int) (30);
            params.height = (int) (30);
            params.rightMargin = (int) (30);
            v.setLayoutParams(params);
            linearLayout.addView(v);
            pointView.add(v);
        }

        final EmojiPagerAdapter emojisAdapter = new EmojiPagerAdapter(views);
        emojisPager.setAdapter(emojisAdapter);

        handleOnClicks(emojisPager);

        findViewById(R.id.emojis_backspace).setOnTouchListener(new RepeatListener(INITIAL_INTERVAL, NORMAL_INTERVAL, new OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (onEmojiBackspaceClickListener != null) {
                    onEmojiBackspaceClickListener.onEmojiBackspaceClicked(view);
                }
            }
        }));

        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        themeAccentColor = value.data;

        emojisPager.setCurrentItem(0);
        onPageSelected(0);
    }

    private void handleOnClicks(final ViewPager emojisPager) {
    }

    public void setOnEmojiBackspaceClickListener(@Nullable final OnEmojiBackspaceClickListener onEmojiBackspaceClickListener) {
        this.onEmojiBackspaceClickListener = onEmojiBackspaceClickListener;
    }

    @NonNull
    private List<FrameLayout> getViews(final Context context, @Nullable final OnEmojiClickedListener onEmojiClickedListener, @NonNull final RecentEmoji recentEmoji) {
        final EmojiGridView peopleGridView = new EmojiGridView(context).init(People.DATA, onEmojiClickedListener);
        final EmojiGridView natureGridView = new EmojiGridView(context).init(Nature.DATA, onEmojiClickedListener);
        final EmojiGridView foodGridView = new EmojiGridView(context).init(Food.DATA, onEmojiClickedListener);
        final EmojiGridView sportGridView = new EmojiGridView(context).init(Sport.DATA, onEmojiClickedListener);
        final EmojiGridView carsGridView = new EmojiGridView(context).init(Cars.DATA, onEmojiClickedListener);
        final EmojiGridView electronicsGridView = new EmojiGridView(context).init(Electronics.DATA, onEmojiClickedListener);
        final EmojiGridView symbolsGridView = new EmojiGridView(context).init(Symbols.DATA, onEmojiClickedListener);
        return Arrays.asList( (FrameLayout)peopleGridView, natureGridView, foodGridView, sportGridView, carsGridView, electronicsGridView, symbolsGridView);
    }

    @Override
    public void onPageSelected(final int i) {
        if(pointView.size()>0){
            int a = 0;
            for(CircleView view:pointView){
                if(i==a){
                    view.setColor(getResources().getColor(R.color.gray_666_text));
                }else{
                    view.setColor(getResources().getColor(R.color.c_line));
                }
                a++;
            }
        }
    }

    @Override
    public void onPageScrolled(final int i, final float v, final int i2) {
        // Don't care
    }

    @Override
    public void onPageScrollStateChanged(final int i) {
        // Don't care
    }
}
