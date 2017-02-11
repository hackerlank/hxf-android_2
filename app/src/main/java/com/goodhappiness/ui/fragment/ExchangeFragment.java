package com.goodhappiness.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.ExchangeRecord;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.GestureListener;
import com.goodhappiness.dao.OnGestureScrollListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.order.MsgConfirmActivity;
import com.goodhappiness.ui.social.PersonActivity;
import com.goodhappiness.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_exchange)
public class ExchangeFragment extends BaseFragment {
    @ViewInject(R.id.fragment_exchange_record_lv)
    private ListView lv;
    @ViewInject(R.id.fragment_post_publish_ll_no_photo)
    private LinearLayout ll;

    private String uid;
    private CommonAdapter<ExchangeRecord.ListBean> adapter;
    private List<ExchangeRecord.ListBean> list = new ArrayList<>();
    public int page = 1;
    private int firstPosition = 0;
    private GestureDetector mGesture = null;
    private boolean hasMore = true;

    public ExchangeFragment() {
        super(R.layout.fragment_exchange);
    }

    public static ExchangeFragment newInstance(String uid) {
        ExchangeFragment fragment = new ExchangeFragment();
        Bundle args = new Bundle();
        args.putString(FieldFinals.UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    protected void reload() {
        onRefresh(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.exchange_record)); //统计页面，"MainScreen"为页面名称，可自定义
        page =1;
        handler.sendEmptyMessageDelayed(0,2000);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.exchange_record));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(FieldFinals.UID);
        }
    }


    @Override
    protected void setData() {
        initAdapter();
        initGesture();

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onRefresh(1);
        }
    };

    private void initGesture() {
        mGesture = new GestureDetector(getActivity(), new GestureListener(new OnGestureScrollListener() {
            @Override
            public void onUpScroll() {
                PersonActivity personActivity = (PersonActivity) getActivity();
                personActivity.scrollToBottom();
                if(!personActivity.isTop)
                onRefresh(2);
            }

            @Override
            public void onDownScroll() {
                if(firstPosition==0){
                    PersonActivity personActivity = (PersonActivity) getActivity();
                    personActivity.scrollToTop();
                }
            }
            @Override
            public void onLeftScroll() {
            }

            @Override
            public void onRightScroll() {
                if(getActivity() instanceof PersonActivity){
                    if(((PersonActivity)getActivity()).vp!=null){
                        ((PersonActivity)getActivity()).vp.setCurrentItem(2);
                    }
                }
            }
        }));
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstPosition = firstVisibleItem;
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<ExchangeRecord.ListBean>(getActivity(), list, R.layout.layout_list_exchange_record) {
            @Override
            public void convert(ViewHolder helper, ExchangeRecord.ListBean item,final int position) {
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
                ImageView imageView = helper.getView(R.id.layout_list_exchange_record_iv);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                if(!uid.equals(getUid()+"")){
                    helper.setVisibility(R.id.layout_exchange_record_rl_status,View.GONE);
                }else{
                    helper.setVisibility(R.id.layout_exchange_record_rl_status,View.VISIBLE);
                }
                helper.setOnclickListener(R.id.layout_list_exchange_record_iv_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFactory.createShareDialog(3, getActivity(), null, new String[]{getDid(), getSid(), FieldFinals.EXCHANGE, String.valueOf(list.get(position).getProductId())});
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(uid.equals(getUid()+"")) {
                    Intent intent = new Intent(getActivity(), MsgConfirmActivity.class);
                    intent.putExtra(FieldFinals.EX_ID, list.get(position).getExId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.USER_EXCHANGE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        if(TextUtils.isEmpty(uid)){
            params.addBodyParameter(FieldFinals.SID, getSid());
        }else{
            params.addBodyParameter(FieldFinals.SID, "");
            params.addBodyParameter(FieldFinals.USER_ID, uid);
        }
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<ExchangeRecord>>() {
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
                ExchangeRecord exchangeRecord = (ExchangeRecord) result.getData();
                if (page == 1) {
                    list.clear();
                }
                list.addAll(exchangeRecord.getList());
                if(list.size()>0){
                    ll.setVisibility(View.GONE);
                }else{
                    ll.setVisibility(View.VISIBLE);
                }
                if (exchangeRecord.getMore() != 1) {
                    hasMore = false;
                }
                adapter.notifyDataSetChanged();
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
            }
        });
    }
    public void onRefresh(int direction) {
        if (direction == 1) {
            page = 1;
            initList();
        } else if (direction == 2) {
            if(hasMore){
                page++;
                initList();
            }else{
                showToast(R.string.list_no_more);
            }
        }
    }
}
