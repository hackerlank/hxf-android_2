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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.FeedInfo;
import com.goodhappiness.bean.FeedInfoList;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.GestureListener;
import com.goodhappiness.dao.OnGestureScrollListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.ui.social.PersonActivity;
import com.goodhappiness.ui.social.SocialDetailActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_post_publish)
public class PostPublishFragment extends BaseFragment {
    @ViewInject(R.id.post_publish_gv)
    private GridView gv;
    @ViewInject(R.id.fragment_post_publish_ll_no_photo)
    private LinearLayout ll;

    private String uid;

    private CommonAdapter<FeedInfo> adapter;
    private boolean hasMore = true;
    private List<FeedInfo> list = new ArrayList<>();
    public int page = 1;
    private int firstPosition = 0;
    private GestureDetector mGesture = null;

    public PostPublishFragment() {
        super(R.layout.fragment_post_publish);
    }

    public static PostPublishFragment newInstance(String uid) {
        PostPublishFragment fragment = new PostPublishFragment();
        Bundle args = new Bundle();
        args.putString(FieldFinals.UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void reload() {
        initList();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.social_person));
        if (GoodHappinessApplication.isDeleteFeed && PreferencesUtil.getIntPreferences(getActivity(), FieldFinals.POSITION, -1) != -1) {
            if (list.size() > PreferencesUtil.getIntPreferences(getActivity(), FieldFinals.POSITION, -1)) {
                list.remove(PreferencesUtil.getIntPreferences(getActivity(), FieldFinals.POSITION, -1));
                adapter.notifyDataSetChanged();
                PreferencesUtil.setPreferences(getActivity(), FieldFinals.POSITION, -1);
                GoodHappinessApplication.isDeleteFeed = false;
            }
        }
        page =1;
        handler.sendEmptyMessageDelayed(0,500);
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.social_person));
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
//        onRefresh(SwipyRefreshLayoutDirection.TOP);

        handler.sendEmptyMessageDelayed(0,500);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initList();
        }
    };

    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.PUBLISH);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, TextUtils.isEmpty(uid) ? getUid() + "" : uid);
        params.addBodyParameter(FieldFinals.PAGE, page + "");
        HttpUtils.post(getActivity(),params, new TypeToken<Result<FeedInfoList>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                dialog.show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                if (page == 1) {
                    list.clear();
                }
                FeedInfoList feedInfo = (FeedInfoList) result.getData();
                if (feedInfo.getList().size() > 0) {
                    list.addAll(feedInfo.getList());
                    adapter.notifyDataSetChanged();
                }
                if (feedInfo.getMore() != 1) {
                    hasMore = false;
                }
                if (feedInfo.getUserInfo() != null && feedInfo.getUserInfo().getUid() != 0) {
                    PersonActivity personActivity = (PersonActivity) getActivity();
                    personActivity.setData(feedInfo.getUserInfo());
                }
                if (list.size() > 0) {
                    ll.setVisibility(View.GONE);
                } else {
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
                dialog.dismiss();
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<FeedInfo>(getActivity(), list, R.layout.layout_grid_img) {
            @Override
            public void convert(ViewHolder helper, FeedInfo item, int position) {
                ImageView iv = helper.getView(R.id.layout_grid_iv);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                params.height = params.width = (int) ((GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 40) / 3);
                iv.setLayoutParams(params);
                if(item.getPostFiles().get(0).getFileType()!=3){
//                    helper.loadImage(iv, item.getPostFiles().get(0).getFileUrl()+StringFinal.IMG_100);
                    Glide.with(getActivity()).load(item.getPostFiles().get(0).getFileUrl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).fallback(R.mipmap.loading_default).error(R.mipmap.loading_default).into(iv);
                }else{
                    helper.loadImage(iv, item.getPostFiles().get(0).getFileUrl()+ StringFinal.VIDEO_URL_END_100);
                }
                if(item.getPostFiles()!=null&&item.getPostFiles().size()>1){
                    helper.setVisibility(R.id.tv_count,View.VISIBLE);
                    helper.setText(R.id.tv_count,item.getPostFiles().size()+"");
                }else{
                    helper.setVisibility(R.id.tv_count,View.GONE);
                }
            }
        };
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SocialDetailActivity.class);
                intent.putExtra(FieldFinals.FEED_ID, list.get(position).getFeedId());
                intent.putExtra(FieldFinals.POSITION, position);
                startActivity(intent);
            }
        });
    }

    private void initGesture() {
        mGesture = new GestureDetector(getActivity(), new GestureListener(new OnGestureScrollListener() {
            @Override
            public void onUpScroll() {
                PersonActivity personActivity = (PersonActivity) getActivity();
                personActivity.scrollToBottom();
                if (!personActivity.isTop)
                    onRefresh();
            }

            @Override
            public void onDownScroll() {
                if (firstPosition == 0) {
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
        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        });
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstPosition = firstVisibleItem;
            }
        });
    }

    public void onRefresh() {
            page++;
            if (hasMore) {
                initList();
            } else {
                showToast(R.string.list_no_more);
            }
    }
}
