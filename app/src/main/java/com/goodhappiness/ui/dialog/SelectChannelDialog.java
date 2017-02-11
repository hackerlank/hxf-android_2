package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Channel;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.dao.OnSelectChannelListener;
import com.goodhappiness.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 常用Dialog dialog_comment_lv
 */
public class SelectChannelDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ListView lv;
    private CommonAdapter<Channel> adapter;
    private List<Channel> list;
    private OnSelectChannelListener onSelectChargeCountListener;
    private String currentChannel;
    private int currentId;

    public SelectChannelDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SelectChannelDialog(final Context context, int theme, OnSelectChannelListener onSelectChargeCountListener) {
        super(context, theme);
        this.context = context;
        this.onSelectChargeCountListener = onSelectChargeCountListener;
        list = new ArrayList<>();
        if (PreferencesUtil.getPreferences(GoodHappinessApplication.getContext(), FieldFinals.CHANNEL) != null) {
            List<Channel> channels = PreferencesUtil.getPreferences(GoodHappinessApplication.getContext(), FieldFinals.CHANNEL);
            if (channels != null && channels.size() > 0) {
                currentId = channels.get(0).getChannelId();
                currentChannel = channels.get(0).getChannelName();
                list.addAll(channels);
            }
        }
        adapter = new CommonAdapter<Channel>(context, list, R.layout.layout_list_channel) {
            @Override
            public void convert(ViewHolder helper, Channel item, int position) {
                if (currentChannel.equals(item.getChannelName())) {
                    helper.getView(R.id.tv_channel).setBackgroundColor(context.getResources().getColor(R.color.white));
                } else {
                    helper.getView(R.id.tv_channel).setBackgroundColor(context.getResources().getColor(R.color.gray));
                }
                helper.setText(R.id.tv_channel, item.getChannelName());
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_channel);
        initView();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_channel);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentChannel = list.get(position).getChannelName();
                currentId = list.get(position).getChannelId();
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.iv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_add_channel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                closeDialog();
                break;
            case R.id.tv_add_channel:
                if (onSelectChargeCountListener != null) {
                    if(!TextUtils.isEmpty(currentChannel)){
                        onSelectChargeCountListener.onSelected(currentChannel, currentId);
                        dismiss();
                    }
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeDialog() {
        if (onSelectChargeCountListener != null) {
            onSelectChargeCountListener.onSelected("",0);
        }
        dismiss();
    }

}
