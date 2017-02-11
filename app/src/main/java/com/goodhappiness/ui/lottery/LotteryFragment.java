package com.goodhappiness.ui.lottery;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Banner;
import com.goodhappiness.bean.BannerData;
import com.goodhappiness.bean.CarNum;
import com.goodhappiness.bean.LotteryList;
import com.goodhappiness.bean.PeriodIng;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnAddCarListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.order.InventoryActivity;
import com.goodhappiness.utils.AnimationUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.StringUtils;
import com.goodhappiness.widget.HeaderGridView;
import com.goodhappiness.widget.UpMarqueeTextView;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.BannerConfig;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖模块
 */
@ContentView(R.layout.fragment_lottery)
public class LotteryFragment extends BaseFragment implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {//SwipyRefreshLayout.OnRefreshListener,
    @ViewInject(R.id.rl_gridview_refresh)
    private BGARefreshLayout mRefreshLayout;
    @ViewInject(R.id.fragment_lottery_rl_root)
    private RelativeLayout rl_root;
    @ViewInject(R.id.revelation_empty_view)
    private LinearLayout ll_fail_view;
    @ViewInject(R.id.lottery_rl_no_list)
    private RelativeLayout rl_no_list;
    @ViewInject(R.id.common_title)
    private TextView tv_title;
    @ViewInject(R.id.common_right)
    private ImageView iv_right;
    @ViewInject(R.id.common_left)
    private ImageView iv_back;
    @ViewInject(R.id.lottery_lv)
    private HeaderGridView lv;
//    @ViewInject(R.id.fragment_lottery_tv_count_price)
//    private TextView buyNumView;//购物车上的数量标签
    //    @ViewInject(R.id.lottery_srl)
//    private SwipyRefreshLayout srl;
    private EditText dialogEditText;
    private RelativeLayout rl_process;
    private RelativeLayout rl_request;
    private TextView tv_process;
    private TextView tv_request;
    private ImageView iv_request;
    private View v_process;
    private View v_request;

    private LinearLayout ll_ad;
    private UpMarqueeTextView upMarqueeTextView;
    private View headView;

    private com.youth.banner.Banner mBanner;
    private LinearLayout ll_tab;
    private int page = 1;
    private int sort = 1;
    private CommonAdapter<PeriodIng> lotteryListCommonAdapter;
    private List<PeriodIng> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private AnimationUtils animationUtils;
    private boolean isRequestClick = false;
    private LotteryList lotteryList;
    private boolean isCanLoad = true;
    private List<BannerData.WinnersBean> winners;
    private int bannerPageCount = 0;
    private boolean isFirstWinner = true;
    private boolean isListCanLoad = true;
    private boolean isAdCanLoad = true;
    private OnAddCarListener onAddCarListener;
    public LotteryFragment() {
        super(R.layout.fragment_lottery);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return EditPicUpdateActivity new instance of fragment LotteryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LotteryFragment newInstance() {
        LotteryFragment fragment = new LotteryFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        layoutInflater = LayoutInflater.from(getActivity());
    }

    @Override
    protected void reload() {
//        srl.setVisibility(View.VISIBLE);
//        srl.setRefreshing(true);
        if (ll_fail_view != null) {
            ll_fail_view.setVisibility(View.GONE);
        }
        initList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.index));
        page = 1;
        initList(true);
//        if (CarUtils.get(getActivity()) != 0) {
//            buyNumView.setText(CarUtils.get(getActivity()) + "");
//            buyNumView.setVisibility(View.VISIBLE);
//        } else {
//            buyNumView.setText("");
//            buyNumView.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.index));
    }

    public static float getPg(int exist, int price) {
        float percentage = ((float) exist * 100) / price;
        int pro = (int) Math.floor(percentage);
        if (pro == 0) {
            if (exist > 0) {
                pro = 1;
            } else {
                pro = 0;
            }
        }
        return pro;
    }

    @Override
    protected void setData() {
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        animationUtils = new AnimationUtils();
        tv_title.setText(getResString(R.string.lottery_title));
//        iv_right.setImageResource(R.mipmap.ico_btn2_header);
//        iv_right.setVisibility(TextView.VISIBLE);
        iv_back.setVisibility(View.GONE);
        lotteryListCommonAdapter = new CommonAdapter<PeriodIng>(getActivity(), list, R.layout.layout_grid_lottery) {
            @Override
            public void convert(ViewHolder helper, PeriodIng item, final int position) {
                float percentage = ((float) item.getExistingTimes() * 100) / item.getGoods().getPrice();
                int pro = (int) Math.floor(percentage);
                if (pro == 0) {
                    if (item.getExistingTimes() > 0) {
                        pro = 1;
                    } else {
                        pro = 0;
                    }
                }
                helper.setPercentage(R.id.layout_list_lottery_pb, pro + "");
                helper.setText(R.id.layout_list_lottery_tv_name, item.getGoods().getName());
                helper.setText(R.id.layout_list_lottery_tv_percentage, "完成进度 " + pro + "%");
//                helper.setText(R.id.layout_list_lottery_tv_price, String.format(getString(R.string.format_need_people, item.getGoods().getPrice())));// "总需" + item.getGoods().getPrice() + "人");
                helper.setText(R.id.layout_list_lottery_tv_num, String.format(getString(R.string.format_what_period, item.getPeriod())));//item.getPeriod() + "期");
//                helper.setText(R.id.layout_list_lottery_remain, (item.getGoods().getPrice() - item.getExistingTimes()) + "");
                final ImageView imageView = helper.getView(R.id.layout_list_lottery_iv_coupon_grid);
                View textView = helper.getView(R.id.layout_list_lottery_tv_add);
                if (item.getGoods() != null && item.getGoods().getPic() != null && item.getGoods().getPic().size() > 0) {
                    helper.loadImage(R.id.layout_list_lottery_iv_coupon_grid, item.getGoods().getPic().get(0));//,new ImageSize(374,272)
                } else {
                    imageView.setImageResource(R.mipmap.img_mrjz);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                params.width = (int) (w / 2 - GoodHappinessApplication.perHeight * 40);
                params.height = (int) (params.width * 272f / 374);
                imageView.setLayoutParams(params);
//                textView.setTag(imageView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        addToCar(position, imageView);
                        if(isUserLogined()){
                            dialogEditText = DialogFactory.createAddToListDialog(getActivity(), LotteryDetailActivity.LOTTERY_INTENT_BUY, list.get(position), onAddCarListener, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialogEditText.setTag(0);
                                }
                            });
                        }
                    }
                });

            }
        };
        initHeadView();
        lv.setAdapter(lotteryListCommonAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), LotteryDetailActivity.class);
                intent.putExtra(FieldFinals.PERIOD, list.get(position).getPeriod());
                intent.putExtra(FieldFinals.BUY_UNIT, list.get(position).getGoods().getBuyUnit());
                startActivity(intent);
            }
        });
        onAddCarListener = new OnAddCarListener() {
            @Override
            public void onAddFinish(boolean isSuccess, int num) {
                if (isSuccess) {
                }
            }
        };
    }


    public void refresh() {
        if (headView != null) {
//            getHeadViewData();
        }

        onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
    }

    private void addToCar(int position, final ImageView v) {
        RequestParams params = new RequestParams(HttpFinal.CAR_ADD);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.GID, list.get(position).getGoods().getGid() + "");
        params.addBodyParameter(FieldFinals.PERIOD, list.get(position).getPeriod() + "");
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.NUM, list.get(position).getGoods().getBuyUnit() + "");
        params.addBodyParameter(FieldFinals.ACTION, FieldFinals.INSERT);
        HttpUtils.post(getActivity(), params, new TypeToken<Result<CarNum>>() {
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
//                final CarNum carNum = (CarNum) result.getData();
//                CarUtils.set(getActivity(), carNum.getCart_num());
//                if (carNum != null) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    buyNumView.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//                    }, 1000);
//                    int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
//                    v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//                    RelativeLayout ball = (RelativeLayout) layoutInflater.inflate(R.layout.layout_ball, rl_root, false);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
//                    ((ImageView) ball.findViewById(R.id.iv)).setImageDrawable(v.getDrawable());
//                    animationUtils.setAnim(getActivity(), buyNumView, ball, startLocation, new AnimationUtils.OnAnimationListener() {
//                        @Override
//                        public void onAnimationEnd() {
//                            buyNumView.setText(carNum.getCart_num() + "");//
//                        }
//                    });// 开始执行动画
//                }
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

    private void initList(final boolean isFirst) {
        if (!isListCanLoad) {
            return;
        }
        isListCanLoad = false;
        RequestParams params = new RequestParams(HttpFinal.LOTTERY_LIST);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        params.addBodyParameter(FieldFinals.SORT, sort + "");
        HttpUtils.post(getActivity(), params, new TypeToken<Result<LotteryList>>() {
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
                lotteryList = (LotteryList) result.getData();
                if (lotteryList.getMore() == 1) {
                    isCanLoad = true;
                } else {
                    isCanLoad = false;
                }
                if (isFirst) {
                    list.clear();
                }
                list.addAll(lotteryList.getList());
                rl_no_list.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                lotteryListCommonAdapter.notifyDataSetChanged();
                if (page == 1) {
                    mRefreshLayout.endRefreshing();
                } else {
                    mRefreshLayout.endLoadingMore();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ll_fail_view != null) {
                    ll_fail_view.setVisibility(View.VISIBLE);
                }
                if (list != null) {
                    list.clear();
                    if (lotteryListCommonAdapter != null)
                        lotteryListCommonAdapter.notifyDataSetChanged();
                }
                if (mRefreshLayout != null)
                    if (page == 1) {
                        mRefreshLayout.endRefreshing();
                    } else {
                        mRefreshLayout.endLoadingMore();
                    }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
                isListCanLoad = true;
                if (page == 1) {
                    mRefreshLayout.endRefreshing();
                } else {
                    mRefreshLayout.endLoadingMore();
                }
//                srl.setRefreshing(false);
            }
        });
    }


    @Event({R.id.common_left, R.id.common_title, R.id.common_right, R.id.revelation_empty_view_tv_reload})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.revelation_empty_view_tv_reload:
                reload();
                break;
            case R.id.common_left:
//                showToast("do something");

                break;
            case R.id.common_right:
//                RongIM.getInstance().startPrivateChat(getActivity(), "10002", "title");
                startActivity(new Intent(getActivity(), InventoryActivity.class));
                break;
            case R.id.common_title:
                //启动会话列表界面
//                try {
//                    RongPushClient.checkManifest(getActivity());
//                } catch (RongException e) {
//                    e.printStackTrace();
//                }
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);//1 can be another integer
                break;
            case R.id.layout_lottery_head_rl_process:
                sort = 1;
                resetTab();
                isRequestClick = false;
                tv_process.setTextColor(getResources().getColor(R.color.black_333_text));
                v_process.setBackgroundColor(getResources().getColor(R.color.theme_color));
                iv_request.setImageResource(R.mipmap.ico_btn_nav);
                page = 1;
                onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                break;
            case R.id.layout_lottery_head_rl_request:
                resetTab();
                tv_request.setTextColor(getResources().getColor(R.color.black_333_text));
                v_request.setBackgroundColor(getResources().getColor(R.color.theme_color));
                if (isRequestClick) {//判断是否第二次点击
                    //第二次
                    iv_request.setImageResource(R.mipmap.ico_btn2_nav);
                    page = 1;
                    sort = 2;
                    onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                } else {
                    //第一次
                    iv_request.setImageResource(R.mipmap.ico_btn3_nav);
                    page = 1;
                    sort = 3;
                    onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                }
                isRequestClick = !isRequestClick;
                break;
        }
    }

    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate() {
        @Override
        public void onFilterManagerInited(FilterManager manager) {
            TuSdk.messageHub().showSuccess(getActivity(), R.string.lsq_inited);
        }
    };

    private void resetTab() {
        tv_process.setTextColor(getResources().getColor(R.color.gray_999_text));
        tv_request.setTextColor(getResources().getColor(R.color.gray_999_text));
        v_process.setBackgroundColor(getResources().getColor(R.color.trans));
        v_request.setBackgroundColor(getResources().getColor(R.color.trans));
    }

    private void initHeadView() {
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = lif.inflate(R.layout.layout_lottery_head, lv, false);//getActivity().getLayoutInflater().inflate(R.layout.layout_lottery_head,lv,false);
        upMarqueeTextView = (UpMarqueeTextView) headView.findViewById(R.id.layout_lottery_head_umt);
        ll_ad = (LinearLayout) headView.findViewById(R.id.layout_lottery_head_ll_adv);
        mBanner = (com.youth.banner.Banner) headView.findViewById(R.id.lottery_sl);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setDelayTime(4000);
        ll_tab = (LinearLayout) headView.findViewById(R.id.layout_lottery_head_ll_tab);
        rl_process = (RelativeLayout) headView.findViewById(R.id.layout_lottery_head_rl_process);
        rl_request = (RelativeLayout) headView.findViewById(R.id.layout_lottery_head_rl_request);
        rl_process.setOnClickListener(this);
        rl_request.setOnClickListener(this);
        tv_process = (TextView) headView.findViewById(R.id.layout_lottery_head_tv_process);
        tv_request = (TextView) headView.findViewById(R.id.layout_lottery_head_tv_request);
        iv_request = (ImageView) headView.findViewById(R.id.layout_lottery_head_iv_request);
        v_process = headView.findViewById(R.id.layout_lottery_head_v_process);
        v_request = headView.findViewById(R.id.layout_lottery_head_v_request);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        params.width = w;
        params.height = (int) ((float) (2 * w) / 5);
        mBanner.setLayoutParams(params);

        getHeadViewData();
        lv.addHeaderView(headView);
    }

    private void getHeadViewData() {
        if (!isAdCanLoad) {
            return;
        }
        isAdCanLoad = false;
        RequestParams params = new RequestParams(HttpFinal.HOME_INDEX);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(getActivity(), params, new TypeToken<Result<BannerData>>() {
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
                BannerData bannerData = (BannerData) result.getData();
                if (bannerData != null)
                    initListHeadView(bannerData);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
                isAdCanLoad = true;
            }
        });
    }

    /**
     * 初始化listview头部的内容
     */
    private void initListHeadView(final BannerData bannerData) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bannerData.getBanners() != null && bannerData.getBanners().size() > 0) {//winner
                            initAd(bannerData.getBanners());
                        }
                        if (bannerData.getWinners() != null && bannerData.getWinners().size() > 0) {//winner
                            winners = bannerData.getWinners();
                            if (isFirstWinner) {
                                isFirstWinner = false;
                                handler.postDelayed(runnable, 4000);
                            }
                            upMarqueeTextView.setText(StringUtils.getWinner(bannerData.getWinners().get(bannerPageCount)));
                            upMarqueeTextView.setPosition(bannerPageCount);
                            upMarqueeTextView.setOnNameClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtils.startToPerson(getActivity(), winners.get(upMarqueeTextView.getPosition()).getUid());
                                }
                            });
                            upMarqueeTextView.setOnPeriosClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentUtils.startToLotteryDetail(getActivity(), winners.get(upMarqueeTextView.getPosition()).getPeriod());
                                }
                            });
                        } else {
                            ll_ad.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //要做的事情
            handler.sendEmptyMessage(102);
            handler.postDelayed(this, 4000);
        }
    };

    /**
     * 首页轮播
     */
    private void initAd(final List<Banner> bannersBeans) {
        final List<String> list = new ArrayList<>();
        for (Banner b : bannersBeans) {
            list.add(b.getImgUrl());
        }
        mBanner.setImages(list);
        mBanner.setOnBannerClickListener(new com.youth.banner.Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                position -= 1;
                if (position >= 0 && position < list.size()) {
                    if (!TextUtils.isEmpty(bannersBeans.get(position).getAppUrl())) {
                        if (!IntentUtils.checkURL(getActivity(), bannersBeans.get(position).getAppUrl())) {
                            IntentUtils.startToWebView(getActivity(), bannersBeans.get(position).getAppUrl());
                        }
                    } else if (!TextUtils.isEmpty(bannersBeans.get(position).getHtmlUrl())) {
                        IntentUtils.startToWebView(getActivity(), bannersBeans.get(position).getHtmlUrl());
                    }
                }
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 102:
                    int size = winners.size();
                    bannerPageCount = ++bannerPageCount % size;
                    upMarqueeTextView.setText(StringUtils.getWinner(winners.get(bannerPageCount)));
                    upMarqueeTextView.setPosition(bannerPageCount);
                    upMarqueeTextView.setOnNameClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtils.startToPerson(getActivity(), winners.get(upMarqueeTextView.getPosition()).getUid());
                        }
                    });
                    upMarqueeTextView.setOnPeriosClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtils.startToLotteryDetail(getActivity(), winners.get(upMarqueeTextView.getPosition()).getPeriod());
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        onEventClick(v);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isCanLoad = true;
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
