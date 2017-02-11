package com.goodhappiness.ui.lottery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.GoodsAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.BasePeriod;
import com.goodhappiness.bean.DuoQuanRecord;
import com.goodhappiness.bean.LotteryDetail;
import com.goodhappiness.bean.MyTextWatcherPara;
import com.goodhappiness.bean.ProductList;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnAddCarListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.WebViewActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.MyListView;
import com.goodhappiness.widget.PercentageBar;
import com.goodhappiness.widget.XProgressDialog;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.goodhappiness.widget.timer.CountdownView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_lottery_detail)
public class LotteryDetailActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.lottery_detail_sl)
    private ImageView iv_lqType;
    @ViewInject(R.id.lottery_detail_sv)
    private ScrollView sv;
    @ViewInject(R.id.lottery_detail_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.tv_tips)
    private TextView tv_tips;
//    @ViewInject(R.id.fragment_lottery_tv_count_price)
//    private TextView buyNumView;//购物车上的数量标签
    //    @ViewInject(R.id.lottery_detail_rl_status)
//    private RelativeLayout rl_status;
    @ViewInject(R.id.lottery_detail_ll_process)
    private LinearLayout ll_process;
    @ViewInject(R.id.lottery_detail_rl_winner_past)
    private RelativeLayout rl_winnerPast;
    @ViewInject(R.id.lottery_detail_rl_graphic_detail)
    private RelativeLayout rl_graphicDetail;
    @ViewInject(R.id.lottery_detail_rl_revelation_ing)
    private RelativeLayout ll_reveal_ing;
    @ViewInject(R.id.lottery_detail_rl_revelation_ing_fault)
    private RelativeLayout ll_reveal_ing_fault;
    @ViewInject(R.id.lottery_detail_rl_loading)
    private RelativeLayout ll_loading;
    @ViewInject(R.id.lottery_detail_rl_add_or_join)
    private RelativeLayout rl_buy;
    @ViewInject(R.id.lottery_detail_rl_join_other)
    private RelativeLayout rl_other;
    @ViewInject(R.id.lottery_detail_ll_over)
    private LinearLayout ll_over;
    @ViewInject(R.id.lottery_detail_ll_buy_tips)
    private LinearLayout ll_buy_tips;
    @ViewInject(R.id.lottery_detail_rl_lottery_num)
    private RelativeLayout rl_lottery_num;
    @ViewInject(R.id.lottery_detail_tv_ip_address)
    private TextView tv_ip_address;
    @ViewInject(R.id.lottery_detail_tv_no_buy_tips)
    private TextView tv_no_buy;
    @ViewInject(R.id.lottery_detail_tv_loading)
    private TextView tv_loading;
    @ViewInject(R.id.lottery_detail_tv_current_period)
    private TextView tv_period;
    @ViewInject(R.id.lottery_detail_tv_owner)
    private TextView tv_owner;
    @ViewInject(R.id.lottery_detail_ll_winner)
    private LinearLayout ll_winner;
    @ViewInject(R.id.lottery_detail_tv_codes)
    private TextView tv_codes;
    @ViewInject(R.id.lottery_detail_tv_id)
    private TextView tv_id;
    @ViewInject(R.id.lottery_detail_tv_period)
    private TextView tv_owner_period;
    @ViewInject(R.id.lottery_detail_tv_cost)
    private TextView tv_cost;
    @ViewInject(R.id.lottery_detail_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.lottery_detail_tv_join_count)
    private TextView tv_joinCount;
    @ViewInject(R.id.lottery_detail_tv_lucky_code)
    private TextView tv_luckyCode;
    @ViewInject(R.id.lottery_tv_currentPeriod)
    private TextView tv_currentPeriod;
    @ViewInject(R.id.lottery_detail_coupon_tv_count)
    private TextView tv_coupon_name;
    @ViewInject(R.id.lottery_detail_tv_go_other)
    private TextView tv_go_other;
    @ViewInject(R.id.lottery_detail_tv_exsitingTimes)
    private TextView tv_price;
    @ViewInject(R.id.lottery_detail_tv_remain)
    private TextView tv_remain;
    @ViewInject(R.id.lottery_detail_lv_time)
    private TextView tv_startTime;
    @ViewInject(R.id.lottery_detail_iv_head)
    private ImageView iv_head;
    @ViewInject(R.id.lottery_detail_iv_status)
    private ImageView iv_status;
    @ViewInject(R.id.lottery_detail_iv_loading)
    private ImageView iv_Loading;
    @ViewInject(R.id.common_right)
    private ImageView iv_share;
    @ViewInject(R.id.lottery_tv_calcTimestamp)
    private CountdownView cv;
    @ViewInject(R.id.lottery_detail_pb)
    private PercentageBar pb;
    @ViewInject(R.id.lottery_detail_lv_join)
    private MyListView lv;

    private CommonAdapter<DuoQuanRecord.ListBean> adapter;
    private List<DuoQuanRecord.ListBean> list = new ArrayList<>();
    public static final int LOTTERY_TYPE_WILL_REVEAL = 1;
    public static final int LOTTERY_TYPE_ING = 2;
    public static final int LOTTERY_TYPE_REVEALED = 3;
    public static final int LOTTERY_INTENT_ADD = 4;
    public static final int LOTTERY_INTENT_BUY = 5;
    private int buyUnit = 1;
    private long period = 1;
    private boolean isLoading = false;
    private LotteryDetail lotteryDetail;
    private int mLotteryType;
    private BasePeriod basePeriod;
    private int page = 0;
    private boolean isCanLoad = true;
    private OnAddCarListener onAddCarListener;
    private EditText dialogEditText;
    private GoodsAdapter goodsAdapter;
    private List<ProductList> channelItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        list.clear();
        page = 0;
        adapter.notifyDataSetChanged();
        isCanLoad = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.lottery_detail));
        MobclickAgent.onResume(this);
        newDialog().show();
        init();
//        if (CarUtils.get(this) != 0) {
//            buyNumView.setText(CarUtils.get(this) + "");
//            buyNumView.setVisibility(View.VISIBLE);
//        } else {
//            buyNumView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.lottery_detail));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        buyUnit = getIntent().getIntExtra(FieldFinals.BUY_UNIT, 1);
        period = getIntent().getLongExtra(FieldFinals.PERIOD, -1);
        onAddCarListener = new OnAddCarListener() {
            @Override
            public void onAddFinish(boolean isSuccess, int num) {
                if (isSuccess) {
//                    buyNumView.setText(num + "");
//                    buyNumView.setVisibility(View.VISIBLE);
                }
            }
        };
        initView();
        if (period != -1) {
            initAdapter();
        }
    }

    @Override
    protected void reload() {
        init();
    }

    private void init() {
        RequestParams params = new RequestParams(HttpFinal.LOTTERY_DETAIL);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PERIOD, getIntent().getLongExtra(FieldFinals.PERIOD, 0) + "");
        HttpUtils.post(this, params, new TypeToken<Result<LotteryDetail>>() {
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
                hideView();
                isLoading = false;
                lotteryDetail = (LotteryDetail) result.getData();
                if (lotteryDetail != null) {
                    tv_startTime.setText("(" + lotteryDetail.getRecordStartTime() + "  开始）");
                    mLotteryType = lotteryDetail.getStatus();
                    initView(mLotteryType);
                    if(channelItems.size()==0){
                        channelItems.clear();
                        channelItems.addAll(lotteryDetail.getProductList());
                        channelItems.add(new ProductList());
                        goodsAdapter.notifyDataSetChanged();
                    }
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
                srl.endRefreshing();
                dialog.dismiss();
            }
        });
    }

    //    DialogFactory.createAddToListDialog(getActivity(),LotteryDetailActivity.LOTTERY_INTENT_BUY);
    private void initAdapter() {
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        adapter = new CommonAdapter<DuoQuanRecord.ListBean>(this, list, R.layout.layout_list_join_record) {
            @Override
            public void convert(ViewHolder helper, final DuoQuanRecord.ListBean item, int position) {
                helper.setText(R.id.layout_list_join_record_tv_name, item.getUser().getNickname());
                helper.setText(R.id.layout_list_join_record_tv_ip, "(" + item.getUser().getIPAddress() + " IP:" + item.getUser().getIP() + ")");//"(IP:" + item.getUser().getIP() + ")");
                helper.setText(R.id.layout_list_join_record_tv_cost, item.getNum() + "");
                helper.setText(R.id.layout_list_join_record_tv_time, String.format(getString(R.string.format_count_space, item.getTime())));//"次   " + item.getTime());
                helper.loadImage(R.id.layout_list_join_record_iv_head, item.getUser().getAvatar());
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startToPerson(LotteryDetailActivity.this, list.get(position).getUser().getUid());
            }
        });
        channelItems = new ArrayList<>();
        goodsAdapter = new GoodsAdapter(this,channelItems);
        goodsAdapter.setOnItemClickLitener(new GoodsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ProductList view, int position) {
                if(position==goodsAdapter.getItemCount()-1){
                    HomepageActivity.type = HomepageActivity.SHOP;
                    IntentUtils.startToHomePage(LotteryDetailActivity.this);
                    finishActivity();
                }else{
                    if(view!=null&&!TextUtils.isEmpty(view.getDetailUrl()))
                    IntentUtils.startToNewWebView(LotteryDetailActivity.this,view.getDetailUrl());
                }
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(goodsAdapter);
        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).color(Color.TRANSPARENT).size(15).build());
    }

    private void hideView() {
        ll_process.setVisibility(View.GONE);
        rl_buy.setVisibility(View.GONE);
        rl_other.setVisibility(View.GONE);
        ll_reveal_ing_fault.setVisibility(View.GONE);
        ll_reveal_ing.setVisibility(View.GONE);
        ll_over.setVisibility(View.GONE);
    }

    private void initView(int lotteryType) {
        tv_title.setText(R.string.lottery_detail);
        iv_share.setVisibility(View.VISIBLE);
        iv_share.setImageResource(R.mipmap.share);
        resetView();
        sv.setOnTouchListener(new TouchListenerImpl());
        initJoinPart();
        switch (lotteryType) {
            case LOTTERY_TYPE_WILL_REVEAL://未开奖
                iv_status.setImageResource(R.mipmap.ico_main_ing);
                ll_process.setVisibility(View.VISIBLE);
                rl_buy.setVisibility(View.VISIBLE);
                if (lotteryDetail.getPeriodIng() == null)
                    return;
                basePeriod = lotteryDetail.getPeriodIng();
                tv_period.setText(lotteryDetail.getPeriodIng().getPeriod() + "");
                tv_coupon_name.setText(lotteryDetail.getPeriodIng().getGoods().getName());
//                float percentage = ((float) lotteryDetail.getPeriodIng().getExistingTimes() * 100) / lotteryDetail.getPeriodIng().getGoods().getPrice();
                pb.setPercentage(LotteryFragment.getPg(lotteryDetail.getPeriodIng().getExistingTimes(), lotteryDetail.getPeriodIng().getGoods().getPrice()));
                tv_price.setText(String.format(getString(R.string.format_need_people, lotteryDetail.getPeriodIng().getGoods().getPrice())));//"总需" + lotteryDetail.getPeriodIng().getGoods().getPrice() + "人");
                tv_remain.setText((lotteryDetail.getPeriodIng().getGoods().getPrice() - lotteryDetail.getPeriodIng().getExistingTimes()) + "");
                break;
            case LOTTERY_TYPE_ING://进行中（在开奖）
                iv_status.setImageResource(R.mipmap.ico_main_jxz);
                rl_other.setVisibility(View.VISIBLE);
                if (lotteryDetail.getPeriodWillReveal() != null) {
                    tv_go_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LotteryDetailActivity.this, LotteryDetailActivity.class);
                            intent.putExtra(FieldFinals.PERIOD, lotteryDetail.getCurrentPeriod());
                            intent.putExtra(FieldFinals.BUY_UNIT, lotteryDetail.getPeriodWillReveal().getGoods().getBuyUnit());
                            startTheActivity(intent);
                        }
                    });
                    basePeriod = lotteryDetail.getPeriodWillReveal();
                    if (lotteryDetail.getPeriodWillReveal().getRemainTime() < 3600000) {
                        cv.customTimeShow(false, false, true, true, true);
                    }
                    if (lotteryDetail.getPeriodWillReveal().getRemainTime() == 86400000) {
                        ll_reveal_ing_fault.setVisibility(View.VISIBLE);
                    } else {
                        ll_reveal_ing.setVisibility(View.VISIBLE);
                        tv_coupon_name.setText(lotteryDetail.getPeriodWillReveal().getGoods().getName());
                        cv.start(lotteryDetail.getPeriodWillReveal().getRemainTime());
                        cv.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                init();
                            }
                        });
                        tv_currentPeriod.setText(String.format(getString(R.string.format_current_period_, lotteryDetail.getPeriodWillReveal().getPeriod())));//"当前期号：" + lotteryDetail.getPeriodWillReveal().getPeriod());
                        tv_coupon_name.setText(lotteryDetail.getPeriodWillReveal().getGoods().getName());
                    }
                }
                break;
            case LOTTERY_TYPE_REVEALED://已揭晓
                iv_status.setImageResource(R.mipmap.ico_main_yjx);
                rl_other.setVisibility(View.VISIBLE);
                ll_over.setVisibility(View.VISIBLE);
                if (lotteryDetail.getPeriodRevealed() != null) {
                    basePeriod = lotteryDetail.getPeriodRevealed();
                    tv_go_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LotteryDetailActivity.this, LotteryDetailActivity.class);
                            intent.putExtra(FieldFinals.PERIOD, lotteryDetail.getCurrentPeriod());
                            intent.putExtra(FieldFinals.BUY_UNIT, lotteryDetail.getPeriodRevealed().getGoods().getBuyUnit());
                            startTheActivity(intent);
                        }
                    });
                    tv_coupon_name.setText(lotteryDetail.getPeriodRevealed().getGoods().getName());
                    tv_owner.setText(lotteryDetail.getPeriodRevealed().getOwner().getNickname());
                    tv_id.setText(String.format(getString(R.string.format_user_id_changeless, lotteryDetail.getPeriodRevealed().getOwner().getUid())));//"用户ID：" + lotteryDetail.getPeriodRevealed().getOwner().getUid() + "(唯一不变标识)");
                    tv_owner_period.setText(String.format(getString(R.string.format_period_, lotteryDetail.getPeriodRevealed().getPeriod())));//"期号：" + lotteryDetail.getPeriodRevealed().getPeriod());
                    tv_cost.setText(lotteryDetail.getPeriodRevealed().getOwnerCost() + "");
                    tv_time.setText(String.format(getString(R.string.format_reveal_time_, lotteryDetail.getPeriodRevealed().getCalcTime())));//"揭晓时间：" + lotteryDetail.getPeriodRevealed().getCalcTime());
                    tv_luckyCode.setText(lotteryDetail.getPeriodRevealed().getLuckyCode() + "");
                    displayImage(iv_head, lotteryDetail.getPeriodRevealed().getOwner().getAvatar());
                    tv_ip_address.setText("(" + lotteryDetail.getPeriodRevealed().getOwner().getIPAddress() + ")");
                    ll_winner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtils.startToPerson(LotteryDetailActivity.this, lotteryDetail.getPeriodRevealed().getOwner().getUid());
                        }
                    });
//                    tv_owner.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            IntentUtils.startToPerson(LotteryDetailActivity.this, lotteryDetail.getPeriodRevealed().getOwner().getUid());
//                        }
//                    });
//                    iv_head.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            IntentUtils.startToPerson(LotteryDetailActivity.this, lotteryDetail.getPeriodRevealed().getOwner().getUid());
//                        }
//                    });
                }
                break;
        }
        buyUnit = basePeriod.getGoods().getBuyUnit();
        tv_tips.setText(basePeriod.getGoods().getNum()+"张"+(basePeriod.getGoods().getGoodsType()==1?"普通":"超级")+"礼券可以兑换以下商品");
        if (basePeriod.getGoods().getPicDetails() != null && basePeriod.getGoods().getPicDetails().size() > 0) {
            displayImage(iv_lqType, basePeriod.getGoods().getPicDetails().get(0));
        } else {
            if (basePeriod.getGoods().getGoodsType() != 1) {
                iv_lqType.setImageResource(R.mipmap.ico_lq_normal);
            } else {
                iv_lqType.setImageResource(R.mipmap.ico_lq_super);
            }
        }
    }

    private void resetView() {
        ll_process.setVisibility(View.GONE);
        rl_buy.setVisibility(View.GONE);
        rl_other.setVisibility(View.GONE);
        ll_over.setVisibility(View.GONE);
        tv_no_buy.setVisibility(View.GONE);
        rl_lottery_num.setVisibility(View.GONE);
        ll_buy_tips.setVisibility(View.GONE);
    }

    private void initJoinPart() {
        if (lotteryDetail != null) {
            if (lotteryDetail.getCodes() != null && lotteryDetail.getCodes().size() > 0) {
                rl_lottery_num.setVisibility(View.VISIBLE);
                ll_buy_tips.setVisibility(View.VISIBLE);
                tv_joinCount.setText(lotteryDetail.getCodes().size() + "");
                StringBuffer a = new StringBuffer();
                int count = 0;
                for (long l : lotteryDetail.getCodes()) {
                    a.append(l).append("   ");
                    if (++count > 6) {
                        break;
                    }
                }
                tv_codes.setText(getString(R.string.my_num_) + a.toString());
            } else {
                tv_no_buy.setVisibility(View.VISIBLE);
            }
        }
    }

    @Event({R.id.common_right2, R.id.lottery_detail_tv_calculate_detail2, R.id.lottery_detail_tv_calculate_detail, R.id.lottery_detail_rl_winner_past, R.id.lottery_detail_rl_graphic_detail, R.id.common_right, R.id.lottery_detail_rl_lottery_num, R.id.lottery_detail_tv_buy})
    private void onEvenClick(View v) {
        switch (v.getId()) {
            case R.id.common_right2:

                break;
            case R.id.lottery_detail_tv_calculate_detail:
            case R.id.lottery_detail_tv_calculate_detail2:
                if (lotteryDetail != null && !TextUtils.isEmpty(lotteryDetail.getCalculateUrl())) {
                    IntentUtils.startToNewWebView(LotteryDetailActivity.this, lotteryDetail.getCalculateUrl());
                }
                break;
            case R.id.lottery_detail_rl_winner_past:
                if (lotteryDetail != null) {
                    Intent intent3 = new Intent(this, WebViewActivity.class);
                    intent3.putExtra(FieldFinals.URL, lotteryDetail.getLotteryHisUrl());
                    if (!TextUtils.isEmpty(lotteryDetail.getLotteryHisUrl()))
                        startTheActivity(intent3);
                }
                break;
            case R.id.lottery_detail_rl_graphic_detail:
                if (lotteryDetail != null) {
                    Intent intent4 = new Intent(this, WebViewActivity.class);
                    intent4.putExtra(FieldFinals.URL, lotteryDetail.getDetailUrl());
                    if (!TextUtils.isEmpty(lotteryDetail.getDetailUrl()))
                        startTheActivity(intent4);
                }
                break;
            case R.id.lottery_detail_rl_lottery_num:
                Task task = new Task();
                task.execute();
                break;
            case R.id.common_right:
//                startTheActivity(new Intent(LotteryDetailActivity.this, InventoryActivity.class));
                if (basePeriod != null)
                    DialogFactory.createShareDialog(3, this, null, new String[]{getDid(), getSid(), FieldFinals.PERIOD, String.valueOf(basePeriod.getPeriod())});
                break;
//            case R.id.lottery_detail_tv_add_list:
//                dialogEditText = DialogFactory.createAddToListDialog(this, LotteryDetailActivity.LOTTERY_INTENT_ADD, basePeriod, onAddCarListener, new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        dialogEditText.setTag(0);
//                    }
//                });
//                break;
            case R.id.lottery_detail_tv_buy:
                if(isUserLogined()){
                    dialogEditText = DialogFactory.createAddToListDialog(this, LotteryDetailActivity.LOTTERY_INTENT_BUY, basePeriod, onAddCarListener, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialogEditText.setTag(0);
                        }
                    });
                }
                break;
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && msg.obj instanceof MyTextWatcherPara && msg.arg1 != 0) {
                        MyTextWatcherPara myTextWatcherPara = ((MyTextWatcherPara) msg.obj);
                        myTextWatcherPara.getEditText().setText(msg.arg1 + "");
                        Editable text = myTextWatcherPara.getEditText().getText();
                        Spannable spanText = text;
                        Selection.setSelection(spanText, text.length());
                    }
                    break;
            }
        }
    };

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isLoading = true;
        init();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (lotteryDetail == null || isLoading)
            return false;
        loadJoin();
        return true;
    }

    class Task extends AsyncTask<Integer, Integer, String> {

        private XProgressDialog xProgressDialog = new XProgressDialog(LotteryDetailActivity.this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            xProgressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            PreferencesUtil.setPreferences(LotteryDetailActivity.this, FieldFinals.LOTTERY_NUMBER2, lotteryDetail);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(LotteryDetailActivity.this, LotteryNumberActivity.class);
            startTheActivity(intent);
            xProgressDialog.dismiss();
        }
    }

    private void loadJoin() {
        if (isCanLoad) {
            isLoading = true;
            iv_Loading.setVisibility(View.VISIBLE);
            ll_loading.setVisibility(View.VISIBLE);
            iv_Loading.startAnimation(rotateAnimation);
            page++;
            RequestParams params = new RequestParams(HttpFinal.WIN_RECORD);
            params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
            params.addBodyParameter(FieldFinals.SID, getSid());
            params.addBodyParameter(FieldFinals.PAGE, page + "");
            params.addBodyParameter(FieldFinals.PERIOD, basePeriod.getPeriod() + "");
            HttpUtils.post(this, params, new TypeToken<Result<DuoQuanRecord>>() {
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
                    DuoQuanRecord duoQuanRecord = (DuoQuanRecord) result.getData();
                    list.addAll(duoQuanRecord.getList());
                    if (duoQuanRecord.getMore() == 1) {
                        isCanLoad = true;
                    } else {
                        isCanLoad = false;
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
                    finishLoadList();
                }
            });
        } else {
            showToast(getString(R.string.list_no_more));
        }
    }

    private void finishLoadList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LotteryDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_Loading.setVisibility(View.GONE);
                        ll_loading.setVisibility(View.GONE);
                        iv_Loading.clearAnimation();
                        adapter.notifyDataSetChanged();
                        srl.endLoadingMore();
                        isLoading = false;
                    }
                });
            }
        }, 0);
    }

//    private String getGid(){
//        switch (mLotteryType){
//            case LOTTERY_TYPE_WILL_REVEAL:
//                if(lotteryDetail.getPeriodWillReveal()!=null&&lotteryDetail.getPeriodWillReveal().getGoods().getGid()!=-1){
//                    return lotteryDetail.getPeriodWillReveal().getGoods().getGid()+"";
//                }
//                break;
//            case LOTTERY_TYPE_ING:
//                if(lotteryDetail.getPeriodIng()!=null&&lotteryDetail.getPeriodIng().getGoods().getGid()!=-1){
//                    return lotteryDetail.getPeriodIng().getGoods().getGid()+"";
//                }
//                break;
//            case LOTTERY_TYPE_REVEALED:
//                if(lotteryDetail.getPeriodRevealed()!=null&&lotteryDetail.getPeriodRevealed().getGoods().getGid()!=-1){
//                    return lotteryDetail.getPeriodRevealed().getGoods().getGid()+"";
//                }
//                break;
//
//        }
//        return "";
//    }

    /**
     * scroll滑动
     */
    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = sv.getChildAt(0).getMeasuredHeight();
                    if (scrollY == 0) {
                    }
                    if ((scrollY + height) >= scrollViewMeasuredHeight) {
                        onBGARefreshLayoutBeginLoadingMore(srl);
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }

}
