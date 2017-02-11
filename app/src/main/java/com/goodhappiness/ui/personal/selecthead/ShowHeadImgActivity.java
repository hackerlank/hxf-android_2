package com.goodhappiness.ui.personal.selecthead;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.QINIUToken;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.UserInfoResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.social.picture.EditPicUpdateActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.SHA1;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author
 * @ClassName: ShowHeadImgActivity
 * @Description: 无原型图, 剪切完图片后展示图片, 并且将文件保存的手机中, 按确认键完成图片上传
 * @date 2015-1-20 上午10:19:22
 */
@ContentView(R.layout.activity_show_head)
public class ShowHeadImgActivity extends BaseActivity {
    private ShowHeadImgActivity activity;//本类对象
    @ViewInject(R.id.activity_show_head_img)
    private ImageView imgHead;// 剪切后的头像
    private boolean isSuccess = false;// 是否提交修改成功标志
    private byte[] b = new byte[]{};// bitmap数组
    private File file = null;
    public static ShowHeadImgActivity instance;
    private QINIUToken qiniuToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.select_avatar));
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.select_avatar));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        instance = activity = this;
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.select_avatar);
        tv_right.setText("确定");
//        b = getIntent().getByteArrayExtra(FieldFinals.IMAGE_PATH);// 上一个界面传过来的图片数组
        b = PreferencesUtil.getPreferences(this, FieldFinals.VALUE);
        PreferencesUtil.setPreferences(this, FieldFinals.VALUE, null);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);// 将数组转化成Bitmap

        if (bitmap != null) {
            imgHead.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void reload() {

    }


    @Event({R.id.common_right_text})
    private void onEventClick(View v) {
        if (v.getId() == R.id.common_right_text) {//上传头像
//            ImageLoader.getInstance().displayImage("file://storage/emulated/0/tencent/MicroMsg/1464350204455_44426ae0.jpg",imgHead);
            dialog.show();
            getToken();
        }
    }

    private void getToken() {
        RequestParams params = new RequestParams(HttpFinal.QINIU_TOKEN);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(this, FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter("sid", getSid());
        HttpUtils.post(this,params, new TypeToken<Result<QINIUToken>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                qiniuToken = (QINIUToken) result.getData();
                if (qiniuToken != null) {
                    getFile(b, FileUtils.getStorageDirectory(), "user_head");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog.dismiss();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void finishSelectedHead() {

    }

    /**
     * 根据byte数组，生成文件存放到手机内存当中
     */
    public void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
//            filePath + "/" + MXApplication.preferences.getString(APPConstants.USER_NAME, "null") + ".jpg"
            SHA1 sha1 = new SHA1();
            file = new File(filePath + "/" + sha1.getDigestOfString(new String(PreferencesUtil.getLongPreferences(this, "uid", 0) + "-" + System.currentTimeMillis()).getBytes()).toUpperCase() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            PreferencesUtil.setPreferences(ShowHeadImgActivity.this, FieldFinals.IMAGE_FILE_PATH, file.getAbsolutePath());
            Log.e("k_", file.getAbsolutePath());
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            upLoad();
        }
    }

    private void upLoad() {
        if (!TextUtils.isEmpty(PreferencesUtil.getStringPreferences(ShowHeadImgActivity.this, FieldFinals.IMAGE_FILE_PATH))) {
            final File file = new File(PreferencesUtil.getStringPreferences(ShowHeadImgActivity.this, FieldFinals.IMAGE_FILE_PATH));
            GoodHappinessApplication.uploadManager.put(file, file.getName(), qiniuToken.getUploadToken(), new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    uploadImagePath(key);
                }
            }, null);
        }
    }

    private void uploadImagePath(String key) {
        RequestParams params = new RequestParams(HttpFinal.UPDATE_USER_INFO);
        params.addBodyParameter("deviceIdentifier", PreferencesUtil.getStringPreferences(this, FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter("sid", getSid());
        params.addBodyParameter("key", "avatar");
        params.addBodyParameter("value", key);
        HttpUtils.post(this,params, new TypeToken<Result<UserInfoResult>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                UserInfoResult userInfoResult = (UserInfoResult) result.getData();
                PreferencesUtil.saveUserInfo(ShowHeadImgActivity.this, userInfoResult.getUserInfo());
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
//                AppManager.getAppManager().finishActivity(PhotoPickerActivity.class);
//                AppManager.getAppManager().finishActivity(ShowImageStep2Activity.class);
//                AppManager.getAppManager().finishActivity(EditPicUpdateActivity.class);
                AppManager.getAppManager().finishActivities(new Class[]{PhotoPickerActivity.class, ShowImageStep2Activity.class
                        , EditPicUpdateActivity.class});
                GoodHappinessApplication.isNeedFinish = true;
                finishActivity();
            }
        });
    }
}
