package com.goodhappiness.dao;

import com.goodhappiness.bean.Result;

import org.xutils.common.Callback;

/**
 * Created by 电脑 on 2016/4/8.
 */
public interface OnHttpRequest {
    void onSuccess(Result result);
    void onError(Throwable ex, boolean isOnCallback);
    void onCancelled(Callback.CancelledException cex);
    void onFinished();
    void onWaiting();
    void onStarted();
    void onLoading(long total, long current, boolean isDownloading);
}
