package com.goodhappiness.ui.social;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Comment;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.FeedInfo;
import com.goodhappiness.bean.Like;
import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.bean.PostUserInfoBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SocialDetail;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.dao.OnSendFlowerListener;
import com.goodhappiness.dao.OnShareClickListener;
import com.goodhappiness.dao.SingleOnclick;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.DateUtil;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.RongIMUtils;
import com.goodhappiness.utils.TextUrlUtil;
import com.goodhappiness.widget.InputMethodLayout;
import com.goodhappiness.widget.emoji.EmojiEditText;
import com.goodhappiness.widget.emoji.EmojiPopup;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.goodhappiness.widget.emoji.emoji.Emoji;
import com.goodhappiness.widget.emoji.listeners.OnEmojiBackspaceClickListener;
import com.goodhappiness.widget.emoji.listeners.OnEmojiClickedListener;
import com.goodhappiness.widget.emoji.listeners.OnEmojiPopupDismissListener;
import com.goodhappiness.widget.emoji.listeners.OnEmojiPopupShownListener;
import com.goodhappiness.widget.emoji.listeners.OnSoftKeyboardCloseListener;
import com.goodhappiness.widget.emoji.listeners.OnSoftKeyboardOpenListener;
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

@ContentView(R.layout.activity_social_detail)
public class SocialDetailActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.social_detail_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.sv)
    private ScrollView sv;
    @ViewInject(R.id.v_send_line)
    private View v_sendLine;
    @ViewInject(R.id.main_activity_root_view)
    private InputMethodLayout rootView;
    @ViewInject(R.id.social_detail_rl_top_hide)
    private RelativeLayout rl_top_hide;
    @ViewInject(R.id.social_detail_rl_bottom)
    private RelativeLayout rl_bottom;
    @ViewInject(R.id.social_detail_rl_comment)
    private RelativeLayout rl_comment;
    @ViewInject(R.id.social_detail_ll_praise_tab)
    private LinearLayout ll_praise_hide;
    @ViewInject(R.id.ll_send)
    private LinearLayout ll_send;
    @ViewInject(R.id.social_detail_ll_comment)
    private LinearLayout ll_commnet_hide;
    @ViewInject(R.id.social_detail_lv)
    private ListView lv;
    @ViewInject(R.id.social_detail_iv_praise)
    private ImageView iv_praise_bottom;
    @ViewInject(R.id.social_detail_iv_praise_tab)
    private ImageView iv_praise_tab;
    @ViewInject(R.id.social_detail_iv_comment)
    private ImageView iv_comment_tab;
    @ViewInject(R.id.social_detail_tv_praise_tab)
    private TextView tv_praise_tab;
    @ViewInject(R.id.social_detail_tv_comment)
    private TextView tv_comment_tab;
    @ViewInject(R.id.social_detail_et)
    private EmojiEditText et;
    @ViewInject(R.id.social_detail_v_praise)
    private View v_social_detail_v_praise;
    @ViewInject(R.id.social_detail_v_comment)
    private View v_social_detail_v_comment;
    @ViewInject(R.id.main_activity_emoji)
    private ImageView emojiButton;

    private EmojiPopup emojiPopup;
    private SocialDetail socialDetail;
    private View headView;
    private List<Like> focusList = new ArrayList<>();
    private CommonAdapter<Like> focusAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private CommonAdapter<Comment> commentAdapter;
    private RelativeLayout rl_top;
    private GridLayout gv;
    private LinearLayout ll_praise, ll_comment;
    private ImageView iv_head, iv_pic, iv_praise_tab_head, iv_comment_tab_head, iv_video, iv_0, iv_1, iv_2, iv_3, iv_4, iv_5, iv_6, iv_7, iv_8, iv_gif0, iv_gif1, iv_gif2, iv_gif3, iv_gif4, iv_gif5, iv_gif6, iv_gif7, iv_gif8;
    private TextView tv_name, tv_location, tv_time, tv_des, tv_praise_tab_head, tv_comment_tab_head, tv_duration;
    private View v_social_detail_v_praise_head, v_social_detail_v_comment_head;
    private Dialog shareDialog;
    private long feed_id = -1;
    private int commentPage = 1;
    private int focusPage = 1;
    private String action = FieldFinals.COMMENT;
    private boolean isLikeLoad = false;
    private boolean isCommentCanLoad = true;
    private boolean isLikeCanLoad = true;
    private long comment_id;
    private Dialog operateDialog;
    private boolean isCommentClick = true;
    private int operateCount = 0;
    private int keyboardHeight = 0;
    private int scrollDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter2 = new IntentFilter(StringFinal.BROADCAST_FLOWER_SEND);
        sendFlowerSuccessBroadcastReceiver = new SendFlowerSuccessBroadcastReceiver();
        registerReceiver(sendFlowerSuccessBroadcastReceiver, filter2);
    }

    private SendFlowerSuccessBroadcastReceiver sendFlowerSuccessBroadcastReceiver;

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (action.equals(FieldFinals.COMMENT)) {
            commentPage = 1;
        } else {
            focusPage = 1;
        }
        initList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (action.equals(FieldFinals.COMMENT)) {
            if (isCommentCanLoad) {
                commentPage++;
                initList();
                return true;
            } else {
                showToast(getString(R.string.list_no_more));
                return false;
            }
        } else {
            if (isLikeCanLoad) {
                focusPage++;
                initList();
                return true;
            } else {
                showToast(getString(R.string.list_no_more));
                return false;
            }
        }
    }

    private void endRefreshing() {
        if (action.equals(FieldFinals.COMMENT)) {
            if (commentPage == 1) {
                srl.endRefreshing();
            } else {
                srl.endLoadingMore();
            }
        } else {
            if (focusPage == 1) {
                srl.endRefreshing();
            } else {
                srl.endLoadingMore();
            }
        }

    }

    private class SendFlowerSuccessBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StringFinal.BROADCAST_FLOWER_SEND) && intent.getStringExtra(FieldFinals.ACTION).contains("SocialDetailActivity")) {
                if (intent.getBooleanExtra(FieldFinals.RESULT, false)) {
                    RongIMUtils.sendFlower(intent.getIntExtra(FieldFinals.COUNT, 0), socialDetail.getFeedInfo().getPostUserInfo().getNickname(), String.valueOf(socialDetail.getFeedInfo().getPostUserInfo().getUid()),
                            new RongIMClient.SendMessageCallback() {
                                @Override
                                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {

                                }
                            }, new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    DialogFactory.createFlowerSendDialog(SocialDetailActivity.this);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                } else {
                    DialogFactory.createDefaultDialog(SocialDetailActivity.this, getString(R.string.send_flower_fail));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.social_detail));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.social_detail));
        MobclickAgent.onPause(this);
        if (socialDetail != null && tv_comment_tab.getText().toString().length() > 0 && tv_praise_tab.getText().toString().length() > 0) {
            if (PreferencesUtil.getPreferences(this, FieldFinals.FEED_INFO) == null) {
                FeedInfo feedInfo = new FeedInfo();
                feedInfo.setFeedId(socialDetail.getFeedInfo().getFeedId());
                feedInfo.setCommentNum(Integer.valueOf(tv_comment_tab.getText().toString()));
                feedInfo.setLikeNum(Integer.valueOf(tv_praise_tab.getText().toString()));
                feedInfo.setIsLike(socialDetail.getFeedInfo().getIsLike());
                PreferencesUtil.setPreferences(this, FieldFinals.FEED_INFO, feedInfo);
                GoodHappinessApplication.isNeedFocusUpdate = true;
                GoodHappinessApplication.isNeedWorldUpdate = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendFlowerSuccessBroadcastReceiver);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.text);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), true));
        keyboardHeight = PreferencesUtil.getIntPreferences(this, FieldFinals.KEYBOARD_HEIGHT, 0);
        initHeadView();
//        initFocusAdapter();
        initCommentAdapter();
        if (getIntent() != null) {
            if (getIntent().getLongExtra(FieldFinals.FEED_ID, -1) != -1) {
                feed_id = getIntent().getLongExtra(FieldFinals.FEED_ID, -1);
                initList();
            }
        }
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emojiPopup.toggle();
            }
        });
        setUpEmojiPopup();
        rootView.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {// 监听软键盘状态

            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case InputMethodLayout.KEYBOARD_STATE_SHOW:// 打开软键盘
//                        showToast("打开软键盘");
//                        showKeyBoard();
                        break;
                    case InputMethodLayout.KEYBOARD_STATE_HIDE:// 关闭软键盘
//                        showToast("关闭软键盘");
                        dismissKeyBoard();
                        break;
                }
            }
        });
    }

    @Override
    protected void reload() {
        if (getIntent() != null) {
            if (getIntent().getLongExtra(FieldFinals.FEED_ID, -1) != -1) {
                feed_id = getIntent().getLongExtra(FieldFinals.FEED_ID, -1);
                initList();
            }
        }
    }

    private void initCommentAdapter() {
        commentAdapter = new CommonAdapter<Comment>(this, commentList, R.layout.layout_list_praise) {
            @Override
            public void convert(ViewHolder helper, final Comment item, final int position) {
                helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                if (item.getFromUserInfo() != null) {
                    helper.loadImage(R.id.layout_list_praise_iv_head, item.getFromUserInfo().getAvatar());
                    helper.setText(R.id.layout_list_fragment_social_tv_name, item.getFromUserInfo().getNickname());

                    if (item.getToUserInfo() != null && !TextUtils.isEmpty(item.getToUserInfo().getNickname())) {
                        SpannableString ss = new SpannableString("回复@" + item.getToUserInfo().getNickname() + ":" + item.getContent());
                        //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
                        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.black_333_text)), 0, 2,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.advert_blue_text)), 2, item.getToUserInfo().getNickname().length() + 4,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ss.setSpan(new ForegroundColorSpan(getTheColor(R.color.gray_999_text)), item.getToUserInfo().getNickname().length() + 4, ss.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setEmojiText(R.id.layout_list_fragment_social_tv_summery, ss);
                    } else {
                        helper.setEmojiText(R.id.layout_list_fragment_social_tv_summery, item.getContent());
                    }
                    EmojiTextView emojiTextView = helper.getView(R.id.layout_list_fragment_social_tv_summery);
                    emojiTextView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (item.getFromUserInfo().getUid() == getUid()) {
                                toastOperationDialog(position, true);
                            } else {
                                toastOperationDialog(position, false);
                            }
                            return false;
                        }
                    });
                    ImageView imageView = helper.getView(R.id.layout_list_praise_iv_head);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IntentUtils.startToPerson(SocialDetailActivity.this, item.getFromUserInfo().getUid(), SocialDetailActivity.class.getName());
                        }
                    });
                }
            }
        };
        lv.setAdapter(commentAdapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (headView.getTop() != 0) {
                    if (headView.getTop() + headView.getHeight() - rl_top.getHeight() <= 1) {
                        rl_top_hide.setVisibility(View.VISIBLE);
                    } else {
                        rl_top_hide.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (commentList.size() > 0 && position - 1 >= 0) {
                    if (isUserLogined()) {
                        if (commentList.get(position - 1).getFromUserInfo().getUid() != getUid()) {
                            showKeyBoard("回复@" + commentList.get(position - 1).getFromUserInfo().getNickname(), position);
                            comment_id = commentList.get(position - 1).getCommentId();
                        } else {//删除评论
                            toastOperationDialog(position - 1, true);
                        }
                    }
                }
            }
        });
    }

    private void toastOperationDialog(final int position, final boolean isMyself) {
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                switch (position2) {
                    case 0://复制
                        ClipboardManager cmb = (ClipboardManager) SocialDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(commentList.get(position).getContent());
                        showToast(R.string.copySuccess);
                        break;
                    case 1://删除
                        if (isMyself) {
                            deleteComment(commentList.get(position).getCommentId(), commentList.get(position).getFeedId(), position);
                        } else {
                            if (socialDetail != null)
                                IntentUtils.startToReport(SocialDetailActivity.this, ReportActivity.REPORT_TYPE_COMMENT, socialDetail.getFeedInfo().getFeedId());
                        }
                        break;
                }
                operateDialog.dismiss();
            }
        };
        operateDialog = DialogFactory.createCommentOperationDialog(SocialDetailActivity.this, onItemClickListener, isMyself);
    }

    private void deleteComment(long commentId, long feedId, final int position) {
        RequestParams params = new RequestParams(HttpFinal.COMMENT_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, feedId + "");
        params.addBodyParameter(FieldFinals.COMMENTID, commentId + "");
        HttpUtils.post(this, params, new TypeToken<Result<BaseBean>>() {
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
                showToast(getString(R.string.deleteSuccess));
                commentList.remove(position);
                operateCount = -1;
                setHeadData();
                commentAdapter.notifyDataSetChanged();
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


    private void initList() {
        RequestParams params = new RequestParams(HttpFinal.POST_INDEX);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, feed_id + "");
        params.addBodyParameter(FieldFinals.PAGE, (action.equals(FieldFinals.COMMENT) ? commentPage : focusPage) + "");
        params.addBodyParameter(FieldFinals.ACTION, action);
        HttpUtils.post(this, params, new TypeToken<Result<SocialDetail>>() {
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
                socialDetail = (SocialDetail) result.getData();
                operateCount = 0;
                setHeadData();
                if (action.equals(FieldFinals.COMMENT)) {
                    if (commentPage == 1) {
                        commentList.clear();
                    }
                    commentList.addAll(socialDetail.getComment().getCommentList());
                    if (socialDetail.getComment().getMore() != 1) {
                        isCommentCanLoad = false;
                    } else {
                        isCommentCanLoad = true;
                    }
                    commentAdapter.notifyDataSetChanged();
                } else {
                    if (focusPage == 1) {
                        focusList.clear();
                    }
                    focusList.addAll(socialDetail.getLike().getLikeList());
                    if (socialDetail.getComment().getMore() != 1) {
                        isLikeCanLoad = false;
                    } else {
                        isLikeCanLoad = true;
                    }
                    focusAdapter.notifyDataSetChanged();
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
                endRefreshing();
            }
        });
    }

    private void initFocusAdapter() {
        focusAdapter = new CommonAdapter<Like>(this, focusList, R.layout.layout_list_praise) {
            @Override
            public void convert(ViewHolder helper, final Like item, final int position) {
                TextView tvStatus = helper.getView(R.id.layout_list_fragment_social_tv_focus);
                helper.setVisibility(R.id.layout_list_fragment_social_tv_time, View.GONE);
                helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.VISIBLE);
                switch (item.getRelation()) {
                    case 0:
                        helper.setVisibility(R.id.layout_list_fragment_social_tv_focus, View.GONE);
                        break;
                    case 1:
                        tvStatus.setText(getString(R.string.focus_status_no));
                        tvStatus.setBackgroundResource(R.drawable.shape_for_back_stroke);
                        tvStatus.setTextColor(getTheColor(R.color.black_333_text));
                        break;
                    case 3:
                        tvStatus.setText(getString(R.string.focus_status_each));
                        tvStatus.setBackgroundResource(R.drawable.shape_for_yellow);
                        tvStatus.setTextColor(getTheColor(R.color.black_333_text));
                        break;
                    case 2:
                        tvStatus.setText(getString(R.string.focus_status_yes));
                        tvStatus.setBackgroundResource(R.drawable.shape_for_black_focus);
                        tvStatus.setTextColor(getTheColor(R.color.white));
                        break;
                }
                tvStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        focus(item);
                    }
                });
                helper.setText(R.id.layout_list_fragment_social_tv_name, item.getNickname());
                helper.loadImage(R.id.layout_list_praise_iv_head, item.getAvatar());
                helper.setVisibility(R.id.layout_list_fragment_social_tv_summery, View.GONE);
                View linearLayout = helper.getView(R.id.layout_list_praise);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                params.height = (int) (GoodHappinessApplication.perHeight * 114);
                linearLayout.setLayoutParams(params);
            }
        };
        lv.setAdapter(focusAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0)
                    IntentUtils.startToPerson(SocialDetailActivity.this, focusList.get(position - 1).getUid(), SocialDetailActivity.class.getName());
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (headView.getTop() != 0) {
                    if (headView.getTop() + headView.getHeight() - rl_top.getHeight() <= 1) {
                        rl_top_hide.setVisibility(View.VISIBLE);
                    } else {
                        rl_top_hide.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void focus(final Like item) {
        if (item.getUid() == getUid()) {
            return;
        }
        RequestParams params = new RequestParams(item.getRelation() == 1 ? HttpFinal.FRIENDSHIP_CREATE : HttpFinal.FRIENDSHIP_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, item.getUid() + "");
        HttpUtils.post(this, params, new TypeToken<Result<PostUserInfoBean>>() {
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
                PostUserInfoBean postUserInfoBean = (PostUserInfoBean) result.getData();
                if (item.getRelation() == 1) {
                    showToast(R.string.focusSuccess);
                } else {
                    showToast(R.string.focusCancel);
                }
                int i = 0;
                if (postUserInfoBean.getUid() != 0) {
                    for (Like feedInfo : focusList) {
                        if (item.getUid() == feedInfo.getUid()) {
                            focusList.get(i).setRelation(postUserInfoBean.getRelation());
                        }
                        i++;
                    }
                    focusAdapter.notifyDataSetChanged();
                }
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

    private void setHeadData() {
        if (socialDetail != null) {
            if(!TextUtils.isEmpty(socialDetail.getFeedInfo().getPostUserInfo().getAvatar())){
                displayImage(iv_head, socialDetail.getFeedInfo().getPostUserInfo().getAvatar());
            }
//            displayImage(iv_pic, socialDetail.getFeedInfo().getPostFiles().get(0).getFileUrl());
            displayPostFile(socialDetail.getFeedInfo().getPostFiles());
            if (socialDetail.getFeedInfo().getPostUserInfo().getUid() == getUid()) {
                v_sendLine.setVisibility(View.GONE);
                ll_send.setVisibility(View.GONE);
            }
            tv_name.setText(socialDetail.getFeedInfo().getPostUserInfo().getNickname());
            if (TextUtils.isEmpty(tv_comment_tab.getText().toString())) {
                tv_comment_tab.setText(socialDetail.getFeedInfo().getCommentNum() + "");
                tv_comment_tab_head.setText(socialDetail.getFeedInfo().getCommentNum() + "");
            } else {
                tv_comment_tab.setText((Integer.valueOf(tv_comment_tab.getText().toString()) + operateCount) + "");
                tv_comment_tab_head.setText((Integer.valueOf(tv_comment_tab_head.getText().toString()) + operateCount) + "");
            }
            if (tv_praise_tab.getText().toString().length() == 0)
                tv_praise_tab.setText(socialDetail.getFeedInfo().getLikeNum() + "");
            if (tv_praise_tab_head.getText().toString().length() == 0)
                tv_praise_tab_head.setText(socialDetail.getFeedInfo().getLikeNum() + "");
//            if (TextUtils.isEmpty(socialDetail.getFeedInfo().getPostContent())) {
//                tv_des.setVisibility(View.GONE);
//            } else {
//                tv_des.setText(socialDetail.getFeedInfo().getPostContent());
//            }
            if (TextUtils.isEmpty(socialDetail.getFeedInfo().getPostContent())) {
                if (socialDetail.getFeedInfo().getTagArray() != null && socialDetail.getFeedInfo().getTagArray().size() > 0) {
                    SpannableString s = new SpannableString(TextUrlUtil.getTagString(socialDetail.getFeedInfo().getTagArray()));
                    TextUrlUtil.dealContent(s, tv_des, R.color.advert_blue_text, new TextUrlUtil.OnClickString() {
                        @Override
                        public void OnClick(String s) {
                            startToSearchTag(s);
                        }
                    });
                } else {
                    tv_des.setVisibility(View.GONE);
                }
            } else {
                SpannableString s = new SpannableString(TextUrlUtil.getTagString(socialDetail.getFeedInfo().getTagArray()) + socialDetail.getFeedInfo().getPostContent());
                TextUrlUtil.dealContent(s, tv_des, R.color.advert_blue_text, new TextUrlUtil.OnClickString() {
                    @Override
                    public void OnClick(String s) {
                        startToSearchTag(s);
                    }
                });
            }
            if (socialDetail.getFeedInfo().getIsLike() == 1) {
                iv_praise_bottom.setImageResource(R.mipmap.ico_like_click);
            } else {
                iv_praise_bottom.setImageResource(R.mipmap.ico_like);
            }
            tv_time.setText(DateUtil.getPublishTime(socialDetail.getFeedInfo().getCreated()));
        }
    }

    private void startToSearchTag(String s) {
        s = s.substring(8, s.length() - 1);
        IntentUtils.startToSearchTag(this, s);
    }

    private int ImageId[] = {R.id.img_0, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_4, R.id.img_5, R.id.img_6, R.id.img_7, R.id.img_8};
    private int gifId[] = {R.id.img_00, R.id.img_01, R.id.img_02, R.id.img_03, R.id.img_04, R.id.img_05, R.id.img_06, R.id.img_07, R.id.img_08};

    private void displayPostFile(final ArrayList<PostFilesBean> postFiles) {
        if (postFiles.size() > 0) {
            if (postFiles.size() == 1) {
                iv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (postFiles.size() > 0 && postFiles.get(0).getFileType() == 1) {
                            IntentUtils.startToDisplayImage(SocialDetailActivity.this, postFiles.get(0).getFileUrl());
                        }
                        if (postFiles.size() > 0 && postFiles.get(0).getFileType() == 3) {
                            IntentUtils.startToVideo(SocialDetailActivity.this, postFiles.get(0).getFileUrl());
                        }
                        if (postFiles.size() > 0 && postFiles.get(0).getFileType() == 4) {
                            IntentUtils.startToDisplayImageGif(SocialDetailActivity.this, postFiles.get(0).getFileUrl());
                        }
                    }
                });
                iv_pic.setVisibility(View.VISIBLE);
                gv.setVisibility(View.GONE);
                if (postFiles.get(0).getFileType() == 1) {
                    iv_video.setVisibility(View.GONE);
                    if(postFiles.get(0).getFileUrl().contains(FieldFinals.WATERMARK)){
                        displayImage(iv_pic, postFiles.get(0).getFileUrl()+StringFinal.IMG_200);
                    }else{
                        displayImage(iv_pic, postFiles.get(0).getFileUrl()+StringFinal.IMG2_200);
                    }
                } else if (postFiles.get(0).getFileType() == 3) {
                    iv_video.setVisibility(View.VISIBLE);
                    tv_duration.setVisibility(View.VISIBLE);
                    tv_duration.setText(postFiles.get(0).getDuration());
                    displayImage(iv_pic, postFiles.get(0).getFileUrl() + StringFinal.VIDEO_URL_END+StringFinal.IMG_200);
                } else if (postFiles.get(0).getFileType() == 4) {
                    if (postFiles.get(0).getFileUrl().contains(FieldFinals.WATERMARK)) {
                        String s[] = postFiles.get(0).getFileUrl().split("\\?");
                        displayImage(iv_pic, s[0] + StringFinal.GIF_URL_END+StringFinal.IMG_200);
                    } else {
                        displayImage(iv_pic, postFiles.get(0).getFileUrl() + StringFinal.GIF_URL_END+StringFinal.IMG_200);
                    }
                    iv_video.setVisibility(View.VISIBLE);
                    iv_video.setImageResource(R.mipmap.btn_gif);
                }
            } else {
                iv_pic.setVisibility(View.GONE);
                iv_video.setVisibility(View.GONE);
                gv.setVisibility(View.VISIBLE);
                int a = postFiles.size() / 3;
                int b = postFiles.size() % 3;
                if (b > 0) {
                    a++;
                }
                float width = (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 40) / 3;
                gv.getLayoutParams().height = (int) ((a * width) + GoodHappinessApplication.perHeight * 6 * a);

                for (int i = 0; i < 9; i++) {
                    setVisibility(ImageId[i], View.GONE);
                    setVisibility(gifId[i], View.GONE);
                }

                for (int i = 0; i < postFiles.size(); i++) {
                    ImageView imgview = getView(ImageId[i]);
                    imgview.setOnClickListener(new SingleOnclick(SocialDetailActivity.this, i, postFiles));
                    setVisibility(ImageId[i], View.VISIBLE);
                    imgview.getLayoutParams().width = (int) width;
                    imgview.getLayoutParams().height = (int) width;
                    if (postFiles.get(i).getFileType() == 1) {
                        setVisibility(gifId[i], View.GONE);
                        if (postFiles.get(i).getFileUrl().contains(FieldFinals.WATERMARK)) {
                            displayImage(imgview, postFiles.get(i).getFileUrl()+StringFinal.IMG_100);
                        }else{
                            displayImage(imgview, postFiles.get(i).getFileUrl()+StringFinal.IMG2_100);
                        }
                    } else if (postFiles.get(i).getFileType() == 4) {
                        setVisibility(gifId[i], View.VISIBLE);
                        if (postFiles.get(i).getFileUrl().contains(FieldFinals.WATERMARK)) {
                            String s[] = postFiles.get(i).getFileUrl().split("\\?");
                            displayImage(imgview, s[0] + StringFinal.GIF_URL_END+StringFinal.IMG_100);
                        } else {
                            displayImage(imgview, postFiles.get(i).getFileUrl() + StringFinal.GIF_URL_END+StringFinal.IMG_100);
                        }
//                        displayImage(imgview, postFiles.get(i).getFileUrl() + StringFinal.GIF_URL_END);
                    }
                }
            }
        } else {
            iv_pic.setImageResource(R.mipmap.loading_default);
        }
    }

    private ImageView getView(int i) {
        return (ImageView) headView.findViewById(i);
    }

    private void setVisibility(int i, int a) {
        headView.findViewById(i).setVisibility(a);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.layout_list_head_social_detail, lv, false);
        iv_head = (ImageView) headView.findViewById(R.id.head_social_detail_iv_head);
        ll_praise = (LinearLayout) headView.findViewById(R.id.head_social_detail_ll_praise_tab);
        ll_comment = (LinearLayout) headView.findViewById(R.id.head_social_detail_ll_comment);
        tv_name = (TextView) headView.findViewById(R.id.head_social_detail_tv_name);
        rl_top = (RelativeLayout) headView.findViewById(R.id.social_detail_rl_top);
        tv_location = (TextView) headView.findViewById(R.id.head_social_detail_tv_location);
        tv_time = (TextView) headView.findViewById(R.id.head_social_detail_tv_time);
        tv_duration = (TextView) headView.findViewById(R.id.tv_duration);
        iv_pic = (ImageView) headView.findViewById(R.id.head_social_detail_iv_pic);
        iv_video = (ImageView) headView.findViewById(R.id.img_video);
        gv = (GridLayout) headView.findViewById(R.id.gridview);
        iv_0 = (ImageView) headView.findViewById(R.id.img_0);
        iv_1 = (ImageView) headView.findViewById(R.id.img_1);
        iv_2 = (ImageView) headView.findViewById(R.id.img_2);
        iv_3 = (ImageView) headView.findViewById(R.id.img_3);
        iv_4 = (ImageView) headView.findViewById(R.id.img_4);
        iv_5 = (ImageView) headView.findViewById(R.id.img_5);
        iv_6 = (ImageView) headView.findViewById(R.id.img_6);
        iv_7 = (ImageView) headView.findViewById(R.id.img_7);
        iv_8 = (ImageView) headView.findViewById(R.id.img_8);
        iv_gif0 = (ImageView) headView.findViewById(R.id.img_00);
        iv_gif1 = (ImageView) headView.findViewById(R.id.img_01);
        iv_gif2 = (ImageView) headView.findViewById(R.id.img_02);
        iv_gif3 = (ImageView) headView.findViewById(R.id.img_03);
        iv_gif4 = (ImageView) headView.findViewById(R.id.img_04);
        iv_gif5 = (ImageView) headView.findViewById(R.id.img_05);
        iv_gif6 = (ImageView) headView.findViewById(R.id.img_06);
        iv_gif7 = (ImageView) headView.findViewById(R.id.img_07);
        iv_gif8 = (ImageView) headView.findViewById(R.id.img_08);
        tv_des = (TextView) headView.findViewById(R.id.head_social_detail_tv_des);
        iv_praise_tab_head = (ImageView) headView.findViewById(R.id.head_social_detail_iv_praise_tab);
        tv_praise_tab_head = (TextView) headView.findViewById(R.id.head_social_detail_tv_praise_tab);
        v_social_detail_v_praise_head = headView.findViewById(R.id.head_social_detail_v_praise);
        iv_comment_tab_head = (ImageView) headView.findViewById(R.id.head_social_detail_iv_comment);
        tv_comment_tab_head = (TextView) headView.findViewById(R.id.head_social_detail_tv_comment);
        tv_comment_tab_head = (TextView) headView.findViewById(R.id.head_social_detail_tv_comment);
        v_social_detail_v_comment_head = headView.findViewById(R.id.head_social_detail_v_comment);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv_pic.getLayoutParams();
//        params.height = GoodHappinessApplication.w;
//        iv_pic.setLayoutParams(params);
        ll_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvent(v);
            }
        });
        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvent(v);
            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socialDetail != null && socialDetail.getFeedInfo() != null) {
                    IntentUtils.startToPerson(SocialDetailActivity.this, socialDetail.getFeedInfo().getPostUserInfo().getUid(), SocialDetailActivity.class.getName());
                }
            }
        });
        lv.addHeaderView(headView);
    }


    @Event({R.id.ll_send, R.id.ll_more, R.id.social_detail_rl_send, R.id.social_detail_rl_comment, R.id.social_detail_ll_comment_bottom, R.id.ll_praise_bottom, R.id.social_detail_ll_praise_tab, R.id.social_detail_ll_comment})
    private void onEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_send:
                if (socialDetail != null && isUserLogined()) {
                    if (IntentUtils.checkHasBind(this)) {
                        RongIMUtils.setUserInfoProvider(new UserInfo(socialDetail.getFeedInfo().getPostUserInfo().getUid() + "", socialDetail.getFeedInfo().getPostUserInfo().getNickname(), Uri.parse(socialDetail.getFeedInfo().getPostUserInfo().getAvatar())));
                        DialogFactory.createSendFlowerDialog(this, new OnSendFlowerListener() {
                            @Override
                            public void onclick(Integer count) {
                                confirmFlowerOrder(count);
                            }
                        });
                    }
                }
                break;
            case R.id.head_social_detail_ll_praise_tab:
            case R.id.social_detail_ll_praise_tab:
                if (isCommentClick) {
                    initFocusAdapter();
                }
                isCommentClick = false;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v_social_detail_v_praise.getLayoutParams();
                params.width = ll_praise.getWidth();
                params.leftMargin = ll_praise.getLeft();
                v_social_detail_v_praise.setLayoutParams(params);
                v_social_detail_v_praise_head.setLayoutParams(params);
                v_social_detail_v_praise.setBackgroundColor(getTheColor(R.color.theme_color));
                v_social_detail_v_praise_head.setBackgroundColor(getTheColor(R.color.theme_color));
                v_social_detail_v_comment.setBackgroundColor(getTheColor(R.color.white));
                v_social_detail_v_comment_head.setBackgroundColor(getTheColor(R.color.white));
                iv_praise_tab.setImageResource(R.mipmap.ico_like_click);
                iv_praise_tab_head.setImageResource(R.mipmap.ico_like_click);
                iv_comment_tab.setImageResource(R.mipmap.ico_comment);
                iv_comment_tab_head.setImageResource(R.mipmap.ico_comment);
                action = FieldFinals.LIKE;
                if (focusPage == 1) {
                    initList();
                }
                break;
            case R.id.head_social_detail_ll_comment:
            case R.id.social_detail_ll_comment:
                if (!isCommentClick) {
                    initCommentAdapter();
                }
                isCommentClick = true;
                v_social_detail_v_comment.setBackgroundColor(getTheColor(R.color.theme_color));
                v_social_detail_v_comment_head.setBackgroundColor(getTheColor(R.color.theme_color));
                v_social_detail_v_praise.setBackgroundColor(getTheColor(R.color.white));
                v_social_detail_v_praise_head.setBackgroundColor(getTheColor(R.color.white));
                iv_praise_tab.setImageResource(R.mipmap.ico_like);
                iv_praise_tab_head.setImageResource(R.mipmap.ico_like);
                iv_comment_tab.setImageResource(R.mipmap.ico_comment_click);
                iv_comment_tab_head.setImageResource(R.mipmap.ico_comment_click);
                action = FieldFinals.COMMENT;
                if (commentPage == 1) {
                    initList();
                }
                break;
            case R.id.ll_praise_bottom:
                if (socialDetail != null) {
                    if (isUserLogined()) {
                        if (!isLikeLoad) {
                            praise(socialDetail.getFeedInfo().getFeedId(), socialDetail.getFeedInfo().getIsLike() == 1 ? HttpFinal.LIKE_DELETE : HttpFinal.LIKE_CREATE);
                        }
                    }
                }
                break;
            case R.id.social_detail_ll_comment_bottom:
                if (isUserLogined()) {
                    comment_id = -1;
                    showKeyBoard("", 0);
                }
                break;
            case R.id.social_detail_rl_comment:
                dismissKeyBoard();
                comment_id = -1;
                break;
            case R.id.social_detail_rl_send:
                if (comment_id != -1) {
                    sendComment(comment_id);
                } else {
                    sendComment(-1);
                }
                break;
            case R.id.ll_more:
                if (socialDetail != null && socialDetail.getFeedInfo() != null) {
                    final int mode = socialDetail.getFeedInfo().getPostUserInfo().getUid() == getUid() ? 1 : 2;
                    shareDialog = DialogFactory.createShareDialog(mode, this, new OnShareClickListener() {
                        @Override
                        public void onclick(int action) {
                            switch (action) {
                                case R.string.delete:
                                    DialogFactory.createSelectDialog(SocialDetailActivity.this, "是否删除？", new OnSelectListener() {
                                        @Override
                                        public void onSelected(boolean isSelected) {
                                            if (isSelected) {
                                                deleteFriends();
                                            }
                                        }
                                    });
                                    break;
                                case R.string.report:
                                    IntentUtils.startToReport(SocialDetailActivity.this, ReportActivity.REPORT_TYPE_FEED, socialDetail.getFeedInfo().getFeedId());
                                    break;
//                                case R.string.sina:
//                                    sendSingleMessage(new BaseShareObject("!!", "!!!!", "http://json.cn/", null, null));
//                                    break;
                            }
                        }
                    }, new String[]{getDid(), getSid(), FieldFinals.FEED, String.valueOf(socialDetail.getFeedInfo().getFeedId())});
                }
                break;
        }
    }

    private void confirmFlowerOrder(final int count) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_CONFIRM);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.UID, socialDetail.getFeedInfo().getPostUserInfo().getUid() + "");
        params.addBodyParameter(FieldFinals.NUM, count + "");
        HttpUtils.post(this, params, new TypeToken<Result<ConfirmOrder>>() {
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
                if (result != null && result.getData() != null) {
                    ConfirmOrder confirmOrder = (ConfirmOrder) result.getData();
                    confirmOrder.setOrderType(1);
                    confirmOrder.setClassName(SocialDetailActivity.this.getClass().getName());
                    confirmOrder.setFlowerCount(count);
                    IntentUtils.startToPayOrder(SocialDetailActivity.this, confirmOrder);
                }
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

    private void deleteFriends() {
        RequestParams params = new RequestParams(HttpFinal.POST_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, socialDetail.getFeedInfo().getFeedId() + "");
        HttpUtils.post(this, params, new TypeToken<Result<PostUserInfoBean>>() {
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
                if (getIntent() != null && getIntent().getIntExtra(FieldFinals.POSITION, -1) != -1) {
                    FeedInfo feedInfo = new FeedInfo();
                    feedInfo.setFeedId(socialDetail.getFeedInfo().getFeedId());
                    feedInfo.setIsDelete(true);
                    PreferencesUtil.setPreferences(SocialDetailActivity.this, FieldFinals.FEED_INFO, feedInfo);
                    PreferencesUtil.setPreferences(SocialDetailActivity.this, FieldFinals.ADD_PUBLISH, true);
                    GoodHappinessApplication.isNeedWorldUpdate = true;
                    GoodHappinessApplication.isDeleteFeed = true;
                    PreferencesUtil.setPreferences(SocialDetailActivity.this, FieldFinals.POSITION, getIntent().getIntExtra(FieldFinals.POSITION, -1));
                }
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

    private void dismissKeyBoard() {
        rl_comment.setVisibility(View.INVISIBLE);
        rl_bottom.setVisibility(View.VISIBLE);
        et.setFocusable(false);
        et.setText("");
        et.setFocusableInTouchMode(false);
        hideKeyBroad();
    }
//    @Override
//    protected void showKeyBroad(){
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(rootView, InputMethodManager.SHOW_FORCED);
//    }

    protected void hideKeyBroad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0); //强制隐藏键盘
    }

    private void showKeyBoard(String s, int count) {
        rl_bottom.setVisibility(View.GONE);
        rl_comment.setVisibility(View.VISIBLE);
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.setHint(s);
        if (TextUtils.isEmpty(s)) {//评论
            if (socialDetail != null && !TextUtils.isEmpty(socialDetail.getFeedInfo().getPostContent())) {
                scrollDistance = (int) (tv_des.getHeight() + w + keyboardHeight + 260 * GoodHappinessApplication.perHeight - h);
            } else {
                scrollDistance = (int) (w + keyboardHeight + 260 * GoodHappinessApplication.perHeight - h);
            }
        } else {//回复
            int position = 0;
            int listHeight = 0;
            for (Comment comment : commentList) {
                if (position < count) {
                    try {
                        listHeight += lv.getChildAt(position + 1).getHeight();
                        position++;
                    } catch (Exception e) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (socialDetail != null && !TextUtils.isEmpty(socialDetail.getFeedInfo().getPostContent())) {
                scrollDistance = (int) (tv_des.getHeight() + w + keyboardHeight + 334 * GoodHappinessApplication.perHeight - h + listHeight);
            } else {
                scrollDistance = (int) (w + keyboardHeight + 334 * GoodHappinessApplication.perHeight - h + listHeight);
            }
        }
        Log.e("q_", "_q:" + scrollDistance);
        showKeyBroad();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SocialDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sv.smoothScrollTo(0, scrollDistance);
                    }
                });
            }
        }, 300);
        sv.smoothScrollTo(0, scrollDistance);
    }

    private void praise(long feedID, final String action) {
        isLikeLoad = true;
        RequestParams params = new RequestParams(action);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, feedID + "");
        HttpUtils.post(this, params, new TypeToken<Result<Like>>() {
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
                Like like = (Like) result.getData();
                if (like != null) {
                    if (action.equals(HttpFinal.LIKE_CREATE)) {
                        iv_praise_bottom.setImageResource(R.mipmap.ico_like_click);
                        if (socialDetail != null) {
                            socialDetail.getFeedInfo().setIsLike(1);
                            focusList.add(like);
                            tv_praise_tab.setText((Integer.valueOf(tv_praise_tab.getText().toString()) + 1) + "");
                            tv_praise_tab_head.setText((Integer.valueOf(tv_praise_tab_head.getText().toString()) + 1) + "");
                        }
                    } else {
                        iv_praise_bottom.setImageResource(R.mipmap.ico_like);
                        if (focusList.size() > 0) {
                            int i = 0;
                            for (Like like2 : focusList) {
                                if (like2.getUid() == like.getUid()) {
                                    focusList.remove(i);
                                    break;
                                }
                                i++;
                            }
                        }
                        if (socialDetail != null) {
                            tv_praise_tab.setText((Integer.valueOf(tv_praise_tab.getText().toString()) - 1) + "");
                            tv_praise_tab_head.setText((Integer.valueOf(tv_praise_tab_head.getText().toString()) - 1) + "");
                            socialDetail.getFeedInfo().setIsLike(0);
                        }
                    }
//                    operateCount = 0;
//                    setHeadData();
                }
                focusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLikeLoad = false;
                dialog.dismiss();
            }
        });
    }

    private void sendComment(long reply_id) {
        if (TextUtils.isEmpty(et.getText().toString())) {
            return;
        }
        RequestParams params = new RequestParams(HttpFinal.COMMENT_CREATE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(this, FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.FEED_ID, feed_id + "");
        params.addBodyParameter(FieldFinals.CONTENT, et.getText().toString().trim());
        if (reply_id != -1) {
            params.addBodyParameter(FieldFinals.REPLY_ID, reply_id + "");
        }
        HttpUtils.post(this, params, new TypeToken<Result<Comment>>() {
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
                dismissKeyBoard();
                Comment commentInfo = (Comment) result.getData();
                if (commentInfo.getCommentId() != -1) {
                    List<Comment> infos = new ArrayList<>();
                    infos.add(commentInfo);
                    infos.addAll(commentList);
                    commentList.clear();
                    commentList.addAll(infos);
                    socialDetail.getFeedInfo().setCommentNum(commentList.size());
                    operateCount = 1;
                    setHeadData();
                }
                commentAdapter.notifyDataSetChanged();
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

    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON", justification = "Sample app we do not care")
    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
            @Override
            public void onEmojiBackspaceClicked(final View v) {
                Log.d("MainActivity", "Clicked on Backspace");
            }
        }).setOnEmojiClickedListener(new OnEmojiClickedListener() {
            @Override
            public void onEmojiClicked(final Emoji emoji) {
                Log.d("MainActivity", "Clicked on emoji");
            }
        }).setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
            @Override
            public void onEmojiPopupShown() {
                Log.d("MainActivity", "setOnEmojiPopupShownListener");
                emojiButton.setImageResource(R.mipmap.ico_keyboard);
            }
        }).setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
            @Override
            public void onKeyboardOpen(final int keyBoardHeight) {
                Log.d("MainActivity", "Opened soft keyboard");
            }
        }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
            @Override
            public void onEmojiPopupDismiss() {
                Log.d("MainActivity", "setOnEmojiPopupDismissListener");
                emojiButton.setImageResource(R.mipmap.ico_emoji);
            }
        }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
            @Override
            public void onKeyboardClose() {
                Log.d("MainActivity", "emojiPopup.dismiss()");
                emojiPopup.dismiss();
            }
        }).build(et);
    }
}
