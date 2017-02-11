package com.goodhappiness.ui.lottery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.LotteryRecordDetail;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.XProgressDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_lottery_record_detail)
public class LotteryRecordDetailActivity extends BaseActivity {
    @ViewInject(R.id.lottery_record_detail_lv)
    private ListView listView;
    @ViewInject(R.id.lottery_record_detail_ll_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.lottery_record_detail_ll_lucky)
    private LinearLayout ll_lucky;
    @ViewInject(R.id.lottery_record_detail_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.lottery_record_detail_tv_period)
    private TextView tv_period;
    @ViewInject(R.id.lottery_record_detail_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.lottery_record_detail_tv_luk)
    private TextView tv_luk;
    @ViewInject(R.id.lottery_record_detail_tv_cost)
    private TextView tv_cost;

    private CommonAdapter<LotteryRecordDetail.AwardArrBean> adapter;
    private List<LotteryRecordDetail.AwardArrBean> list = new ArrayList<>();
    private LotteryRecordDetail lotteryRecordDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.look_detail));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.look_detail));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.look_detail);
        if (getIntent() != null) {
            adapter = new CommonAdapter<LotteryRecordDetail.AwardArrBean>(this, list, R.layout.layout_list_lottery_record_detail_codes) {
                @Override
                public void convert(ViewHolder helper, LotteryRecordDetail.AwardArrBean item, int position) {
                    helper.setText(R.id.layout_list_lottery_record_detail_codes_tv_time, item.getDobkTime());
                    helper.setText(R.id.layout_list_lottery_record_detail_codes_tv_cost, item.getCount_by() + "");
                }
            };
            listView.setAdapter(adapter);
            initData();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Task task = new Task(position);
                    task.execute();
                }
            });
        }

    }

    class Task extends AsyncTask<Integer, Integer, String> {

        private XProgressDialog xProgressDialog = new XProgressDialog(LotteryRecordDetailActivity.this, getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);

        private int position;

        public Task(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            xProgressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            PreferencesUtil.setPreferences(LotteryRecordDetailActivity.this, FieldFinals.LOTTERY_RECORD_DETAIL, lotteryRecordDetail);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(LotteryRecordDetailActivity.this, LotteryCodeActivity.class);
            intent.putExtra(FieldFinals.POSITION,position);
            startTheActivity(intent);
            xProgressDialog.dismiss();
        }
    }


    @Override
    protected void reload() {
        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpFinal.USER_PERIODS_CODE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PERIOD, getIntent().getLongExtra(FieldFinals.PERIOD, 0) + "");
        HttpUtils.post(this,params, new TypeToken<Result<LotteryRecordDetail>>() {
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
                lotteryRecordDetail = (LotteryRecordDetail) result.getData();
                if (lotteryRecordDetail != null&&lotteryRecordDetail.getLuckyCode()!=-1&&!TextUtils.isEmpty(lotteryRecordDetail.getName())) {
                    list.addAll(lotteryRecordDetail.getAwardArr());
                    adapter.notifyDataSetChanged();
                    tv_name.setText(lotteryRecordDetail.getName());
                    tv_period.setText(lotteryRecordDetail.getPeriod() + "");
                    tv_cost.setText(lotteryRecordDetail.getTotalCount_by() + "");
                    if (lotteryRecordDetail.getLuckyCode() != 0) {
                        tv_time.setText(lotteryRecordDetail.getCalcTime() + "");
                        ll_lucky.setVisibility(View.VISIBLE);
                        ll_time.setVisibility(View.VISIBLE);
                        tv_luk.setText(lotteryRecordDetail.getLuckyCode() + "");
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
                dialog.dismiss();
            }
        });
    }
}
