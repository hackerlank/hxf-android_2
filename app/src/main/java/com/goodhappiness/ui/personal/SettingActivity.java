package com.goodhappiness.ui.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.wxapi.Constants;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import io.rong.imkit.RongIM;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.setting));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.setting));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void reload() {

    }

    @Event({R.id.setting_problem, R.id.setting_line_us, R.id.setting_about_us, R.id.setting_logout})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.setting_problem:
                IntentUtils.startToWebView(this, HttpFinal.INSRALL_NORMAL);
                break;
            case R.id.setting_line_us:
                DialogFactory.createSelectDialog(this, getString(R.string.dial_custom_mobil), getString(R.string.dial_custom_mobil_tips), new OnSelectListener() {
                    @Override
                    public void onSelected(boolean isSelected) {
                        if (isSelected) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse(Constants.CUSTOM_NUM);
                            intent.setData(data);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.setting_about_us:
                startTheActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.setting_logout:
                logout();
                break;
        }
    }

    private void logout() {
        RequestParams params = new RequestParams(HttpFinal.LOGOUT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.DEVICETYPE, FieldFinals.ANDROID);
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(this, params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {

            @Override
            public void onSuccess(Result result) {
                HomepageActivity.type = HomepageActivity.LOTTERY;
                GoodHappinessApplication.isNeedRefresh = true;
                showToast(R.string.logout_success);
                RongIM.getInstance().logout();
                finishActivity();
                PreferencesUtil.clearUserInfo(SettingActivity.this);
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
