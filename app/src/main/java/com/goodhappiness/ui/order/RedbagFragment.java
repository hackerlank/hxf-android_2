package com.goodhappiness.ui.order;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Redbag;
import com.goodhappiness.bean.RedbagList;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.HttpUtils;
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

@ContentView(R.layout.fragment_friendship)
public class RedbagFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String redbag_status = "redbag_status";
    private static final String ARG_PARAM2 = "param2";

    @ViewInject(R.id.focus_me_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.focus_me_lv)
    private ListView lv;
    @ViewInject(R.id.empty_view_no_list)
    private LinearLayout ll_empty_list;
    private boolean hasMore = true;

    private CommonAdapter<Redbag> adapter;
    private List<Redbag> list = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String redbagStatus;
    private String mParam2;
    private int page = 1;


    public RedbagFragment() {
        super(R.layout.fragment_friendship);
    }

    public static RedbagFragment newInstance(String param1, String param2) {
        RedbagFragment fragment = new RedbagFragment();
        Bundle args = new Bundle();
        args.putString(redbag_status, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            redbagStatus = getArguments().getString(redbag_status);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.redbag_page)); //统计页面，"MainScreen"为页面名称，可自定义
        onBGARefreshLayoutBeginRefreshing(srl);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.redbag_page));
    }

    @Override
    protected void reload() {

    }

    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.RED_BAG);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, getUid()+"");
        params.addBodyParameter(FieldFinals.USE_STATUS, redbagStatus);//1为关注， 2为粉丝
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<RedbagList>>() {
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
                RedbagList friendShip = (RedbagList) result.getData();
                list.addAll(friendShip.getList());
                if (friendShip.getMore() != 1) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                if(redbagStatus.equals("0")){
                    ((RedbagActivity)getActivity()).setCanUseCount(friendShip.getTotal());
                }
                adapter.notifyDataSetChanged();
                ll_empty_list.setVisibility(list.size() < 1 ? View.VISIBLE : View.GONE);
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

    private void endRefreshing(){
        if (page == 1) {
            srl.endRefreshing();
        } else {
            srl.endLoadingMore();
        }
    }

    @Override
    protected void setData() {
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        adapter = new CommonAdapter<Redbag>(getActivity(), list, R.layout.layout_list_redbag_item) {
            @Override
            public void convert(ViewHolder helper, Redbag item, int position) {
                RelativeLayout rlCover = helper.getView(R.id.rl_cover);
                ImageView ivStatus = helper.getView(R.id.iv_status);
                if (item.getUseStatus()==0) {
                    rlCover.setVisibility(View.GONE);
                    ivStatus.setVisibility(View.GONE);
                } else {
                    rlCover.setVisibility(View.VISIBLE);
                    ivStatus.setVisibility(View.VISIBLE);
                    if(item.getUseStatus()==1){
                        ivStatus.setImageResource(R.mipmap.use_over);
                    }else{
                        ivStatus.setImageResource(R.mipmap.deadline);
                    }
                }
                helper.setText(R.id.red_name,item.getRedName());
                helper.setText(R.id.start_time,item.getStartTime());
                helper.setText(R.id.end_time,item.getEndTime());
                helper.setText(R.id.desc,item.getDesc());
                helper.setText(R.id.money,item.getMoney()+"");
                helper.setText(R.id.rest_money,item.getRestMoney()+"");
            }
        };
        lv.setAdapter(adapter);
        lv.setDivider(new ColorDrawable(getResources().getColor(R.color.trans)));
        lv.setDividerHeight(0);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        initList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (hasMore) {
            page++;
            initList();
            return true;
        } else {
            showToast(R.string.list_no_more);
            return false;
        }
    }
}
