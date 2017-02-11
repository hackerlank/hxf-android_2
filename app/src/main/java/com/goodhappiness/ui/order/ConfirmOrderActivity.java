package com.goodhappiness.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Car;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.Redbag;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SubmitOrder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.CarUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.widget.MyListView;
import com.goodhappiness.widget.StretchPanel;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单
 */
@ContentView(R.layout.activity_submit_order)
public class ConfirmOrderActivity extends BaseActivity {
    public static final int CONFIRM_REQUEST_CODE = 10;
    @ViewInject(R.id.submit_order_sp)
    private StretchPanel sp;
    @ViewInject(R.id.submit_order_tv_total)
    private TextView tv_total;
    @ViewInject(R.id.submit_order_total)
    private TextView tv_total2;
    @ViewInject(R.id.usable_count)
    private TextView tv_usableCount;
    @ViewInject(R.id.redbag_name)
    private TextView tv_redbagName;
    @ViewInject(R.id.tv_redbag_discount)
    private TextView tv_redbagDiscount;
    @ViewInject(R.id.rl_choose_redbag)
    private RelativeLayout rl_chooseRedbag;
    @ViewInject(R.id.rl_redbag_discount)
    private RelativeLayout rl_redbagDiscount;

    private View contentView, stretchView;
    private ImageView arrowView;
    private CommonAdapter<Car> adapter;
    private List<Car> list = new ArrayList<>();
    private List<Redbag> availableRedbags = new ArrayList<>();
    private List<Redbag> notAvailableRedbags = new ArrayList<>();
    private List<Redbag> allRedbags = new ArrayList<>();
    private SubmitOrder submitOrder;
    private Redbag mRedbag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.confirm_order));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.confirm_order));
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.confirm_order));
        submitOrder = getIntent().getExtras().getParcelable(FieldFinals.SUBMIT_ORDER);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                finishActivity();
            }
        };
        if (submitOrder != null)
            initSp();
    }

    @Override
    protected void reload() {

    }


    private void initSp() {
        contentView = View.inflate(this, R.layout.layout_stretch_submit_order_head, null);
        TextView textView = (TextView) contentView.findViewById(R.id.layout_stretch_submit_order_head_tv_count);
        textView.setText(submitOrder.getTotal() + "");
        tv_total.setText("￥" + (submitOrder.getFees()-submitOrder.getDiscountPrice()));
        tv_total2.setText(submitOrder.getFees() + ".00元");
        if (submitOrder.getAvaliRedBag() != null&&submitOrder.getAvaliRedBag().size()>0) {
            tv_usableCount.setText(submitOrder.getAvaliRedBag().size() + "个可用");
            if (submitOrder.getDiscountPrice() != 0) {
                tv_redbagName.setText("立減" + submitOrder.getDiscountPrice() + "元");
                mRedbag = submitOrder.getAvaliRedBag().get(0);
                setRedbagTv();
            }else{
                tv_usableCount.setVisibility(View.GONE);
                tv_redbagName.setText("暂无可用红包");
                rl_redbagDiscount.setVisibility(View.GONE);
            }
        }
        stretchView = View.inflate(this, R.layout.layout_stretch_submit_order, null);
        MyListView lv = (MyListView) stretchView.findViewById(R.id.layout_stretch_submit_order_lv);
        list.addAll(submitOrder.getOrder());
        if(submitOrder.getAvaliRedBag()!=null)
        availableRedbags.addAll(submitOrder.getAvaliRedBag());
        if(submitOrder.getNotAvaliRedBag()!=null)
        notAvailableRedbags.addAll(submitOrder.getNotAvaliRedBag());
        allRedbags.addAll(availableRedbags);
        allRedbags.addAll(notAvailableRedbags);
        adapter = new CommonAdapter<Car>(this, list, R.layout.layout_list_submit_order) {
            @Override
            public void convert(ViewHolder helper, Car item, int position) {
                helper.setText(R.id.layout_list_submit_order_tv_name, item.getInfo().getGoods().getName());
                helper.setText(R.id.layout_list_submit_order_tv_period, "(" + item.getInfo().getPeriod() + "期)");
                helper.setText(R.id.layout_list_submit_order_tv_count, item.getNum() + "");
            }
        };
        lv.setAdapter(adapter);
        arrowView = (ImageView) contentView.findViewById(R.id.layout_stretch_submit_order_head_iv_arrow);
        contentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (sp.isStretchViewOpened()) {
                    sp.closeStretchView();
                } else {
                    sp.openStretchView();
                }
            }
        });
        sp.setStretchView(stretchView);
        sp.setContentView(contentView);
        sp.setOnStretchListener(new StretchPanel.OnStretchListener() {
            @Override
            public void onStretchFinished(boolean isOpened) {
                Animation animation = AnimationUtils.loadAnimation(ConfirmOrderActivity.this, R.anim.arrowrote);
                if (isOpened) {
                    arrowView.setImageResource(R.mipmap.btn_main_up);
                    arrowView.startAnimation(animation);
                } else {
                    arrowView.setImageResource(R.mipmap.btn_main_down);
                    arrowView.startAnimation(animation);
                }
            }
        });
    }

    private void setRedbagTv() {
        if(submitOrder.getDiscountPrice()==0){
            tv_redbagDiscount.setText("-" + submitOrder.getFees() + "元");
            tv_total.setText("￥0");
        }else{
            tv_redbagDiscount.setText("-" + submitOrder.getDiscountPrice() + "元");
            tv_total.setText("￥"+(submitOrder.getFees()-submitOrder.getDiscountPrice()));
        }
    }

    @Event({R.id.submit_order_tv_submit, R.id.rl_choose_redbag})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choose_redbag:
                Intent intent = new Intent(this, RedbagListActivity.class);
                intent.putParcelableArrayListExtra(FieldFinals.REDBAG_LIST, (ArrayList<? extends Parcelable>) allRedbags);
                startActivityForResult(intent, CONFIRM_REQUEST_CODE);
                break;
            case R.id.submit_order_tv_submit:
                RequestParams params = new RequestParams(HttpFinal.CONFIRM);
                params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                params.addBodyParameter(FieldFinals.SID, getSid());
                if(mRedbag!=null){
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(String.valueOf(mRedbag.getRid()));
                    Log.e("json",jsonArray.toString());
                    params.addBodyParameter(FieldFinals.REDBAGS,jsonArray.toString());
                }
                HttpUtils.post(this, params, new TypeToken<Result<ConfirmOrder>>() {
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
                        CarUtils.clear(ConfirmOrderActivity.this);
                        Intent intent = new Intent(ConfirmOrderActivity.this, PayOrderActivity.class);
                        ConfirmOrder confirmOrder = (ConfirmOrder) result.getData();
                        confirmOrder.setOrderType(0);
                        intent.putExtra(FieldFinals.CONFIRM_ORDER, confirmOrder);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable(FieldFinals.CONFIRM_ORDER, (ConfirmOrder) result.getData());
//                        intent.putExtras(bundle);
                        startTheActivity(intent);
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
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == CONFIRM_REQUEST_CODE) {
            submitOrder = data.getExtras().getParcelable(FieldFinals.SUBMIT_ORDER)!=null? (SubmitOrder) data.getExtras().getParcelable(FieldFinals.SUBMIT_ORDER) :submitOrder;
            int position = data.getIntExtra(FieldFinals.POSITION, -1);
            if (position != -1&&position != -2) {
                setRedbagData(position);
                return;
            }
            if (position == -2) {
                mRedbag = null;
                tv_redbagName.setText("不使用红包");
                tv_redbagDiscount.setText("0元");
                tv_total.setText("￥" + submitOrder.getFees());
            }
        }
    }

    private void setRedbagData(int position) {
        if (availableRedbags.size() >= position) {
            mRedbag = availableRedbags.get(position);
            tv_redbagName.setText("立減" + submitOrder.getDiscountPrice() + "元");
            setRedbagTv();
        }
    }
}
