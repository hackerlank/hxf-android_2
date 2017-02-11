package com.goodhappiness.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.OtherFinals;
import com.goodhappiness.dao.Observer;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.SystemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 后台下载服务
 */
@SuppressLint("UseSparseArrays")
public class DownloadService extends Service implements Observer{

    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private boolean cancelUpdate = false;
    public static boolean isDownLoading = false;
    private MyHandler myHandler;
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
    public Map<Integer, Integer> download = new HashMap<Integer, Integer>();
    public Context context;
    private Intent mIntent;
    private int count = 0;
    private boolean isFinish = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!TextUtils.isEmpty(intent.getStringExtra("url"))) {
            mIntent = intent;
            downNewFile(intent.getStringExtra("url"), count, intent.getStringExtra("name"));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void reDownload(){
        if (!TextUtils.isEmpty(mIntent.getStringExtra("url"))) {
            downNewFile(mIntent.getStringExtra("url"), count, "Download");
        }
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
    public void onCreate() {
        super.onCreate();
        isDownLoading = true;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        myHandler = new MyHandler(Looper.myLooper(), DownloadService.this);
        GoodHappinessApplication.netStateUtils.addObserver(this);
        context = DownloadService.this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    public static void stopService() {
//        myHandler.sendEmptyMessage(100);
//    }

    private void downNewFile(final String url, final int notificationId, final String name) {
        if (download.containsKey(notificationId)){
            notificationManager.cancel(count);
        }
        builder = new Notification.Builder(
                context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.begin_download_new_version)).setContentInfo("0%")
                .setOngoing(true).setContentTitle(getString(R.string.downloading_new_version)).setAutoCancel(false);
        download.put(notificationId, 0);
        notificationManager.notify(0, builder.build());
        downFile(url, notificationId, name);
    }

    // 下载更新文件
    private void downFile(final String url, final int notificationId, final String name) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                File tempFile = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    // params[0]代表连接的url
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    if (is != null) {
                        FileUtils.createRootFile(context);
                        tempFile = new File(FileUtils.getDownloadStorageDirectory(context), File.separator + OtherFinals.APK_NAME);//File.separator + APPConstants.MX_FILE_HEAD +
                        if (tempFile.exists())
                            tempFile.delete();
                        tempFile.createNewFile();
                        Log.i("mark", tempFile.getPath());
                        // 已读出流作为参数创建一个带有缓冲的输出流
                        BufferedInputStream bis = new BufferedInputStream(is);
                        // 创建一个新的写入流，讲读取到的图像数据写入到文件中
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        // 已写入流作为参数创建一个带有缓冲的写入流
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        int read;
                        long count = 0;
                        int precent = 0;
                        byte[] buffer = new byte[1024];
                        while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
                            bos.write(buffer, 0, read);
                            count += read;
                            precent = (int) (((double) count / length) * 100);
                            // 每下载完成1%就通知任务栏进行修改下载进度
                            if (precent - download.get(notificationId) >= 1) {
                                download.put(notificationId, precent);
                                Log.e("k_","precent:"+precent);
                                myHandler.sendMessage(myHandler.obtainMessage(3,precent,0));
                            }
                        }
                        bos.flush();
                        bos.close();
                        fos.flush();
                        fos.close();
                        is.close();
                        bis.close();
                    }

                    if (!cancelUpdate) {
                        Message message = myHandler.obtainMessage(2, tempFile);
                        message.arg1 = notificationId;
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                    } else {
                        tempFile.delete();
                    }
                } catch (ClientProtocolException e) {
                    if (tempFile.exists())
                        tempFile.delete();
                    Message message = myHandler.obtainMessage(4, name + "下载失败：网络异常！");
                    message.arg1 = notificationId;
                    myHandler.sendMessage(message);
                    SystemUtils.systemBrowser(context, url);
                } catch (IOException e) {
                    if (tempFile.exists())
                        tempFile.delete();
                    Message message = myHandler.obtainMessage(4, name + "下载失败：文件传输异常");
                    message.arg1 = notificationId;
                    myHandler.sendMessage(message);
                    SystemUtils.systemBrowser(context, url);
                } catch (Exception e) {
                    if (tempFile != null && tempFile.exists())
                        tempFile.delete();
                    Message message = myHandler.obtainMessage(4, name + "下载失败," + e.getMessage());
                    message.arg1 = notificationId;
                    myHandler.sendMessage(message);
                    SystemUtils.systemBrowser(context, url);
                }
            }
        });
    }

    // 下载更新文件
    public void downPatchFile(final String url, final Context context, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                File tempFile = null;
                try {
                    HttpClient client = new DefaultHttpClient();
                    // params[0]代表连接的url
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    if (is != null) {
                        FileUtils.createRootFile(context);
                        tempFile = new File(FileUtils.getDownloadStorageDirectory(context), File.separator + name);
                        if (tempFile.exists())
                            tempFile.delete();
                        tempFile.createNewFile();
                        Log.i("mark", tempFile.getName());
                        // 已读出流作为参数创建一个带有缓冲的输出流
                        BufferedInputStream bis = new BufferedInputStream(is);
                        // 创建一个新的写入流，讲读取到的图像数据写入到文件中
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        // 已写入流作为参数创建一个带有缓冲的写入流
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        int read;
                        byte[] buffer = new byte[1024];
                        while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
                            bos.write(buffer, 0, read);
                        }
                        bos.flush();
                        bos.close();
                        fos.flush();
                        fos.close();
                        is.close();
                        bis.close();
                    }

                } catch (ClientProtocolException e) {
                    Log.i("mark", e.getMessage());
                } catch (IOException e) {
                    Log.i("mark", e.getMessage());
                } catch (Exception e) {
                    Log.i("mark", e.getMessage());
                }
            }
        }).start();
    }

    // 安装下载后的apk文件
    private void install(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    public void onNetStateChange(boolean isConnect) {
        if(isConnect&&!isFinish){
            reDownload();
        }
    }

    /* 事件处理类 */
    class MyHandler extends Handler {
        private Context context;

        public MyHandler(Looper looper, Context c) {
            super(looper);
            this.context = c;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        download.remove(msg.arg1);
                        break;
                    case 1:
                        break;
                    case 2:
                        builder.setContentTitle("下载完毕")
                                .setProgress(100, 100, false).setOngoing(false).setContentInfo("");
                        notificationManager.notify(0, builder.build());
                        download.remove(msg.arg1);
                        notificationManager.cancel(msg.arg1);
                        isFinish = true;
                        install((File) msg.obj, context);
                        break;
                    case 3:
                        builder.setProgress(100, msg.arg1, false).setContentInfo(msg.arg1+"%").setAutoCancel(true);
                        notificationManager.notify(0, builder.build());
                        break;
                    case 4:
                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        download.remove(msg.arg1);
                        builder .setAutoCancel(true);
                        notificationManager.cancel(msg.arg1);
                        break;
                }
                stopSelf();
            }
        }
    }

}
