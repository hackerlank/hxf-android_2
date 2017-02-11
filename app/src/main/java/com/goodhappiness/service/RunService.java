package com.goodhappiness.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.receiver.PushReciver;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.List;

/**
 * 记录前台后台时间的服务
 */
public class RunService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		// 创建Intent
		Intent intent = new Intent(this, PushReciver.class);
		intent.setAction("com.example.clock");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		// 周期触发
		manager.setRepeating(AlarmManager.RTC, 0, 5 * 1000, pendingIntent);
		super.onCreate();
	}

	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
			if (!isApplicationBroughtToBackground(getApplicationContext())) {// 前台
				if(!PreferencesUtil.getBooleanPreferences(this, FieldFinals.MOBIL_STATUS_FLAG,false)){
					PreferencesUtil.setPreferences(this, FieldFinals.MOBIL_STATUS_FLAG,true);
					postActive(FieldFinals.OPEN);
				}
			} else {// 后台
				if(PreferencesUtil.getBooleanPreferences(this, FieldFinals.MOBIL_STATUS_FLAG,false)){
					PreferencesUtil.setPreferences(this, FieldFinals.MOBIL_STATUS_FLAG,false);
					postActive(FieldFinals.QUIT);
				}
			}
		return super.onStartCommand(intent, flags, startId);
	}

	private void postActive(String action){
		RequestParams params = new RequestParams(HttpFinal.ACTIVE);
		params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getDid());
		params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getSid());
		params.addBodyParameter(FieldFinals.ACTION,action);
		HttpUtils.post(this,params, new TypeToken<Result<BaseBean>>() {
		}.getType(), new OnHttpRequest() {
			@Override
			public void onSuccess(Result result) {

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(Callback.CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onWaiting() {

			}

			@Override
			public void onStarted() {

			}

			@Override
			public void onLoading(long total, long current, boolean isDownloading) {

			}
		});
}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
