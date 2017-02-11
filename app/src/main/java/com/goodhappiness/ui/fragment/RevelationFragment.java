package com.goodhappiness.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.PeriodRevealed;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.RevelationList;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.lottery.LotteryDetailActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.goodhappiness.widget.timer.CountdownView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * EditPicUpdateActivity simple {@link Fragment} subclass.
 * Use the {@link RevelationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_revelation)
public class RevelationFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.revelation_lv)
    private ListView lv;
    @ViewInject(R.id.revelation_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.common_left)
    private ImageView iv_back;
    @ViewInject(R.id.common_title)
    private TextView tv_title;
    @ViewInject(R.id.inventory_rl_empty)
    private RelativeLayout empty_view;
    @ViewInject(R.id.revelation_empty_view)
    private LinearLayout ll_fail_view;
    private CommonAdapter<RevelationList.ListBean> revelationCommonAdapter;
    private List<RevelationList.ListBean> list = new ArrayList<>();
    private int page = 1;
    private boolean isCanLoad = true;
    private boolean isListCanLoad = true;
    private boolean isNeedRefresh = false;
    private ArrayList<Timer> timers = new ArrayList<>();
    private ArrayList<Long> pastTimes = new ArrayList<>();
    private Map<Integer,Long> periodTask = new HashMap<>();

    public RevelationFragment() {
        super(R.layout.fragment_revelation);
    }

    /**
     */
    // TODO: Rename and change types and number of parameters
    public static RevelationFragment newInstance() {
        RevelationFragment fragment = new RevelationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected void reload() {
        empty_view.setVisibility(View.GONE);
        ll_fail_view.setVisibility(View.GONE);
        onBGARefreshLayoutBeginRefreshing(srl);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.least_revelation));
//        onRefresh(SwipyRefreshLayoutDirection.TOP);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.least_revelation));
    }


    @Override
    protected void setData() {
        initAdapter();
        iv_back.setVisibility(View.GONE);
        tv_title.setText(R.string.least_revelation);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
    }

    @Event({R.id.tv_join, R.id.revelation_empty_view_tv_reload})
    private void OnEventClick(View v) {
        switch (v.getId()) {
            case R.id.tv_join:
                HomepageActivity.type = HomepageActivity.LOTTERY;
                IntentUtils.startToHomePage(getActivity());
                break;
            case R.id.revelation_empty_view_tv_reload:
                reload();
                break;
        }
    }

    private void initList(final boolean isFirst) {
        if (!isListCanLoad) {
            return;
        }
        isListCanLoad = false;
        RequestParams params = new RequestParams(HttpFinal.REVEAL_LIST);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<RevelationList>>() {
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
                final RevelationList revelationList = (RevelationList) result.getData();
                if (isFirst) {
                    if (list.size() > 0 && revelationList.getList().size() > 0) {
                        if (!isNeedRefresh) {
                            if (list.get(0).getPeriodWillReveal() != null && revelationList.getList().get(0).getPeriodWillReveal() != null) {
                                if (list.get(0).getPeriodWillReveal().getPeriod() == revelationList.getList().get(0).getPeriodWillReveal().getPeriod()) {
                                    endRefreshing(page);
                                    return;
                                }
                            }
                            if (list.get(0).getPeriodRevealed() != null && revelationList.getList().get(0).getPeriodRevealed() != null) {
                                if (list.get(0).getPeriodRevealed().getPeriod() == revelationList.getList().get(0).getPeriodRevealed().getPeriod()) {
                                    endRefreshing(page);
                                    return;
                                }
                            } else {
                                isNeedRefresh = false;
                            }
                        }
                    }
                    for (Timer timer : timers) {
                        timer.cancel();
                        timer.purge();
                    }
                    timers.clear();
                    pastTimes.clear();
                    list.clear();
//                    revelationCommonAdapter.notifyDataSetChanged();
                }
                if (revelationList.getList().size() > 0) {
                    empty_view.setVisibility(View.GONE);
                    ll_fail_view.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getActivity() != null)
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (RevelationList.ListBean listBean : revelationList.getList()) {
                                            if (listBean.getPeriodWillReveal() != null) {
                                                listBean.setListPosition(page - 1);
                                            }
                                        }
                                        if (isFirst && list.size() > 0) {
                                            endRefreshing(page);
                                            return;
                                        }
                                        addList(page - 1);
                                        list.addAll(revelationList.getList());
                                        if (revelationList.getMore() == 1) {
                                            isCanLoad = true;
                                        } else {
                                            isCanLoad = false;
                                        }
                                        revelationCommonAdapter.notifyDataSetChanged();
                                        endRefreshing(page);
                                    }
                                });
                        }
                    }, 0);
                } else {
                    empty_view.setVisibility(View.VISIBLE);
                    ll_fail_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(empty_view!=null)
                empty_view.setVisibility(View.GONE);
                if(ll_fail_view!=null)
                ll_fail_view.setVisibility(View.VISIBLE);

                if(srl!=null)
                if (page == 1) {
                    srl.endRefreshing();
                } else {
                    srl.endLoadingMore();
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
                isListCanLoad = true;
                endRefreshing(page);
            }
        });
    }

    private void endRefreshing(int page){
        if (page == 1) {
            srl.endRefreshing();
        } else {
            srl.endLoadingMore();
        }
    }

    private void addList(final int listPosition) {
        pastTimes.add(listPosition, 0L);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pastTimes.set(listPosition, pastTimes.get(listPosition) + 100);
            }
        }, 0, 100);
        timers.add(listPosition, timer);
    }

    private void initAdapter() {
        revelationCommonAdapter = new CommonAdapter<RevelationList.ListBean>(getActivity(), list, R.layout.layout_list_revelation) {
            @Override
            public void convert(ViewHolder helper, final RevelationList.ListBean item, final int position) {
                final LinearLayout ll_willReveal = helper.getView(R.id.layout_list_revelation_ll_will_reveal);
                final LinearLayout ll_revealed = helper.getView(R.id.layout_list_revelation_ll_revealed);
                final LinearLayout ll_willReveal_normal = helper.getView(R.id.layout_list_revelation_ll_normal);
                final LinearLayout ll_willReveal_abnormal = helper.getView(R.id.layout_list_revelation_ll_abnormal);
                ll_willReveal_abnormal.setVisibility(View.GONE);
                if (item.getPeriodWillReveal() != null) {//即将揭晓
                    if (item.getPeriodWillReveal().getGoods() != null && item.getPeriodWillReveal().getGoods().getPicNew() != null && item.getPeriodWillReveal().getGoods().getPicNew().size() > 0) {
                        helper.loadImage(R.id.layout_list_revelation_iv, item.getPeriodWillReveal().getGoods().getPicNew().get(0));//,new ImageSize(374,272)
                    }else{
                        helper.setImageResource(R.id.layout_list_revelation_iv,R.mipmap.img_mrjz);
                    }
                    helper.setText(R.id.layout_list_revelation_revealed_tv_name, item.getPeriodWillReveal().getGoods().getName());
                    helper.setText(R.id.layout_list_revelation_revealed_tv_period, "" + item.getPeriodWillReveal().getPeriod());
                    if (item.getPeriodWillReveal().getRemainTime() == 86400000) {//出现故障
                        ll_willReveal.setVisibility(View.VISIBLE);
                        ll_revealed.setVisibility(View.GONE);
                        ll_willReveal_normal.setVisibility(View.GONE);
                        ll_willReveal_abnormal.setVisibility(View.VISIBLE);
                    } else {//没有故障
                        if (item.isRefresh()) {//已请求GetWinner
                            ll_willReveal.setVisibility(View.GONE);
                            ll_revealed.setVisibility(View.VISIBLE);
                        } else {//没请求GetWinner
                            ll_willReveal.setVisibility(View.VISIBLE);
                            ll_revealed.setVisibility(View.GONE);
                            ll_willReveal_normal.setVisibility(View.VISIBLE);
                            //------------------------------------------
                            CountdownView view = helper.getView(R.id.layout_list_revelation_cv);
                            if (item.getPeriodWillReveal().getRemainTime() < 3600000) {
                                view.customTimeShow(false, false, true, true, true);
                            }
                            if (item.getPeriodWillReveal().getRemainTime() - pastTimes.get(item.getListPosition()) > 0) {
                                CountdownView.OnCountdownEndListener onCountdownEndListener = new CountdownView.OnCountdownEndListener() {
                                    @Override
                                    public void onEnd(CountdownView cv) {
                                        item.setIsRefresh(true);
                                        refreshItem(ll_willReveal, ll_revealed, position);
                                    }
                                };
                                Log.e("reveal", "Settime-1-position:" + position + "-----time:" + (item.getPeriodWillReveal().getRemainTime() - pastTimes.get(item.getListPosition())) + "---listposition:" + item.getListPosition());
                                view.start(item.getPeriodWillReveal().getRemainTime() - pastTimes.get(item.getListPosition()));
                                view.setOnCountdownEndListener(onCountdownEndListener);
                            } else {
                                item.setIsRefresh(true);
                                refreshItem(ll_willReveal, ll_revealed, position);
                            }

                        }
                    }
                } else if (item.getPeriodRevealed() != null) {//已揭晓
                    list.get(position).setIsRefresh(true);
                    ll_willReveal.setVisibility(View.GONE);
                    ll_revealed.setVisibility(View.VISIBLE);
                    if (item.getPeriodRevealed().getGoods() != null && item.getPeriodRevealed().getGoods().getPicNew() != null && item.getPeriodRevealed().getGoods().getPicNew().size() > 0) {
                        helper.loadImage(R.id.layout_list_revelation_iv, item.getPeriodRevealed().getGoods().getPicNew().get(0));//,new ImageSize(374,272)
                    }else{
                        helper.setImageResource(R.id.layout_list_revelation_iv,R.mipmap.img_mrjz);
                    }
                    helper.setText(R.id.name, item.getPeriodRevealed().getGoods().getName());
                    helper.setText(R.id.layout_list_revelation_tv_period, item.getPeriodRevealed().getPeriod() + "");
                    helper.setText(R.id.layout_list_revelation_tv_owner, item.getPeriodRevealed().getOwner().getNickname());
                    helper.setText(R.id.layout_list_revelation_tv_owner_cost, item.getPeriodRevealed().getOwnerCost() + "");
                    helper.setText(R.id.layout_list_revelation_tv_lucky_code, item.getPeriodRevealed().getLuckyCode() + "");
                    helper.setText(R.id.layout_list_revelation_tv_cal_time, item.getPeriodRevealed().getCalcTime());
                }
            }
        };
        lv.setAdapter(revelationCommonAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LotteryDetailActivity.class);
                if (list.get(position).getPeriodWillReveal() != null) {
                    intent.putExtra(FieldFinals.PERIOD, list.get(position).getPeriodWillReveal().getPeriod());
                    intent.putExtra(FieldFinals.BUY_UNIT, list.get(position).getPeriodWillReveal().getGoods().getBuyUnit());
                    startActivity(intent);
                }
                if (list.get(position).getPeriodRevealed() != null) {
                    intent.putExtra(FieldFinals.PERIOD, list.get(position).getPeriodRevealed().getPeriod());
                    intent.putExtra(FieldFinals.BUY_UNIT, list.get(position).getPeriodRevealed().getGoods().getBuyUnit());
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshItem(LinearLayout ll_willReveal, LinearLayout ll_revealed, int position) {
        if(ll_willReveal!=null&&ll_revealed!=null){
            ll_willReveal.setVisibility(View.GONE);
            ll_revealed.setVisibility(View.VISIBLE);
            getWinner(list.get(position).getPeriodWillReveal().getPeriod(), list.get(position).getPeriodWillReveal().getGoods().getGid(), position);
        }
    }

    private void getWinner(long period, long gid, final int position) {
        for (Map.Entry<Integer, Long> entry:periodTask.entrySet()) {
            if(entry.getKey()==period){
                return;
            }
        }
        periodTask.put(position,period);
        RequestParams params = new RequestParams(HttpFinal.GET_WINNER);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.GID, gid + "");
        params.addBodyParameter(FieldFinals.PERIOD, period + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<PeriodRevealed>>() {
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
                PeriodRevealed periodRevealed = (PeriodRevealed) result.getData();
                list.get(position).setPeriodRevealed(periodRevealed);
                list.get(position).setPeriodWillReveal(null);
                periodTask.remove(position);
                revelationCommonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void refresh() {
        page = 1;
        initList(true);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isNeedRefresh = true;
        page = 1;
        initList(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (isCanLoad) {
            page++;
            initList(false);
            return true;
        } else {
            showToast(R.string.list_no_more);
            return false;
        }
    }
}
