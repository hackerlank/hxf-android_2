package com.goodhappiness.ui.social;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.activity_flower_record)
public class FlowerActivity extends BaseFragmentActivity implements OnTabSelectListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout tabLayout_5;
    private FlowerReceiveListFragment focusFragment;
    private FlowerSendListFragment fansFragment;
    public static final String ACTION_FOCUS = "1";
    public static final String ACTION_FANS = "2";
    private long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendFlowerSuccessBroadcastReceiver = new SendFlowerSuccessBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter(StringFinal.BROADCAST_FLOWER_SEND);
        registerReceiver(sendFlowerSuccessBroadcastReceiver, filter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendFlowerSuccessBroadcastReceiver);
    }

    @Override
    protected void setData() {
        uid = getIntent().getLongExtra(FieldFinals.UID, -1);
        mTitles = new String[]{getString(R.string.receive_flower), getString(R.string.send_flower)};
        focusFragment = FlowerReceiveListFragment.newInstance("1");
        fansFragment = FlowerSendListFragment.newInstance("2");
        mFragments.add(focusFragment);
        mFragments.add(fansFragment);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout_5.setViewPager(vp);
        tabLayout_5.setOnTabSelectListener(this);
        if (getIntent().getIntExtra(FieldFinals.POSITION, 0) == 0) {
            vp.setCurrentItem(0);
        } else {
            vp.setCurrentItem(1);
        }
    }
    private SendFlowerSuccessBroadcastReceiver sendFlowerSuccessBroadcastReceiver;

    private class SendFlowerSuccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isFinishing()){
                return;
            }
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FLOWER_SEND) && intent.getStringExtra(FieldFinals.ACTION).contains(FlowerActivity.class.getName())) {
                if(intent.getBooleanExtra(FieldFinals.RESULT,false)){
                    RongIMUtils.sendFlower(intent.getIntExtra(FieldFinals.COUNT, 0), intent.getStringExtra(FieldFinals.NICKNAME), String.valueOf(intent.getLongExtra(FieldFinals.UID,-1)),
                            new RongIMClient.SendMessageCallback() {
                                @Override
                                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {

                                }
                            }, new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    if(!isFinishing()){
                                        DialogFactory.createFlowerSendDialog(FlowerActivity.this);
                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }else{
                    if(!isFinishing()){
                        DialogFactory.createDefaultDialog(FlowerActivity.this,getString(R.string.send_flower_fail));
                    }
                }
            }
        }
    }
    @Event({R.id.common_left,R.id.friendship_invite})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_left:
                finishActivity();
                break;
            case R.id.friendship_invite:
                DialogFactory.createShareDialog(0,this, null, new String[]{PreferencesUtil.getDid(), PreferencesUtil.getSid(), FieldFinals.DONATE, String.valueOf(PreferencesUtil.getUid())});
                break;
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
