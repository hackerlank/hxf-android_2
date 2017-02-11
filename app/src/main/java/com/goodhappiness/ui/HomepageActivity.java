package com.goodhappiness.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.DeviceInfo;
import com.goodhappiness.bean.PushWinner;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.service.DownloadService;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.fragment.RevelationFragment;
import com.goodhappiness.ui.fragment.ShopFragment;
import com.goodhappiness.ui.lottery.LotteryFragment;
import com.goodhappiness.ui.personal.PersonalFragment;
import com.goodhappiness.ui.register.LoginActivity;
import com.goodhappiness.ui.social.SocialFragment;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PermissionHelper;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.StringUtils;
import com.goodhappiness.utils.SystemUtils;
import com.goodhappiness.widget.NoScrollDisPatchViewPager;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.push.notification.PushNotificationMessage;

//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;

public class HomepageActivity extends BaseFragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private TextView tvLotteryNormal, tvLotteryPress, tvRevelationNormal, tvRevelationPress, tvFriendsNormal, tvFriendsPress, tvMeNormal, tvMePress, tvShopNormal, tvShopPress, tvNotice;
    public LinearLayout ll_lottery, ll_revelation, ll_friends, ll_mine, ll_shop;

    public static int type = 1;
    private NoScrollDisPatchViewPager viewPager;
    private ImageView iv_normal, iv_press;
    public static final int LOTTERY = 1;
    public static final int REVELATION = 2;
    public static final int FRIENDS = 3;
    public static final int SHOP = 4;
    public static final int MINE = 5;

    private View v_measure;
    private View v_measureVertical;
    private LotteryFragment lotteryFragment;
    private RevelationFragment revelationFragment;
    private SocialFragment socialFragment;
    private ShopFragment shopFragment;
    private PersonalFragment personalFragment;
    private boolean isJump = false;

    private class NewMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_NEW_MSG)) {
                tvNotice.setVisibility(View.VISIBLE);
            }
        }
    }

    private PushWinnerBroadcastReceiver pushWinnerBroadcastReceiver;

    private class PushWinnerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                return;
            }
            if (TextUtils.isEmpty(PreferencesUtil.getSid())) {
                return;
            }
            if (intent != null) {
                if (intent.getAction().equals(StringFinal.BROADCAST_PUSH_EXTRAS)) {
                    if (!TextUtils.isEmpty(intent.getStringExtra(FieldFinals.PUSH_EXTRAS))) {
                        PushWinner pushWinner = (PushWinner) StringUtils.toGsonBean(intent.getStringExtra(FieldFinals.PUSH_EXTRAS), new TypeToken<PushWinner>() {
                        }.getType());
                        if (pushWinner != null && pushWinner.getPushType() == 1) {
                            DialogFactory.createPushWinnerDialog(HomepageActivity.this, pushWinner);
                            Log.e("q_", pushWinner.toString());
                        }
                    }
                }

            }
        }
    }

    private SendFlowerSuccessBroadcastReceiver sendFlowerSuccessBroadcastReceiver;

    private class SendFlowerSuccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                return;
            }
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FLOWER_SEND) && intent.getStringExtra(FieldFinals.ACTION).contains(HomepageActivity.class.getName())) {
                if (intent.getBooleanExtra(FieldFinals.RESULT, false)) {
                    RongIMUtils.sendFlower(intent.getIntExtra(FieldFinals.COUNT, 0), intent.getStringExtra(FieldFinals.NICKNAME), String.valueOf(intent.getLongExtra(FieldFinals.UID, -1)),
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
                                    if (!isFinishing()) {
                                        DialogFactory.createFlowerSendDialog(HomepageActivity.this);
                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                } else {
                    if (!isFinishing()) {
                        DialogFactory.createDefaultDialog(HomepageActivity.this, getString(R.string.send_flower_fail));
                    }
                }
            }
        }
    }

    public void setNoticeVisible(boolean visible) {
        if (visible) {
            tvNotice.setVisibility(View.VISIBLE);
        } else {
            tvNotice.setVisibility(View.GONE);
        }
    }

    private NewMsgReceiver receiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(pushWinnerBroadcastReceiver);
        unregisterReceiver(sendFlowerSuccessBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new NewMsgReceiver();
        sendFlowerSuccessBroadcastReceiver = new SendFlowerSuccessBroadcastReceiver();
        pushWinnerBroadcastReceiver = new PushWinnerBroadcastReceiver();

        IntentFilter filter = new IntentFilter(StringFinal.BROADCAST_NEW_MSG);
        IntentFilter filter2 = new IntentFilter(StringFinal.BROADCAST_FLOWER_SEND);
        IntentFilter filter3 = new IntentFilter(StringFinal.BROADCAST_PUSH_EXTRAS);
        registerReceiver(receiver, filter);
        registerReceiver(sendFlowerSuccessBroadcastReceiver, filter2);
        registerReceiver(pushWinnerBroadcastReceiver, filter3);
        x.view().inject(this);
        AppManager.getAppManager().finishActivities(new Class[]{StartActivity.class, WelcomeActivity.class});
        MobclickAgent.openActivityDurationTrack(false);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                if (type == SHOP && shopFragment != null && shopFragment.webView != null) {
                    if (shopFragment.webView.canGoBack()) {
                        shopFragment.webView.goBack();
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        };
        findView();
        final DeviceInfo.UpdateVersionInfoBean bean = PreferencesUtil.getPreferences(this, FieldFinals.UPDATE_INFO);
        if (bean != null && bean.getVersionCode() > SystemUtils.getVersionCode(this)) {
            DialogFactory.createSelectDialog(this, getString(R.string.version_update), bean.getContent().replace("\\n", "\n"), new OnSelectListener() {
                @Override
                public void onSelected(boolean isSelected) {
                    if (isSelected) {
                        Intent intent = new Intent(HomepageActivity.this, DownloadService.class);
                        intent.putExtra(FieldFinals.URL, bean.getUrl());
                        startService(intent);
                    } else {
                        if (bean.getIsRequired() == 1) {
                            SystemUtils.endApp();
                        }
                    }
                }
            });
        }
        PermissionHelper permissionHelper = new PermissionHelper(this);
        if (!permissionHelper.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            permissionHelper.permissionsCheck("android.permission.WRITE_EXTERNAL_STORAGE", 12);
        }
    }

    @Override
    protected void setData() {

    }

    private void findView() {
        v_measure = findViewById(R.id.v_measure);
        v_measureVertical = findViewById(R.id.v_measure_vertical);
        tvNotice = (TextView) findViewById(R.id.notice);
        tvLotteryNormal = (TextView) findViewById(R.id.tv_lottery_normal);
        tvLotteryPress = (TextView) findViewById(R.id.tv_lottery_press);
        tvRevelationNormal = (TextView) findViewById(R.id.tv_revelation_normal);
        tvRevelationPress = (TextView) findViewById(R.id.tv_revelation_press);
        tvFriendsNormal = (TextView) findViewById(R.id.tv_friends_normal);
        tvFriendsPress = (TextView) findViewById(R.id.tv_friends_press);
        tvMeNormal = (TextView) findViewById(R.id.tv_me_normal);
        tvMePress = (TextView) findViewById(R.id.tv_me_press);
        tvShopNormal = (TextView) findViewById(R.id.tv_shop_normal);
        tvShopPress = (TextView) findViewById(R.id.tv_shop_press);
        iv_normal = (ImageView) findViewById(R.id.main_iv_normal);
        iv_press = (ImageView) findViewById(R.id.main_iv_press);
        ll_lottery = (LinearLayout) findViewById(R.id.ll_lottery);//首页
        ll_revelation = (LinearLayout) findViewById(R.id.ll_revelation);//订单
        ll_friends = (LinearLayout) findViewById(R.id.ll_friends);//消息
        ll_shop = (LinearLayout) findViewById(R.id.ll_shop);//消息
        ll_mine = (LinearLayout) findViewById(R.id.ll_me);//我

        //默认选中第一个
        setTransparency();
        tvLotteryPress.getBackground().setAlpha(255);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        if (GoodHappinessApplication.checkType == 2) {
            findViewById(R.id.ll_lottery).setVisibility(View.GONE);
            findViewById(R.id.ll_revelation).setVisibility(View.GONE);
            findViewById(R.id.rl_tv_lottery).setVisibility(View.GONE);
            findViewById(R.id.rl_tv_revelation).setVisibility(View.GONE);
            findViewById(R.id.main_v_lottery).setVisibility(View.GONE);
            findViewById(R.id.main_v_reveal).setVisibility(View.GONE);
        } else {
            lotteryFragment = LotteryFragment.newInstance();
            revelationFragment = RevelationFragment.newInstance();
            fragmentList.add(lotteryFragment);
            fragmentList.add(revelationFragment);
        }
        socialFragment = SocialFragment.newInstance();
        shopFragment = ShopFragment.newInstance();
        personalFragment = PersonalFragment.newInstance();
        fragmentList.add(socialFragment);
        fragmentList.add(shopFragment);
        fragmentList.add(personalFragment);

        viewPager = (NoScrollDisPatchViewPager) findViewById(R.id.view_pager);
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        if (GoodHappinessApplication.checkType == 2) {
            viewPager.setOffscreenPageLimit(2);
        } else {
            viewPager.setOffscreenPageLimit(4);
        }
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //根据ViewPager滑动位置更改透明度
                int diaphaneity_one = (int) (255 * positionOffset);
                int diaphaneity_two = (int) (255 * (1 - positionOffset));
                if (GoodHappinessApplication.checkType == 2) {
                    switch (position) {
                        case 0:
                            tvFriendsNormal.getBackground().setAlpha(diaphaneity_one);
                            tvFriendsPress.getBackground().setAlpha(diaphaneity_two);
                            tvShopNormal.getBackground().setAlpha(diaphaneity_two);
                            tvShopPress.getBackground().setAlpha(diaphaneity_one);
                            break;
                        case 1:
                            tvShopNormal.getBackground().setAlpha(diaphaneity_one);
                            tvShopPress.getBackground().setAlpha(diaphaneity_two);
                            tvMeNormal.getBackground().setAlpha(diaphaneity_two);
                            tvMePress.getBackground().setAlpha(diaphaneity_one);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            tvLotteryNormal.getBackground().setAlpha(diaphaneity_one);
                            tvLotteryPress.getBackground().setAlpha(diaphaneity_two);
                            tvRevelationNormal.getBackground().setAlpha(diaphaneity_two);
                            tvRevelationPress.getBackground().setAlpha(diaphaneity_one);
                            break;
                        case 1:
                            tvRevelationNormal.getBackground().setAlpha(diaphaneity_one);
                            tvRevelationPress.getBackground().setAlpha(diaphaneity_two);
                            tvFriendsNormal.getBackground().setAlpha(diaphaneity_two);
                            tvFriendsPress.getBackground().setAlpha(diaphaneity_one);
                            break;
                        case 2:
                            tvFriendsNormal.getBackground().setAlpha(diaphaneity_one);
                            tvFriendsPress.getBackground().setAlpha(diaphaneity_two);
                            tvShopNormal.getBackground().setAlpha(diaphaneity_two);
                            tvShopPress.getBackground().setAlpha(diaphaneity_one);
                            break;
                        case 3:
                            tvShopNormal.getBackground().setAlpha(diaphaneity_one);
                            tvShopPress.getBackground().setAlpha(diaphaneity_two);
                            tvMeNormal.getBackground().setAlpha(diaphaneity_two);
                            tvMePress.getBackground().setAlpha(diaphaneity_one);
                            break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (GoodHappinessApplication.checkType == 2) {
                    type = position + 3;
                } else {
                    type = position + 1;
                }
                switch (type) {
                    case LOTTERY:
                        if (lotteryFragment != null) {
                            lotteryFragment.refresh();
                        }
                        break;
                    case REVELATION:
                        if (revelationFragment != null) {
                            revelationFragment.refresh();
                        }
                        break;
                    case FRIENDS:
                        break;
                    case SHOP:
                        break;
                    case MINE:
                        if (isUserLogined()) {
                            if (personalFragment != null) {
                                personalFragment.refresh();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    protected boolean isUserLogined() {
        if (!"".equals(PreferencesUtil.getStringPreferences(this, "sid"))) {
            return true;
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
    }

    private void logout() {
//        DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
//        List<HttpCookie> cookies = dbCookieStore.getCookies();
//        for (HttpCookie cookie : cookies) {
//            if (cookie.getName().contains(FieldFinals.SID)) {
//                dbCookieStore.remove(null,cookie);
//                break;
//            }
//        }

        RequestParams params = new RequestParams(HttpFinal.LOGOUT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.DEVICETYPE, FieldFinals.ANDROID);
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(this, params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {

            @Override
            public void onSuccess(Result result) {

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

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra(FieldFinals.ACTION))) {
            DialogFactory.createDefaultDialog(HomepageActivity.this, getString(R.string.other_login));
            GoodHappinessApplication.isNeedRefresh = true;
            PreferencesUtil.clearUserInfo(HomepageActivity.this);
            logout();
        }
    }

    private long messageId = 0;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        refreshView();
        measure();
        AppManager.getAppManager().finishOther();
        if (getIntent() != null && getIntent().getParcelableExtra(FieldFinals.PUSH_MSG) != null && !TextUtils.isEmpty(getSid())) {
            PushNotificationMessage message = getIntent().getParcelableExtra(FieldFinals.PUSH_MSG);
            Log.e("q_", message.getReceivedTime() + "...");
            if (!isJump) {
                if (!TextUtils.isEmpty(message.getTargetId())) {
                    Log.e("q_", message.getExtra() + "???");
                    RongIM.getInstance().startPrivateChat(this, message.getTargetId(), message.getTargetUserName());
                    isJump = true;
                } else {
                    if (!TextUtils.isEmpty(message.getExtra())) {
                        Log.e("q_", message.getExtra() + "!!!");
                        PushWinner pushWinner = (PushWinner) StringUtils.toGsonBean(message.getExtra(), new TypeToken<PushWinner>() {
                        }.getType());
                        if (pushWinner != null && pushWinner.getPushType() == 2) {
                            if (!TextUtils.isEmpty(pushWinner.getAndroidUrl())) {
                                IntentUtils.checkURL(HomepageActivity.this, pushWinner.getAndroidUrl());
                                isJump = true;
                                return;
                            }
                            if (!TextUtils.isEmpty(pushWinner.getJumpUrl())) {
                                IntentUtils.startToWebView(HomepageActivity.this, pushWinner.getJumpUrl());
                                isJump = true;
                                return;
                            }
                        }
                    }
                }
                messageId = message.getReceivedTime();
            }
        }
    }

    private void measure() {
        if (v_measure != null && v_measure.getHeight() != 0 && GoodHappinessApplication.perHeight == 0) {
            GoodHappinessApplication.perHeight = (float) v_measure.getHeight() / 80;
            GoodHappinessApplication.per = (float) v_measure.getWidth() / 1080;
            GoodHappinessApplication.w = v_measureVertical.getWidth();
            GoodHappinessApplication.h = v_measureVertical.getHeight();
            Log.e("q_", "per:" + GoodHappinessApplication.per + "    perHeight" + GoodHappinessApplication.perHeight);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void refreshView() {
        switch (type) {
            case LOTTERY:
                initNav(ll_lottery);
                break;
            case REVELATION:
                initNav(ll_revelation);
                break;
            case FRIENDS:
                initNav(ll_friends);
                break;
            case SHOP:
                initNav(ll_shop);
                break;
            case MINE:
                initNav(ll_mine);
                break;
            default:
                initNav(ll_shop);
                break;
        }
    }

    @Event({R.id.main_v_lottery, R.id.main_v_shop, R.id.main_v_friends, R.id.main_v_reveal, R.id.main_v_me})
    private void onclick(View v) {
        if (v.getId() == R.id.main_v_me && TextUtils.isEmpty(PreferencesUtil.getStringPreferences(this, "sid"))) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (v.getId()) {
            case R.id.main_v_lottery:
                initNav(ll_lottery);
                if (lotteryFragment != null) {
//                    lotteryFragment.refresh();
                }
                type = LOTTERY;
                break;
            case R.id.main_v_reveal:
                initNav(ll_revelation);
                if (revelationFragment != null) {
//                    revelationFragment.refresh();
                }
                type = REVELATION;
                break;
            case R.id.main_v_friends:
                initNav(ll_friends);
                type = FRIENDS;
                break;
            case R.id.main_v_shop:
                initNav(ll_shop);
                type = SHOP;
                break;
            case R.id.main_v_me:
                initNav(ll_mine);
                if (personalFragment != null) {
                    viewPager.setCurrentItem(4);
//                    personalFragment.refresh();
                }
                type = MINE;
                break;
        }
//        initNav(v);
    }

    /**
     * 设置透明度
     * 把press图片、文字全部隐藏
     */
    private void setTransparency() {
        tvLotteryNormal.getBackground().setAlpha(255);
        tvRevelationNormal.getBackground().setAlpha(255);
        tvFriendsNormal.getBackground().setAlpha(255);
        tvShopNormal.getBackground().setAlpha(255);
        tvMeNormal.getBackground().setAlpha(255);
        iv_normal.setAlpha(255);

        tvLotteryPress.getBackground().setAlpha(1);
        tvRevelationPress.getBackground().setAlpha(1);
        tvFriendsPress.getBackground().setAlpha(1);
        tvShopPress.getBackground().setAlpha(1);
        tvMePress.getBackground().setAlpha(1);
        iv_press.setAlpha(1);
    }

    public void initNav(View v) {
        setTransparency();
        switch (v.getId()) {
            case R.id.ll_lottery:
                viewPager.setCurrentItem(0, false);
                tvLotteryPress.getBackground().setAlpha(255);
                break;
            case R.id.ll_revelation:
                viewPager.setCurrentItem(1, false);
                tvRevelationPress.getBackground().setAlpha(255);
                break;
            case R.id.ll_friends:
                if(GoodHappinessApplication.checkType==2){
                    viewPager.setCurrentItem(0, false);
                    tvFriendsPress.getBackground().setAlpha(255);
                }else{
                    viewPager.setCurrentItem(2, false);
                    tvFriendsPress.getBackground().setAlpha(255);
                }
                break;
            case R.id.ll_shop:
                if(GoodHappinessApplication.checkType==2){
                    viewPager.setCurrentItem(1, false);
                    tvShopPress.getBackground().setAlpha(255);
                }else{
                    viewPager.setCurrentItem(3, false);
                    tvShopPress.getBackground().setAlpha(255);
                }
                break;
            case R.id.ll_me:
                if(GoodHappinessApplication.checkType==2){
                    viewPager.setCurrentItem(2, false);
                    tvMePress.getBackground().setAlpha(255);
                }else{
                    viewPager.setCurrentItem(4, false);
                    tvMePress.getBackground().setAlpha(255);
                }
                break;
        }
    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> list;
        android.support.v4.app.FragmentManager fm;

        public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DialogFactory.createPublishDialog(this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
