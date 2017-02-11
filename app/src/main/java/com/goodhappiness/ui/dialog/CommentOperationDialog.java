package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.SimpleListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class CommentOperationDialog extends Dialog {
    private ListView listView;
    private CommonAdapter<SimpleListItem> adapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<SimpleListItem> list = new ArrayList<>();
    private boolean hasDelete;
    public CommentOperationDialog(Context context) {
        this(context, true,null);
    }

    public CommentOperationDialog(Context context, int theme,AdapterView.OnItemClickListener onItemClickListener,boolean hasDelete) {
        super(context, theme);
        this.onItemClickListener = onItemClickListener;
        this.hasDelete = hasDelete;
    }

    protected CommentOperationDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment_operation);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.dialog_comment_lv);
        list.add(new SimpleListItem(getContext().getString(R.string.copy)));
        if(hasDelete){
            list.add(new SimpleListItem(getContext().getString(R.string.delete_comment)));
        }else{
            list.add(new SimpleListItem(getContext().getString(R.string.report)));
        }
        adapter = new CommonAdapter<SimpleListItem>(getContext(),list,R.layout.dialog_comment_operation_list) {
            @Override
            public void convert(ViewHolder helper, SimpleListItem item, int position) {
                helper.setText(R.id.dialog_comment_operation_list_tv,item.getItem());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
