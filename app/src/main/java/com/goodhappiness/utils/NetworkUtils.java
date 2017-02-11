package com.goodhappiness.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 网络工具类
 * @author hy 2014-10-8上午 14:23
 */
public class NetworkUtils {
	private static final String TAG = NetworkUtils.class.getSimpleName();
	public static final int NO_NETWORK = 0;// 无网络
	public static final int TYPE_WIFI = 1;// WIFI
	public static final int TYPE_MOBILE = 2;// 运营商网络
	
	public enum NetType {
		None(1), Mobile(2), Wifi(4), Other(8);
		NetType(int value) {
			this.value = value;
		}

		public int value;
	}
	
	/**
	 * @description:检测当前的网络状态
	 * @return
	 */
	public static int checkNetwork(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getType() == ConnectivityManager.TYPE_WIFI) {
					return TYPE_WIFI;
				} else {
					return TYPE_MOBILE;
				}
			} else {
				return NO_NETWORK;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return NO_NETWORK;
		}
	}
	
	/**
	 * 获取ConnectivityManager
	 */
	public static ConnectivityManager getConnManager(Context context) {
		return (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 判断网络连接是否有效（此时可传输数据）。
	 * 
	 * @param context
	 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
	 */
	public static boolean isConnected(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		return net != null && net.isConnected();
	}

	/**
	 * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
	 * 
	 * @param context
	 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
	 */
	public static boolean isConnectedOrConnecting(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.isConnectedOrConnecting()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取当前连接的网络类型
	 * @param context
	 * @return
	 */
	public static NetType getConnectedType(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		if (net != null) {
			switch (net.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				return NetType.Wifi;
			case ConnectivityManager.TYPE_MOBILE:
				return NetType.Mobile;
			default:
				return NetType.Other;
			}
		}
		return NetType.None;
	}

	/**
	 * 是否存在有效的WIFI连接
	 */
	public static boolean isWifiConnected(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		return net != null && net.getType() == ConnectivityManager.TYPE_WIFI
				&& net.isConnected();
	}

	/**
	 * 是否存在有效的移动连接
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobileConnected(Context context) {
		NetworkInfo net = getConnManager(context).getActiveNetworkInfo();
		return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE
				&& net.isConnected();
	}

	/**
	 * 检测网络是否为可用状态
	 */
	public static boolean isAvailable(Context context) {
		return isWifiAvailable(context)
				|| (isMobileAvailable(context) && isMobileEnabled(context));
	}

	/**
	 * 判断是否有可用状态的Wifi，以下情况返回false： 1. 设备wifi开关关掉; 2. 已经打开飞行模式； 3. 设备所在区域没有信号覆盖；
	 * 4. 设备在漫游区域，且关闭了网络漫游。
	 * 
	 * @param context
	 * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
	 */
	public static boolean isWifiAvailable(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.getType() == ConnectivityManager.TYPE_WIFI) {
					return net.isAvailable();
				}
			}
		}
		return false;
	}

	/**
	 * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
	 * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。 以下情况它是不可用的，将返回false： 1.
	 * 设备打开飞行模式； 2. 设备所在区域没有信号覆盖； 3. 设备在漫游区域，且关闭了网络漫游。
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobileAvailable(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.getType() == ConnectivityManager.TYPE_MOBILE) {
					return net.isAvailable();
				}
			}
		}
		return false;
	}

	/**
	 * 设备是否打开移动网络开关
	 * 
	 * @param context
	 * @return boolean 打开移动网络返回true，反之false
	 */
	public static boolean isMobileEnabled(Context context) {
		try {
			Method getMobileDataEnabledMethod = ConnectivityManager.class
					.getDeclaredMethod("getMobileDataEnabled");
			getMobileDataEnabledMethod.setAccessible(true);
			return (Boolean) getMobileDataEnabledMethod
					.invoke(getConnManager(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 打印当前各种网络状态
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean printNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo in = connectivity.getActiveNetworkInfo();
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {

					Log.i(TAG,
							"NetworkInfo[" + i + "]isAvailable : "
									+ info[i].isAvailable());
					Log.i(TAG,
							"NetworkInfo[" + i + "]isConnected : "
									+ info[i].isConnected());
					Log.i(TAG,
							"NetworkInfo[" + i + "]isConnectedOrConnecting : "
									+ info[i].isConnectedOrConnecting());
					Log.i(TAG, "NetworkInfo[" + i + "]: " + info[i]);

				}
				Log.i(TAG, "\n");
			} else {
				Log.i(TAG, "getAllNetworkInfo is null");
			}
		}
		return false;
	}

	/**
	 * 检测连接的网络是否正常
	 * @return
	 */
	public static boolean ping() {

		String result = null;

		String ip = "www.163.com";

		try {
			// Process
			// process=Runtime.getRuntime().exec("ping "+ip+" "+"-c 3 >null");

			Process process = Runtime.getRuntime().exec(
					"/system/bin/ping -c 3 -w 100 " + ip);
			InputStream inputStream = process.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuffer strbuff = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				strbuff.append(content);
			}

			Log.i("han", "result content:" + strbuff.toString());

			int status = process.waitFor();
			if (status == 0) {

				result = "successful~~~~";
				
				return true;
			}

			else {
				result = "failed~cannot reach the IP address";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "failed~InterruptedException";
		} finally {

			Log.i("han", "result=" + result);
		}

		return false;

	}
}