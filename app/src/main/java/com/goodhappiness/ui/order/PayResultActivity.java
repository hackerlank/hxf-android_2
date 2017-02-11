package com.goodhappiness.ui.order;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.LotteryDetail;
import com.goodhappiness.bean.PayResult;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.WebViewActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.lottery.LotteryNumberActivity;
import com.goodhappiness.ui.lottery.LotteryRecordActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.CarUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.MyListView;
import com.goodhappiness.widget.XProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_pay_result)
public class PayResultActivity extends BaseActivity {
    @ViewInject(R.id.pay_result_ll_success)
    private LinearLayout ll_head_success;
    @ViewInject(R.id.pay_result_rl_waiting)
    private RelativeLayout rl_waiting;
    @ViewInject(R.id.pay_result_ll_fault)
    private LinearLayout ll_head_fault;
    @ViewInject(R.id.pay_result_ll_list_success)
    private LinearLayout ll_list_success;
    @ViewInject(R.id.pay_result_ll_list_fault)
    private LinearLayout ll_list_fault;
    @ViewInject(R.id.pay_result_ll_haf_success)
    private LinearLayout ll_head_haf_success;
    @ViewInject(R.id.pay_result_lv_success)
    private MyListView lv_success;
    @ViewInject(R.id.pay_result_lv_fault)
    private MyListView lv_fault;
    @ViewInject(R.id.pay_result_tv_fault_count)
    private TextView tv_faultCount;
    @ViewInject(R.id.pay_result_tv_success_count)
    private TextView tv_successCount;
    @ViewInject(R.id.pay_result_tv_success_total_count)
    private TextView tv_successTotalCount;

    private PayResult payResult;
    private CommonAdapter<PayResult.ListBean.SuccessBean> successAdapter;
    private CommonAdapter<PayResult.ListBean.SuccessBean> faultAdapter;
    private List<PayResult.ListBean.SuccessBean> successList = new ArrayList<>();
    private List<PayResult.ListBean.SuccessBean> faultList = new ArrayList<>();
    private static final int PAY_RESULT_SUCCESS = 3;
    private static final int PAY_RESULT_HAF_SUCCESS = 2;
    private static final int PAY_RESULT_FAULT = 4;
    private Timer timer;
    private boolean isLoad = false;
    private int loadCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.pay_result));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.pay_result));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.pay_result));
        if(getIntent()!=null){
            if(!getIntent().getBooleanExtra(FieldFinals.IS_USE_BANK,true)){//余额支付
                loadCount = 999999;
            }
        }
        initAdapter();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (loadCount-- > 0) {
                    Log.e("k_","loadCount:"+loadCount);
                    if (!isLoad) {
                        isLoad = true;
                        getData();
                    }
                } else {
                    showEmptyView(true);
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 5000);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                finishThis();
            }
        };
    }

    @Override
    protected void reload() {
        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(HttpFinal.RESULT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());//"3fb4b72b550e81f1bbfa4f170cb7e773");//
        params.addBodyParameter(FieldFinals.SID, getSid());//"SESSION-3fb4b72b550e81f1bbfa4f170cb7e773-25");//
        params.addBodyParameter(FieldFinals.SN, PreferencesUtil.getStringPreferences(this, FieldFinals.SN));//"F420463411401452");//
        HttpUtils.post(this,params, new TypeToken<Result<PayResult>>() {
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
                payResult = (PayResult) result.getData();
                if (payResult == null) {
                    isLoad = false;
                    return;
                }
                if (payResult.getPayStatus() == 1) {
                    if(loadCount<=0){
                        showEmptyView(true);
                    }
                    isLoad = false;
                    return;
                }
                initView(payResult.getPayStatus());
                successList.clear();
                faultList.clear();
                rl_waiting.setVisibility(View.GONE);
                successList.addAll(payResult.getList().getSuccess());
                if (successList.size() > 0) {
                    ll_list_success.setVisibility(View.VISIBLE);
                    int total = 0;
                    for (PayResult.ListBean.SuccessBean bean : successList) {
                        total += bean.getShares();
                    }
                    tv_successCount.setText(successList.size() + "");
                    tv_successTotalCount.setText(total + "");
                }
                faultList.addAll(payResult.getList().getFailure());
                if (faultList.size() > 0) {
                    ll_list_fault.setVisibility(View.VISIBLE);
                    tv_faultCount.setText(faultList.size() + "");
                }
                successAdapter.notifyDataSetChanged();
                faultAdapter.notifyDataSetChanged();
                isLoad = false;
                timer.cancel();
                timer.purge();
                loadCount = 0;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isLoad = false;
                showEmptyView(true);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void initAdapter() {
        successAdapter = new CommonAdapter<PayResult.ListBean.SuccessBean>(this, successList, R.layout.layout_list_pay_result_success) {
            @Override
            public void convert(ViewHolder helper, final PayResult.ListBean.SuccessBean item, int position) {
                if (position == successList.size() - 1) {
                    helper.getView(R.id.layout_list_pay_result_success_dash).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.layout_list_pay_result_success_dash).setVisibility(View.VISIBLE);
                }
                RelativeLayout rl = helper.getView(R.id.layout_list_pay_result_success_num);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LotteryDetail lotteryDetail = new LotteryDetail();
                        lotteryDetail.setCodes(item.getCodes());
                        Task task = new Task(lotteryDetail);
                        task.execute();

                    }
                });
                StringBuffer codes = new StringBuffer(getString(R.string.my_num_));
                int count = 0;
                for (Long l : item.getCodes()) {
                    codes.append(l + "    ");
                    if(++count>8){
                        break;
                    }
                }
                helper.setText(R.id.layout_list_pay_result_tv_name, item.getGoods().getName());
                helper.setText(R.id.layout_list_pay_result_tv_period,String.format(getString(R.string.format_goods_period_,item.getPeriod())) );//"商品期号：" + item.getPeriod());
                helper.setText(R.id.layout_list_pay_result_tv_cost, item.getShares() + "");
                helper.setText(R.id.layout_list_pay_result_tv_codes, codes.toString());
            }
        };
        lv_success.setAdapter(successAdapter);
        faultAdapter = new CommonAdapter<PayResult.ListBean.SuccessBean>(this, faultList, R.layout.layout_list_pay_result_fault) {
            @Override
            public void convert(ViewHolder helper, PayResult.ListBean.SuccessBean item, int position) {
                if (position == faultList.size() - 1) {
                    helper.getView(R.id.layout_list_pay_result_fault_dash).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.layout_list_pay_result_fault_dash).setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.layout_list_pay_result_fault_name, item.getGoods().getName());
                helper.setText(R.id.layout_list_pay_result_fault_period, String.format(getString(R.string.format_goods_period_,item.getPeriod())));
            }
        };
        lv_fault.setAdapter(faultAdapter);
    }
    class Task extends AsyncTask<Integer,Integer,String> {

        LotteryDetail lotteryDetail;

        public Task(LotteryDetail lotteryDetail) {
            this.lotteryDetail = lotteryDetail;
        }

        private XProgressDialog xProgressDialog = new XProgressDialog(PayResultActivity.this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            xProgressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            PreferencesUtil.setPreferences(PayResultActivity.this,FieldFinals.LOTTERY_NUMBER2,lotteryDetail);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(PayResultActivity.this, LotteryNumberActivity.class);
            startTheActivity(intent);
            xProgressDialog.dismiss();
        }
    }
    private void initView(int payResult) {
        switch (payResult) {
            case PAY_RESULT_SUCCESS:
                ll_head_success.setVisibility(View.VISIBLE);
                break;
            case PAY_RESULT_FAULT:
                ll_head_fault.setVisibility(View.VISIBLE);
                break;
            case PAY_RESULT_HAF_SUCCESS:
                ll_head_haf_success.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Event({R.id.pay_result_tv_review_record, R.id.pay_result_tv_continue_join})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.pay_result_tv_review_record:
                startTheActivity(new Intent(this, LotteryRecordActivity.class));
                finishThis();
                break;
            case R.id.pay_result_tv_continue_join:
                finishThis();
                break;
        }
    }

    private void finishThis() {
//        AppManager.getAppManager().finishActivity(InventoryActivity.class);
//        AppManager.getAppManager().finishActivity(ConfirmOrderActivity.class);
//        AppManager.getAppManager().finishActivity(PayOrderActivity.class);
        if(timer!=null){
            timer.cancel();
        }
        AppManager.getAppManager().finishActivities(new Class[]{InventoryActivity.class, ConfirmOrderActivity.class
                , PayOrderActivity.class, WebViewActivity.class});
        finishActivity();
        CarUtils.clear(this);
    }
}
