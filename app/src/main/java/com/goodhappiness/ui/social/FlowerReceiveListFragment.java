package com.goodhappiness.ui.social;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.FlowerList;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.DateUtil;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlowerReceiveListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_flower_list)
public class FlowerReceiveListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.lv)
    private ListView lv;
    @ViewInject(R.id.empty_view_no_list)
    private LinearLayout ll_empty_list;

    private CommonAdapter<FlowerList.ListBean> adapter;
    private List<FlowerList.ListBean> list = new ArrayList<>();
    private String type = "1";
    private int page = 1;
    private boolean isCanLoad = true;

    public FlowerReceiveListFragment() {
        super(R.layout.fragment_flower_list);
    }

    // TODO: Rename and change types and number of parameters
    public static FlowerReceiveListFragment newInstance(String param1) {
        FlowerReceiveListFragment fragment = new FlowerReceiveListFragment();
        Bundle args = new Bundle();
        args.putString(FieldFinals.TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(FieldFinals.TYPE);
        }
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.flower_receive_record)); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.flower_receive_record));
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void setData() {
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        adapter = new CommonAdapter<FlowerList.ListBean>(getActivity(), list, R.layout.layout_list_flower_receive) {
            @Override
            public void convert(ViewHolder helper, FlowerList.ListBean item, int position) {
                helper.setText(R.id.user_name, item.getNickname());
                helper.setText(R.id.flower_count, item.getNum() + "");
                helper.loadImage(R.id.iv_head, item.getAvatar());
                helper.setText(R.id.time, DateUtil.dateFormat(item.getCreated(), ""));
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startToPerson(getActivity(),list.get(position).getUid(),FlowerActivity.class.getName());
            }
        });
    }


    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_LIST);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.TYPE, type);
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<FlowerList>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onSuccess(Result result) {
                FlowerList data = (FlowerList) result.getData();
                if (data != null && data.getList() != null) {
                    if (isFirst) {
                        list.clear();
                    }
                    list.addAll(data.getList());
                    ll_empty_list.setVisibility(list.size()<1?View.VISIBLE:View.GONE);
                    isCanLoad = data.getMore() == 1 ? true : false;
                    adapter.notifyDataSetChanged();
                }
                endRefreshing();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                endRefreshing();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
                endRefreshing();
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

    private void endRefreshing(){
        if (page == 1) {
            srl.endRefreshing();
        } else {
            srl.endLoadingMore();
        }
    }
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        initList(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(isCanLoad){
            page++;
            initList(false);
            return true;
        }else{
            showToast(R.string.list_no_more);
            return false;
        }
    }
}
