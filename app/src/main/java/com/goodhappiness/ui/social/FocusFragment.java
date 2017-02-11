package com.goodhappiness.ui.social;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.GalleryAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Banner;
import com.goodhappiness.bean.Channel;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.FeedInfo;
import com.goodhappiness.bean.FeedInfoList;
import com.goodhappiness.bean.FriendShipChangeBean;
import com.goodhappiness.bean.PostUserInfoBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.dao.OnSendFlowerListener;
import com.goodhappiness.dao.OnShareClickListener;
import com.goodhappiness.dao.SingleOnclick;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.DateUtil;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.TextUrlUtil;
import com.goodhappiness.utils.ViewSizeUtils;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.BannerConfig;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.functions.Action1;

@ContentView(R.layout.fragment_focus)
public class FocusFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String POST_TYPE = "post_type";
    private static final String CHANNEL = "channel";
    public static final String POST_TYPE_FOCUS = "1";
    public static final String POST_TYPE_WORLD = "2";
    public static final String POST_TYPE_SEARCH = "3";
    public static final String POST_TYPE_CHANNEL_FOCUS = "4";
    public static final String POST_TYPE_CHANNEL_WORLD = "5";
    @ViewInject(R.id.fragment_social_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.fragment_social_lv)
    private ListView lv;
    @ViewInject(R.id.revelation_empty_view)
    private LinearLayout ll_fail_view;
    @ViewInject(R.id.revelation_empty_data)
    private LinearLayout ll_fail_data;

    private CommonAdapter<FeedInfo> adapter;
    private List<FeedInfo> list = new ArrayList<>();
    private String post_type;
    private int page = 1;
    private boolean isCanLoad = true;
    private Dialog shareDialog;
    private View headView;
    private TextView tv_channel;
    private com.youth.banner.Banner mBanner;
    private GalleryAdapter galleryAdapter;
    private List<Channel> channelItems;
    private int listCount = 1;
    private int ImageId[] = {R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4, R.id.img_5, R.id.img_6, R.id.img_7, R.id.img_8};
    private int gifId[] = {R.id.img_00, R.id.img_01, R.id.img_02, R.id.img_03, R.id.img_04, R.id.img_05, R.id.img_06, R.id.img_07, R.id.img_08};
    private String currentKey = "";
    private int channelId = -1;

    public FocusFragment() {
        super(R.layout.fragment_focus);
    }

    @Override
    protected void reload() {
        ll_fail_view.setVisibility(View.GONE);
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        switch (post_type) {
            case POST_TYPE_FOCUS:
            case POST_TYPE_WORLD:
                page = 1;
                initList(true);
                break;
            case POST_TYPE_SEARCH:
                if (currentKey != null) {
                    onSearch(currentKey);
                } else {
                    endRefreshing();
                }
                break;
            case POST_TYPE_CHANNEL_FOCUS:
                page = 1;
                loadChannelList(POST_TYPE_FOCUS);
                break;
            case POST_TYPE_CHANNEL_WORLD:
                page = 1;
                loadChannelList(POST_TYPE_WORLD);
                break;
        }
    }


    public void onSearch(String key) {
        currentKey = key;
        page = 1;
        RequestParams params = new RequestParams(HttpFinal.SEARCH);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SEARCH_KEY, key);
        params.addBodyParameter(FieldFinals.ACTION, "tag");
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<FeedInfoList>>() {
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
                FeedInfoList feedInfo = (FeedInfoList) result.getData();
                list.clear();
                list.addAll(feedInfo.getList());
                adapter.notifyDataSetChanged();
                if (feedInfo.getMore() != 1) {
                    isCanLoad = false;
                } else {
                    isCanLoad = true;
                }
                lv.setSelection(0);
                ll_fail_view.setVisibility(View.GONE);
                endRefreshing();
                if (list.size() > 0) {
                    ll_fail_data.setVisibility(View.GONE);
                } else {
                    ll_fail_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ll_fail_view.setVisibility(View.VISIBLE);
                endRefreshing();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                endRefreshing();
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        switch (post_type) {
            case POST_TYPE_FOCUS:
            case POST_TYPE_WORLD:
                if (isCanLoad) {
                    page++;
                    initList(false);
                    return true;
                } else {
                    showToast(R.string.list_no_more);
                    return false;
                }
            case POST_TYPE_SEARCH:
                if (isCanLoad) {
                    page++;
                    onSearch(currentKey);
                    return true;
                } else {
                    showToast(R.string.list_no_more);
                    return false;
                }
            case POST_TYPE_CHANNEL_FOCUS:
                if (isCanLoad) {
                    page++;
                    loadChannelList(POST_TYPE_FOCUS);
                    return true;
                } else {
                    showToast(R.string.list_no_more);
                    return false;
                }
            case POST_TYPE_CHANNEL_WORLD:
                if (isCanLoad) {
                    page++;
                    loadChannelList(POST_TYPE_WORLD);
                    return true;
                } else {
                    showToast(R.string.list_no_more);
                    return false;
                }
            default:
        }
        return false;
    }

    private class FeedChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FEED_CHANGE)) {
                FriendShipChangeBean bean = (FriendShipChangeBean) intent.getSerializableExtra(FieldFinals.FEED_INFO_LIST);
                if (bean != null) {
                    switch (bean.getAction()) {
                        case FieldFinals.FOCUS_CHANGE:
                            if (bean.getFeedInfo() != null && list != null && list.size() > 0) {
                                for (FeedInfo feedInfo : list) {
                                    if (bean.getFeedInfo().getPostUserInfo().getUid() == feedInfo.getPostUserInfo().getUid()) {
                                        feedInfo.getPostUserInfo().setRelation(bean.getFeedInfo().getPostUserInfo().getRelation());
                                    }
                                }
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    private FeedChangeBroadcastReceiver receiver;
    private SendFlowerSuccessBroadcastReceiver sendFlowerSuccessBroadcastReceiver;

    private class SendFlowerSuccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FLOWER_SEND) && intent.getStringExtra(FieldFinals.ACTION).contains(FocusFragment.this.getClass().getName() + post_type)) {
                if (intent.getBooleanExtra(FieldFinals.RESULT, false)) {
                    RongIMUtils.sendFlower(intent.getIntExtra(FieldFinals.COUNT, 0), intent.getStringExtra(FieldFinals.NICKNAME), String.valueOf(intent.getLongExtra(FieldFinals.UID, 0)),
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
                                    DialogFactory.createFlowerSendDialog(getActivity());
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                } else {
                    DialogFactory.createDefaultDialog(getActivity(), getString(R.string.send_flower_fail));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.social_friends));

        //发布更新
        if (post_type.equals(POST_TYPE_WORLD) && PreferencesUtil.getBooleanPreferences(getActivity(), FieldFinals.ADD_PUBLISH, false)) {
            PreferencesUtil.setPreferences(getActivity(), FieldFinals.ADD_PUBLISH, false);
            onBGARefreshLayoutBeginRefreshing(srl);
        }
        //退出或登录时刷新
        if (post_type.equals(POST_TYPE_FOCUS) && PreferencesUtil.getBooleanPreferences(getActivity(), FieldFinals.FOCUS_REFRESH, false)) {
            PreferencesUtil.setPreferences(getActivity(), FieldFinals.FOCUS_REFRESH, false);
            onBGARefreshLayoutBeginRefreshing(srl);
        }
        //退出或登录时刷新
        if (post_type.equals(POST_TYPE_WORLD) && PreferencesUtil.getBooleanPreferences(getActivity(), FieldFinals.WORLD_REFRESH, false)) {
            PreferencesUtil.setPreferences(getActivity(), FieldFinals.WORLD_REFRESH, false);
            onBGARefreshLayoutBeginRefreshing(srl);
        }
        if (post_type.equals(POST_TYPE_WORLD) && GoodHappinessApplication.isNeedWorldUpdate) {
            GoodHappinessApplication.isNeedWorldUpdate = false;
            FeedInfo feedInfo = PreferencesUtil.getPreferences(getActivity(), FieldFinals.FEED_INFO);
            if (feedInfo != null && !feedInfo.isDelete() && list.size() > 0) {
                for (FeedInfo info : list) {
                    if (info.getFeedId() == feedInfo.getFeedId()) {
                        info.setLikeNum(feedInfo.getLikeNum());
                        info.setCommentNum(feedInfo.getCommentNum());
                        info.setIsLike(feedInfo.getIsLike());
                        PreferencesUtil.setPreferences(getActivity(), FieldFinals.FEED_INFO, null);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            if (feedInfo != null && feedInfo.isDelete() && list.size() > 0) {//delete
                int count = 0;
                for (FeedInfo info : list) {
                    if (info.getFeedId() == feedInfo.getFeedId()) {
                        list.remove(count);
                        PreferencesUtil.setPreferences(getActivity(), FieldFinals.FEED_INFO, null);
                        break;
                    }
                    count++;
                }
                adapter.notifyDataSetChanged();
            }

        }
        if (post_type.equals(POST_TYPE_FOCUS) && GoodHappinessApplication.isNeedFocusUpdate) {
            GoodHappinessApplication.isNeedFocusUpdate = false;
            FeedInfo feedInfo = PreferencesUtil.getPreferences(getActivity(), FieldFinals.FEED_INFO);
            if (feedInfo != null && list.size() > 0) {
                for (FeedInfo info : list) {
                    if (info.getFeedId() == feedInfo.getFeedId()) {
                        info.setLikeNum(feedInfo.getLikeNum());
                        info.setCommentNum(feedInfo.getCommentNum());
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.social_friends));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(sendFlowerSuccessBroadcastReceiver);
    }

    public static FocusFragment newInstance(String param1, int channel) {
        FocusFragment fragment = new FocusFragment();
        Bundle args = new Bundle();
        args.putString(POST_TYPE, param1);
        args.putInt(CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post_type = getArguments().getString(POST_TYPE);
            channelId = getArguments().getInt(CHANNEL);
//            if(POST_TYPE_FOCUS.equals(post_type)||POST_TYPE_WORLD.equals(post_type)){
            IntentFilter filter = new IntentFilter(StringFinal.BROADCAST_FEED_CHANGE);
            receiver = new FeedChangeBroadcastReceiver();
            getActivity().registerReceiver(receiver, filter);
            IntentFilter filter2 = new IntentFilter(StringFinal.BROADCAST_FLOWER_SEND);
            sendFlowerSuccessBroadcastReceiver = new SendFlowerSuccessBroadcastReceiver();
            getActivity().registerReceiver(sendFlowerSuccessBroadcastReceiver, filter2);
//            }
        }
    }

    @Override
    protected void setData() {
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        switch (post_type) {
            case POST_TYPE_FOCUS:
            case POST_TYPE_WORLD:
                onBGARefreshLayoutBeginRefreshing(srl);
                break;
            case POST_TYPE_SEARCH:
                onBGARefreshLayoutBeginRefreshing(srl);
                break;
            case POST_TYPE_CHANNEL_FOCUS:
                page = 1;
                loadChannelList(POST_TYPE_FOCUS);
                break;
            case POST_TYPE_CHANNEL_WORLD:
                page = 1;
                loadChannelList(POST_TYPE_WORLD);
                break;
        }
        initAdapter();
        ViewGroup.LayoutParams p = lv.getLayoutParams();
        p.width = GoodHappinessApplication.w;
        p.height = (int) (GoodHappinessApplication.h - GoodHappinessApplication.perHeight * 170);
        lv.setLayoutParams(p);
    }

    private void loadChannelList(String postType) {
        if (channelId == -1) {
            return;
        }
        RequestParams params = new RequestParams(HttpFinal.CHANNEL);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.CHANNEL_ID, channelId + "");
        params.addBodyParameter(FieldFinals.POST_TYPE, postType);
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<FeedInfoList>>() {
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
                if (page == 1 && list != null) {
                    list.clear();
                }
                FeedInfoList feedInfo = (FeedInfoList) result.getData();
                list.addAll(feedInfo.getList());
                adapter.notifyDataSetChanged();
                if (feedInfo.getMore() != 1) {
                    isCanLoad = false;
                } else {
                    isCanLoad = true;
                }
                if (page == 1) {
                    lv.setSelection(0);
                }
                ll_fail_view.setVisibility(View.GONE);
                if (list.size() > 0) {
                    ll_fail_data.setVisibility(View.GONE);
                } else {
                    ll_fail_data.setVisibility(View.VISIBLE);
                }
                endRefreshing();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ll_fail_view.setVisibility(View.VISIBLE);
                endRefreshing();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                endRefreshing();
            }
        });
    }


    @Event({R.id.revelation_empty_view_tv_reload})
    private void OnEventClick(View v) {
        switch (v.getId()) {
            case R.id.revelation_empty_view_tv_reload:
                reload();
                break;
        }
    }

    private void initAdapter() {
        adapter = new CommonAdapter<FeedInfo>(getActivity(), list, R.layout.layout_list_fragment_social) {
            @Override
            public void convert(ViewHolder helper, final FeedInfo item, final int position) {
                Log.e("q_", " ..");
                ImageView imageView = helper.getView(R.id.head_social_detail_iv_pic);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getPostFiles().size() > 0 && item.getPostFiles().get(0).getFileType() == 1) {
                            IntentUtils.startToDisplayImage(getActivity(), item.getPostFiles().get(0).getFileUrl());
                        }
                        if (item.getPostFiles().size() > 0 && item.getPostFiles().get(0).getFileType() == 3) {
                            IntentUtils.startToVideo(getActivity(), item.getPostFiles().get(0).getFileUrl());
                        }
                        if (item.getPostFiles().size() > 0 && item.getPostFiles().get(0).getFileType() == 4) {
                            IntentUtils.startToDisplayImageGif(getActivity(), item.getPostFiles().get(0).getFileUrl());
                        }
                    }
                });
//                ViewSizeUtils.setLinearLayoutViewWithFullWidth(imageView);
//                if(!TextUtils.isEmpty(item.getPostUserInfo().getAvatar())){
                helper.setImageResource(R.id.layout_list_fragment_social_iv_head, R.mipmap.loading_default);
                helper.loadImage(R.id.layout_list_fragment_social_iv_head, item.getPostUserInfo().getAvatar());
//                }
                helper.setText(R.id.layout_list_fragment_social_tv_name, item.getPostUserInfo().getNickname());
                helper.setVisibility(R.id.img_video, View.GONE);
                helper.setVisibility(R.id.tv_duration, View.GONE);
//                PostFilesBean bean =  item.getPostFiles().get(0);
//                item.getPostFiles().clear();
//                int qwe = (int) (((Math.random()+1)*9)%8);
//                for(int i = 0;i<qwe;i++){
//                    item.getPostFiles().add(bean);
//                }
                if (item.getPostFiles().size() > 0) {
                    if (item.getPostFiles().size() == 1) {
                        helper.setVisibility(R.id.head_social_detail_iv_pic, View.VISIBLE);
                        helper.setVisibility(R.id.gridview, View.GONE);
                        if (item.getPostFiles().get(0).getFileType() == 1) {
                            helper.setVisibility(R.id.img_video, View.GONE);
                            if (item.getPostFiles().get(0).getFileUrl().contains(FieldFinals.WATERMARK)) {
                                helper.loadImage(R.id.head_social_detail_iv_pic, item.getPostFiles().get(0).getFileUrl() + StringFinal.IMG_200);
                            } else {
                                helper.loadImage(R.id.head_social_detail_iv_pic, item.getPostFiles().get(0).getFileUrl() + StringFinal.IMG2_200);
                            }
                        } else if (item.getPostFiles().get(0).getFileType() == 3) {
                            helper.setVisibility(R.id.img_video, View.VISIBLE);
                            helper.setImageResource(R.id.img_video, R.mipmap.btn_video);
                            helper.setVisibility(R.id.tv_duration, View.VISIBLE);
                            helper.setText(R.id.tv_duration, item.getPostFiles().get(0).getDuration());
                            helper.loadImage(R.id.head_social_detail_iv_pic, item.getPostFiles().get(0).getFileUrl() + StringFinal.VIDEO_URL_END + StringFinal.IMG_200);
                        } else if (item.getPostFiles().get(0).getFileType() == 4) {
                            if (item.getPostFiles().get(0).getFileUrl().contains(FieldFinals.WATERMARK)) {
                                String s[] = item.getPostFiles().get(0).getFileUrl().split("\\?");
                                helper.loadImage(R.id.head_social_detail_iv_pic, s[0] + StringFinal.GIF_URL_END + StringFinal.IMG_200);
                            } else {
                                helper.loadImage(R.id.head_social_detail_iv_pic, item.getPostFiles().get(0).getFileUrl() + StringFinal.GIF_URL_END + StringFinal.IMG_200);
                            }
                            helper.setVisibility(R.id.img_video, View.VISIBLE);
                            helper.setImageResource(R.id.img_video, R.mipmap.btn_gif);
                        }
                    } else {
                        helper.setVisibility(R.id.head_social_detail_iv_pic, View.GONE);
                        helper.setVisibility(R.id.gridview, View.VISIBLE);
                        GridLayout gridview = helper.getView(R.id.gridview);
                        int a = item.getPostFiles().size() / 3;
                        int b = item.getPostFiles().size() % 3;
                        if (b > 0) {
                            a++;
                        }
                        float width = (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 40) / 3;
                        gridview.getLayoutParams().height = (int) ((a * width) + GoodHappinessApplication.perHeight * 6 * a);

                        for (int i = 0; i < 9; i++) {
                            helper.setVisibility(ImageId[i], View.GONE);
                            helper.setVisibility(gifId[i], View.GONE);
                        }

                        for (int i = 0; i < item.getPostFiles().size(); i++) {
                            ImageView imgview = helper.getView(ImageId[i]);
                            imgview.setOnClickListener(new SingleOnclick(getActivity(), i, item.getPostFiles()));
                            helper.setVisibility(ImageId[i], View.VISIBLE);
                            imgview.getLayoutParams().width = (int) width;
                            imgview.getLayoutParams().height = (int) width;
                            if (item.getPostFiles().get(i).getFileType() == 1) {
                                helper.setVisibility(gifId[i], View.GONE);
                                if (item.getPostFiles().get(i).getFileUrl().contains(FieldFinals.WATERMARK)) {
                                    helper.loadImage(imgview, item.getPostFiles().get(i).getFileUrl() + StringFinal.IMG_100);
                                } else {
                                    helper.loadImage(imgview, item.getPostFiles().get(i).getFileUrl() + StringFinal.IMG2_100);
                                }
                            } else if (item.getPostFiles().get(i).getFileType() == 4) {
                                helper.setVisibility(gifId[i], View.VISIBLE);
                                if (item.getPostFiles().get(i).getFileUrl().contains(FieldFinals.WATERMARK)) {
                                    String s[] = item.getPostFiles().get(i).getFileUrl().split("\\?");
                                    helper.loadImage(imgview, s[0] + StringFinal.GIF_URL_END + StringFinal.IMG_100);
                                } else {
                                    helper.loadImage(imgview, item.getPostFiles().get(i).getFileUrl() + StringFinal.GIF_URL_END + StringFinal.IMG_100);
                                }
                            }
                        }
                    }
                } else {
                    helper.setImageResource(R.id.head_social_detail_iv_pic, R.mipmap.loading_default);
                }
                if (TextUtils.isEmpty(item.getPostContent())) {
                    if (item.getTagArray() != null && item.getTagArray().size() > 0) {
                        helper.setVisibility(R.id.layout_list_fragment_social_tv_des, View.VISIBLE);
                        SpannableString s = new SpannableString(TextUrlUtil.getTagString(item.getTagArray()));
                        TextUrlUtil.dealContent(s, (TextView) helper.getView(R.id.layout_list_fragment_social_tv_des), R.color.advert_blue_text, new TextUrlUtil.OnClickString() {
                            @Override
                            public void OnClick(String s) {
                                IntentUtils.startToSocialDetail(getActivity(), item.getFeedId());
//                                startToSearchTag(s);
                            }
                        });
                    } else {
                        helper.setVisibility(R.id.layout_list_fragment_social_tv_des, View.GONE);
                    }
                } else {
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_des, View.VISIBLE);
                    SpannableString s = new SpannableString(TextUrlUtil.getTagString(item.getTagArray()) + item.getPostContent());
                    TextUrlUtil.dealContent(s, (TextView) helper.getView(R.id.layout_list_fragment_social_tv_des), R.color.advert_blue_text, new TextUrlUtil.OnClickString() {
                        @Override
                        public void OnClick(String s) {
                            IntentUtils.startToSocialDetail(getActivity(), item.getFeedId());
//                            startToSearchTag(s);
                        }
                    });
                }
                helper.setText(R.id.layout_list_fragment_social_tv_praise, String.format(getString(R.string.format_what_praise, item.getLikeNum())));//item.getLikeNum() + "赞");
                helper.setText(R.id.layout_list_fragment_social_tv_comment, String.format(getString(R.string.format_what_comment, item.getCommentNum())));//item.getCommentNum() + "评论");
                helper.setOnclickListener(R.id.layout_list_fragment_social_tv_comment, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SocialDetailActivity.class);
                        intent.putExtra(FieldFinals.FEED_ID, item.getFeedId());
                        startActivity(intent);
                    }
                });
                if (post_type.equals(POST_TYPE_FOCUS) || item.getPostUserInfo().getRelation() == 2 || item.getPostUserInfo().getRelation() == 0 || item.getPostUserInfo().getRelation() == 3) {
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_time, View.VISIBLE);
                    helper.setText(R.id.layout_list_fragment_social_tv_time, DateUtil.getPublishTime(item.getCreated()));
                } else {
                    TextView textView = helper.getView(R.id.layout_list_fragment_social_tv_focus);
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.VISIBLE);
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_time, View.GONE);
                    switch (item.getPostUserInfo().getRelation()) {
                        case 0:
                            helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                            break;
                        case 1:
                            textView.setText(getResString(R.string.focus_status_no));
                            textView.setBackgroundResource(R.drawable.shape_for_white_stroke);
                            textView.setTextColor(getTheColor(R.color.black_333_text));
                            break;
                        case 2:
                            textView.setText(getResString(R.string.focus_status_yes));
                            textView.setBackgroundResource(R.drawable.shape_for_black_focus);
                            textView.setTextColor(getTheColor(R.color.white));
                            break;
                        case 3:
                            textView.setText(getResString(R.string.focus_status_each));
                            textView.setBackgroundResource(R.drawable.shape_for_yellow);
                            textView.setTextColor(getTheColor(R.color.black_333_text));
                            break;
                    }
                }

                if (item.getIsLike() == 1) {
                    helper.setImageResource(R.id.layout_list_fragment_social_iv_praise, R.mipmap.ico_like_click);
                } else {
                    helper.setImageResource(R.id.layout_list_fragment_social_iv_praise, R.mipmap.ico_like);
                }
//                helper.setOnclickListener(R.id.layout_list_fragment_social_iv_praise, position, onPraiseClickListener);
                ImageView imageView1 = helper.getView(R.id.layout_list_fragment_social_iv_praise);
                RxView.clicks(imageView1).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isUserLogined())
                            if (list.get(position).getIsLike() == 1) {//取消
                                praise(position, list.get(position).getFeedId(), HttpFinal.LIKE_DELETE);
                            } else {//点赞
                                praise(position, list.get(position).getFeedId(), HttpFinal.LIKE_CREATE);
                            }
                    }
                });
//                imageView1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                TextView textView2 = helper.getView(R.id.layout_list_fragment_social_tv_focus);
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isUserLogined()) {
                            focus(position, list.get(position).getPostUserInfo().getUid());
                        }
                    }
                });
                ImageView imageView2 = helper.getView(R.id.layout_list_fragment_social_iv_head);
                imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.startToPerson(getActivity(), list.get(position).getPostUserInfo().getUid());
                    }
                });
                helper.getView(R.id.rl_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SocialDetailActivity.class);
                        intent.putExtra(FieldFinals.FEED_ID, item.getFeedId());
                        startActivity(intent);
                    }
                });
                helper.getView(R.id.rl_content).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SocialDetailActivity.class);
                        intent.putExtra(FieldFinals.FEED_ID, item.getFeedId());
                        startActivity(intent);
                    }
                });
                ImageView imageView3 = helper.getView(R.id.layout_list_fragment_social_iv_share);
                imageView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int mode = list.get(position).getPostUserInfo().getUid() == getUid() ? 1 : 2;
                        shareDialog = DialogFactory.createShareDialog(mode, getActivity(), new OnShareClickListener() {
                            @Override
                            public void onclick(int action) {
                                switch (action) {
                                    case R.string.delete:
                                        DialogFactory.createSelectDialog(getActivity(), getString(R.string.is_delete), new OnSelectListener() {
                                            @Override
                                            public void onSelected(boolean isSelected) {
                                                if (isSelected) {
                                                    deleteFriends(position);
                                                }
                                            }
                                        });
                                        break;
                                    case R.string.report:
                                        IntentUtils.startToReport(getActivity(), ReportActivity.REPORT_TYPE_FEED, list.get(position).getFeedId());
                                        break;
                                }
                            }
                        }, new String[]{getDid(), getSid(), FieldFinals.FEED, String.valueOf(list.get(position).getFeedId())});
                    }
                });
                LinearLayout llSendFlower = helper.getView(R.id.layout_list_fragment_social_ll_send_flower);
                if (item.getPostUserInfo().getUid() == getUid()) {
                    llSendFlower.setVisibility(View.GONE);
                } else {
                    llSendFlower.setVisibility(View.VISIBLE);
                    llSendFlower.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isUserLogined()) {
                                if (IntentUtils.checkHasBind(getActivity())) {
                                    RongIMUtils.setUserInfoProvider(new UserInfo(list.get(position).getPostUserInfo().getUid() + "", list.get(position).getPostUserInfo().getNickname(), Uri.parse(list.get(position).getPostUserInfo().getAvatar())));
                                    DialogFactory.createSendFlowerDialog(getActivity(), new OnSendFlowerListener() {
                                        @Override
                                        public void onclick(Integer count) {
                                            confirmFlowerOrder(count, list.get(position).getPostUserInfo().getUid());
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        };
        if (post_type.equals(POST_TYPE_FOCUS) || post_type.equals(POST_TYPE_WORLD)) {
            addHeadView();
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= list.size() + listCount && position - listCount >= 0) {
                    Intent intent = new Intent(getActivity(), SocialDetailActivity.class);
                    intent.putExtra(FieldFinals.FEED_ID, list.get(position - listCount).getFeedId());
                    startActivity(intent);
                }
            }
        });
    }

    private void startToSearchTag(String s) {
        s = s.substring(8, s.length() - 1);
        if (getActivity().getClass().getName().equals(SearchActivity.class.getName())) {
            ((SearchActivity) getActivity()).searchTag(s);
        } else {
            IntentUtils.startToSearchTag(getActivity(), s);
        }
    }

    private void confirmFlowerOrder(final int count, long uid) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_CONFIRM);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, uid + "");
        params.addBodyParameter(FieldFinals.NUM, count + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<ConfirmOrder>>() {
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
                    confirmOrder.setClassName(FocusFragment.this.getClass().getName() + post_type);
                    confirmOrder.setFlowerCount(count);
                    IntentUtils.startToPayOrder(getActivity(), confirmOrder);
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

    private void addHeadView() {
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = lif.inflate(R.layout.layout_focus_advertisement, lv, false);//getActivity().getLayoutInflater().inflate(R.layout.layout_lottery_head,lv,false);
        mBanner = (com.youth.banner.Banner) headView.findViewById(R.id.advert);
        tv_channel = (TextView) headView.findViewById(R.id.tv_channel);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBanner.getLayoutParams();
//        params.width = w;
        ViewSizeUtils.setLinearLayoutViewWithFullWidth(750, 300, mBanner, 20);
//        headView.findViewById(R.id.rl_channel).setVisibility(View.GONE);
        RecyclerView mRecyclerView = (RecyclerView) headView.findViewById(R.id.rv);
////设置布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
////设置adapter
        channelItems = new ArrayList<>();
//        channelItems.add(new ChannelItem(0));
//        channelItems.add(new ChannelItem(1));
//        channelItems.add(new ChannelItem(2));
//        channelItems.add(new ChannelItem(3));
//        channelItems.add(new ChannelItem(4));
        galleryAdapter = new GalleryAdapter(getActivity(), channelItems);
        mRecyclerView.setAdapter(galleryAdapter);
        galleryAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(Channel channel, int position) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                intent.putExtra(FieldFinals.CHANNEL_ID, channel.getChannelId());
                intent.putExtra(FieldFinals.CHANNEL, channel.getChannelName());
                intent.putExtra(FieldFinals.POST_TYPE, post_type);
                startActivity(intent);
            }
        });
//        List<Banner> bannersBeans = new ArrayList<>();
//        initAD(bannersBeans);
        lv.addHeaderView(headView);
    }

    private void initAD(final List<Banner> bannersBeans) {
        List<String> list = new ArrayList<>();
        for (Banner b : bannersBeans) {
            list.add(b.getImgUrl());
            Log.e("q_", b.getImgUrl());
        }
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        if (list.size() > 1) {
            mBanner.setDelayTime(4000);
            mBanner.isAutoPlay(true);
        }
        mBanner.setImages(list);
        mBanner.setOnBannerClickListener(new com.youth.banner.Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                position -= 1;
                if (!TextUtils.isEmpty(bannersBeans.get(position).getAppUrl())) {
                    if (!IntentUtils.checkURL(getActivity(), bannersBeans.get(position).getAppUrl())) {
                        IntentUtils.startToWebView(getActivity(), bannersBeans.get(position).getAppUrl());
                    }
                } else if (!TextUtils.isEmpty(bannersBeans.get(position).getHtmlUrl())) {
                    IntentUtils.startToWebView(getActivity(), bannersBeans.get(position).getHtmlUrl());
                }
            }
        });
    }

    private void deleteFriends(final int position) {
        RequestParams params = new RequestParams(HttpFinal.POST_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, list.get(position).getFeedId() + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<PostUserInfoBean>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                dialog.show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                list.remove(position);
                adapter.notifyDataSetChanged();

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

    private void focus(final int position, final long user_id) {
        if (list.get(position).getPostUserInfo().getUid() == getUid()) {
            return;
        }
        RequestParams params = new RequestParams(list.get(position).getPostUserInfo().getRelation() == 1 ? HttpFinal.FRIENDSHIP_CREATE : HttpFinal.FRIENDSHIP_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, user_id + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<PostUserInfoBean>>() {
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
                if (list.get(position).getPostUserInfo().getRelation() == 1) {
                    showToast(R.string.focusSuccess);
                } else {
                    showToast(R.string.focusCancel);
                }
                int i = 0;
                if (postUserInfoBean.getUid() != 0) {
                    for (FeedInfo feedInfo : list) {
                        if (user_id == feedInfo.getPostUserInfo().getUid()) {
                            list.get(i).getPostUserInfo().setRelation(postUserInfoBean.getRelation());
                        }
                        i++;
                    }
                    adapter.notifyDataSetChanged();
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


    private void praise(final int position, long feedID, final String action) {
        RequestParams params = new RequestParams(action);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(getActivity(), FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, feedID + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<FeedInfoList>>() {
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
                list.get(position).setLikeNum(action.equals(HttpFinal.LIKE_CREATE) ? (list.get(position).getLikeNum() + 1) : (list.get(position).getLikeNum() - 1));
                list.get(position).setIsLike(action.equals(HttpFinal.LIKE_CREATE) ? 1 : 0);
                adapter.notifyDataSetChanged();
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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.POST_LIST);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.POST_TYPE, post_type);
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<FeedInfoList>>() {
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
                if (isFirst) {
                    list.clear();
                }
                FeedInfoList feedInfo = (FeedInfoList) result.getData();
                if (feedInfo.getBanners() != null) {
                    if (feedInfo.getBanners().size() > 0) {
                        initAD(feedInfo.getBanners());
                        mBanner.setVisibility(View.VISIBLE);
                    } else {
                        mBanner.setVisibility(View.GONE);
                    }
                } else {
                    mBanner.setVisibility(View.GONE);
                }
                if (feedInfo.getChannel() != null && feedInfo.getChannel().size() > 0) {
                    PreferencesUtil.setPreferences(getActivity(), FieldFinals.CHANNEL, feedInfo.getChannel());
                    channelItems.clear();
                    channelItems.addAll(feedInfo.getChannel());
                    galleryAdapter.notifyDataSetChanged();
                }else{
                    tv_channel.setVisibility(View.GONE);
                }
                list.addAll(feedInfo.getList());
                adapter.notifyDataSetChanged();
                if (feedInfo.getMore() != 1) {
                    isCanLoad = false;
                } else {
                    isCanLoad = true;
                }
                if (isFirst) {
                    lv.setSelection(0);
                }
                ll_fail_view.setVisibility(View.GONE);
                endRefreshing();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ll_fail_view.setVisibility(View.VISIBLE);
                endRefreshing();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                endRefreshing();
            }
        });
    }

    private void endRefreshing() {
        if (page == 1) {
            srl.endRefreshing();
        } else {
            srl.endLoadingMore();
        }
    }
}
