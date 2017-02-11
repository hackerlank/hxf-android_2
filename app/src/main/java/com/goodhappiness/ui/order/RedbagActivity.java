package com.goodhappiness.ui.order;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
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
public class RedbagActivity extends BaseFragmentActivity implements OnTabSelectListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout tabLayout_5;
    private MyPagerAdapter myPagerAdapter;
    private RedbagFragment focusFragment;
    private RedbagFragment fansFragment;
    public static final String STATUS_CAN_USE = "0";
    public static final String STATUS_CANT_USE = "1";
    private long uid;

    @Override
    protected void setData() {
        uid = getIntent().getLongExtra(FieldFinals.UID, -1);
        mTitles = new String[]{getString(R.string.can_use), getString(R.string.over_or_deadline)};
        focusFragment = RedbagFragment.newInstance(STATUS_CAN_USE, "");
        fansFragment = RedbagFragment.newInstance(STATUS_CANT_USE, "");
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

    public void setCanUseCount(int count){
        mTitles = new String[]{getString(R.string.can_use)+"("+count+"个)", getString(R.string.over_or_deadline)};
        myPagerAdapter.notifyDataSetChanged();
        tabLayout_5.notifyDataSetChanged();
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
