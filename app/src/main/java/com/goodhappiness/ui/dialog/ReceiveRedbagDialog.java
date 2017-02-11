package com.goodhappiness.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Redbag;
import com.goodhappiness.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/19.
 */
public class ReceiveRedbagDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private CommonAdapter<Redbag> adapter;
    private List<Redbag> list = new ArrayList<>();
    private ListView lv;

    public ReceiveRedbagDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ReceiveRedbagDialog(Context context, int theme,List<Redbag> redbags) {
        super(context, theme);
        this.context = context;
        list.addAll(redbags);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_receive_redbag);
        initView();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        findViewById(R.id.iv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_go_to_redbag_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startToRedbag(context,0);
                dismiss();
            }
        });
        if (list.size() >= 3) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lv.getLayoutParams();
            params.height = (int) (GoodHappinessApplication.perHeight * 500);
            lv.setLayoutParams(params);
        }
        adapter = new CommonAdapter<Redbag>(context, list, R.layout.layout_list_redbag_small) {
            @Override
            public void convert(ViewHolder helper, Redbag item, int position) {
                helper.setText(R.id.name_small,item.getRedName());
                helper.setText(R.id.start_time_small,item.getStartTime());
                helper.setText(R.id.end_time_small,item.getEndTime());
                helper.setText(R.id.desc_small,item.getDesc());
                helper.setText(R.id.money_small,item.getMoney()+"");
                helper.setText(R.id.rest_money_small,item.getRestMoney()+"");
            }
        };
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
