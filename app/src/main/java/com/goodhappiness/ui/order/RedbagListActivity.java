package com.goodhappiness.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Redbag;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SubmitOrder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_redbag_list)
public class RedbagListActivity extends BaseActivity{
    public static final int REDBAG_RESULT_CODE = 0;
    @ViewInject(R.id.charge_record_lv)
    private ListView lv;
    @ViewInject(R.id.charge_record_ll_no_charge)
    private LinearLayout ll;

    private CommonAdapter<Redbag> adapter;
    private List<Redbag> list = new ArrayList<>();

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
        MobclickAgent.onPageEnd(getString(R.string.choose_redbag));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.choose_redbag));
        tv_right.setText(getString(R.string.don_use_redbag));
        tv_right.setTextColor(getTheColor(R.color.join_red));
        initAdapter();
//            ll.setVisibility(View.VISIBLE);
    }

    @Override
    protected void reload() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.choose_redbag));
        MobclickAgent.onResume(this);
    }


    private void initAdapter() {
        List<Redbag> redbags = getIntent().getParcelableArrayListExtra(FieldFinals.REDBAG_LIST);
        if(redbags!=null){
            list.addAll(redbags);
            if(redbags.size()==0){
                ll.setVisibility(View.VISIBLE);
            }
        }
        adapter = new CommonAdapter<Redbag>(this, list, R.layout.layout_list_redbag_item) {
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getRedbagDiscount(position);
            }
        });
    }

    private void getRedbagDiscount(final int position) {
        if(list.get(position).getUseStatus()==0){
            if(!TextUtils.isEmpty(PreferencesUtil.getStringPreferences(this,FieldFinals.ORDER_PERIOD))){
                RequestParams params = new RequestParams(HttpFinal.CART_SUBMIT);
                params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
                params.addBodyParameter(FieldFinals.SID, getSid());
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(String.valueOf(list.get(position).getRid()));
                Log.e("json",jsonArray.toString());
                params.addBodyParameter(FieldFinals.REDBAGS,jsonArray.toString());
                params.addBodyParameter(FieldFinals.PERIOD, PreferencesUtil.getStringPreferences(this,FieldFinals.ORDER_PERIOD));
                HttpUtils.post(this,params, new TypeToken<Result<SubmitOrder>>() {
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
                        Intent intent = new Intent();
                        intent.putExtra(FieldFinals.POSITION,position);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(FieldFinals.SUBMIT_ORDER, (SubmitOrder) result.getData());
                        intent.putExtras(bundle);
                        setResult(REDBAG_RESULT_CODE,intent);
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
    }

    @Event({R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right_text:
                Intent intent = new Intent();
                intent.putExtra(FieldFinals.POSITION,-2);
                setResult(REDBAG_RESULT_CODE,intent);
                finishActivity();
                break;
        }
    }
}