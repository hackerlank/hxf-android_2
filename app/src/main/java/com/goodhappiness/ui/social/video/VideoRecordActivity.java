package com.goodhappiness.ui.social.video;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.BitmapEntity;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.widget.VideoRecorderView;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

@ContentView(R.layout.activity_video_record)
public class VideoRecordActivity extends BaseActivity {
    @ViewInject(R.id.video_cancel)
    private ImageView iv_cancel;
    @ViewInject(R.id.video_complete)
    private ImageView iv_complete;
    @ViewInject(R.id.camera_iv)
    private ImageView iv_camera;
    @ViewInject(R.id.iv_flash)
    private ImageView iv_flash;
    private VideoRecorderView recoderView;
    private Button videoController;
    private TextView message;
    private boolean isCancel = false;
    private String videoPath ="";
    private ArrayList<BitmapEntity> bit = new ArrayList<>();
    private boolean isFlashOpen = false;
    private long firstTime;
    private long endtTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVideo();
        initVideoList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        recoderView.stopVideo();
    }
    @Event({R.id.iv_flash,R.id.camera_iv,R.id.iv_back})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.iv_flash:
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    showToast(R.string.do_not_support_flash);
                    return;
                }
                if(!recoderView.isRecording()){
                    showToast(R.string.open_flash_light_until_record);
                    return;
                }
                isFlashOpen = !isFlashOpen;
                if(isFlashOpen){
                    recoderView.turnOnFlashLight();
                    iv_flash.setImageResource(R.mipmap.ico_flash_on);
                }else{
                    recoderView.turnOffFlashLight();
                    iv_flash.setImageResource(R.mipmap.ico_flash_off);
                }
                break;
            case R.id.iv_back:
                if(!recoderView.isRecording()) {
                    finishActivity();
                }
                break;
            case R.id.camera_iv:
                if(!recoderView.isRecording()){
                    if(bit.size()>0){
                        Intent intent = new Intent(this,VideoListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(FieldFinals.ACTION,bit);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        showToast(R.string.no_local_video);
                    }
                }
                break;
        }
    }

    private void initVideoList() {
        newDialog();
        new SearchPhoto().start();
    }


    /**
     *
     * 遍历系统数据库找出相应的是视频的信息，每找出一条视频信息的同时利用与之关联的找出对应缩略图的uri
     * 再异步加载缩略图，
     * 由于查询速度非常快，全部查找完成在设置，等待时间不会太长
     * @author Administrator
     *
     */
    class SearchPhoto extends Thread {
        @Override
        public void run() {
            // 如果有sd卡（外部存储卡）
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = VideoRecordActivity.this.getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null, null);
                if (cursor == null) {
                    return;
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if(!cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)).contains("mp4")){
                        continue;
                    }
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    if(size>1024*1024*5){
                        continue;
                    }
                    Log.e("q_size",size+"");
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    //获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String[] selectionArgs = new String[] { id + "" };
                    String[] thumbColumns = new String[] { MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID };
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";

                    String uri_thumb = "";
                    Cursor thumbCursor = (VideoRecordActivity.this.getApplicationContext().getContentResolver()).query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
                            null);

                    if (thumbCursor != null && thumbCursor.moveToFirst()) {
                        uri_thumb = thumbCursor
                                .getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

                    }

                    BitmapEntity bitmapEntity = new BitmapEntity(title, path, size, uri_thumb, duration);
                    bit.add(bitmapEntity);

                }
                if (cursor != null) {
                    cursor.close();
                    mHandler.sendEmptyMessage(0);
                }
            }
            dialog.dismiss();
        }
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(bit.size()>0&&!TextUtils.isEmpty(bit.get(0).getUri_thumb())){
                        ImageLoader.getInstance().displayImage("file://" + bit.get(0).getUri_thumb(), iv_camera);
                    }
                    break;
                case 1:
//                    if(recoderView.getCamera()!=null&&recoderView.getCamera().getParameters()!=null){
//                        Camera.Size size = recoderView.getCloselyPreSize(GoodHappinessApplication.w,GoodHappinessApplication.h,recoderView.getCamera().getParameters().getSupportedPreviewSizes());
//                        Log.e("q,","");
//                    }
                    break;
            }
        }
    };

    private void initVideo() {
        recoderView = (VideoRecorderView) findViewById(R.id.recoder);
        videoController = (Button) findViewById(R.id.videoController);
        message = (TextView) findViewById(R.id.message);
//        ViewGroup.LayoutParams params = recoderView.getLayoutParams();
//        int[] dev = PhoneUtil.getResolution(this);
//        mHandler.sendEmptyMessageDelayed(1,5000);
//        params.width = dev[0];
//       showToast("q_h:"+getIntent().getIntExtra(FieldFinals.WIDTH,4)+" w:"+getIntent().getIntExtra(FieldFinals.HEIGHT,3));
//        params.height = (int) (((float) dev[0]) *dev[1]);// getIntent().getIntExtra(FieldFinals.WIDTH,4) / getIntent().getIntExtra(FieldFinals.HEIGHT,3));//
//        recoderView.setLayoutParams(params);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_btn);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rl.getLayoutParams();
        params1.height = (int) (GoodHappinessApplication.h - GoodHappinessApplication.perHeight * 80 - GoodHappinessApplication.w);
        rl.setLayoutParams(params1);
        videoController.setOnTouchListener(new VideoTouchListener());
        RxView.clicks(videoController).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
            }
        });
        recoderView.setRecorderListener(new VideoRecorderView.RecorderListener() {

            @Override
            public void recording(int maxtime, int nowtime) {

            }

            @Override
            public void recordSuccess(File videoFile) {
                System.out.println("recordSuccess");
                iv_flash.setImageResource(R.mipmap.ico_flash_off);
                if (videoFile != null){
                    videoPath = videoFile.getAbsolutePath();
                }else{
                    videoPath = "";
                }
                releaseAnimations();
                setRecordStatus(false);
            }

            @Override
            public void recordStop() {
                System.out.println("recordStop");
                iv_flash.setImageResource(R.mipmap.ico_flash_off);
            }

            @Override
            public void recordCancel(int result) {
                System.out.println("recordCancel");
                releaseAnimations();
                setRecordStatus(true);
                iv_flash.setImageResource(R.mipmap.ico_flash_off);
            }

            @Override
            public void recordStart() {
                System.out.println("recordStart");
                setRecordStatus(true);
            }

            @Override
            public void videoStop() {
                System.out.println("videoStop");
                iv_flash.setImageResource(R.mipmap.ico_flash_off);
            }

            @Override
            public void videoStart() {
                System.out.println("videoStart");
            }
        });
    }

    @Event({R.id.video_cancel,R.id.video_complete})
    private void OnEventClick(View v){
        switch (v.getId()){
            case R.id.video_cancel:
                setRecordStatus(true);
                if(recoderView!=null){
                    recoderView.reRecord();
                }
            break;
            case R.id.video_complete:
                if(!TextUtils.isEmpty(videoPath)){
                    Intent intent = new Intent(this,VideoPublishActivity.class);
                    ContentResolver localContentResolver = getContentResolver();
                    ContentValues localContentValues = FileUtils.getVideoContentValues(new File(videoPath));
                    localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    intent.putExtra(FieldFinals.FILE_PATH,videoPath);
                    intent.putExtra(FieldFinals.DURATION,FileUtils.getVideoDuration(videoPath));
                    startActivity(intent);
                }
            break;
        }
    }

    private void setRecordStatus(boolean isRecording) {
        iv_camera.setVisibility(isRecording ? View.VISIBLE : View.GONE);
        iv_flash.setVisibility(isRecording ? View.VISIBLE : View.GONE);
        iv_cancel.setVisibility(isRecording ? View.GONE : View.VISIBLE);
        iv_complete.setVisibility(isRecording ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void reload() {

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isCanRecord = true;
                    break;
            }
        }
    };

    private boolean isCanRecord = true;

    public class VideoTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(!isCanRecord){
                        showToast("请稍后再试");
                        return false;
                    }
                    if(recoderView.isRecording()){
                        return false;
                    }
                    recoderView.startRecord();
                    firstTime = System.currentTimeMillis();
                    isCancel = false;
                    pressAnimations();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!isCanRecord){
                        return false;
                    }
                    if (event.getX() > 0
                            && event.getX() < videoController.getWidth()
                            && event.getY() > 0
                            && event.getY() < videoController.getHeight()) {
                        showPressMessage();
                        isCancel = false;
                    } else {
                        cancelAnimations();
                        isCancel = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(!isCanRecord){
                        return false;
                    }
                    endtTime = System.currentTimeMillis();
                    if(endtTime-firstTime<5*1000){
                        isCanRecord = false;
                        Log.e("q_","t:"+(endtTime-firstTime));
                        if (!isCancel) {
                            showToast("录制时间太短");
                        }
                        recoderView.cancelRecord(0);
                        handler.sendEmptyMessageDelayed(0,3000);
                        recoderView.reRecord();
                        setRecordStatus(true);
                        return false;
                    }
                    if (isCancel) {
                        recoderView.cancelRecord(0);
                    } else {
                        recoderView.endRecord();
                    }
                    message.setVisibility(View.GONE);
                    releaseAnimations();

                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 移动取消弹出动画
     */
    public void cancelAnimations() {
        message.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        message.setTextColor(getResources().getColor(android.R.color.white));
        message.setText("松手取消");
    }

    /**
     * 显示提示信息
     */
    public void showPressMessage() {
        message.setVisibility(View.VISIBLE);
        message.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        message.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        message.setText("上移取消");
    }


    /**
     * 按下时候动画效果
     */
    public void pressAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f,
                1, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        videoController.startAnimation(animationSet);
    }

    /**
     * 释放时候动画效果
     */
    public void releaseAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f,
                1.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        message.setVisibility(View.GONE);
        videoController.startAnimation(animationSet);
    }
}
