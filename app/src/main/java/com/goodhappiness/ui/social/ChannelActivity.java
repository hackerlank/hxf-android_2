package com.goodhappiness.ui.social;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.goodhappiness.R;
import com.goodhappiness.adapter.MyFragmentPagerAdapter;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_channel)
public class ChannelActivity extends BaseFragmentActivity {
    @ViewInject(R.id.search_pager)
    private ViewPager viewPager;
    private FocusFragment focusFragment;
    private ArrayList<Fragment> fragmentsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        if(!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.CHANNEL))){
            tv_title.setText(getIntent().getStringExtra(FieldFinals.CHANNEL));
        }
        fragmentsList = new ArrayList<>();
        focusFragment = FocusFragment.newInstance(getIntent().getStringExtra(FieldFinals.POST_TYPE).equals(FocusFragment.POST_TYPE_FOCUS)?FocusFragment.POST_TYPE_CHANNEL_FOCUS:FocusFragment.POST_TYPE_CHANNEL_WORLD,getIntent().getIntExtra(FieldFinals.CHANNEL_ID,0));
        fragmentsList.add(focusFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentsList));
    }
}
