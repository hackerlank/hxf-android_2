package com.goodhappiness.ui.social;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.FeedInfo;
import com.goodhappiness.bean.FriendShip;
import com.goodhappiness.bean.FriendShipChangeBean;
import com.goodhappiness.bean.PostUserInfoBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_friendship)
public class SearchUserFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.focus_me_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.focus_me_lv)
    private ListView lv;
    @ViewInject(R.id.empty_view_no_list)
    private LinearLayout ll_empty_list;

    private boolean hasMore = true;
    private CommonAdapter<PostUserInfoBean> adapter;
    private List<PostUserInfoBean> list = new ArrayList<>();
    private TvStatusOnclickListener tvStatusOnclickListener = new TvStatusOnclickListener();
    private int page = 1;
    private String currentKey = "";
    public SearchUserFragment() {
        super(R.layout.fragment_friendship);
    }

    public static SearchUserFragment newInstance() {
        SearchUserFragment fragment = new SearchUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.focus_or_fans_)); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.focus_or_fans_));
    }

    @Override
    protected void setData() {
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        initAdapter();
        onBGARefreshLayoutBeginRefreshing(srl);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_empty_list.getLayoutParams();
        params.setMargins(0,0,0,0);
        ll_empty_list.setLayoutParams(params);
        ll_empty_list.setVisibility(View.GONE);
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<PostUserInfoBean>(getActivity(), list, R.layout.layout_list_praise) {
            @Override
            public void convert(ViewHolder helper, final PostUserInfoBean item, int position) {
                helper.setVisibility(R.id.layout_list_fragment_social_tv_time, View.GONE);
                if (item.getUid() != getUid()) {
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.VISIBLE);
                    TextView tvStatus = helper.getView(R.id.layout_list_fragment_social_tv_focus);
                    switch (item.getRelation()) {
                        case 0:
                            tvStatus.setVisibility(View.GONE);
                            break;
                        case 1:
                            tvStatus.setText(getString(R.string.focus_status_no));
                            tvStatus.setBackgroundResource(R.drawable.shape_for_white_stroke);
                            tvStatus.setTextColor(getTheColor(R.color.black));
                            break;
                        case 3:
                            tvStatus.setText(getString(R.string.focus_status_each));
                            tvStatus.setBackgroundResource(R.drawable.shape_for_yellow);
                            tvStatus.setTextColor(getTheColor(R.color.black_333_text));
                            break;
                        case 2:
                            tvStatus.setText(getString(R.string.focus_status_yes));
                            tvStatus.setBackgroundResource(R.drawable.shape_for_black_focus);
                            tvStatus.setTextColor(getTheColor(R.color.white));
                            break;
                    }
                    tvStatus.setTag(position);
                    tvStatus.setOnClickListener(tvStatusOnclickListener);
                } else {
                    helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                }
                helper.setText(R.id.layout_list_fragment_social_tv_name, item.getNickname());
                helper.loadImage(R.id.layout_list_praise_iv_head, item.getAvatar());
                helper.setVisibility(R.id.layout_list_fragment_social_tv_summery, View.GONE);
                View linearLayout = helper.getView(R.id.layout_list_praise);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                params.height = (int) (GoodHappinessApplication.perHeight * 114);
                linearLayout.setLayoutParams(params);
                helper.setOnclickListener(R.id.layout_list_praise_iv_head, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.startToPerson(getActivity(), item.getUid());
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getUid() != getUid()) {
                    if(RongIMUtils.isCanChat(getActivity(),list.get(position).getRelation())){
                        RongIMUtils.setUserInfoProvider(new UserInfo(String.valueOf(list.get(position).getUid()), list.get(position).getNickname(), Uri.parse(list.get(position).getAvatar())));
                        RongIMUtils.startPrivate(getActivity(), list.get(position).getUid(), list.get(position).getNickname());
                    }
                }
            }
        });
    }

    private void focus(final int position, long user_id, final String action) {
        RequestParams params = new RequestParams(action);
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
                if (postUserInfoBean != null) {
                    changeFeed(postUserInfoBean);
                    list.get(position).setRelation(postUserInfoBean.getRelation());
                    if (!action.equals(HttpFinal.FRIENDSHIP_CREATE)) {
//                        list.remove(position);
                        showToast(R.string.cancel_focus);
                    }
                    GoodHappinessApplication.isNeedRefresh = true;
                    adapter.notifyDataSetChanged();
                    ll_empty_list.setVisibility(list.size() < 1 ? View.VISIBLE : View.GONE);
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
                endRefreshing();
            }
        });
    }

    private void changeFeed(PostUserInfoBean postUserInfoBean) {
        FriendShipChangeBean bean = new FriendShipChangeBean();
        FeedInfo feedInfo = new FeedInfo();
        feedInfo.setPostUserInfo(postUserInfoBean);
        bean.setFeedInfo(feedInfo);
        bean.setAction(FieldFinals.FOCUS_CHANGE);
        IntentUtils.sendFeedChangeBroadcast(getActivity(), bean);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(currentKey!=null){
            page = 1;
            onSearch(currentKey,true);
        }else{
            endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(!TextUtils.isEmpty(currentKey)){
            if (hasMore) {
                page++;
                onSearch(currentKey,false);
                return true;
            } else {
                showToast(R.string.list_no_more);
                return false;
            }
        }else{
            return false;
        }
    }

    class TvStatusOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getTag() != null) {
                int position = (int) v.getTag();
                switch (list.get(position).getRelation()) {
                    case 1://没有关注
                        focus(position, list.get(position).getUid(), HttpFinal.FRIENDSHIP_CREATE);
                        break;
                    case 2://已关注
                        focus(position, list.get(position).getUid(), HttpFinal.FRIENDSHIP_DELETE);
                        break;
                    case 3://相互关注
                        focus(position, list.get(position).getUid(), HttpFinal.FRIENDSHIP_DELETE);
                        break;
                }
            }
        }
    }

    public void onSearch(String key, final boolean isFisrt) {
        currentKey = key;
        RequestParams params = new RequestParams(HttpFinal.SEARCH);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SEARCH_KEY, key);
        params.addBodyParameter(FieldFinals.ACTION, "user");
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<FriendShip>>() {
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
                if (page == 1||isFisrt) {
                    list.clear();
                }
                FriendShip friendShip = (FriendShip) result.getData();
                list.addAll(friendShip.getFriendshipList());
                if (friendShip.getMore() != 1) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                adapter.notifyDataSetChanged();
                ll_empty_list.setVisibility(list.size() < 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showEmptyView(true);
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
