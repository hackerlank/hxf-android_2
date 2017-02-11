package com.goodhappiness.ui.order;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.NewWebViewActivity;
import com.goodhappiness.R;
import com.goodhappiness.bean.AliyPayResult;
import com.goodhappiness.bean.AliyPayResultBean;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.PayFlowerResult;
import com.goodhappiness.bean.PayType;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UnionPayResult;
import com.goodhappiness.bean.WechatRequest;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.CheckoutResultListener;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.lottery.LotteryDetailActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.CarUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.SHA1;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_confirm_order)
public class PayOrderActivity extends BaseActivity {
    @ViewInject(R.id.confirm_order_rl_remain)
    private RelativeLayout rl_remain;
    @ViewInject(R.id.confirm_order_vb_wechat)
    private RadioButton rb_wechat;
    @ViewInject(R.id.confirm_order_vb_aliy)
    private RadioButton rb_aliy;
    @ViewInject(R.id.confirm_order_vb_union_pay)
    private RadioButton rb_union;
    @ViewInject(R.id.ll_other_pay_way)
    private LinearLayout ll_otherPayWay;
    @ViewInject(R.id.rl_redbag_discount)
    private RelativeLayout rl_redbagDiscount;
    @ViewInject(R.id.rl_real_pay)
    private RelativeLayout rl_realPay;
    //    @ViewInject(R.id.confirm_order_rl_wechat_pay)
//    private RelativeLayout rl_wechatPay;
    @ViewInject(R.id.confirm_order_iv_remain_pay_choose)
    private ImageView iv_remainPay;
    @ViewInject(R.id.confirm_order_iv_remain_pay_no_choose)
    private ImageView iv_noRemainPay;
    //    @ViewInject(R.id.confirm_order_iv_wechat_pay)
//    private ImageView iv_WechatPay;
    @ViewInject(R.id.confirm_order_tv_other_count)
    private TextView tv_otherCount;
    @ViewInject(R.id.confirm_order_total)
    private TextView tv_total;
    @ViewInject(R.id.confirm_order_vg_pay_type)
    private RadioGroup rg_payType;
    @ViewInject(R.id.confirm_order_tv_remain)
    private TextView tv_remain;
    @ViewInject(R.id.redbag_discount)
    private TextView tv_redbagDiscount;
    @ViewInject(R.id.real_pay)
    private TextView tv_realPay;
    @ViewInject(R.id.confirm_order_sn)
    private TextView tv_sn;
    @ViewInject(R.id.receiver)
    private TextView tv_receiver;
    @ViewInject(R.id.flower_count)
    private TextView tv_flowerCount;
    @ViewInject(R.id.order_flower)
    private LinearLayout ll_flower;
    @ViewInject(R.id.confirm_order_tv_countdown)
    private TextView tv_countdown;
    @ViewInject(R.id.confirm_order_remain)
    private TextView tv_remainPayCount;//余额支付金额

    private boolean isSendSuccess = false;

    private boolean isRemainPay = false;//是否余额支付
    private boolean isWechatPay = true;//是否其他方式支付
    private boolean isHasRemain = false;//有余额
    private boolean isLackCount = true;//是否余额不足
    private int totalCount = 0;//总价
    private Double remainCount = 0.0;//余额
    private ConfirmOrder confirmOrder;
    public static final int activityPayType = 1001;
    public static final int activityPayTypeFlower = 1003;
    private boolean isPaying = false;
    private Timer timer;
    private int count = 30 * 60;
    private Date date = new Date(count * 1000);
    private DateFormat format2;// = new SimpleDateFormat(getString(R.string.format_surplus_time));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.pay_order));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.pay_order));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        format2 = new SimpleDateFormat(getString(R.string.format_surplus_time));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1000);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                DialogFactory.createSelectDialog(PayOrderActivity.this, getString(R.string.exit_order_), getString(R.string.exit_pay_tips), new OnSelectListener() {
                    @Override
                    public void onSelected(boolean isSelected) {
                        if (isSelected) {
                            finishThis();
                        }
                    }
                });
            }
        };
        tv_title.setText(getString(R.string.pay_order));
        confirmOrder = (ConfirmOrder) getIntent().getSerializableExtra(FieldFinals.CONFIRM_ORDER);
        if (confirmOrder != null && confirmOrder.getOrderType() != 1) {
            ll_flower.setVisibility(View.GONE);
        } else if (confirmOrder != null && confirmOrder.getOrderType() == 1) {
            tv_receiver.setText(confirmOrder.getToUserInfo().getNickname());
            tv_flowerCount.setText(confirmOrder.getFlowerCount() + "");
            rl_redbagDiscount.setVisibility(View.GONE);
            rl_realPay.setVisibility(View.GONE);
        }
        if (confirmOrder != null && !TextUtils.isEmpty(confirmOrder.getOrderSn())) {
            if (confirmOrder.getDiscountPrice() != 0) {
                tv_redbagDiscount.setText("-" + confirmOrder.getDiscountPrice() + "元");
            } else {
                tv_redbagDiscount.setText("0元");
            }
            totalCount = (int) (confirmOrder.getTotal() - confirmOrder.getDiscountPrice());
            if (confirmOrder.getTotal() >= 1000) {
                findViewById(R.id.tv_lottery_warring).setVisibility(View.VISIBLE);
            }
            tv_total.setText(String.format(getString(R.string.format_what_price, confirmOrder.getTotal())));//totalCount + "元");format_what_price
            tv_realPay.setText(Float.valueOf(confirmOrder.getTotal() - confirmOrder.getDiscountPrice()) + "元");
            tv_sn.setText(String.format(getString(R.string.format_oder_sn_, confirmOrder.getOrderSn())));//"订单编号：" + confirmOrder.getOrderSn());
            remainCount = confirmOrder.getUserMoney();//10000.00;
            tv_remain.setText(String.format(getString(R.string.format_remain_pay_price, BigDecimal.valueOf(remainCount).toPlainString())));//"余额支付 （余额：" + BigDecimal.valueOf(remainCount).toPlainString() + "元）");
            if (remainCount > 0) {//如果有余额
                isHasRemain = true;
            } else {
                tv_otherCount.setText(String.format(getString(R.string.format_what_price, totalCount)));//totalCount + "元");
            }
            if (remainCount >= totalCount) {//余额大于总价
                tv_otherCount.setText("0.00元");
                isLackCount = false;
            } else {//余额小于总价
                if (isHasRemain) {
                    tv_remainPayCount.setText(String.format(getString(R.string.format_what_price, remainCount)));//remainCount + "元");
                    tv_otherCount.setText(String.format(getString(R.string.format_what_price, (totalCount - remainCount))));//(totalCount - remainCount) + "元");
                }
            }
            if (totalCount == 0) {
                rl_remain.setVisibility(View.GONE);
                ll_otherPayWay.setVisibility(View.GONE);
                initView(true);
                return;
            }
            initView(isHasRemain);
        }
    }

    @Override
    protected void reload() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            if (count > 0) {
                date.setTime(count * 1000);
                tv_countdown.setText(format2.format(date));
            } else {
                timer.cancel();
                DialogFactory.createDefaultDialog(PayOrderActivity.this, getString(R.string.order_be_canceled), new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finishThis();
                    }
                });
            }
        }
    };

    public void finishThis() {
        AppManager.getAppManager().finishActivities(new Class[]{InventoryActivity.class, ConfirmOrderActivity.class, LotteryDetailActivity.class});
        finishActivity();
        CarUtils.clear(this);
    }

    private void initView(boolean isHasRemain) {
        if (isHasRemain) {//有余额
            isRemainPay = true;
        } else {//没有余额
            rl_remain.setFocusable(false);
            rl_remain.setClickable(false);
            rg_payType.setFocusable(false);
            rg_payType.setClickable(false);
            isWechatPay = true;
            isRemainPay = false;
            tv_otherCount.setText(getString(R.string.format_what_price, totalCount));//totalCount + "元");
            tv_remainPayCount.setText(" ");
        }
        setRemainImage(isRemainPay);
    }

    @Event({R.id.confirm_order_tv_submit, R.id.confirm_order_rl_remain})
    private void onEvenClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_order_rl_remain:
                isRemainPay = !isRemainPay;
                setRemainImage(isRemainPay);
                break;
//            case R.id.confirm_order_rl_wechat_pay:
//                isWechatPay = !isWechatPay;
//                if (isWechatPay) {
//                    iv_WechatPay.setImageResource(R.mipmap.ico_choose);
//                } else {
//                    iv_WechatPay.setImageResource(R.mipmap.ico_nchoose);
//                }
//                break;
            case R.id.confirm_order_tv_submit:
                if (!isPaying) {
                    if (confirmOrder.getOrderType() == 0) {
                        pay("");
                    } else {
                        if (!isRemainPay) {
                            pay("");
                        } else {
                            if (!isLackCount) {//余额充足
                                DialogFactory.createCheckoutDialog(PayOrderActivity.this, new CheckoutResultListener() {
                                    @Override
                                    public void result(String psw) {
                                        pay(psw);
                                    }
                                });
                            } else {
                                pay("");
                            }

                        }
                    }
                }
                break;

        }
    }

    private void pay(String psw) {
        isPaying = true;
        RequestParams params = new RequestParams(confirmOrder.getOrderType() == 0 ? HttpFinal.CASHIER_CONFIRM : HttpFinal.CASHIER_DONATE_CONFIRM);
        params.setConnectTimeout(30 * 1000);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        if (confirmOrder.getOrderType() == 1 && !TextUtils.isEmpty(psw)) {
            SHA1 sha1 = new SHA1();
            params.addBodyParameter(FieldFinals.PASSWORD, sha1.getDigestOfString(psw.getBytes()));
        }
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SN, confirmOrder.getOrderSn());
        PreferencesUtil.setPreferences(this, FieldFinals.SN, confirmOrder.getOrderSn());
        if (isRemainPay) {
            params.addBodyParameter(FieldFinals.PAY_TYPE, "1");
        } else {
            params.addBodyParameter(FieldFinals.PAY_TYPE, "2");
        }
        switch (rg_payType.getCheckedRadioButtonId()) {
            case R.id.confirm_order_vb_wechat:
                params.addBodyParameter(FieldFinals.BANK_ID, "1");
                wxPay(params, psw);
                break;
            case R.id.confirm_order_vb_aliy:
                params.addBodyParameter(FieldFinals.BANK_ID, "2");
                aliyPay(params, psw);
                break;
            case R.id.confirm_order_vb_union_pay:
                params.addBodyParameter(FieldFinals.BANK_ID, "3");
                unionPay(params);
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
                PreferencesUtil.setPreferences(PayOrderActivity.this, FieldFinals.PAY_RESULT_URL, unionPayResult.getUrl());
                if (unionPayResult.getParams() != null && !TextUtils.isEmpty(unionPayResult.getParams().getHtml())) {
                    Intent intent = new Intent(PayOrderActivity.this, NewWebViewActivity.class);
                    if(confirmOrder.getToUserInfo()!=null){
                        intent.putExtra(FieldFinals.ACTION, confirmOrder.getClassName());
                        intent.putExtra(FieldFinals.COUNT, confirmOrder.getFlowerCount());
                        intent.putExtra(FieldFinals.RESULT, false);
                        intent.putExtra(FieldFinals.NICKNAME, confirmOrder.getToUserInfo().getNickname());
                        intent.putExtra(FieldFinals.UID, confirmOrder.getToUserInfo().getUid());
                    }
                    intent.putExtra(FieldFinals.HTML, unionPayResult.getParams().getHtml());
                    startActivity(intent);
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
                isPaying = false;
                dialog.dismiss();
            }
        });
    }

    private void aliyPay(RequestParams params, String psw) {
        HttpUtils.post(this, params, new TypeToken<Result<AliyPayResult>>() {
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
                final AliyPayResult aliyPayResult = (AliyPayResult) result.getData();
                PreferencesUtil.setPreferences(PayOrderActivity.this, FieldFinals.PAY_RESULT_URL, aliyPayResult.getUrl());
                if (aliyPayResult != null) {
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask payTask = new PayTask(PayOrderActivity.this);
                            String result = payTask.pay(aliyPayResult.getParams().getAlipay(), true);
//                            Log.e("web",aliyPayResult.getParams().getAlipay());
                            Message msg = new Message();
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
                isPaying = false;
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
                        showToast(R.string.pay_success);
                        if (confirmOrder.getOrderType() == 1) {
                            getSendFlowerResult(confirmOrder.getOrderSn());
                        } else {
                            IntentUtils.startToNewWebView(PayOrderActivity.this, PreferencesUtil.getStringPreferences(PayOrderActivity.this, FieldFinals.PAY_RESULT_URL));
                            finishThis();
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast(R.string.pay_result_confirm);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToast(R.string.pay_fail);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void wxPay(RequestParams params, String psw) {
        HttpUtils.post(this, params, new TypeToken<Result<WechatRequest>>() {
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
                GoodHappinessApplication.activityPayType = confirmOrder.getOrderType() == 0 ? PayOrderActivity.activityPayType : PayOrderActivity.activityPayTypeFlower;
                if (signParams.isUse_bank()) {
                    PayReq request = new PayReq();
                    request.appId = signParams.getParams().getAppid();
                    request.partnerId = signParams.getParams().getPartnerid();
                    request.prepayId = signParams.getParams().getPrepayid();
                    request.packageValue = signParams.getParams().getPackageX();
                    request.nonceStr = signParams.getParams().getNonce_str();
                    request.timeStamp = signParams.getParams().getTimestamp();
                    request.sign = signParams.getParams().getSign();
                    GoodHappinessApplication.wxapi.sendReq(request);
                    PreferencesUtil.setPreferences(PayOrderActivity.this, FieldFinals.PAY_RESULT_URL, signParams.getUrl());
                    if (confirmOrder.getOrderType() == 1) {
                        PreferencesUtil.setPreferences(PayOrderActivity.this, FieldFinals.CONFIRM_ORDER, confirmOrder);
                    }
//                    finishThis();
                } else {
                    if (confirmOrder.getOrderType() == 0) {
//                        Intent intent = new Intent(PayOrderActivity.this, PayResultActivity.class);
//                        intent.putExtra(FieldFinals.IS_USE_BANK, false);
//                        startActivity(intent);
                        IntentUtils.startToNewWebView(PayOrderActivity.this, signParams.getUrl());
                        finishThis();
                    } else {
                        getSendFlowerResult(confirmOrder.getOrderSn());
                    }
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
                isPaying = false;
                dialog.dismiss();
            }
        });
    }

    private void getSendFlowerResult(String sn) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_RESULT);
        params.setConnectTimeout(30 * 1000);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SN, sn);
        HttpUtils.post(this, params, new TypeToken<Result<PayFlowerResult>>() {
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
                    PayFlowerResult payResult = (PayFlowerResult) result.getData();
                    isSendSuccess = payResult.getPayStatus() == 3 ? true : false;
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
                IntentUtils.sendFlowerSendBroadcastWith(PayOrderActivity.this, confirmOrder.getClassName(), confirmOrder.getFlowerCount(), isSendSuccess, confirmOrder.getToUserInfo().getNickname(), confirmOrder.getToUserInfo().getUid());
                finishThis();
            }
        });
    }

    private void setRemainImage(boolean isRemain) {
        if (!isLackCount) {//余额充足
            if (isRemain) {
                iv_remainPay.setVisibility(View.VISIBLE);
                iv_noRemainPay.setVisibility(View.GONE);
                rg_payType.setVisibility(View.GONE);
                tv_remainPayCount.setText(String.format(getString(R.string.format_what_price, totalCount)));//totalCount + "元");
                tv_otherCount.setText("");
            } else {
                iv_remainPay.setVisibility(View.GONE);
                iv_noRemainPay.setVisibility(View.VISIBLE);
                PayType payType=confirmOrder.getPayType();
                if(payType==null ||
                        (TextUtils.isEmpty(payType.getWx()) && TextUtils.isEmpty(payType.getAliPay()) && TextUtils.isEmpty(payType.getUnionPay()))){
                    rg_payType.setVisibility(View.GONE);
                }else{
                    rg_payType.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(payType.getWx())){
                        rb_wechat.setVisibility(View.VISIBLE);
                        rb_wechat.setText(payType.getWx());
                    }else{
                        rb_wechat.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(payType.getAliPay())){
                        rb_aliy.setVisibility(View.VISIBLE);
                        rb_aliy.setText(payType.getAliPay());
                    }else{
                        rb_aliy.setVisibility(View.GONE);
                    }
                    if(!TextUtils.isEmpty(payType.getUnionPay())){
                        rb_union.setVisibility(View.VISIBLE);
                        rb_union.setText(payType.getUnionPay());
                    }else{
                        rb_union.setVisibility(View.GONE);
                    }
                }
                tv_remainPayCount.setText("");
                tv_otherCount.setText(String.format(getString(R.string.format_what_price, totalCount)));
            }
        } else {//余额不足
            if (isRemain) {
                iv_remainPay.setVisibility(View.VISIBLE);
                iv_noRemainPay.setVisibility(View.GONE);
                tv_remainPayCount.setText(String.format(getString(R.string.format_what_price, remainCount)));
                tv_otherCount.setText(String.format(getString(R.string.format_what_price, (totalCount - remainCount))));
            } else {
                iv_remainPay.setVisibility(View.GONE);
                iv_noRemainPay.setVisibility(View.VISIBLE);
                tv_remainPayCount.setText(" ");
                tv_otherCount.setText(String.format(getString(R.string.format_what_price, totalCount)));
            }
            PayType payType=confirmOrder.getPayType();
            if(payType==null ||
                    (TextUtils.isEmpty(payType.getWx()) && TextUtils.isEmpty(payType.getAliPay()) && TextUtils.isEmpty(payType.getUnionPay()))){
                rg_payType.setVisibility(View.GONE);
            }else{
                rg_payType.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(payType.getWx())){
                    rb_wechat.setVisibility(View.VISIBLE);
                    rb_wechat.setText(payType.getWx());
                }else{
                    rb_wechat.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(payType.getAliPay())){
                    rb_aliy.setVisibility(View.VISIBLE);
                    rb_aliy.setText(payType.getAliPay());
                }else{
                    rb_aliy.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(payType.getUnionPay())){
                    rb_union.setVisibility(View.VISIBLE);
                    rb_union.setText(payType.getUnionPay());
                }else{
                    rb_union.setVisibility(View.GONE);
                }
            }
        }
        if (rg_payType.getVisibility()==View.VISIBLE) {
            PayType payType=confirmOrder.getPayType();
            if(!TextUtils.isEmpty(payType.getWx())) {
                rb_wechat.setChecked(true);
            }else if(!TextUtils.isEmpty(payType.getAliPay())){
                rb_aliy.setChecked(true);
            }else if(!TextUtils.isEmpty(payType.getUnionPay())){
                rb_union.setChecked(true);
            }
        }
    }
}