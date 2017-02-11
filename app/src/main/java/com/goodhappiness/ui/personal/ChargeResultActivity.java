package com.goodhappiness.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.ChargeResult;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_charge_result)
public class ChargeResultActivity extends BaseActivity {
    @ViewInject(R.id.charge_result_tips)
    private TextView tv_Tips;
    @ViewInject(R.id.charge_result_back)
    private TextView tv_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.charge));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.charge));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.charge);
        initData();
        if (HomepageActivity.type==HomepageActivity.MINE) {
            tv_finish.setText(R.string.back_to_person);
        }
    }

    @Override
    protected void reload() {
        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpFinal.RECHARGE_RESULT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.SN, PreferencesUtil.getStringPreferences(this, FieldFinals.SN));//PreferencesUtil.getStringPreferences(this, "sn"));
        HttpUtils.post(this,params, new TypeToken<Result<ChargeResult>>() {
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
                ChargeResult chargeResult = (ChargeResult) result.getData();
                if(chargeResult!=null){
                    if (chargeResult.getPayStatus() == 3) {
                        tv_Tips.setText(String.format(getString(R.string.format_congratulate_get_coin,chargeResult.getFee())));//"恭喜您，获得" + chargeResult.getFee() + "个金币!");
                    } else {
                        tv_Tips.setText(R.string.charge_fail);
                    }
                    if(chargeResult.getRedBags()!=null&&chargeResult.getRedBags().size()>0){
                        DialogFactory.createReceiveRedbagDialog(ChargeResultActivity.this,chargeResult.getRedBags());
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

    @Event({R.id.charge_result_back})
    private void onEventClick(View v){
        AppManager.getAppManager().finishActivity(ChargeRecordActivity.class);
        finishActivity();
    }
}
