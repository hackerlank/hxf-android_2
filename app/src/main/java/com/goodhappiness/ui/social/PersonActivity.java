package com.goodhappiness.ui.social;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.FeedInfo;
import com.goodhappiness.bean.FocusChangeBean;
import com.goodhappiness.bean.FriendShipChangeBean;
import com.goodhappiness.bean.PostUserInfoBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserInfo;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSendFlowerListener;
import com.goodhappiness.dao.ScrollViewListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.fragment.ExchangeFragment;
import com.goodhappiness.ui.fragment.PostPublishFragment;
import com.goodhappiness.ui.fragment.WinRecordFragment;
import com.goodhappiness.ui.lottery.LotteryRecordFragment;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.widget.ObservableScrollView;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

@ContentView(R.layout.activity_person)
public class PersonActivity extends BaseFragmentActivity implements OnTabSelectListener {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;// = {"相册", "抽奖", "中奖", "兑换"};
    @ViewInject(R.id.vp)
    public ViewPager vp;
    @ViewInject(R.id.tl_4)
    private SlidingTabLayout tabLayout_4;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout tabLayout_5;
    //    @ViewInject(R.id.person_srl)
//    private SwipyRefreshLayout srl;
    @ViewInject(R.id.person_sv)
    private ObservableScrollView sv;
    @ViewInject(R.id.person_rl_hide_tab)
    private RelativeLayout rl_hide;
    @ViewInject(R.id.person_rl_show)
    private RelativeLayout rl_show;
    @ViewInject(R.id.person_tv_name_top)
    private EmojiTextView tv_name_top;
    @ViewInject(R.id.person_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.person_tv_id)
    private TextView tv_id;
    @ViewInject(R.id.person_tv_focus_tab)
    private TextView tv_focus_tab;
    @ViewInject(R.id.person_tv_focus)
    private TextView tv_focus;
    @ViewInject(R.id.person_tv_fans)
    private TextView tv_fans;
    @ViewInject(R.id.person_tv_focus_click)
    private TextView tv_focus_click;
    @ViewInject(R.id.person_tv_chat_click)
    private TextView tv_chat_click;
    @ViewInject(R.id.person_tv_fans_tab)
    private TextView tv_fans_tab;
    @ViewInject(R.id.person_iv_head)
    private ImageView iv_head;
    @ViewInject(R.id.send_flower)
    private ImageView iv_flower;
    @ViewInject(R.id.person_rl_top)
    private RelativeLayout rl_top;
    private int totalHigh;
    //    private GestureDetector mGesture = null;
    public PostPublishFragment postPublishFragment;
    private LotteryRecordFragment lotteryRecordFragment;
    private WinRecordFragment winRecordFragment;
    private ExchangeFragment exchangeFragment;
    public boolean isTop = true;
    private long uid = -1;
    private UserInfo userInfo = null;
    onFriendShipClick onFriendShipClick;
    MyPagerAdapter myPagerAdapter;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(false);
                    }
                });
            }
        },1000);
        Log.e("e_","onNewIntent");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(StringFinal.BROADCAST_FOCUS_CHANGE);
        receiver = new FocusChangeBroadcastReceiver();
        registerReceiver(receiver, filter);


        MobclickAgent.openActivityDurationTrack(false);
    }

    private FocusChangeBroadcastReceiver receiver;

    private class FocusChangeBroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FOCUS_CHANGE)) {
                FocusChangeBean bean = (FocusChangeBean) intent.getSerializableExtra(FieldFinals.FOCUS_CHANGE);
                if (bean != null && tv_focus != null) {
                    tv_focus.setText(bean.getFocusCount() + "");
                }
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void setData() {
        if(GoodHappinessApplication.checkType==2){
            mTitles = new String[]{getString(R.string.gallery), getString(R.string.buy)};
        }else{
            mTitles = new String[]{getString(R.string.gallery), getString(R.string.join), getString(R.string.get), getString(R.string.buy)};
        }
        tv_name_top.setAlpha(0);
        initView();
        onFriendShipClick = new onFriendShipClick();
        if (getIntent() != null) {
            refreshData(true);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vp.getLayoutParams();
            params.height = GoodHappinessApplication.h - (int) (GoodHappinessApplication.perHeight * 170);//(int)((float)(GoodHappinessApplication.h*2/3));//-(int)(GoodHappinessApplication.perHeight*466);
            params.width = GoodHappinessApplication.w;
            vp.setLayoutParams(params);
            vp.setOffscreenPageLimit(3);
            myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
            vp.setAdapter(myPagerAdapter);
            tabLayout_4.setViewPager(vp);
            tabLayout_5.setViewPager(vp);
            tabLayout_5.setOnTabSelectListener(this);
            rl_top.getBackground().setAlpha(0);
            tv_name_top.setTextColor(Color.argb(0, 51, 51, 51));
            sv.setScrollViewListener(new ScrollViewListener() {
                @Override
                public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                    totalHigh = rl_show.getTop() - rl_top.getHeight();
                    if (y > totalHigh) {
                        rl_hide.setVisibility(View.VISIBLE);
                    } else {
                        rl_hide.setVisibility(View.INVISIBLE);
                    }
                    if (y - totalHigh <= 0) {
                        showTop((float) y / totalHigh);
                    }
                }
            });
            sv.smoothScrollTo(0, 0);
        }
    }

    private void refreshData(boolean isFirst) {
        uid = getIntent().getLongExtra(FieldFinals.ACTION, uid);
        tv_id.setText(String.format(getString(R.string.format_id),uid));
        if(!isFirst){
            postPublishFragment.setUid(String.valueOf(uid));
            postPublishFragment.onResume();
            if(GoodHappinessApplication.checkType==1){
                lotteryRecordFragment.setUid(String.valueOf(uid));
                lotteryRecordFragment.onResume();
                winRecordFragment.setUid(String.valueOf(uid));
                winRecordFragment.onResume();
            }
            exchangeFragment.setUid(String.valueOf(uid));
            exchangeFragment.onResume();
        }else{
            postPublishFragment = PostPublishFragment.newInstance(uid + "");
            exchangeFragment = ExchangeFragment.newInstance(uid + "");
            mFragments.add(postPublishFragment);
            mFragments.add(exchangeFragment);
            if(GoodHappinessApplication.checkType==1){
                lotteryRecordFragment = LotteryRecordFragment.newInstance(uid + "", LotteryRecordFragment.STATUS_ALL);
                winRecordFragment = WinRecordFragment.newInstance(uid + "");
                mFragments.add(lotteryRecordFragment);
                mFragments.add(winRecordFragment);
            }
        }
    }

    public void setData(UserInfo data) {
        if (data == null)
            return;
        userInfo = data;
        tv_focus.setText(data.getFollowingNum() + "");
        if(uid==getUid()){
            PreferencesUtil.setPreferences(this,FieldFinals.TOTAL,data.getFollowingNum());
        }
        tv_fans.setText(data.getFollowerNum() + "");
        tv_name_top.setText(data.getNickname());
        tv_name.setText(data.getNickname());
        tv_focus.setOnClickListener(onFriendShipClick);
        tv_focus_tab.setOnClickListener(onFriendShipClick);
        tv_fans.setOnClickListener(onFriendShipClick);
        tv_fans_tab.setOnClickListener(onFriendShipClick);
        displayImage(iv_head, data.getAvatar());
        if (!TextUtils.isEmpty(getSid())) {
            if (uid == getUid()) {
                iv_flower.setVisibility(View.GONE);
                tv_focus_click.setVisibility(View.GONE);
                tv_chat_click.setVisibility(View.GONE);
            } else {
                iv_flower.setVisibility(View.VISIBLE);
                tv_focus_click.setVisibility(View.VISIBLE);
                tv_chat_click.setVisibility(View.VISIBLE);
                if (data.getRelation() != 1) {
                    setFocusText(true,false);
                }
                RongIMUtils.setUserInfoProvider(new io.rong.imlib.model.UserInfo(String.valueOf(userInfo.getUid()), userInfo.getNickname(), Uri.parse(userInfo.getAvatar())));
            }
        }
    }

    class onFriendShipClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!isUserLogined()) {
                return;
            }
            if (userInfo == null)
                return;
            switch (v.getId()) {
                case R.id.person_tv_focus:
                case R.id.person_tv_focus_tab:
                    IntentUtils.startToFriendship(PersonActivity.this, 0, FriendshipFragment.FROM_TYPE_PERSON, userInfo.getUid());
                    break;
                case R.id.person_tv_fans_tab:
                case R.id.person_tv_fans:
                    IntentUtils.startToFriendship(PersonActivity.this, 1, FriendshipFragment.FROM_TYPE_PERSON, userInfo.getUid());
                    break;
            }
        }
    }

    private void setFocusText(boolean b,boolean isAction) {
        if (b) {
            tv_focus_click.setText(R.string.cancel_focus);
            tv_focus_click.setTextColor(getTheColor(R.color.gray_666_text));
            tv_focus_click.setBackgroundResource(R.drawable.shape_for_black_focus);
            if(isAction)
            tv_fans.setText(String.valueOf((Integer.valueOf(tv_fans.getText().toString()) + 1)));
        } else {
            tv_focus_click.setText(R.string.focus_status_no);
            tv_focus_click.setTextColor(getTheColor(R.color.black_333_text));
            tv_focus_click.setBackgroundResource(R.drawable.shape_for_yellow);
            if(isAction)
            tv_fans.setText(String.valueOf((Integer.valueOf(tv_fans.getText().toString()) - 1)));
        }
    }

    @Event({R.id.send_flower, R.id.person_tv_focus_click, R.id.person_tv_chat_click})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.send_flower:
                if (userInfo != null&&isUserLogined()) {
                    if(IntentUtils.checkHasBind(this)){
                        DialogFactory.createSendFlowerDialog(this, new OnSendFlowerListener() {
                            @Override
                            public void onclick(Integer count) {
                                confirmFlowerOrder(count);
                            }
                        });
                    }
                }
                break;
            case R.id.person_tv_focus_click:
                if (isUserLogined()) {
                    if (userInfo != null)
                        focus(userInfo);
                }
                break;
            case R.id.person_tv_chat_click:
                if (isUserLogined()) {
                    if (userInfo != null) {
                        if(RongIMUtils.isCanChat(PersonActivity.this,userInfo.getRelation())){
                            RongIMUtils.setUserInfoProvider(new io.rong.imlib.model.UserInfo(String.valueOf(userInfo.getUid()), userInfo.getNickname(), Uri.parse(userInfo.getAvatar())));
                            RongIMUtils.startPrivate(this, userInfo.getUid(), userInfo.getNickname());
                        }
                    }
                }
                break;
        }
    }

    private void confirmFlowerOrder(final int count) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_CONFIRM);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, userInfo.getUid() + "");
        params.addBodyParameter(FieldFinals.NUM, count + "");
        HttpUtils.post(this,params, new TypeToken<Result<ConfirmOrder>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                newDialog().show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                if (result != null && result.getData() != null) {
                    ConfirmOrder confirmOrder = (ConfirmOrder) result.getData();
                    confirmOrder.setOrderType(1);
                    if(!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.CLAZZ_NAME))){
                        confirmOrder.setClassName(getIntent().getStringExtra(FieldFinals.CLAZZ_NAME));
                    }else{
                        confirmOrder.setClassName(HomepageActivity.class.getName());
                    }
                    confirmOrder.setFlowerCount(count);
                    IntentUtils.startToPayOrder(PersonActivity.this, confirmOrder);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    private void focus(final UserInfo userInfo) {
        RequestParams params = new RequestParams(userInfo.getRelation() == 1 ? HttpFinal.FRIENDSHIP_CREATE : HttpFinal.FRIENDSHIP_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, userInfo.getUid() + "");
        HttpUtils.post(this,params, new TypeToken<Result<PostUserInfoBean>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                PostUserInfoBean postUserInfoBean = (PostUserInfoBean) result.getData();
                if (postUserInfoBean != null) {
                    FriendShipChangeBean bean = new FriendShipChangeBean();
                    FeedInfo feedInfo = new FeedInfo();
                    feedInfo.setPostUserInfo(postUserInfoBean);
                    bean.setFeedInfo(feedInfo);
                    bean.setAction(FieldFinals.FOCUS_CHANGE);
                    IntentUtils.sendFeedChangeBroadcast(PersonActivity.this, bean);
                }
                if (userInfo.getRelation() == 1) {
                    setFocusText(true,true);
                    userInfo.setRelation(2);
                    showToast(R.string.focusSuccess);
                } else {
                    setFocusText(false,true);
                    userInfo.setRelation(1);
                    showToast(R.string.focusCancel);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void showTop(float f) {
        if (f > 0.5) {
            f *= 1.2;
        }
        if (f > 1) {
            f = 1;
        }
        rl_top.getBackground().setAlpha((int) (f * 255));
        tv_name_top.setTextColor(Color.argb((int) (f * 255), 51, 51, 51));
        tv_name_top.setAlpha(f);
        if (f == 1) {
            iv_back.setImageResource(R.mipmap.back);
        } else {
            iv_back.setImageResource(R.mipmap.ico_white_back);
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

    public void scrollToBottom() {
        isTop = false;
        sv.smoothScrollTo(0, sv.getHeight());
    }

    public void scrollToTop() {
        isTop = true;
        sv.smoothScrollTo(0, 0);
    }

}
