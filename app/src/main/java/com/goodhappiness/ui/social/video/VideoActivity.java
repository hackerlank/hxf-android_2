package com.goodhappiness.ui.social.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_video)
public class VideoActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback{

    private static final String TAG = "VideoActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";

    @Override
    protected void setData() {

    }
    @Override
    protected void reload() {

    }

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mVideoLayout;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        if(getIntent()!=null&&!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.URL))){
            setVideoAreaSize(getIntent().getStringExtra(FieldFinals.URL));
        }
        mVideoView.setVideoViewCallback(this);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion ");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }

    /**
     * 置视频区域大小
     * @param stringExtra
     */
    private void setVideoAreaSize(final String stringExtra) {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
//                int width = mVideoLayout.getWidth();
//                cachedHeight = (int) (width * 405f / 720f);
//                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
//                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                videoLayoutParams.height = cachedHeight;
//                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(stringExtra);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
        }

    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

}
