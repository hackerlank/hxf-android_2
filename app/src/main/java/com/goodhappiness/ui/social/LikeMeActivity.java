package com.goodhappiness.ui.social;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Like;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SocialDetail;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.DateUtil;
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

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

@ContentView(R.layout.activity_like_me)
public class LikeMeActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.comment_to_me_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.comment_to_me_lv)
    private ListView lv;
    @ViewInject(R.id.empty_view_no_list)
    private LinearLayout ll_empty_list;

    private boolean hasMore = true;
    private int page = 1;

    private CommonAdapter<Like> adapter;
    private List<Like> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.like_to_me));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.like_to_me));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        RongIMUtils.closeNotify(this, 3);
        tv_title.setText(R.string.like_to_me);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        initAdapter();
        onBGARefreshLayoutBeginRefreshing(srl);
        RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "3", new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<Like>(this, list, R.layout.layout_list_comment) {
            @Override
            public void convert(ViewHolder helper, Like item, int position) {
                helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                helper.loadImage(R.id.layout_list_praise_iv_head, item.getAvatar());
                helper.loadImage(R.id.layout_list_fragment_social_iv, item.getFileUrl());
                helper.setText(R.id.layout_list_fragment_social_tv_time, DateUtil.getPublishTime(item.getCreated()));
                helper.setText(R.id.layout_list_fragment_social_tv_name, item.getNickname());
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startToSocialDetail(LikeMeActivity.this, list.get(position).getFeedId());
            }
        });
    }


    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.LIKE_TO_ME);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
//        params.addBodyParameter(FieldFinals.UID, getUid()+"");
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(this,params, new TypeToken<Result<SocialDetail.LikeBean>>() {
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
                if (page == 1) {
                    list.clear();
                }
                SocialDetail.LikeBean commentList = (SocialDetail.LikeBean) result.getData();
                list.addAll(commentList.getLikeList());
                if (commentList.getMore() != 1) {
                    hasMore = false;
                }else{
                    hasMore = true;
                }
                ll_empty_list.setVisibility(list.size()<1?View.VISIBLE:View.GONE);
                adapter.notifyDataSetChanged();
                endRefreshing();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showEmptyView(true);
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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        initList();
    }

    private void endRefreshing(){
        if (page == 1) {
            srl.endRefreshing();
        } else {
            srl.endLoadingMore();
        }
    }
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(hasMore){
            page++;
            initList();
            return true;
        }else{
            showToast(R.string.list_no_more);
            return false;
        }
    }
}
