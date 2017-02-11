package com.goodhappiness.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Address;
import com.goodhappiness.bean.AddressList;
import com.goodhappiness.bean.Result;
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

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_address_list)
public class AddressListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    @ViewInject(R.id.address_list_lv)
    private ListView lv;
    @ViewInject(R.id.address_list_ll_add)
    private RelativeLayout rl_add;
    @ViewInject(R.id.address_list_srl)
    private BGARefreshLayout srl;

    public static AddressListActivity instance;
    private CommonAdapter<Address> adapter;
    private List<Address> list = new ArrayList<>();
    private int page = 1;
    private boolean isCanLoad = true;
    private boolean isChooseAddress = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.address_list));
        MobclickAgent.onResume(this);
        initList(true);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.address_list));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    protected void setData() {
        initView();
        if(getIntent()!=null&&getIntent().getIntExtra(AddAddressActivity.ACTIVITY_TYPE,-1)==AddAddressActivity.ACTIVITY_TYPE_CONFIRM){
            isChooseAddress = true;
        }
        instance = this;
        tv_title.setText(R.string.address_list);
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        tv_right.setText(R.string.add_address);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        initAdapter();

    }

    @Override
    protected void reload() {
        initList(true);
    }

    private void initList(final boolean isFirst) {
        RequestParams params = new RequestParams(HttpFinal.USER_ADDRESS);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(this,params, new TypeToken<Result<AddressList>>() {
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
                AddressList addressList = (AddressList) result.getData();
                if (isFirst) {
                    list.clear();
                }
                if (addressList.getMore() == 1) {
                    isCanLoad = true;
                } else {
                    isCanLoad = false;
                }
                list.addAll(addressList.getList());
                if (list.size() > 0) {
                    rl_add.setVisibility(View.GONE);
                } else {
                    rl_add.setVisibility(View.VISIBLE);
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
                endRefreshing();
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
        adapter = new CommonAdapter<Address>(this, list, R.layout.layout_list_address) {
            @Override
            public void convert(ViewHolder helper, Address item, int position) {
                helper.setText(R.id.layout_list_address_tv_name,item.getUsername());
                helper.setText(R.id.layout_list_address_tv_phone,item.getMobile());
                helper.setText(R.id.layout_list_address_tv_address,item.getAddressHead()+item.getAddress());
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddressListActivity.this,AddAddressActivity.class);
                if(isChooseAddress){
                    intent.putExtra(AddAddressActivity.ACTIVITY_TYPE,AddAddressActivity.ACTIVITY_TYPE_CONFIRM);
                }else{
                    intent.putExtra(AddAddressActivity.ACTIVITY_TYPE,AddAddressActivity.ACTIVITY_TYPE_MODIFY);
                }
                intent.putExtra(FieldFinals.ADDRESS_,list.get(position));
                startTheActivity(intent);
            }
        });
    }

    @Event({R.id.common_right_text,R.id.address_list_tv_add})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.common_right_text:
            case R.id.address_list_tv_add:
                Intent intent = new Intent(this,AddAddressActivity.class);
                if(isChooseAddress){
                    intent.putExtra(AddAddressActivity.ACTIVITY_TYPE,AddAddressActivity.ACTIVITY_TYPE_CONFIRM);
                }else{
                    intent.putExtra(AddAddressActivity.ACTIVITY_TYPE,AddAddressActivity.ACTIVITY_TYPE_ADD);
                }
                startTheActivity(intent);
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
