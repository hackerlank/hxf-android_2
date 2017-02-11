package com.goodhappiness.ui.fragment;

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

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.WinRecord;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.GestureListener;
import com.goodhappiness.dao.OnGestureScrollListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.social.PersonActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_win_record)
public class WinRecordFragment extends BaseFragment {
    @ViewInject(R.id.fragment_win_record_lv)
    private ListView lv;
    @ViewInject(R.id.fragment_post_publish_ll_no_photo)
    private LinearLayout ll;

    private String uid;
    private CommonAdapter<WinRecord.ListBean> adapter;
    private List<WinRecord.ListBean> list = new ArrayList<>();
    public int page = 1;
    private int firstPosition = 0;
    private GestureDetector mGesture = null;
    private boolean hasMore = true;

    public WinRecordFragment() {
        super(R.layout.fragment_win_record);
    }

    public static WinRecordFragment newInstance(String uid) {
            WinRecordFragment fragment = new WinRecordFragment();
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
        page =1;
        handler.sendEmptyMessageDelayed(0,1500);
        MobclickAgent.onPageStart(getResString(R.string.win_record));
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.win_record));
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
        handler.sendEmptyMessageDelayed(0,1500);
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
        adapter = new CommonAdapter<WinRecord.ListBean>(getActivity(), list, R.layout.layout_list_win_record) {
            @Override
            public void convert(ViewHolder helper, WinRecord.ListBean item,final int position) {
                helper.setText(R.id.layout_list_win_record_tv_name, item.getName());
                helper.setText(R.id.layout_list_win_record_tv_num, item.getPeriod() + "");
                helper.setText(R.id.layout_list_win_record_tv_price, item.getPrice() + "人次");
                helper.setText(R.id.layout_list_win_record_win_tv_number, item.getLuckyCode() + "");
                helper.setText(R.id.layout_list_win_record_tv_join_count, item.getOwnerCost() + "");
                helper.setText(R.id.layout_list_win_record_tv_time, item.getCalcTime());
                helper.setText(R.id.layout_list_win_record_num, item.getNum()+"张");
//                if(item.getGoodsType()==1){
//                    helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_ptlq);
//                }else{
//                    helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_cjlq);
//                }
                ImageView imageView = helper.getView(R.id.layout_list_win_record_iv);
                if (item.getPic() != null && item.getPic().size() > 0) {
                    helper.loadImage(R.id.layout_list_win_record_iv, item.getPic().get(0));//,new ImageSize(374,272)
                    imageView.setPadding(0,0,0,0);
                }else{
                    imageView.setImageResource(R.mipmap.img_mrjz);
//                    imageView.setPadding(50,20,50,20);
//                    if(item.getGoodsType()==1){
//                        helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_ptlq);
//                    }else{
//                        helper.setImageResource(R.id.layout_list_win_record_iv,R.mipmap.img_cjlq);
//                    }
                }
                helper.setOnclickListener(R.id.layout_list_win_record_iv_share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFactory.createShareDialog(3, getActivity(), null, new String[]{getDid(), getSid(), FieldFinals.AWARD, String.valueOf(list.get(position).getPeriod())});
                    }
                });
                helper.setOnclickListener(R.id.buy_goods, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.startToShop(getActivity());
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startToLotteryDetail(getActivity(), list.get(position).getPeriod());
            }
        });
    }
    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.PERIODS_AWARD);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        if(TextUtils.isEmpty(uid)){
            params.addBodyParameter(FieldFinals.SID, getSid());
        }else{
            params.addBodyParameter(FieldFinals.SID, "");
            params.addBodyParameter(FieldFinals.USER_ID, uid);
        }
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<WinRecord>>() {
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
                WinRecord winRecord = (WinRecord) result.getData();
                if (page==1) {
                    list.clear();
                }
                list.addAll(winRecord.getList());
                if (winRecord.getMore() != 1) {
                    hasMore = false;
                }
                if(list.size()>0){
                    ll.setVisibility(View.GONE);
                }else{
                    ll.setVisibility(View.VISIBLE);
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
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void onRefresh(int action) {
        if (action == 1) {
            page = 1;
            initList();
        } else if (action == 2) {
            if(hasMore){
                page++;
                initList();
            }else{
                showToast(R.string.list_no_more);
            }
        }
    }
}
