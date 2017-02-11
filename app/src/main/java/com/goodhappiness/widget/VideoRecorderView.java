package com.goodhappiness.widget;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suzhenpeng on 2015/6/1.
 */
public class VideoRecorderView extends LinearLayout implements MediaRecorder.OnErrorListener {

    //视频展示
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHoler;

    private SurfaceView videoSurfaceView;
    private ImageView playVideo;

    //进度条
    private ProgressBar progressBar_left;
    private ProgressBar progressBar_right;

    //录制视频
    private MediaRecorder mediaRecorder;
    //摄像头
    private Camera camera;
    private Timer timer;
    private Camera.Size cSize;
    //视频播放
    private MediaPlayer mediaPlayer;

    //时间限制
    private static final int recordMaxTime = 60;
    private int timeCount;
    //生成的文件
    private File vecordFile;

    private Context context;
    private Camera.AutoFocusCallback autoFocusCallback;
    //正在录制
    private boolean isRecording = false;
    //录制成功
    private boolean isSuccess = false;

    private RecorderListener recorderListener;

    public VideoRecorderView(Context context) {
        super(context, null);
        this.context = context;
    }

    public VideoRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
        init();
    }

    public VideoRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void init() {
//        mediaPlayer = new MediaPlayer();
        LayoutInflater.from(context).inflate(R.layout.ui_recorder, this);
        autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    Log.e("a_","s");
                }else{
                    Log.e("a_","f");
                }
            }
        };
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera!=null){
                    if(isRecording){
                        camera.autoFocus(autoFocusCallback);
                    }
                }
            }
        });
        videoSurfaceView = (SurfaceView) findViewById(R.id.playView);
        playVideo = (ImageView) findViewById(R.id.playVideo);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_process);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        params.setMargins(0, (int) (GoodHappinessApplication.perHeight*76+GoodHappinessApplication.w),0,0);
        ll.setLayoutParams(params);
        progressBar_left = (ProgressBar) findViewById(R.id.progressBar_left);
        progressBar_right = (ProgressBar) findViewById(R.id.progressBar_right);

        progressBar_left.setMax(recordMaxTime * 20);
        progressBar_right.setMax(recordMaxTime * 20);

        progressBar_left.setProgress(recordMaxTime * 20);

        surfaceHoler = surfaceView.getHolder();
        surfaceHoler.addCallback(new CustomCallBack());
        surfaceHoler.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        initCamera();
        playVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }
    public static Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution){
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float)screenResolution.x / (float)screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for(Camera.Size s : supportedPreviewSizes){
            tmp = Math.abs(((float)s.height/(float)s.width)-x_d_y);
            if(tmp<mindiff){
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }
    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth
     *            需要被进行对比的原宽
     * @param surfaceHeight
     *            需要被进行对比的原高
     * @param preSizeList
     *            需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public static Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> preSizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
//        if (mIsPortrait) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
//        } else {
//            ReqTmpWidth = surfaceWidth;
//            ReqTmpHeight = surfaceHeight;
//        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }
    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            freeCameraResource();
        }
    }


    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (Exception e) {

        }
    }

    public void reRecord(){
        progressBar_left.setProgress(recordMaxTime * 20);
        progressBar_right.setProgress(0);
        surfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setVisibility(GONE);
        playVideo.setVisibility(View.GONE);
        initCamera();
        recorderListener.recordSuccess(null);
    }

    /**
     * 初始化摄像头
     */
    public void initCamera() {
        if (camera != null)
            freeCameraResource();
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (camera == null)
            return;

        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHoler);
//            Camera.Parameters parameters = camera.getParameters();
//            for(Camera.Size size:parameters.getSupportedPreviewSizes()){
//                Log.e("q,","llllllist:"+size.height+"-----"+size.width);
//                if(size.width>=600&&size.width<=1000){
//                    cSize = size;
//                    break;
//                }
//            }
//            Camera.Size size = VideoRecorderView.getCloselyPreSize(PhoneUtil.getResolution(context)[0],PhoneUtil.getResolution(context)[1],parameters.getSupportedPreviewSizes());
            Camera.Size size =camera.getParameters().getPreviewSize();
            cSize = size;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
            params.width = GoodHappinessApplication.w;
            if(size.height>size.width){
                params.height = (int) (((float)params.width*size.height)/size.width);
            }else{
                params.height = (int) (((float)params.width*size.width)/size.height);
            }
            surfaceView.setLayoutParams(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        camera.unlock();
    }
    private static final int OFF_TIME = 3 * 60 * 1000;

    public boolean turnOnFlashLight() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                parameter.set("flash-mode", "torch");
            }
            camera.setParameters(parameter);
        }
        return true;
    }

    public boolean turnOffFlashLight() {
        if (camera != null) {
            Camera.Parameters parameter = camera.getParameters();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                parameter.set("flash-mode", "off");
            }
            camera.setParameters(parameter);
        }
        return true;
    }
    public Camera getCamera() {
        return camera;
    }

    /**
     * 初始化摄像头配置
     */
    private void initRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        if (camera != null)
            mediaRecorder.setCamera(camera);

        mediaRecorder.setOnErrorListener(this);
        mediaRecorder.setPreviewDisplay(surfaceHoler.getSurface());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        try {
            camera.setPreviewDisplay(surfaceHoler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        if(cSize!=null){
        mediaRecorder.setVideoSize(cSize.width, cSize.height);
        }

//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//        mediaRecorder.setVideoFrameRate(25);
        mediaRecorder.setAudioEncodingBitRate(48000);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setVideoEncodingBitRate(500*1024);
        mediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mediaRecorder.setMaxDuration(recordMaxTime * 1000);
        mediaRecorder.setOutputFile(vecordFile.getAbsolutePath());
    }

    private void prepareRecord() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        Log.e("a_","startRecord");
        //录制中
        if (isRecording)
            return;
        //创建文件
        createRecordDir();
        initCamera();

        videoSurfaceView.setVisibility(View.GONE);
        playVideo.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
        //初始化控件
        initRecord();
        prepareRecord();
        isRecording = true;
        if (recorderListener != null)
            recorderListener.recordStart();
        //10秒自动化结束
        timeCount = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeCount++;
                Log.e("q_","tt");
                progressBar_left.setProgress(timeCount);
                progressBar_right.setProgress(recordMaxTime * 20 - timeCount);
                if (recorderListener != null)
                    recorderListener.recording(recordMaxTime * 1000, timeCount * 50);
                if (timeCount == recordMaxTime * 20) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        }, 0, 50);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                endRecord();
            }else if(msg.what==2){
                progressBar_left.setProgress(recordMaxTime * 20);
                progressBar_right.setProgress(0);
                Log.e("q_","hh");
            }
        }
    };

    /**
     * 停止录制
     */
    public void endRecord() {
        Log.e("a_","endRecord");
        if (!isRecording)
            return;
        isRecording = false;
        if (recorderListener != null) {
            recorderListener.recordStop();
            recorderListener.recordSuccess(vecordFile);
        }
        stopRecord();
        releaseRecord();
        freeCameraResource();
        videoSurfaceView.setVisibility(View.VISIBLE);
        playVideo.setVisibility(View.VISIBLE);
    }

    /**
     * 取消录制
     */
    public void cancelRecord(int result) {
        Log.e("a_","cancelRecord");
        Message message = new Message();
        message.what = 2;
        handler.sendMessageDelayed(message,500);
        videoSurfaceView.setVisibility(View.GONE);
        playVideo.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
        if (!isRecording)
            return;
        isRecording = false;
        stopRecord();
        releaseRecord();
        freeCameraResource();
        isRecording = false;
        if (vecordFile.exists()){
            vecordFile.delete();

        }
        if (recorderListener != null)
            recorderListener.recordCancel(result);
        initCamera();
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        Log.e("a_","stopRecord");
        progressBar_left.setProgress(0);
        progressBar_right.setProgress(recordMaxTime * 20);

        if (timer != null)
            timer.cancel();
        if (mediaRecorder != null) {
            // 设置后不会崩
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setPreviewDisplay(null);
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void destoryMediaPlayer() {
        if (mediaPlayer == null)
            return;
        mediaPlayer.setDisplay(null);
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        surfaceView.setVisibility(View.GONE);
        videoSurfaceView.setVisibility(View.VISIBLE);
        playVideo.setVisibility(View.GONE);

        try {
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(context, Uri.parse(vecordFile.getAbsolutePath()));
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(vecordFile.getAbsolutePath());
            mediaPlayer.setDisplay(videoSurfaceView.getHolder());
            mediaPlayer.prepare();//缓冲
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (recorderListener != null)
            recorderListener.videoStart();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopVideo();
            }
        });
    }

    public void stopVideo() {
        if (recorderListener != null){
            recorderListener.videoStop();
            if(vecordFile!=null&&vecordFile.exists()){
                playVideo.setVisibility(View.VISIBLE);
            }
        }
    }

    public RecorderListener getRecorderListener() {
        return recorderListener;
    }

    public void setRecorderListener(RecorderListener recorderListener) {
        this.recorderListener = recorderListener;
    }

    public SurfaceView getSurfaceView() {

        return surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public interface RecorderListener {

        public void recording(int maxtime, int nowtime);

        public void recordSuccess(File videoFile);

        public void recordStop();

        public void recordCancel(int result);

        public void recordStart();

        public void videoStop();

        public void videoStart();
    }


    /**
     * 创建视频文件
     */
    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "GoodHappiness/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            vecordFile = new File(sampleDir.getAbsolutePath()+"/" + "hxf"+ System.currentTimeMillis()+ ".mp4");
            if (!vecordFile.exists()) {
                vecordFile.createNewFile();
            }
//            vecordFile = File.createTempFile("hxf_record_", ".mp4", vecordDir);//mp4格式
        } catch (IOException e) {
        }
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            try {
                mediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mediaRecorder = null;
    }

    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }
}
