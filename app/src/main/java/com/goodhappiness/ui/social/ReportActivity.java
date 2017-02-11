package com.goodhappiness.ui.social;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.ReportData;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/6/20.
 */
@ContentView(R.layout.activity_report)
public class ReportActivity extends BaseActivity {
    public static final String REPORT_TYPE_FEED = "feed";
    public static final String REPORT_TYPE_COMMENT = "comment";
    @ViewInject(R.id.report_lv)
    private ListView listView;
    private CommonAdapter<ReportData> commonAdapter;
    private int currentPosition = -1;
    private List<ReportData> reportData = new ArrayList<>();
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart((getString(R.string.report)));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd((getString(R.string.report)));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.report);
        CharSequence [] china = getResources().getTextArray(R.array.report_china);
        CharSequence [] english = getResources().getTextArray(R.array.report_english);
        int i = 0;
        for(CharSequence c:china){
            reportData.add(new ReportData(english[i].toString(),china[i].toString()));
            i++;
        }

        commonAdapter = new CommonAdapter<ReportData>(this,reportData,R.layout.layout_list_report) {
            @Override
            public void convert(ViewHolder helper, ReportData item, int position) {
                helper.setText(R.id.layout_list_report_tv,item.getChina());
                if(item.isSelect()){
                    helper.setVisibility(R.id.layout_list_report_iv,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id.layout_list_report_iv,View.GONE);
                }
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                for(ReportData d:reportData){
                    d.setIsSelect(false);
                }
                reportData.get(position).setIsSelect(true);
                commonAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void reload() {

    }

    @Event({R.id.report_tv_confirm})
    private void onEventClick(View v){
        if(currentPosition==-1){
            showToast(R.string.report_reason);
            return;
        }
        report();
    }

    private void report() {
        RequestParams params = new RequestParams(HttpFinal.REPORT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.TYPE, getIntent().getStringExtra(FieldFinals.TYPE));
        params.addBodyParameter(FieldFinals.POST_ID, getIntent().getLongExtra(FieldFinals.POST_ID,-1)+"");
        params.addBodyParameter(FieldFinals.REASON, reportData.get(currentPosition).getEnglish());
        HttpUtils.post(this,params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onSuccess(Result result) {
                showToast(R.string.report_success);
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

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                newDialog().show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }
}
