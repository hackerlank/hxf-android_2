package com.goodhappiness.ui.personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserInfoResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.social.picture.EditPicUpdateActivity;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import io.rong.imkit.RongIM;

@ContentView(R.layout.activity_personal_data)
public class PersonalDataActivity extends BaseActivity {
    @ViewInject(R.id.personal_data_tv_nickname)
    private TextView tv_nickname;
    @ViewInject(R.id.personal_data_tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.personal_data_tv_id)
    private TextView tv_uid;
    @ViewInject(R.id.personal_data_iv_head)
    private ImageView iv_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.personal_data);
    }

    @Override
    protected void reload() {
        if (!TextUtils.isEmpty(getSid())) {
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.personal_data));
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.personal_data));
        MobclickAgent.onResume(this);
        if (!TextUtils.isEmpty(getSid())) {
            initData();
        }
        Bitmap bitmap = FileUtils.getUserHeadImg(this);
        if (bitmap != null) {
            iv_head.setImageBitmap(bitmap);
        }
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpFinal.USER_INFO);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
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
                if (userInfoResult != null) {
                    PreferencesUtil.saveUserInfo(PersonalDataActivity.this, userInfoResult.getUserInfo());
                    RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(getUid()), getNickName(), Uri.parse(userInfoResult.getUserInfo().getAvatar())));
                    tv_nickname.setText(userInfoResult.getUserInfo().getNickname());
                    tv_phone.setText(userInfoResult.getUserInfo().getMobile() + "");
                    tv_uid.setText(userInfoResult.getUserInfo().getUid() + "");
                    displayImage(iv_head, userInfoResult.getUserInfo().getAvatar());
                    if (!TextUtils.isEmpty(userInfoResult.getUserInfo().getAvatar())) {
                        PreferencesUtil.clearHeadImgPathInfo(PersonalDataActivity.this);
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

    @Event({R.id.personal_date_rl_phone, R.id.personal_date_rl_nickname, R.id.personal_data_iv_head, R.id.personal_date_rl_address})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.personal_date_rl_phone:
//                startTheActivity(new Intent(this, VerifyMobileActivity.class));
//                CameraUtils.showSample(this, null);
                break;
            case R.id.personal_date_rl_nickname:
                Intent intent2 = new Intent(this, UpdateUserInformationActivity.class);
                if (!TextUtils.isEmpty(tv_nickname.getText().toString())) {
                    intent2.putExtra(FieldFinals.NICKNAME, tv_nickname.getText().toString());
                }
                startTheActivity(intent2);
                break;
            case R.id.personal_data_iv_head:
                IntentUtils.startToEditPic(this, EditPicUpdateActivity.INTENT_HEAD);
//                Intent intent = new Intent(this, PhotoPickerActivity.class);
//                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
//                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
//                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
//                intent.putExtra(PhotoPickerActivity.EXTRA_JUMP_MODE, PhotoPickerActivity.JUMP_MODE_SELECT_HEAD);
//                startTheActivity(intent);
                break;
            case R.id.personal_date_rl_address:
                startTheActivity(new Intent(this, AddressListActivity.class));
                break;
        }
    }
}
