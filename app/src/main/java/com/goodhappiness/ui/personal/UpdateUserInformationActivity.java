package com.goodhappiness.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.goodhappiness.R;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserInfoResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_change_name)
public class UpdateUserInformationActivity extends BaseActivity {
    @ViewInject(R.id.change_name_cet)
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.change_nickname));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.change_nickname));
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.change_nickname);
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        tv_right.setText(getString(R.string.commit));
        if (getIntent() != null) {
            editText.setText(getIntent().getStringExtra(FieldFinals.NICKNAME));
        }
    }

    @Override
    protected void reload() {

    }

    @Event({R.id.common_right_text})
    private void onEventClick(View v) {
//        int count = editText.getText().toString().trim().length();
//        if (count < 4 || count > 20) {
//            DialogFactory.createDefaultDialog(this, getString(R.string.input_right_text_length));
//            return;
//        }
        RequestParams params = new RequestParams(HttpFinal.UPDATE_USER_INFO);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.KEY, FieldFinals.NICKNAME);
        params.addBodyParameter(FieldFinals.VALUE, editText.getText().toString());
        HttpUtils.post(this,params, new TypeToken<Result<UserInfoResult>>() {
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
                UserInfoResult userInfoResult = (UserInfoResult) result.getData();
                PreferencesUtil.saveUserInfo(UpdateUserInformationActivity.this, userInfoResult.getUserInfo());
                showToast(R.string.modify_finish);
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
        });
    }
}
