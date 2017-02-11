package com.goodhappiness.ui.social;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.MyFragmentPagerAdapter;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseFragmentActivity {
    @ViewInject(R.id.et_key)
    private EditText et_key;
    @ViewInject(R.id.search_pager)
    private ViewPager viewPager;
    @ViewInject(R.id.search_v_line)
    private ImageView ivBottomLine;
    @ViewInject(R.id.search_tv_user)
    private TextView txtPopular;
    @ViewInject(R.id.search_tv_tag)
    private TextView txtSellGood;
    private int currIndex = 0;

    private ArrayList<Fragment> fragmentsList;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    public SearchUserFragment focusFragment;
    public FocusFragment worldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        InitWidth();
        InitTextView();
        InitViewPager();
        TranslateAnimation animation = new TranslateAnimation(position_one,
                position_one - offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.SEARCH_KEY))) {
            searchTag(getIntent().getStringExtra(FieldFinals.SEARCH_KEY));
        }
    }

    private void InitTextView() {
        txtPopular.setOnClickListener(new MyOnClickListener(0));
        txtSellGood.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        fragmentsList = new ArrayList<>();
        focusFragment = new SearchUserFragment().newInstance();
        worldFragment = new FocusFragment().newInstance("3", -1);
        fragmentsList.add(focusFragment);
        fragmentsList.add(worldFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentsList));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        if (getIntent() != null && getIntent().getIntExtra(FieldFinals.POSITION, 0) != 0) {
            viewPager.setCurrentItem(1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Animation animation = new TranslateAnimation(offset, position_one, 0, 0);
                            currIndex = 1;
                            animation.setFillAfter(true);
                            animation.setDuration(300);
                            ivBottomLine.startAnimation(animation);
                        }
                    });
                }
            }, 500);
        }
    }

    private void InitWidth() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivBottomLine.getLayoutParams();
        bottomLineWidth = layoutParams.width = (int) ((float) (GoodHappinessApplication.w * 13) / 48);
        ivBottomLine.setLayoutParams(layoutParams);
        offset = bottomLineWidth;
        position_one = GoodHappinessApplication.w / 2;
    }

    @Event({R.id.tv_search, R.id.iv_back})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(et_key.getText().toString())) {
                    showToast(R.string.please_input_all_msg);
                    return;
                }
                if (currIndex == 0) {
                    focusFragment.onSearch(et_key.getText().toString(),true);
                } else {
                    worldFragment.onSearch(et_key.getText().toString());
                }
                break;
        }
    }

    public void searchTag(String key) {
        worldFragment.onSearch(key);
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                // 指滑动到右边的页面
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_one - offset, 0,
                                0);
                    }
                    break;
                // 滑动到左边的页面
                case 1:

                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0,
                                0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

}
