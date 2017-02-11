package com.goodhappiness.ui.social.picture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.SearchHistoryAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SearchResult;
import com.goodhappiness.bean.database.SearchHistory;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.social.video.VideoPublishActivity;
import com.goodhappiness.utils.DBUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.widget.FlowLayout;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_add_tag)
public class AddTagActivity extends BaseActivity {
    public static final String TAG = AddTagActivity.class.getName();

    @ViewInject(R.id.add_tag_rl)
    private FlowLayout fl_tag;
    @ViewInject(R.id.add_tag_rv)
    private RecyclerView rv_history;
    @ViewInject(R.id.add_tag_lv)
    private ListView lv;
    private List<SearchHistory> searchHistories;
    private CommonAdapter<String> adapter;
    private List<String> list = new LinkedList<>();
    private LinkedList<TextView> viewList = new LinkedList<>();
    private EditText editText;
    private String lastString = "";
    private String etString = "";
    private Timer timer;
    private View.OnClickListener onTextClickListener;
    private String currentSelectedTag = "";
    private View currentView = null;
    public static final int RESULT_CODE = 1010;
    private int tagCount = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event({R.id.common_right_text})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.common_right_text:
                if(getIntent()!=null&&!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.ACTION))){
                    Intent intent = null;
                    switch (getIntent().getStringExtra(FieldFinals.ACTION)){
                        case PublishActivity.TAG:
                            intent = new Intent(this,PublishActivity.class);
                            break;
                        case VideoPublishActivity.TAG:
                            intent = new Intent(this,VideoPublishActivity.class);
                            break;
                        default:
                            return;
                    }
                    ArrayList<String> strings = new ArrayList<>();
                    for (TextView textView:viewList){
                        strings.add(textView.getText().toString());
                    }
                    if(editText!=null&&!TextUtils.isEmpty(editText.getText().toString())&&viewList.size()<5){
                        strings.add(editText.getText().toString());
                        DBUtils.insertSearchHistory(editText.getText().toString());
                    }
                    intent.putStringArrayListExtra(FieldFinals.STRING_LIST,strings);
                    setResult(RESULT_CODE,intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void setData() {
        initView();
        initHistory();
        onTextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTag = ((TextView) v).getText().toString();
                if (selectedTag.equals(currentSelectedTag)) {//重复点击
                    setTagBg(v, false);
                    currentSelectedTag = "";
                    currentView = null;
                } else {
                    for (View view : viewList) {
                        setTagBg(view, false);
                    }
                    setTagBg(v, true);
                    currentSelectedTag = selectedTag;
                    currentView = v;
                }
                editTextGetFocus();
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!etString.equals(lastString)) {
                    etString = lastString;
                    try {
                        if(lastString.trim().length()!=lastString.length()){
                            AddTagActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addTagToLayout(lastString.trim());
                                }
                            });
                        }
                        searchTag(lastString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        }, 1000, 500);
        tv_title.setText(R.string.addtag);
        tv_right.setText(R.string.complete);
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        adapter = new CommonAdapter<String>(this, list, R.layout.layout_list_add_tag) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                TextView textView = helper.getView(R.id.tv_add_tag);
                if (position == 0) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_tag, item);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    addTagToLayout(editText.getText().toString());
                } else {
                    addTagToLayout(list.get(position));
                }
                editTextGetFocus();
            }
        });
        editText = (EditText) LayoutInflater.from(this).inflate(R.layout.et_tag, fl_tag, false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    lastString = s.toString();
                }else{
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (editText != null) {
                        if (!TextUtils.isEmpty(currentSelectedTag)) {//delete
                            if (currentView != null) {
                                viewList.remove(currentView);
                                resetFlowerLayout();
                                currentSelectedTag = "";
                                currentView = null;
                            }
                        } else {
                            if (editText.getText().toString().length() == 0&&viewList!=null&&viewList.size()>0) {
                                setTagBg(viewList.getLast(), true);

                                currentSelectedTag = ((TextView) viewList.getLast()).getText().toString();
                                currentView = viewList.getLast();
                            } else {
                                return false;
                            }
                        }
                    }
                    editTextGetFocus();
                }
                return false;
            }
        });
        fl_tag.addView(editText);
        if(getIntent()!=null&&getIntent().getStringArrayListExtra(FieldFinals.STRING_LIST)!=null){
            for(String s:getIntent().getStringArrayListExtra(FieldFinals.STRING_LIST)){
                addTagToLayout(s);
            }
        }
    }

    private void searchTag(final String ss) throws UnsupportedEncodingException {
        if(TextUtils.isEmpty(lastString)){
            list.clear();
            handler.sendEmptyMessage(0);
            return;
        }
        if(ss.getBytes("GBK").length>12){
            list.clear();
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
            return;
        }
        list.clear();
        list.add(ss);
        handler.sendEmptyMessage(0);
        RequestParams params = new RequestParams(HttpFinal.TAG_SEARCH);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SEARCH_KEY, lastString);
        HttpUtils.post(AddTagActivity.this, params, new TypeToken<Result<SearchResult>>() {
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
                if(isFinishing()){
                    return;
                }
                if(ss.equals(lastString)){
                    SearchResult searchResult = (SearchResult) result.getData();
                    if(searchResult!=null&&searchResult.getList()!=null&&searchResult.getList().size()>0){
                        list.clear();
                        list.add(ss);
                        list.addAll(searchResult.getList());
                        handler.sendEmptyMessage(0);
                    }else{
                        list.clear();
                        list.add(ss);
                        handler.sendEmptyMessage(0);
                    }
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

    private void setTagBg(View v, boolean isSelected) {
        v.setBackgroundResource(isSelected ? R.drawable.shape_for_picture_tag_select : R.drawable.shape_for_picture_tag);
    }

    private void initHistory() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_history.setLayoutManager(manager);
        searchHistories = DBUtils.getSearchHistory();
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(this, searchHistories);
        searchHistoryAdapter.setOnItemClickLitener(new SearchHistoryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                addTagToLayout(searchHistories.get(position).getItem());
                editTextGetFocus();
            }
        });
        rv_history.setAdapter(searchHistoryAdapter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    showToast(R.string.tag_length_limit);
                    break;
            }
        }
    };

    private void addTagToLayout(String s) {
        if (TextUtils.isEmpty(s)) {
            showToast(R.string.please_input_all_msg);
            return;
        }
        for (View v : viewList) {
            if (((TextView) v).getText().toString().equals(s)) {
                showToast(R.string.have_tag);
                return;
            }
        }
        if (fl_tag.getChildCount() >= tagCount) {
            showToast(R.string.input_tag_limit);
            return;
        }

        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_tag, fl_tag, false);
        textView.setText(s);
        textView.setOnClickListener(onTextClickListener);
        viewList.add(textView);
        DBUtils.insertSearchHistory(s);
        resetFlowerLayout();
        resetEditText();
    }

    private void resetFlowerLayout() {
        fl_tag.removeAllViews();
        for (View view : viewList) {
            fl_tag.addView(view);
        }
        fl_tag.addView(editText);
    }

    private void resetEditText() {
        lastString = "";
        list.clear();
        adapter.notifyDataSetChanged();
        editText.setText("");
        editTextGetFocus();
    }

    private void editTextGetFocus(){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @Override
    protected void reload() {

    }
}
