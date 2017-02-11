package com.goodhappiness.ui.social.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.TagAdapter;
import com.goodhappiness.bean.DeviceInfo;
import com.goodhappiness.bean.QINIUToken;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.TimeChange;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectChannelListener;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.fragment.SelectChannelFragment;
import com.goodhappiness.ui.social.picture.AddTagActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.QiNiuUploadUtils;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

@ContentView(R.layout.activity_video_publish)
public class VideoPublishActivity extends BaseFragmentActivity{

    public static final String TAG = "VideoPublishActivity";

    @ViewInject(R.id.publish_cet)
    private EditText et;
    @ViewInject(R.id.publish_iv)
    private ImageView iv;
    @ViewInject(R.id.publish_iv_save)
    private ImageView iv_save;
    @ViewInject(R.id.publish_iv_save_choose)
    private ImageView iv_saveChoose;
    @ViewInject(R.id.publish_iv_save_no_choose)
    private ImageView iv_saveNoChoose;
    @ViewInject(R.id.tv_channel)
    private TextView tv_channel;
    @ViewInject(R.id.tv_channel_tips)
    private TextView tv_channelTips;
    @ViewInject(R.id.tv_add_tag)
    private TextView tv_addTag;
    @ViewInject(R.id.rv_tag)
    private RecyclerView rv_tag;

    public static final int REQUEST_CODE = 1001;
    private boolean isSaveChoose;
    private ArrayList<String> arrayList = new ArrayList<>();
    private String filePath = "";
    private int currentId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart((getString(R.string.publish)));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd((getString(R.string.publish)));
        MobclickAgent.onPause(this);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    iv.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void setData() {
        if(getIntent()!=null&&!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.FILE_PATH))){
            filePath = getIntent().getStringExtra(FieldFinals.FILE_PATH);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    message.what =0;
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                            filePath, MediaStore.Images.Thumbnails.MICRO_KIND);//创建一个视频缩略图
                    bitmap=ThumbnailUtils.extractThumbnail(bitmap, 200, 200,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//指定视频缩略图的大小
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }).start();
        }else{
            return;
        }
        initView();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_tag.setLayoutManager(manager);
        tv_title.setText(R.string.publish);
        tv_right.setText(getString(R.string.commit));
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        isSaveChoose = PreferencesUtil.getBooleanPreferences(this, FieldFinals.SAVE_CHOOSE, false);
        setSaveStatus();
        RxView.clicks(findViewById(R.id.common_right_text)).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                newDialog().show();
                QiNiuUploadUtils.getToken(VideoPublishActivity.this,new QiNiuUploadUtils.onGetTokenListener() {
                    @Override
                    public void onGetToken(QINIUToken qiniuToken) {
                        if (qiniuToken != null) {
                            if(!FileUtils.isFileExists2(filePath)){
                                dialog.dismiss();
                                return;
                            }
                            Log.e("q_",filePath);
                            GoodHappinessApplication.uploadManager.put(FileUtils.File2byte(filePath), MD5.md5(String.format(getString(R.string.format_video),System.currentTimeMillis())+PreferencesUtil.getUid()), qiniuToken.getUploadToken(), new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    Log.e("q_",key);
                                    publish(key);
                                }
                            }, null);
                        } else {
                            showToast(R.string.upload_fail);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setSaveStatus() {
        if (isSaveChoose) {
            iv_save.setImageResource(R.mipmap.ico_save_click);
            iv_saveChoose.setVisibility(View.VISIBLE);
            iv_saveNoChoose.setVisibility(View.GONE);
        } else {
            iv_save.setImageResource(R.mipmap.ico_save);
            iv_saveChoose.setVisibility(View.GONE);
            iv_saveNoChoose.setVisibility(View.VISIBLE);
        }
        PreferencesUtil.setPreferences(this, FieldFinals.SAVE_CHOOSE, isSaveChoose);
    }


    @Event({R.id.publish_rl_channel,R.id.publish_rl_tag,R.id.publish_iv, R.id.publish_rl_save})//, R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.publish_rl_channel:
                SelectChannelFragment fragment = SelectChannelFragment.newInstance(2, 5, false, false);
                fragment.setOnSelectListener(new OnSelectChannelListener() {
                    @Override
                    public void onSelected(String channel,int channelId) {
                        if(TextUtils.isEmpty(channel)){
                            tv_channelTips.setVisibility(View.VISIBLE);
                            tv_channel.setVisibility(View.GONE);
                            currentId = -1;
                        }else{
                            tv_channelTips.setVisibility(View.GONE);
                            tv_channel.setVisibility(View.VISIBLE);
                            tv_channel.setText(channel);
                            currentId = channelId;
                        }
                    }
                });
                fragment.show(getFragmentManager(), "selectChannel");
                break;
            case R.id.publish_rl_tag:
            case R.id.rv_tag:
                gotoAddTag();
                break;
            case R.id.publish_iv:
                if(!TextUtils.isEmpty(filePath)){
                    IntentUtils.startToLocalVideoPlayer(VideoPublishActivity.this,filePath);
                }
                break;
            case R.id.publish_rl_save:
                isSaveChoose = !isSaveChoose;
                setSaveStatus();
                break;
        }
    }


    private void gotoAddTag() {
        Intent addTagIntent = new Intent(this, AddTagActivity.class);
        addTagIntent.putExtra(FieldFinals.ACTION, VideoPublishActivity.TAG);
        addTagIntent.putStringArrayListExtra(FieldFinals.STRING_LIST,arrayList);
        startActivityForResult(addTagIntent,REQUEST_CODE);
    }

    private void publish(String... keys) {
        RequestParams params = new RequestParams(HttpFinal.POST_CREATE);
        JSONArray jsonArray = new JSONArray();
        for (String key : keys) {
            jsonArray.add(key);
        }
        JSONArray filesType = new JSONArray();
            filesType.add(3);
        if(arrayList!=null&&arrayList.size()>0){
            JSONArray tagJsonArray = new JSONArray();
            for (String tag : arrayList) {
                tagJsonArray.add(tag);
            }
            params.addBodyParameter(FieldFinals.TAG_ARRAY, tagJsonArray.toString());
        }
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.DURATION, TimeChange.setTime(getIntent().getIntExtra(FieldFinals.DURATION,0)));
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.CHANNEL, tv_channel.getText().toString());
        params.addBodyParameter(FieldFinals.FILES_TYPE, filesType.toString());
        params.addBodyParameter(FieldFinals.POST_CONTENT, et.getText().toString());
        if(currentId!=-1){
            params.addBodyParameter(FieldFinals.CHANNEL_ID, currentId+"");
        }
        params.addBodyParameter(FieldFinals.FILES_ID, jsonArray.toString());
        params.addBodyParameter(FieldFinals.LAT, 0 + "");
        params.addBodyParameter(FieldFinals.LNG, 0 + "");
        HttpUtils.post(this,params, new TypeToken<Result<DeviceInfo>>() {
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
                PreferencesUtil.setPreferences(VideoPublishActivity.this, FieldFinals.ADD_PUBLISH, true);
                AppManager.getAppManager().finishActivities(new Class[]{VideoRecordActivity.class, VideoListActivity.class
                });
                if(!isSaveChoose){
                    FileUtils.deleteFile(filePath);
                }
                GoodHappinessApplication.isNeedFinish = true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==AddTagActivity.RESULT_CODE&&data!=null){
            ArrayList<String> strings = data.getStringArrayListExtra(FieldFinals.STRING_LIST);
            if(strings!=null&&strings.size()>0){
                setTag(strings);
                arrayList.clear();
                arrayList.addAll(strings);
            }else{
                rv_tag.setVisibility(View.GONE);
                tv_addTag.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTag(ArrayList<String> strings) {

        rv_tag.setVisibility(View.VISIBLE);
        tv_addTag.setVisibility(View.GONE);
        TagAdapter tagAdapter = new TagAdapter(this, strings);
        rv_tag.setAdapter(tagAdapter);
    }
}
