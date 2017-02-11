package com.goodhappiness.ui.personal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.AliyPayResult;
import com.goodhappiness.bean.AliyPayResultBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UnionPayResult;
import com.goodhappiness.bean.WechatRequest;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectChargeCountListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.order.PayOrderActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_charge)
public class ChargeActivity extends BaseActivity {
    @ViewInject(R.id.charge_gv)
    private GridView gridView;
    @ViewInject(R.id.confirm_order_vg_pay_type)
    private RadioGroup rg;

    private CommonAdapter<Integer> adapter;
    private List<Integer> list = new ArrayList<>();
    private int clickPosition = 0;
    private int chargeCount = 20;
    public static final int activityPayType = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.charge));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.charge));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.charge));
        initAdapter();
    }

    @Override
    protected void reload() {

    }

    private void initAdapter() {
        list.add(20);
        list.add(50);
        list.add(100);
        list.add(200);
        list.add(500);
        list.add(0);
        adapter = new CommonAdapter<Integer>(this, list, R.layout.layout_grid_charge_count) {
            @Override
            public void convert(ViewHolder helper, Integer item, int position) {

                helper.setText(R.id.grid_charge_count, item != 0 ? item + "" : getString(R.string.other_sum));

                TextView textView = helper.getView(R.id.grid_charge_count);
                if (position == clickPosition) {
                    textView.setBackgroundResource(R.drawable.shape_for_charge_count_click);
                    textView.setTextColor(getResources().getColor(R.color.join_red));
                } else {
                    textView.setBackgroundResource(R.drawable.shape_for_charge_count);
                    textView.setTextColor(getResources().getColor(R.color.gray_999_text));
                }
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickPosition = position;
                adapter.notifyDataSetChanged();
                if (position == 5) {
                    int defaultCount = 0;
                    if (list.get(5) == 0) {
                        defaultCount = 20;
                    } else {
                        defaultCount = list.get(5);
                    }
                    DialogFactory.createChargeDialog(ChargeActivity.this, new OnSelectChargeCountListener() {
                        @Override
                        public void onSelectChargeCount(int chargeCount) {
                            ChargeActivity.this.chargeCount = chargeCount;
                            list.set(clickPosition, chargeCount);
                            adapter.notifyDataSetChanged();
                        }
                    }, defaultCount);
                } else {
                    chargeCount = list.get(position);
                }
            }
        });
    }

    @Event({R.id.charge_confirm})
    private void onEventClick(View v) {
        RequestParams params = new RequestParams(HttpFinal.RECHARGE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEE, chargeCount + "");
        switch (v.getId()) {
            case R.id.charge_confirm:
                switch (rg.getCheckedRadioButtonId()){
                    case R.id.confirm_order_vb_wechat:
                        params.addBodyParameter(FieldFinals.BANK_ID, 1 + "");
                        wxCharge(params);
                        break;
                    case R.id.confirm_order_vb_aliy:
                        params.addBodyParameter(FieldFinals.BANK_ID, 2 + "");
                        aliyCharge(params);
                        break;
                    case R.id.confirm_order_vb_union_pay:
                        params.addBodyParameter(FieldFinals.BANK_ID, "3");
                        unionPay(params);
                        break;
                }
                break;
        }
    }
    private void unionPay(RequestParams params) {
        HttpUtils.post(this, params, new TypeToken<Result<UnionPayResult>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                newDialog(false).show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                Log.e("e_", result.getStatus());
                final UnionPayResult unionPayResult = (UnionPayResult) result.getData();
                PreferencesUtil.setPreferences(ChargeActivity.this, FieldFinals.PAY_RESULT_URL, unionPayResult.getUrl());
                if (unionPayResult.getParams() != null && !TextUtils.isEmpty(unionPayResult.getParams().getHtml())) {
                    IntentUtils.startToWebViewWithData(ChargeActivity.this, unionPayResult.getParams().getHtml());
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
    private void aliyCharge(RequestParams params) {
        HttpUtils.post(this,params, new TypeToken<Result<AliyPayResult>>() {
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
                final AliyPayResult aliyPayResult = (AliyPayResult) result.getData();
                if(aliyPayResult!=null){
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask payTask = new PayTask(ChargeActivity.this);
                            PreferencesUtil.setPreferences(ChargeActivity.this, FieldFinals.SN, aliyPayResult.getSn());
                            String result = payTask.pay(aliyPayResult.getParams().getAlipay(),true);
                            Message msg = new Message();
                            GoodHappinessApplication.activityPayType = ChargeActivity.activityPayType;
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
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
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    AliyPayResultBean payResult = new AliyPayResultBean((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChargeActivity.this, ChargeResultActivity.class));
                        finishActivity();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ChargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private void wxCharge(RequestParams params) {
        HttpUtils.post(this,params, new TypeToken<Result<WechatRequest>>() {
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
                WechatRequest signParams = (WechatRequest) result.getData();
                PayReq request = new PayReq();
                request.appId = signParams.getParams().getAppid();
                request.partnerId = signParams.getParams().getPartnerid();
                request.prepayId = signParams.getParams().getPrepayid();
                request.packageValue = signParams.getParams().getPackageX();
                request.nonceStr = signParams.getParams().getNonce_str();
                request.timeStamp = signParams.getParams().getTimestamp();
                request.sign = signParams.getParams().getSign();
                PreferencesUtil.setPreferences(ChargeActivity.this, FieldFinals.SN, signParams.getSn());
                GoodHappinessApplication.activityPayType = ChargeActivity.activityPayType;
                GoodHappinessApplication.wxapi.sendReq(request);
                finishActivity();
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
}
