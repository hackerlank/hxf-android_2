package com.goodhappiness.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.WinRecord;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_win_record)
public class WinRecordActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @ViewInject(R.id.win_record_lv)
    private ListView lv;
    @ViewInject(R.id.win_record_ll_no_win)
    private LinearLayout ll;
    @ViewInject(R.id.win_record_srl)
    private BGARefreshLayout srl;
    private int page;
    private CommonAdapter<WinRecord.ListBean> adapter;
    private List<WinRecord.ListBean> list = new ArrayList<>();
    private boolean isCanLoad = true;
    private boolean hasRecord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.win_record));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.win_record));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.win_record);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        onBGARefreshLayoutBeginRefreshing(srl);
        initAdapter();
//        if (hasRecord) {
//        } else {
//
//        }
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    private void initAdapter() {
        adapter = new CommonAdapter<WinRecord.ListBean>(this, list, R.layout.layout_list_win_record) {
            @Override
            public void convert(ViewHolder helper, WinRecord.ListBean item,final int position) {
                helper.setText(R.id.layout_list_win_record_tv_name, item.getName());
                helper.setText(R.id.layout_list_win_record_tv_num, item.getPeriod() + "");
                helper.setText(R.id.layout_list_win_record_tv_price,String.format(getString(R.string.people_count,item.getPrice())));//item.getPrice() + "人次");
                helper.setText(R.id.layout_list_win_record_win_tv_number, item.getLuckyCode() + "");
                helper.setText(R.id.layout_list_win_record_tv_join_count, item.getOwnerCost() + "");
                helper.setText(R.id.layout_list_win_record_tv_time, item.getCalcTime());
                helper.setText(R.id.layout_list_win_record_num, String.format(getString(R.string.format_what_piece,item.getNum())));//item.getNum()+"张");
                if(item.getGoodsType()==1){
                    helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_ptlq);
                }else{
                    helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_cjlq);
                }
                helper.setOnclickListener(R.id.layout_list_win_record_iv_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFactory.createShareDialog(3, WinRecordActivity.this, null, new String[]{getDid(), getSid(), FieldFinals.AWARD, String.valueOf(list.get(position).getPeriod())});
                    }
                });
                helper.setOnclickListener(R.id.buy_goods, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.startToShop(WinRecordActivity.this);
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startToLotteryDetail(WinRecordActivity.this,list.get(position).getPeriod());
            }
        });
    }


    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.PERIODS_AWARD);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(this,params, new TypeToken<Result<WinRecord>>() {
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
                WinRecord winRecord = (WinRecord) result.getData();
                if (isFirst) {
                    list.clear();
                }
                list.addAll(winRecord.getList());
                if (list.size() <= 0) {
                    ll.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.INVISIBLE);
                }
                if (winRecord.getMore() == 1) {
                    isCanLoad = true;
                } else {
                    isCanLoad = false;
                }
                adapter.notifyDataSetChanged();
                endRefreshing();
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
                dialog.dismiss();
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
    @Event({R.id.win_record_win})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.win_record_win:
                HomepageActivity.type = HomepageActivity.LOTTERY;
                finishActivity();
                break;
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
