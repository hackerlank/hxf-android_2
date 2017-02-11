package com.goodhappiness.ui.social.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.TagAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.DeviceInfo;
import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.bean.QINIUToken;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectChannelListener;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.fragment.SelectChannelFragment;
import com.goodhappiness.utils.AlxGifHelper;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.QiNiuUploadUtils;
import com.goodhappiness.widget.emoji.EmojiTextView;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;
import rx.functions.Action1;

@ContentView(R.layout.activity_publish)
public class PublishActivity extends BaseFragmentActivity {
    public static final String TAG = "PublishActivity";
    @ViewInject(R.id.publish_cet)
    private EditText et;
    @ViewInject(R.id.publish_iv)
    private ImageView iv;
    @ViewInject(R.id.publish_gv)
    private GridView gv;
    @ViewInject(R.id.publish_iv_mark)
    private ImageView iv_mark;
    @ViewInject(R.id.publish_iv_mark_choose)
    private ImageView iv_markChoose;
    @ViewInject(R.id.publish_iv_mark_no_choose)
    private ImageView iv_markNoChoose;
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
    @ViewInject(R.id.gif_photo_view)
    private GifImageView gi;

    public static final int REQUEST_CODE = 1001;
    private boolean isMarkChoose;
    private boolean isSaveChoose;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<PostFilesBean> postFileslist = new ArrayList<>();
    private CommonAdapter<PostFilesBean> postFilesBeanCommonAdapter;
    private boolean isCanUpLoad = false;
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

    @Override
    protected void setData() {
        initView();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_tag.setLayoutManager(manager);
        tv_title.setText(R.string.publish);
        tv_right.setText(getString(R.string.commit));
        gi.setVisibility(View.GONE);
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
        isMarkChoose = PreferencesUtil.getBooleanPreferences(this, FieldFinals.WATERMARK_CHOOSE, true);
        isSaveChoose = PreferencesUtil.getBooleanPreferences(this, FieldFinals.SAVE_CHOOSE, false);
        setMarkStatus();
        setSaveStatus();
        initImg();
        RxView.clicks(findViewById(R.id.common_right_text)).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (postFileslist != null && postFileslist.size() > 0) {
                    new PublishTask().execute(0);
                }
            }
        });
    }

    class PublishTask extends AsyncTask<Integer, Integer, ArrayList<PostFilesBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newDialog().show();
        }

        @Override
        protected ArrayList<PostFilesBean> doInBackground(Integer... params) {
            for (final PostFilesBean bean : postFileslist) {
                int postType = 0;
                byte bytes[] = null;
                if (bean.isProcessed()) {
                    postType = 1;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bean.getBitmap().compress(Bitmap.CompressFormat.PNG, 50, baos);
                    bytes = baos.toByteArray();
                    if (isSaveChoose) {
                        FileUtils.saveImgFile(PublishActivity.this, bytes, FileUtils.getStorageDirectory());
                    }
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final int finalPostType = postType;
                final byte[] finalBytes = bytes;
                QiNiuUploadUtils.getToken(PublishActivity.this, new QiNiuUploadUtils.onGetTokenListener() {
                    @Override
                    public void onGetToken(QINIUToken qiniuToken) {
                        if (qiniuToken != null) {
                            StringBuffer buffer = new StringBuffer();
                            buffer.append(String.format(getString(R.string.format_photo_head), System.currentTimeMillis()));
                            if (finalPostType == 0) {
                                GoodHappinessApplication.uploadManager.put(bean.getGifPath(), buffer.toString(), qiniuToken.getUploadToken(), new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject response) {
                                        Log.e("q_", key);
                                        bean.setDuration(key);
                                    }
                                }, null);
                            } else {
                                GoodHappinessApplication.uploadManager.put(finalBytes, buffer.toString(), qiniuToken.getUploadToken(), new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject response) {
                                        Log.e("q_", key);
                                        bean.setDuration(key);
                                    }
                                }, null);
                            }
                        } else {
                            showToast(R.string.upload_fail);
                        }
                    }
                });
            }
            while (!isCanUpLoad) {
                int i = 0;
                for (PostFilesBean bean : postFileslist) {
                    if (TextUtils.isEmpty(bean.getDuration())) {
                        break;
                    } else {
                        if (i == postFileslist.size() - 1) {
                            isCanUpLoad = true;
                            return postFileslist;
                        }
                    }
                    i++;
                }
            }
            return postFileslist;
        }

        @Override
        protected void onPostExecute(ArrayList<PostFilesBean> postFilesBeen) {
            super.onPostExecute(postFilesBeen);
            dialog.dismiss();
            publish(postFilesBeen);
            Log.e("q_", postFilesBeen.toString());
        }
    }

    private void initImg() {
        ArrayList<PostFilesBean> postFilesBeen = (ArrayList<PostFilesBean>) getIntent().getExtras().getParcelableArrayList(FieldFinals.FEED_INFO_LIST).clone();
        for (PostFilesBean bean : postFilesBeen) {
            if (bean.isTrans()) {
                byte[] b = PreferencesUtil.getPreferences(PublishActivity.this, bean.getFileId());
                if (b != null) {
                    bean.setBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                }
            }
            bean.setDuration("");
        }
        Log.e("q_", postFilesBeen.toString());
        postFileslist.addAll(postFilesBeen);
        if (postFileslist.size() == 1) {
            gv.setVisibility(View.GONE);
            if (postFileslist.get(0).isProcessed()) {
                iv.setImageBitmap(postFileslist.get(0).getBitmap());
                postFileslist.get(0).setWidth(postFileslist.get(0).getBitmap().getWidth());
            } else {
                if (postFileslist.get(0).getFileType() == 1) {
                    ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + postFileslist.get(0).getGifPath(), iv, GoodHappinessApplication.options, null);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 2;
                    postFileslist.get(0).setWidth(BitmapFactory.decodeFile(postFileslist.get(0).getGifPath(),opts).getWidth());
                } else if (postFileslist.get(0).getFileType() == 4) {
                    if (!TextUtils.isEmpty(postFileslist.get(0).getGifPath())){
                        gi.setVisibility(View.VISIBLE);
                        AlxGifHelper.displayImage(new File(postFileslist.get(0).getGifPath()), gi, (int) (GoodHappinessApplication.perHeight*200));
//                        Glide.with(PublishActivity.this).load(postFileslist.get(0).getGifPath()).asGif().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
                    }
                }
            }
        } else {
            iv.setVisibility(View.GONE);
            postFilesBeanCommonAdapter = new CommonAdapter<PostFilesBean>(this, postFileslist, R.layout.layout_grid_publish_display) {
                @Override
                public void convert(ViewHolder helper, PostFilesBean item, int position) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) helper.getView(R.id.gallery_iv).getLayoutParams();
                    params.width = params.height = (int) (GoodHappinessApplication.perHeight * 190 / 3);
                    RelativeLayout relativeLayout =  helper.getView(R.id.gif_group);
                    relativeLayout.setVisibility(View.GONE);
                    helper.getView(R.id.gallery_iv).setLayoutParams(params);
                    if (item.getFileType() == 1) {//image
                        if (item.isProcessed()) {
                            helper.setImageBitmap(R.id.gallery_iv, item.getBitmap());
                            postFileslist.get(position).setWidth(item.getBitmap().getWidth());
                        } else {
                            ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + item.getGifPath(), (ImageView) helper.getView(R.id.gallery_iv), GoodHappinessApplication.options, null);
                            if (postFileslist.get(position).getWidth() == 0) {
                                BitmapFactory.Options opts = new BitmapFactory.Options();
                                opts.inSampleSize = 2;
                                postFileslist.get(position).setWidth(BitmapFactory.decodeFile(postFileslist.get(position).getGifPath(),opts).getWidth());
                            }
                        }
                    } else if (item.getFileType() == 4) {//gif
                        if (!TextUtils.isEmpty(item.getGifPath())){
                            relativeLayout.setVisibility(View.VISIBLE);
                            AlxGifHelper.displayImage(new File(item.getGifPath()), (GifImageView) helper.getView(R.id.gif_photo_view), (int) (GoodHappinessApplication.perHeight*60));
                            helper.getView(R.id.progress_wheel).setVisibility(View.GONE);
                            helper.getView(R.id.tv_progress).setVisibility(View.GONE);
//                            Glide.with(PublishActivity.this).load(item.getGifPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into((ImageView) helper.getView(R.id.gallery_iv));
                        }

                    }
                }
            };
            gv.setAdapter(postFilesBeanCommonAdapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PublishActivity.this, DisplayImgActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra(FieldFinals.ACTION, FieldFinals.LOCAL);
                    intent.putExtra(FieldFinals.POSITION, position);
                    bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, getIntent().getExtras().getParcelableArrayList(FieldFinals.FEED_INFO_LIST));
                    intent.putExtras(bundle);
                    startTheActivity(intent);

                }
            });
        }
    }

    private void setImageWidth(String s, Bitmap bitmap) {
        for (PostFilesBean bean : postFileslist) {
            if (bean.getFileType() == 1) {
                if ((s).endsWith(StringFinal.IMG_URI_HEAD + bean.getGifPath())) {
                    if (bean.getWidth() == 0) {
                        bean.setWidth(bitmap.getWidth());
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Bitmap getWaterMarkBitmap(Bitmap bitmap) {
        View view = LayoutInflater.from(this).inflate(R.layout.tv, null, false);
        EmojiTextView textView = (EmojiTextView) view.findViewById(R.id.tv);
        textView.setText(String.format(getString(R.string.format_photo_by, PreferencesUtil.getStringPreferences(this, FieldFinals.NICKNAME))));//"- Photo by " + PreferencesUtil.getStringPreferences(this, FieldFinals.NICKNAME) + " -");
        Bitmap q = convertViewToBitmap(view);
        Bitmap photoMark = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(photoMark);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(q, (bitmap.getWidth() - q.getWidth()) / 2, bitmap.getHeight() - q.getHeight() * 1.5f, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return photoMark;
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

    private void setMarkStatus() {
        if (isMarkChoose) {
            iv_mark.setImageResource(R.mipmap.ico_watermark_click);
            iv_markChoose.setVisibility(View.VISIBLE);
            iv_markNoChoose.setVisibility(View.GONE);
        } else {
            iv_mark.setImageResource(R.mipmap.ico_watermark);
            iv_markChoose.setVisibility(View.GONE);
            iv_markNoChoose.setVisibility(View.VISIBLE);
        }
        PreferencesUtil.setPreferences(this, FieldFinals.WATERMARK_CHOOSE, isMarkChoose);
    }

    @Event({R.id.publish_rl_channel, R.id.publish_rl_tag, R.id.publish_iv, R.id.publish_rl_watermark, R.id.publish_rl_save})
//, R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.publish_rl_channel:
                SelectChannelFragment fragment = SelectChannelFragment.newInstance(2, 5, false, false);
                fragment.setOnSelectListener(new OnSelectChannelListener() {
                    @Override
                    public void onSelected(String channel, int channelId) {
                        if (TextUtils.isEmpty(channel)) {
                            tv_channelTips.setVisibility(View.VISIBLE);
                            tv_channel.setVisibility(View.GONE);
                            currentId = -1;
                        } else {
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
                Intent intent = new Intent(PublishActivity.this, DisplayImgActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra(FieldFinals.ACTION, FieldFinals.LOCAL);
                intent.putExtra(FieldFinals.POSITION, 0);
                bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, getIntent().getExtras().getParcelableArrayList(FieldFinals.FEED_INFO_LIST));
                intent.putExtras(bundle);
                dialog.dismiss();
                startTheActivity(intent);
                break;
            case R.id.publish_rl_watermark:
                isMarkChoose = !isMarkChoose;
                setMarkStatus();
                break;
            case R.id.publish_rl_save:
                isSaveChoose = !isSaveChoose;
                setSaveStatus();
                break;
//            case R.id.common_right_text:
//                newDialog().show();
//                if (isMarkChoose) {
//                    getWaterMarkBitmap();
//                    if (photoMark != null) {
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        photoMark.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        b = new byte[]{};
//                        b = baos.toByteArray();
//                    }
//                }
//                if (isSaveChoose) {
//                }
//
//                QiNiuUploadUtils.getToken(this,new QiNiuUploadUtils.onGetTokenListener() {
//                    @Override
//                    public void onGetToken(QINIUToken qiniuToken) {
//                        if (qiniuToken != null) {
//                            GoodHappinessApplication.uploadManager.put(b, String.format(getString(R.string.format_photo_head),System.currentTimeMillis()), qiniuToken.getUploadToken(), new UpCompletionHandler() {
//                                @Override
//                                public void complete(String key, ResponseInfo info, JSONObject response) {
//                                    publish(key);
//                                }
//                            }, null);
//                        } else {
//                            showToast(R.string.upload_fail);
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                break;
        }
    }

    private void gotoAddTag() {
        Intent addTagIntent = new Intent(this, AddTagActivity.class);
        addTagIntent.putExtra(FieldFinals.ACTION, PublishActivity.TAG);
        addTagIntent.putStringArrayListExtra(FieldFinals.STRING_LIST, arrayList);
        startActivityForResult(addTagIntent, REQUEST_CODE);
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private void publish(ArrayList<PostFilesBean> postFileslist) {
        RequestParams params = new RequestParams(HttpFinal.POST_CREATE);
        JSONArray filesId = new JSONArray();
        JSONArray filesType = new JSONArray();
        for (PostFilesBean key : postFileslist) {
            Log.e("q_", "key:" + key.getWidth());
            if (isMarkChoose) {
                String waterMark = "- Photo by " + PreferencesUtil.getNickName() + " -";
                waterMark = Base64.encodeToString(waterMark.getBytes(), Base64.DEFAULT);
                waterMark = waterMark.replace("+", "-").replace("/", "_");
                filesId.add(new StringBuilder(key.getDuration()).append(StringFinal.WATERMARK_HEAD).append(waterMark).append(StringFinal.WATERMARK_END1).append(key.getWidth() != 0 ? key.getWidth() : 300).append(StringFinal.WATERMARK_END2).toString().replace("\n", ""));
            } else {
                filesId.add(key.getDuration());
            }
            filesType.add(key.getFileType());
        }
        if (arrayList != null && arrayList.size() > 0) {
            JSONArray tagJsonArray = new JSONArray();
            for (String tag : arrayList) {
                tagJsonArray.add(tag);
            }
            params.addBodyParameter(FieldFinals.TAG_ARRAY, tagJsonArray.toString());
        }
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.CHANNEL, tv_channel.getText().toString());
        if (currentId != -1) {
            params.addBodyParameter(FieldFinals.CHANNEL_ID, currentId + "");
        }
        params.addBodyParameter(FieldFinals.POST_CONTENT, et.getText().toString());
        params.addBodyParameter(FieldFinals.FILES_TYPE, filesType.toString());
        params.addBodyParameter(FieldFinals.FILES_ID, filesId.toString());
        params.addBodyParameter(FieldFinals.LAT, 0 + "");
        params.addBodyParameter(FieldFinals.LNG, 0 + "");
        HttpUtils.post(this, params, new TypeToken<Result<DeviceInfo>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                newDialog().show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                PreferencesUtil.setPreferences(PublishActivity.this, FieldFinals.ADD_PUBLISH, true);
                AppManager.getAppManager().finishActivities(new Class[]{EditPicV5Activity.class
                });
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
        if (resultCode == AddTagActivity.RESULT_CODE && data != null) {
            ArrayList<String> strings = data.getStringArrayListExtra(FieldFinals.STRING_LIST);
            if (strings != null && strings.size() > 0) {
                setTag(strings);
                arrayList.clear();
                arrayList.addAll(strings);
            } else {
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
        tagAdapter.setOnItemClickLitener(new TagAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                gotoAddTag();
            }
        });
    }
}
