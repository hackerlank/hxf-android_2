package com.goodhappiness.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserRecharge;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_charge_record)
public class ChargeRecordActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    @ViewInject(R.id.charge_record_lv)
    private ListView lv;
    @ViewInject(R.id.charge_record_ll_no_charge)
    private LinearLayout ll;
    @ViewInject(R.id.charge_record_srl)
    private BGARefreshLayout srl;

    private CommonAdapter<UserRecharge.ListBean> adapter;
    private List<UserRecharge.ListBean> list = new ArrayList<>();

    private boolean hasRecord = true;
    private int page = 1;
    private boolean isCanLoad = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.charge_record));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.charge_record));
        tv_right.setText(getString(R.string.charge));
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        initAdapter();
//            ll.setVisibility(View.VISIBLE);
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.USER_RECHARGE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(this,params, new TypeToken<Result<UserRecharge>>() {
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
                UserRecharge exchangeRecord = (UserRecharge) result.getData();
                if (isFirst) {
                    list.clear();
                }
                list.addAll(exchangeRecord.getList());
                if (list.size() > 0) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                }
                if (exchangeRecord.getMore() == 1) {
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

    private void initAdapter() {
        adapter = new CommonAdapter<UserRecharge.ListBean>(this, list, R.layout.layout_list_charge_record) {
            @Override
            public void convert(ViewHolder helper, UserRecharge.ListBean item, int position) {
                helper.setText(R.id.layout_list_charge_record_time, item.getRechargeTime());
                helper.setText(R.id.layout_list_charge_record_count, BigDecimal.valueOf(item.getRechargeMoney()).toPlainString());
                switch (item.getPay_type()){
                    case 1:
                        helper.setText(R.id.layout_list_charge_record_name,R.string.wechat_pay);
                    break;
                    case 2:
                        helper.setText(R.id.layout_list_charge_record_name,R.string.ali_pay);
                    break;
                    case 3:
                        helper.setText(R.id.layout_list_charge_record_name,R.string.jd_pay);
                    break;
                    case 4:
                        helper.setText(R.id.layout_list_charge_record_name,R.string.union_pay);
                    break;
                }
                if(item.getStatus()==1){
                    helper.setText(R.id.layout_list_charge_record_status,R.string.paid);
                }
            }
        };
        lv.setAdapter(adapter);
    }

    @Event({R.id.charge_record_charge, R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right_text:
            case R.id.charge_record_charge:
                startTheActivity(new Intent(this, ChargeActivity.class));
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
