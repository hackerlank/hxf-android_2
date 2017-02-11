package com.goodhappiness.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.ExchangeRecord;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.order.MsgConfirmActivity;
import com.goodhappiness.utils.AppManager;
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

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_exchange_record)
public class ExchangeRecordActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    public static int activityPayType = 1004;
    @ViewInject(R.id.exchange_record_lv)
    private ListView lv;
    @ViewInject(R.id.exchange_record_ll_no_exchange)
    private LinearLayout ll;
    @ViewInject(R.id.exchange_record_srl)
    private BGARefreshLayout srl;
    private int page;
    private boolean isCanLoad = true;
    private CommonAdapter<ExchangeRecord.ListBean> adapter;
    private List<ExchangeRecord.ListBean> list = new ArrayList<>();

    private boolean hasRecord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.exchange_record));
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                AppManager.getAppManager().finishActivity(ExchangeRecordActivity.class);
                finishActivity();
            }
        };
        initAdapter();
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onBGARefreshLayoutBeginRefreshing(srl);
        MobclickAgent.onPageStart(getString(R.string.exchange_record));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.exchange_record));
        MobclickAgent.onPause(this);
    }
    private void initAdapter() {
        adapter = new CommonAdapter<ExchangeRecord.ListBean>(this, list, R.layout.layout_list_exchange_record) {
            @Override
            public void convert(ViewHolder helper, ExchangeRecord.ListBean item, final int position) {
                TextView textView = helper.getView(R.id.layout_exchange_record_tv_status);
                switch (item.getStatus()) {
                    case 1:
                        textView.setText(R.string.confirm_address);
                        textView.setBackgroundResource(R.drawable.shape_for_theme_btn);
                        break;
                    case 2:
                        textView.setText(R.string.goods_sending);
                        textView.setBackgroundResource(R.color.white);
                        break;
                    case 3:
                        textView.setText(R.string.confirm_receive);
                        textView.setBackgroundResource(R.drawable.shape_for_theme_btn);
                        break;
                    case 4:
                        textView.setText(R.string.received);
                        textView.setBackgroundResource(R.color.white);
                        break;
                }
                helper.loadImage(R.id.layout_list_exchange_record_iv, item.getProductImage());
                helper.setText(R.id.layout_list_exchange_record_tv_name, item.getProductName());
                helper.setText(R.id.layout_list_exchange_record_tv_happy_coin, item.getHappyCoin() + "");
                helper.setText(R.id.layout_list_exchange_record_tv_general_coin, item.getGeneralCoin() + "");
                helper.setText(R.id.layout_list_exchange_record_tv_time, item.getExchangeTime());
                helper.setOnclickListener(R.id.layout_list_exchange_record_iv_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFactory.createShareDialog(3, ExchangeRecordActivity.this, null, new String[]{getDid(), getSid(), FieldFinals.EXCHANGE, String.valueOf(list.get(position).getProductId())});
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExchangeRecordActivity.this, MsgConfirmActivity.class);
                intent.putExtra(FieldFinals.EX_ID,list.get(position).getExId());
                startTheActivity(intent);
            }
        });
    }

    @Event({R.id.exchange_record_exchange})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.exchange_record_exchange:
                HomepageActivity.type = HomepageActivity.SHOP;
//                AppManager.getAppManager().finishActivity();
                finishActivity();
            break;
        }
    }

    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.USER_EXCHANGE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(this,params, new TypeToken<Result<ExchangeRecord>>() {
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
                ExchangeRecord exchangeRecord = (ExchangeRecord) result.getData();
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
                endRefreshing();
                adapter.notifyDataSetChanged();
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
