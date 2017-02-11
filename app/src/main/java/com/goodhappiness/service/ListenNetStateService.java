package com.goodhappiness.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.goodhappiness.GoodHappinessApplication;

public class ListenNetStateService extends Service {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "网络状态已经改变");
				connectivityManager = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
					GoodHappinessApplication.isNetConnect = true;
					GoodHappinessApplication.netStateUtils.giveNewMess(true);
					GoodHappinessApplication.netStateUtils.notifyObservers();
				} else {
					Log.d("mark", "没有可用网络");
					GoodHappinessApplication.isNetConnect = false;
					GoodHappinessApplication.netStateUtils.giveNewMess(false);
					GoodHappinessApplication.netStateUtils.notifyObservers();
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
}