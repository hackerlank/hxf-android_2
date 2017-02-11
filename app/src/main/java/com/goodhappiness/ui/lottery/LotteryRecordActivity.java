package com.goodhappiness.ui.lottery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 抽奖记录
 */
@ContentView(R.layout.activity_lottery_record)
public class LotteryRecordActivity extends BaseFragmentActivity implements OnTabSelectListener {
    public static final int STATUS_ALL = 0;
    public static final int STATUS_ING = 1;
    public static final int STATUS_REVEALED = 3;
//    @ViewInject(R.id.lottery_record_ll_no_record)
//    private LinearLayout ll;
//    @ViewInject(R.id.lottery_record_tv_all)
//    private TextView tv_all;
//    @ViewInject(R.id.lottery_record_tv_ing)
//    private TextView tv_ing;
//    @ViewInject(R.id.lottery_record_tv_revealed)
//    private TextView tv_revealed;
//    @ViewInject(R.id.lottery_record_v_all)
//    private View v_all;
//    @ViewInject(R.id.lottery_record_v_ing)
//    private View v_ing;
//    @ViewInject(R.id.lottery_record_v_reveal)
//    private View v_revealed;
//    @ViewInject(R.id.lottery_record_lv)
//    private ListView lv;
//    @ViewInject(R.id.lottery_record_srl)
//    private SwipyRefreshLayout srl;

//    private int page;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout tabLayout_5;
    private int status = 0;
    private LotteryRecordFragmentInPerson allLotteryRecordFragment;
    private LotteryRecordFragmentInPerson ingLotteryRecordFragment;
    private LotteryRecordFragmentInPerson revealedLotteryRecordFragment;
//    private CommonAdapter<LotteryRecord.ListBean> adapter;
//    private List<LotteryRecord.ListBean> list = new ArrayList<>();
//    private boolean hadRecord = true;
//    private boolean isCanLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.lottery_record));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.lottery_record));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        mTitles = new String[]{getString(R.string.all), getString(R.string.period_ing), getString(R.string.period_revealed)};
        initView();
        tv_title.setText(getString(R.string.lottery_record));
        allLotteryRecordFragment = LotteryRecordFragmentInPerson.newInstance(getUid() + "",LotteryRecordFragment.STATUS_ALL);
        ingLotteryRecordFragment = LotteryRecordFragmentInPerson.newInstance(getUid() + "",LotteryRecordFragment.STATUS_ING);
        revealedLotteryRecordFragment = LotteryRecordFragmentInPerson.newInstance(getUid() + "",LotteryRecordFragment.STATUS_REVEALED);
        mFragments.add(allLotteryRecordFragment);
        mFragments.add(ingLotteryRecordFragment);
        mFragments.add(revealedLotteryRecordFragment);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout_5.setViewPager(vp);
        tabLayout_5.setOnTabSelectListener(this);
        if (getIntent() != null) {
            status = getIntent().getIntExtra(FieldFinals.STATUS, STATUS_ALL);
            switch (status){
                case 0:
                    vp.setCurrentItem(0);
                    break;
                case 1:
                    vp.setCurrentItem(1);
                    break;
                case 3:
                    vp.setCurrentItem(2);
                    break;
            }
        }
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
    private void init() {
        if (status == 2) {
//            onEventClick(tv_ing);
        } else if (status == 3) {
//            onEventClick(tv_revealed);
        }
    }

}
