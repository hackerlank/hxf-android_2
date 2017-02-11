package com.goodhappiness.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.Address;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.ExchangeBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.personal.AddAddressActivity;
import com.goodhappiness.ui.personal.AddressListActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.TimeAxis;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_msg_confirm)
public class MsgConfirmActivity extends BaseActivity {
    @ViewInject(R.id.msg_confirm_ta1)
    private TimeAxis ta1;
    @ViewInject(R.id.msg_confirm_ta2)
    private TimeAxis ta2;
    @ViewInject(R.id.msg_confirm_ta3)
    private TimeAxis ta3;
    @ViewInject(R.id.msg_confirm_ta4)
    private TimeAxis ta4;
    @ViewInject(R.id.msg_confirm_ta5)
    private TimeAxis ta5;
    private int count = 0;
    @ViewInject(R.id.msg_confirm_ll_address_ok)
    private LinearLayout llAddressOK;
    @ViewInject(R.id.msg_confirm_ll_address_msg)
    private LinearLayout llAddressMsg;
    @ViewInject(R.id.msg_confirm_ll_ems_msg)
    private LinearLayout llEsmMsg;
    @ViewInject(R.id.msg_confirm_tv_receiver)
    private TextView tv_receiver;
    @ViewInject(R.id.msg_confirm_tv_receiver_phone)
    private TextView tv_receiverPhone;
    @ViewInject(R.id.msg_confirm_tv_receiver_address)
    private TextView tv_receiverAddress;
    @ViewInject(R.id.msg_confirm_tv_address_ok_tab)
    private TextView tv_addressOkTab;
    @ViewInject(R.id.msg_confirm_tv_ems_tab)
    private TextView tv_EmsTab;
    @ViewInject(R.id.msg_confirm_tv_goods_ok_tab)
    private TextView tv_GoodsOkTab;
    @ViewInject(R.id.msg_confirm_tv_exchange_ok_tab)
    private TextView tv_ExchangeOkTab;
    @ViewInject(R.id.msg_confirm_tv_user_name)
    private TextView tvName;
    @ViewInject(R.id.msg_confirm_tv_user_phone)
    private TextView tvPhone;
    @ViewInject(R.id.msg_confirm_tv_address)
    private TextView tvAddress;
    @ViewInject(R.id.msg_confirm_tv_ems_name)
    private TextView tvEmsName;
    @ViewInject(R.id.msg_confirm_tv_ems_order_sn)
    private TextView tvEmsSn;
    @ViewInject(R.id.msg_confirm_tv_confirm_address)
    private TextView tvConfirmAddress;
    @ViewInject(R.id.msg_confirm_tv_confirm_ok)
    private TextView tvConfirmOk;//确认收货
    @ViewInject(R.id.msg_confirm_tv_address_ok_time)
    private TextView addressConfirmTime;//确认地址时间
    @ViewInject(R.id.layout_list_msg_confirm_tv_express_time)
    private TextView expressTime;//派发时间
    @ViewInject(R.id.layout_list_msg_confirm_tv_address_time)
    private TextView addressTime;//确认地址时间
    @ViewInject(R.id.layout_list_msg_confirm_tv_confirm_time)
    private TextView confirmTime;//确认地址时间
    @ViewInject(R.id.name)
    private TextView tvGoodsName;//确认地址时间
    @ViewInject(R.id.msg_confirm_pic)
    private ImageView ivPic;//确认地址时间
    private Address address;
    private long addressId = -1;
    private static final String ADDRESS_OK = "addressOk";
    private static final String CONFIRM_OK = "confirmOk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.msg_confirm));
        if(getIntent()!=null){
            initData();
        }

    }

    @Override
    protected void reload() {
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferencesUtil.setPreferences(this, FieldFinals.ADDRESS_BEAN, null);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.msg_confirm));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.msg_confirm));
        MobclickAgent.onResume(this);
        address = PreferencesUtil.getPreferences(this,FieldFinals.ADDRESS_BEAN);
        if(address!=null){
            Log.e("k_",address.toString());
            tv_receiver.setText(address.getUsername());
            tv_receiverAddress.setText(address.getAddress());
            tv_receiverPhone.setText(address.getMobile());
            addressId = address.getAddressId();
        }
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpFinal.USER_EXCHANGE_DETAIL);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.EX_ID, getIntent().getLongExtra(FieldFinals.EX_ID, 0) + "");
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(this,params, new TypeToken<Result<ExchangeBean>>() {
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
                ExchangeBean exchangeBean = (ExchangeBean) result.getData();
                if (exchangeBean != null && exchangeBean.getStatus() != 0) {
                    initData(exchangeBean);
                }
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
            }
        });
    }

    private void initData(ExchangeBean exchangeBean) {
        setStatus(exchangeBean.getStatus());
        addressConfirmTime.setText(exchangeBean.getAddressOkTime());
        expressTime.setText(exchangeBean.getExpressTime());
        addressTime.setText(exchangeBean.getConfirmTime());
        confirmTime.setText(exchangeBean.getExchangeTime());
        tvName.setText(exchangeBean.getUsername());
        tvPhone.setText(exchangeBean.getMobile());
        tvAddress.setText(exchangeBean.getAddress());
        tvEmsName.setText(exchangeBean.getExpress_company());
        tvEmsSn.setText(exchangeBean.getExpress_no());
        tvGoodsName.setText(exchangeBean.getProductName());
        tv_receiver.setText(exchangeBean.getUsername());
        tv_receiverPhone.setText(exchangeBean.getMobile());
        tv_receiverAddress.setText(exchangeBean.getAddress());
        displayImage(ivPic, exchangeBean.getProductImage());
    }

    private void setStatus(int status) {
        llAddressMsg.setVisibility(View.VISIBLE);
        llEsmMsg.setVisibility(View.VISIBLE);
        switch (status) {
            case 1:
                ta1.setStatus(TimeAxis.STATUS_PAST);
                ta2.setStatus(TimeAxis.STATUS_NOW);
                llAddressMsg.setVisibility(View.GONE);
                llEsmMsg.setVisibility(View.GONE);
                tv_addressOkTab.setTextColor(getTheColor(R.color.black_333_text));
                tvConfirmOk.setVisibility(View.GONE);
                break;
            case 2:
                ta1.setStatus(TimeAxis.STATUS_PAST);
                ta2.setStatus(TimeAxis.STATUS_PAST);
                ta3.setStatus(TimeAxis.STATUS_NOW);
                tv_addressOkTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_EmsTab.setTextColor(getTheColor(R.color.black_333_text));
                llAddressOK.setVisibility(View.GONE);
                tvConfirmOk.setVisibility(View.GONE);
                break;
            case 3:
                ta1.setStatus(TimeAxis.STATUS_PAST);
                ta2.setStatus(TimeAxis.STATUS_PAST);
                ta3.setStatus(TimeAxis.STATUS_PAST);
                ta4.setStatus(TimeAxis.STATUS_NOW);
                tv_addressOkTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_EmsTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_GoodsOkTab.setTextColor(getTheColor(R.color.black_333_text));
                llAddressOK.setVisibility(View.GONE);
                break;
            case 4:
                ta1.setStatus(TimeAxis.STATUS_PAST);
                ta2.setStatus(TimeAxis.STATUS_PAST);
                ta3.setStatus(TimeAxis.STATUS_PAST);
                ta4.setStatus(TimeAxis.STATUS_PAST);
                ta5.setStatus(TimeAxis.STATUS_NOW);
                tv_addressOkTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_EmsTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_GoodsOkTab.setTextColor(getTheColor(R.color.black_333_text));
                tv_ExchangeOkTab.setTextColor(getTheColor(R.color.black_333_text));
                llAddressOK.setVisibility(View.GONE);
                tvConfirmOk.setVisibility(View.GONE);
                break;
        }
    }

    @Event({R.id.msg_confirm_tv_confirm_ok,R.id.msg_confirm_use_other,R.id.msg_confirm_tv_confirm_address})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.msg_confirm_use_other:
                startToAddressList();
                break;
            case R.id.msg_confirm_tv_confirm_address:
                if(TextUtils.isEmpty(tv_receiver.getText().toString())){
                    startToAddressList();
                }else{
                    confirmAddress(ADDRESS_OK);
                }
                break;
            case R.id.msg_confirm_tv_confirm_ok:
                confirmAddress(CONFIRM_OK);
                break;

        }
    }

    private void startToAddressList() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra(AddAddressActivity.ACTIVITY_TYPE,AddAddressActivity.ACTIVITY_TYPE_CONFIRM);
        startActivity(intent);
    }

    private void confirmAddress(String action) {
        RequestParams params = new RequestParams(HttpFinal.USER_EXCHANGE_STATUS_CONFIRM);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.ACTION, action);
        params.addBodyParameter(FieldFinals.EX_ID, getIntent().getLongExtra(FieldFinals.EX_ID, 0) + "");
        if(action.equals(ADDRESS_OK)){
            if(addressId!=-1){
                params.addBodyParameter(FieldFinals.ADDRESS_ID, addressId + "");
            }
        }
        HttpUtils.post(this,params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onSuccess(Result result) {
                GoodHappinessApplication.isNeedRefresh = true;
                initData();
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

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                dialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }

//    private void initAdapter() {
//        list.add(new OrderStatus(0));
//        list.add(new OrderStatus(0));
//        list.add(new OrderStatus(1));
//        list.add(new OrderStatus(2));
//        list.add(new OrderStatus(2));
//        adapter = new CommonAdapter<OrderStatus>(this,list,R.layout.layout_list_msg_confirm) {
//            @Override
//            public void convert(ViewHolder helper, OrderStatus item, int position) {
//                View upView = helper.getView(R.id.layout_list_msg_confirm_black_view_up);
//                View downView = helper.getView(R.id.layout_list_msg_confirm_black_view_down);
//                View buttonView = helper.getView(R.id.layout_list_msg_confirm_view_button);
//                buttonView.setVisibility(View.VISIBLE);
//                if(position==0){
//                    upView.setVisibility(View.GONE);
//                    downView.setVisibility(View.VISIBLE);
//                }else if(position==list.size()-1){
//                    upView.setVisibility(View.VISIBLE);
//                    downView.setVisibility(View.GONE);
//                    buttonView.setVisibility(View.GONE);
//                }else{
//                    upView.setVisibility(View.VISIBLE);
//                    downView.setVisibility(View.VISIBLE);
//                }
//                switch (item.getStatus()){
//                    case 0:
//                        helper.setImageResource(R.id.layout_list_msg_confirm_iv_point,R.mipmap.point_no_over);
//                        break;
//                    case 1:
//                        helper.setImageResource(R.id.layout_list_msg_confirm_iv_point,R.mipmap.point_new);
//                        break;
//                    case 2:
//                        helper.setImageResource(R.id.layout_list_msg_confirm_iv_point,R.mipmap.point_over);
//                        break;
//                }
//            }
//        };
//        lv.setAdapter(adapter);
//    }
}
