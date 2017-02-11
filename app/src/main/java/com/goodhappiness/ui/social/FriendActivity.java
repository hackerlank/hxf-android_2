package com.goodhappiness.ui.social;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.activity_friendship2)
public class FriendActivity extends BaseFragmentActivity implements OnTabSelectListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout tabLayout_5;
    private FriendshipFragment focusFragment;
    private FriendshipFragment fansFragment;
    public static final String ACTION_FOCUS = "1";
    public static final String ACTION_FANS = "2";
    private long uid;
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        uid = intent.getLongExtra(FieldFinals.UID, -1);
        focusFragment.setUid(uid);
        fansFragment.setUid(uid);
        focusFragment.onResume();
        fansFragment.onResume();
        if (intent.getIntExtra(FieldFinals.POSITION, 0) == 0) {
            vp.setCurrentItem(0);
        } else {
            vp.setCurrentItem(1);
        }
}


    @Override
    protected void setData() {
        setData2();
    }

    private void setData2() {
        uid = getIntent().getLongExtra(FieldFinals.UID, -1);
        mTitles = new String[]{getString(R.string.focus), getString(R.string.fans)};
        focusFragment = FriendshipFragment.newInstance(ACTION_FOCUS, uid, getIntent().getIntExtra(FieldFinals.TYPE, 1));
        fansFragment = FriendshipFragment.newInstance(ACTION_FANS, uid, getIntent().getIntExtra(FieldFinals.TYPE, 1));
        mFragments.add(focusFragment);
        mFragments.add(fansFragment);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(myPagerAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0&& GoodHappinessApplication.isNeedRefresh){
                    focusFragment.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout_5.setViewPager(vp);
        tabLayout_5.setOnTabSelectListener(this);
        if (getIntent().getIntExtra(FieldFinals.POSITION, 0) == 0) {
            vp.setCurrentItem(0);
        } else {
            vp.setCurrentItem(1);
        }
    }

    @Event({R.id.common_left})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_left:
                finishActivity();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
