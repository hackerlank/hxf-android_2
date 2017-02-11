package com.goodhappiness.ui.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.LotteryNumber;
import com.goodhappiness.bean.LotteryRecordDetail;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_lottery_code)
public class LotteryCodeActivity extends BaseActivity {

    @ViewInject(R.id.lottery_code_ll_lucky)
    private LinearLayout ll_lucky;
    @ViewInject(R.id.lottery_code_rl_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.lottery_code_gv)
    private GridView gv;
    @ViewInject(R.id.lottery_code_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.lottery_code_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.lottery_code_tv_period)
    private TextView tv_period;
    @ViewInject(R.id.lottery_code_tv_luk)
    private TextView tv_luk;
    @ViewInject(R.id.lottery_code_tv_cost)
    private TextView tv_cost;

    private CommonAdapter<LotteryNumber> adapter;
    private List<LotteryNumber> list = new ArrayList<>();
    private LotteryRecordDetail lotteryRecordDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.check_code);
        lotteryRecordDetail = PreferencesUtil.getPreferences(this,FieldFinals.LOTTERY_RECORD_DETAIL);
        PreferencesUtil.setPreferences(this, FieldFinals.LOTTERY_RECORD_DETAIL, null);
        if (lotteryRecordDetail != null) {
            initData();
        }

    }

    @Override
    protected void reload() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.check_code));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.check_code));
        MobclickAgent.onPause(this);
    }
    private void initData() {
        if (lotteryRecordDetail != null) {
            for (Integer i : lotteryRecordDetail.getAwardArr().get(getIntent().getIntExtra(FieldFinals.POSITION, 0)).getAwardNo()) {
                list.add(new LotteryNumber(i + ""));
            }
            tv_name.setText(lotteryRecordDetail.getName());
            tv_period.setText(lotteryRecordDetail.getPeriod()+"");
            if (lotteryRecordDetail.getLuckyCode() != 0) {
                ll_lucky.setVisibility(View.VISIBLE);
                ll_time.setVisibility(View.VISIBLE);
                tv_time.setText(lotteryRecordDetail.getAwardArr().get(getIntent().getIntExtra(FieldFinals.POSITION, 0)).getDobkTime() + "");
                tv_luk.setText(lotteryRecordDetail.getLuckyCode() + "");
            }
            tv_cost.setText(lotteryRecordDetail.getAwardArr().get(getIntent().getIntExtra(FieldFinals.POSITION, 0)).getCount_by() + "");
            adapter = new CommonAdapter<LotteryNumber>(this, list, R.layout.layout_grid_lottery_number) {
                @Override
                public void convert(ViewHolder helper, LotteryNumber item, int position) {
                    helper.setText(R.id.layout_grid_lottery_number_tv, item.getNumber());
                }
            };
            gv.setAdapter(adapter);
        }
    }
}
