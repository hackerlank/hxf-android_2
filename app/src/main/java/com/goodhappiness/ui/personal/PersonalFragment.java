package com.goodhappiness.ui.personal;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserInfoResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.lottery.LotteryRecordActivity;
import com.goodhappiness.ui.register.RegisterActivity;
import com.goodhappiness.ui.social.picture.PreviewActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.functions.Action1;

/**
 * EditPicUpdateActivity simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {// implements SwipyRefreshLayout.OnRefreshListener {

    PullToZoomScrollViewEx scrollView;
    private TextView tv_nickname,tv_HappyCoin,tv_generalCoin,tv_remain,tv_completeMsg;
    private ImageView iv_im,iv_sign,iv_head,iv_setting;
    private LinearLayout ll_fail_view;
    private boolean isCanLoad = true;
    public static String imgURL = "";

    public PersonalFragment() {
    }

    public static PersonalFragment newInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.personal));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.revelation_empty_view_tv_reload:
                reload();
                break;
            case R.id.fragment_personal_rl_info:
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            case R.id.fragment_personal_iv_head:
                Intent infoIntent = new Intent(getActivity(), PreviewActivity.class);
                infoIntent.putExtra(FieldFinals.URL, imgURL);
                startActivity(infoIntent);
                break;
            case R.id.fragment_personal_rl_redbag:
                IntentUtils.startToRedbag(getActivity(), 0);
                break;
//            case R.id.fragment_personal_rl_comment:
//                startActivity(new Intent(getActivity(), CommentToMeActivity.class));
//                break;
            case R.id.fragment_personal_tv_charge:
//                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.SYSTEM, "100023", "标题");
                startActivity(new Intent(getActivity(), ChargeActivity.class));
                break;
            case R.id.fragment_personal_iv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.fragment_personal_iv_note:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startConversationList(getActivity());
                }
                break;
            case R.id.fragment_personal_ll_flower:
                IntentUtils.startToFlower(getActivity(), 0);
                break;
            case R.id.fragment_personal_ll_invite:
                IntentUtils.startToWebView(getContext(),HttpFinal.INVITE);
                break;
            case R.id.fragment_personal_ll_sign:
                IntentUtils.startToWebView(getContext(),HttpFinal.SIGN);
                break;
            case R.id.fragment_personal_ll_gallery:
                IntentUtils.startToPerson(getActivity(), PreferencesUtil.getUid());
                break;
            case R.id.fragment_personal_rl_lottery_record:
                startActivity(new Intent(getActivity(), LotteryRecordActivity.class));
                break;
            case R.id.fragment_personal_rl_win_record:
                startActivity(new Intent(getActivity(), WinRecordActivity.class));
                break;
            case R.id.fragment_personal_rl_exchange_record:
                startActivity(new Intent(getActivity(), ExchangeRecordActivity.class));
                break;
            case R.id.fragment_personal_rl_charge_record:
                startActivity(new Intent(getActivity(), ChargeRecordActivity.class));
                break;
        }
    }

    protected void reload() {
        if (!TextUtils.isEmpty(PreferencesUtil.getSid())) {
            ll_fail_view.setVisibility(View.GONE);
            initData();
        }
    }

    private class NewMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_NEW_MSG)) {
                iv_im.setImageResource(R.mipmap.ico_has_msg);
            }
        }
    }

    private NewMsgReceiver receiver;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.personal));
        IntentFilter filter = new IntentFilter(StringFinal.BROADCAST_NEW_MSG);
        receiver = new NewMsgReceiver();
        getActivity().registerReceiver(receiver, filter);
        if (!TextUtils.isEmpty(PreferencesUtil.getSid())) {
            initData();
        }
        getUnReadCount();
        if(tv_completeMsg!=null&&PreferencesUtil.getIntPreferences(getContext(),FieldFinals.IS_BIND,0)==1){
            tv_completeMsg.setVisibility(View.GONE);
        }
        Log.e("e_f","onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ll_fail_view = (LinearLayout) view.findViewById(R.id.person_empty_view);
        RxView.clicks(view.findViewById(R.id.revelation_empty_view_tv_reload)).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                reload();
            }
        });
        loadViewForCode(view);
        return view;
    }


    private void loadViewForCode(View view) {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view1);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_person_data_pull_head, null, false);
        tv_nickname = (TextView) headView.findViewById(R.id.fragment_personal_tv_nickname2);
        tv_completeMsg = (TextView) headView.findViewById(R.id.tv_complete_msg);
        tv_completeMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(FieldFinals.ACTION,RegisterActivity.REGISTER_TYPE_BIND);
                startActivity(intent);
            }
        });
        tv_HappyCoin = (TextView) headView.findViewById(R.id.fragment_personal_tv_happy_coin);
        tv_generalCoin = (TextView) headView.findViewById(R.id.fragment_personal_tv_general_coin);
        tv_remain = (TextView) headView.findViewById(R.id.fragment_personal_tv_remain2);
        iv_head = (ImageView) headView.findViewById(R.id.fragment_personal_iv_head);
        iv_im = (ImageView) headView.findViewById(R.id.fragment_personal_iv_note);
        iv_setting = (ImageView) headView.findViewById(R.id.fragment_personal_iv_setting);
        iv_head.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
        iv_im.setOnClickListener(this);
        headView.findViewById(R.id.fragment_personal_rl_info).setOnClickListener(this);
        headView.findViewById(R.id.fragment_personal_tv_charge).setOnClickListener(this);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_person_data_pull_zoom, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_person_data_pull_content, null, false);
        contentView.findViewById(R.id.fragment_personal_ll_flower).setOnClickListener(this);
        contentView.findViewById(R.id.fragment_personal_ll_invite).setOnClickListener(this);
        contentView.findViewById(R.id.fragment_personal_ll_gallery).setOnClickListener(this);
        if(GoodHappinessApplication.checkType==2){
            contentView.findViewById(R.id.fragment_personal_rl_lottery_record).setVisibility(View.GONE);
            contentView.findViewById(R.id.fragment_personal_rl_win_record).setVisibility(View.GONE);
        }else{
            contentView.findViewById(R.id.fragment_personal_rl_lottery_record).setOnClickListener(this);
            contentView.findViewById(R.id.fragment_personal_rl_win_record).setOnClickListener(this);
        }
        contentView.findViewById(R.id.fragment_personal_rl_exchange_record).setOnClickListener(this);
        contentView.findViewById(R.id.fragment_personal_rl_charge_record).setOnClickListener(this);
        contentView.findViewById(R.id.fragment_personal_rl_redbag).setOnClickListener(this);
        contentView.findViewById(R.id.fragment_personal_ll_sign).setOnClickListener(this);
        iv_sign = (ImageView) contentView.findViewById(R.id.fragment_personal_iv_sign);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public void refresh() {
        Log.e("e_f","refresh");
        initData();
        getUnReadCount();

    }



    public void getUnReadCount() {
        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if(iv_im==null||getActivity()==null){
                    return;
                }
                if (integer > 0) {
                    iv_im.setImageResource(R.mipmap.ico_has_msg);
                    ((HomepageActivity) getActivity()).setNoticeVisible(true);
                } else {
                    iv_im.setImageResource(R.mipmap.ico_no_msg);
                    ((HomepageActivity) getActivity()).setNoticeVisible(false);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if(iv_im!=null){
                    iv_im.setImageResource(R.mipmap.ico_no_msg);
                    ((HomepageActivity) getActivity()).setNoticeVisible(false);
                }
            }
        });
    }

    private void initData() {
        if (isCanLoad) {
            isCanLoad = false;
            RequestParams params = new RequestParams(HttpFinal.USER_INFO);
            params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getDid());
            params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getSid());
            HttpUtils.post(getActivity(), params, new TypeToken<Result<UserInfoResult>>() {
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
                    UserInfoResult userInfoResult = (UserInfoResult) result.getData();
                    if (userInfoResult != null && userInfoResult.getUserInfo() != null) {
                        ll_fail_view.setVisibility(View.GONE);
                        PreferencesUtil.saveUserInfo(getActivity(), userInfoResult.getUserInfo());
                        PreferencesUtil.setPreferences(getActivity(), FieldFinals.IS_BIND, userInfoResult.getUserInfo().getIsBind());
                        if(PreferencesUtil.getIntPreferences(getContext(),FieldFinals.IS_BIND,0)==1){
                            tv_completeMsg.setVisibility(View.GONE);
                        }else{
                            tv_completeMsg.setVisibility(View.VISIBLE);
                        }
                        if(userInfoResult.getUserInfo().getIsCheck()==0){
                            iv_sign.setImageResource(R.mipmap.ico_sign_normal);
                        }else{
                            iv_sign.setImageResource(R.mipmap.ico_sign_press);
                        }
                        tv_HappyCoin.setText(userInfoResult.getUserInfo().getHappyCoin() + "");
                        tv_generalCoin.setText(userInfoResult.getUserInfo().getGeneralCoin() + "");

                        if (userInfoResult.getUserInfo().getAvatar() != null && iv_head.getTag() != null) {
                            if (!imgURL.equals(userInfoResult.getUserInfo().getAvatar())) {
                                displayImage(iv_head, userInfoResult.getUserInfo().getAvatar());
                            }
                        }
                        RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(userInfoResult.getUserInfo().getUid()), String.valueOf(userInfoResult.getUserInfo().getNickname()), Uri.parse(userInfoResult.getUserInfo().getAvatar())));
                        if (iv_head.getTag() == null) {
                            displayImage(iv_head, userInfoResult.getUserInfo().getAvatar());
                        }
                        imgURL = userInfoResult.getUserInfo().getAvatar();
                        iv_head.setTag(userInfoResult.getUserInfo().getAvatar());
                        if (!TextUtils.isEmpty(userInfoResult.getUserInfo().getAvatar())) {
                            PreferencesUtil.clearHeadImgPathInfo(GoodHappinessApplication.getContext());
                        }
                        if (userInfoResult.getUserInfo().getNickname() != null)
                            tv_nickname.setText(userInfoResult.getUserInfo().getNickname());
//                        tv_remain.setText("余额：" + BigDecimal.valueOf(userInfoResult.getUserInfo().getMoney()).toPlainString() + "元");
                        tv_remain.setText("余额：" + userInfoResult.getUserInfo().getMoney() + "元");
//                        PreferencesUtil.setPreferences(getActivity(), FieldFinals.REDBAG_LIST, userInfoResult.getRedBags());
//                        List<Redbag> redbags = PreferencesUtil.getPreferences(getActivity(),FieldFinals.REDBAG_LIST);
                        if(userInfoResult.getRedBags()!=null&&userInfoResult.getRedBags().size()>0){
                            DialogFactory.createReceiveRedbagDialog(getActivity(),userInfoResult.getRedBags());
                            PreferencesUtil.setPreferences(getActivity(),FieldFinals.REDBAG_LIST,null);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if(ll_fail_view!=null)
                    ll_fail_view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    isCanLoad = true;
                }
            });
        } else {
        }
    }

    public void displayImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, GoodHappinessApplication.options);
    }
}
