package com.goodhappiness.ui.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.ItemClickListenerTag;
import com.goodhappiness.bean.LotteryRecord;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.social.PersonActivity;
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

@ContentView(R.layout.fragment_lottery_record_in_person)
public class LotteryRecordFragmentInPerson extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    @ViewInject(R.id.fragment_lottery_record_lv)
    private ListView lv;
    @ViewInject(R.id.fragment_lottery_record_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.lottery_record_tv_join)
    private TextView tv_join;
    @ViewInject(R.id.tips)
    private TextView tv_tips;
    @ViewInject(R.id.fragment_post_publish_ll_no_photo)
    private LinearLayout ll;
    private String uid;
    private CommonAdapter<LotteryRecord.ListBean> adapter;
    private List<LotteryRecord.ListBean> list = new ArrayList<>();
    public int page = 1;
    private int firstPosition = 0;
    private boolean hasMore = true;
    private String status ="0";
    public LotteryRecordFragmentInPerson() {
        super(R.layout.fragment_lottery_record);
    }


    public static LotteryRecordFragmentInPerson newInstance(String uid,String status) {
        LotteryRecordFragmentInPerson fragment = new LotteryRecordFragmentInPerson();
        Bundle args = new Bundle();
        args.putString(FieldFinals.UID, uid);
        args.putString(FieldFinals.STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(FieldFinals.UID);
            status = getArguments().getString(FieldFinals.STATUS);
        }
    }

    @Override
    protected void reload() {
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.lottery_record));
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.lottery_record));
    }
    @Override
    protected void setData() {
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
//        params.height = GoodHappinessApplication.h-(int)(GoodHappinessApplication.perHeight*160);
//        params.width = GoodHappinessApplication.w;
//        lv.setLayoutParams(params);
        initAdapter();
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        onBGARefreshLayoutBeginRefreshing(srl);
//        onRefresh(SwipyRefreshLayoutDirection.BOTH);
    }


    @Event({R.id.lottery_record_tv_join})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.lottery_record_tv_join:
                HomepageActivity.type = HomepageActivity.LOTTERY;
                if(getActivity() instanceof LotteryRecordActivity) {
                    ((LotteryRecordActivity) getActivity()).finishActivity();
                }else if(getActivity() instanceof PersonActivity){
                    ((PersonActivity) getActivity()).finishActivity();
                }
                break;
        }
    }

    private void initAdapter() {
        adapter = new CommonAdapter<LotteryRecord.ListBean>(getActivity(), list, R.layout.layout_list_lottery_record) {
            @Override
            public void convert(ViewHolder helper, LotteryRecord.ListBean item, final int position) {
                if (!uid.equals((getUid() + ""))) {
                    helper.setVisibility(R.id.layout_list_lottery_record_rl_bottom, View.GONE);
                }
                helper.setOnclickListener(R.id.layout_list_lottery_record_tv_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LotteryRecordDetailActivity.class);
                        intent.putExtra(FieldFinals.PERIOD, list.get(position).getPeriod());
                        startActivity(intent);
                    }
                });
                hiedView(helper);
                TextView view = null;
                switch (item.getStatus()) {
                    case 1:
                        helper.getView(R.id.layout_list_lottery_record_rl_will).setVisibility(View.VISIBLE);
//                        float percentage = ((float) item.getExistingTimes() * 100) / item.getPrice();
//                        float b = (int) Math.floor(percentage);
//                        float percentage = ((float) item.getExistingTimes() * 100) / item.getGoods().getPrice();
//                        int pro = (int) Math.floor(percentage);
                        helper.setPercentage(R.id.layout_list_lottery_record_pb, String.valueOf(LotteryFragment.getPg(item.getExistingTimes(),item.getPrice())));
                        helper.setText(R.id.layout_list_lottery_detail_tv_exsitingTimes, String.format(getString(R.string.format_need,item.getPrice())));//"总需" + item.getPrice());
                        helper.setText(R.id.layout_list_lottery_detail_tv_remain, (item.getPrice() - item.getExistingTimes()) + "");
                        view = helper.getView(R.id.layout_list_lottery_record_tv_will_buy);
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_lucky_code,View.GONE);
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_time,View.GONE);
                        //追加layout_list_lottery_record_tv_will_buy
                        break;
                    case 2:
                        helper.getView(R.id.layout_list_lottery_record_rl_ing).setVisibility(View.VISIBLE);
                        view = helper.getView(R.id.layout_list_lottery_record_tv_ing_buy);
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_lucky_code, View.GONE);
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_time,View.GONE);
                        //再次购买layout_list_lottery_record_tv_ing_buy
                        break;
                    case 3:
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_lucky_code, View.VISIBLE);
                        helper.setVisibility(R.id.layout_list_lottery_record_ll_time,View.VISIBLE);
                        helper.getView(R.id.layout_list_lottery_record_rl_revealed).setVisibility(View.VISIBLE);
                        helper.setText(R.id.layout_list_lottery_record_tv_winner, item.getNickname());
                        helper.setText(R.id.layout_list_lottery_record_tv_win_cost, item.getOwnerCost() + "");
                        helper.setText(R.id.layout_list_win_record_win_tv_number, item.getLuckyCode() + "");
                        helper.setText(R.id.layout_list_win_record_tv_time, item.getCalcTime() + "");
                        view = helper.getView(R.id.layout_list_lottery_record_tv_revealed_buy);
                        //再次购买layout_list_lottery_record_tv_revealed_buy
                        break;
                }
                if (view != null)
                    if (view.getTag() == null) {
                        setClickListener(position, view);
                    } else {
                        ItemClickListenerTag itemClickListenerTag = (ItemClickListenerTag) view.getTag();
                        if (itemClickListenerTag.getPosition() != position) {
                            setClickListener(position, view);
                        }
                    }
                helper.setText(R.id.layout_list_lottery_record_tv_name, item.getName());
                helper.setText(R.id.layout_list_lottery_record_tv_num, String.format(getString(R.string.format_what_piece,item.getNum())));//item.getNum() + "张");
                helper.setText(R.id.layout_list_lottery_record_tv_period,String.format(getString(R.string.format_period_,item.getPeriod())));// "期号：" + item.getPeriod());
                helper.setText(R.id.layout_list_lottery_record_tv_cost, "" + item.getCount_buy());
                ImageView imageView = helper.getView(R.id.layout_list_lottery_record_iv);
                if (item.getPic() != null && item.getPic().size() > 0) {
                    helper.loadImage(R.id.layout_list_lottery_record_iv, item.getPic().get(0));//,new ImageSize(374,272)
                    imageView.setPadding(0,0,0,0);
                }else{
                    imageView.setImageResource(R.mipmap.img_mrjz);
                }
//                else{
//                    imageView.setPadding(50,20,50,20);
//                    if(item.getGoodsType()==1){
//                        helper.setImageResource(R.id.layout_list_lottery_record_iv,R.mipmap.img_ptlq);
//                    }else{
//                        helper.setImageResource(R.id.layout_list_lottery_record_iv,R.mipmap.img_cjlq);
//                    }
//                }
            }

            private void hiedView(ViewHolder helper) {
                helper.getView(R.id.layout_list_lottery_record_rl_will).setVisibility(View.GONE);
                helper.getView(R.id.layout_list_lottery_record_rl_ing).setVisibility(View.GONE);
                helper.getView(R.id.layout_list_lottery_record_rl_revealed).setVisibility(View.GONE);
            }
        };

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (uid.equals(getUid() + "")) {
                Intent intent = new Intent(getActivity(), LotteryDetailActivity.class);
                intent.putExtra(FieldFinals.PERIOD, list.get(position).getPeriod());
                startActivity(intent);
//                }
            }
        });
    }

    private void setClickListener(final int position, TextView view) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemClickListenerTag itemClickListenerTag = (ItemClickListenerTag) v.getTag();
                if (itemClickListenerTag != null) {
                    Intent intent = new Intent(getActivity(), LotteryDetailActivity.class);
                    intent.putExtra(FieldFinals.PERIOD, list.get(position).getCurrentPeriod());
                    startActivity(intent);
                }
            }
        };
        view.setOnClickListener(onClickListener);
        view.setTag(new ItemClickListenerTag(onClickListener, position));
    }

    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.PERIODS);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        if (TextUtils.isEmpty(uid)) {
            params.addBodyParameter(FieldFinals.SID, getSid());
        } else {
            params.addBodyParameter(FieldFinals.SID, "");
            params.addBodyParameter(FieldFinals.USER_ID, uid);
        }
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        params.addBodyParameter(FieldFinals.STATUS, status);
        HttpUtils.post(getActivity(),params, new TypeToken<Result<LotteryRecord>>() {
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
                LotteryRecord lotteryRecord = (LotteryRecord) result.getData();
                if (page == 1) {
                    list.clear();
                }
                list.addAll(lotteryRecord.getList());
                if (lotteryRecord.getMore() != 1) {
                    hasMore = false;
                }else{
                    hasMore = true;
                }
                if(list.size()>0){
                    ll.setVisibility(View.GONE);
                }else{
                    ll.setVisibility(View.VISIBLE);
                    if(Long.valueOf(uid)!=getUid()){
                        tv_join.setVisibility(View.GONE);
                        tv_tips.setText(R.string.no_data);
                    }
                }

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
                endRefreshing();
                adapter.notifyDataSetChanged();
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
        initList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (hasMore) {
            page++;
            initList();
            return true;
        } else {
            showToast(R.string.list_no_more);
            return false;
        }
    }
}
