package com.goodhappiness.ui.lottery;

import android.os.Bundle;
import android.widget.GridView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.LotteryDetail;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_lottery_number)
public class LotteryNumberActivity extends BaseActivity {
    @ViewInject(R.id.lottery_number_gv)
    private GridView gv;
//    @ViewInject(R.id.lottery_number_tv_time)
//    private TextView tv;

    private CommonAdapter<Long> adapter;
    private List<Long> list = new ArrayList<>();
    private LotteryDetail lotteryDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.my_num));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.my_num));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.my_num);
        initAdapter();

    }

    @Override
    protected void reload() {

    }

    private void initAdapter() {
        lotteryDetail = PreferencesUtil.getPreferences(this, FieldFinals.LOTTERY_NUMBER2);
        PreferencesUtil.setPreferences(this,FieldFinals.LOTTERY_NUMBER2,null);
        if(lotteryDetail!=null){
            list.addAll(lotteryDetail.getCodes());
//            if(getIntent().getStringExtra("time")!=null){
//                tv.setText(getIntent().getStringExtra("time"));
//            }
        }
        adapter = new CommonAdapter<Long>(this,list,R.layout.layout_grid_lottery_number) {
            @Override
            public void convert(ViewHolder helper, Long item, int position) {
                helper.setText(R.id.layout_grid_lottery_number_tv,item+"");
            }
        };
        gv.setAdapter(adapter);
    }
}
