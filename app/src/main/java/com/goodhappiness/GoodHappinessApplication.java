package com.goodhappiness;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.goodhappiness.constant.OtherFinals;
import com.goodhappiness.utils.PushNetStateUtils;
import com.goodhappiness.wxapi.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.storage.UploadManager;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import org.lasque.tusdk.core.TuSdk;
import org.litepal.LitePalApplication;
import org.xutils.x;

import java.io.File;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import io.rong.imkit.RongIM;
import io.rong.push.RongPushClient;

/**
 * Created by 刘嘉宇 on 2016/3/25.
 */
public class GoodHappinessApplication extends LitePalApplication {
    public static DisplayImageOptions options;
    public static UploadManager uploadManager;
    public static IWXAPI wxapi;
    public static PushNetStateUtils netStateUtils;// 加载网络类
    public static String currentPhotoPath = "";
    private static Context context;
    public static float perHeight = 0;
    public static float per = 0;
    public static int w, h = 0;
    public static int activityPayType = 0;
    public static boolean isAhead = false;
    public static boolean isInventory = false;
    public static boolean isNetConnect = true;
    public static boolean isNeedRefresh = false;
    public static boolean isNeedFinish = false;
    public static boolean isNeedFocusUpdate = false;
    public static boolean isNeedWorldUpdate = false;
    public static boolean isDeleteFeed = false;
    public static boolean isFirstToInventory = true;
    public static int checkType = 1;
    public static IWeiboShareAPI mWeiboShareAPI = null;
    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";


    @Override
    public void onCreate() {
        super.onCreate();
//        if (quickStart()) {
//            return;
//        }
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(true);
        //        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
        //小米推送
        RongPushClient.registerMiPush(this, Constants.XM_APP_ID, Constants.XM_APP_KEY);
        //华为推送
        RongPushClient.registerHWPush(this);
        /**
         * IMKit SDK调用第一步 初始化
         */
        RongIM.init(this);
//        }
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getApplicationContext(), Constants.SINA_APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        //初始化XUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //初始化融云

        //初始化Crash日志
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        //初始化TuSdk
        TuSdk.init(this.getApplicationContext(), Constants.TUSDK_KEY);
        TuSdk.enableDebugLog(true);
        //初始化七牛
        uploadManager = new UploadManager();
        //创建文件夹
        makeDirects();
        //初始化ImageLoader
        initImageLoader(this);
        context = this;
        //初始化微信
        registerToWechat();
        //网络监听工具类
        netStateUtils = new PushNetStateUtils();
//        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
            MultiDex.install(this);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            java.util.jar.Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }
    public static PackageInfo getPackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getPackageInfo",e.getLocalizedMessage());
        }
        return  new PackageInfo();
    }

    private void registerToWechat() {
        wxapi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        wxapi.registerApp(Constants.WX_APP_ID);
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
        //微信 appid appsecret
//        PlatformConfig.setSinaWeibo(Constants.SINA_APP_KEY,Constants.SINA_APP_SECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_SECRET);
        // QQ和Qzone appid appkey

    }

    /**
     * 一次创建程序所需要的文件夹
     */
    private void makeDirects() {
        File img = new File(OtherFinals.DIR_IMG);
        if (!img.exists()) {
            img.mkdirs();
        }

        img = new File(OtherFinals.DIR_CACHE);
        if (!img.exists()) {
            img.mkdirs();
        }
        img = new File(OtherFinals.DIR_APK);
        if (!img.exists()) {
            img.mkdirs();
        }
        img = new File(OtherFinals.DIR_IMG2);
        if (!img.exists()) {
            img.mkdirs();
        }
    }

    public static Context getContext() {
        return context;
    }

    /**
     * init ImageLoader
        *
        * @param context
                */
        public static void initImageLoader(Context context) {
            // This configuration tuning is custom. You can tune every option, you
            // may tune some of them,
            // or you can create default configuration by
            // ImageLoaderConfiguration.createDefault(this);
            // method.
            options = new DisplayImageOptions.Builder()//.displayer(new FadeInBitmapDisplayer(100))
                    .showImageOnLoading(R.mipmap.loading_default).resetViewBeforeLoading(false)
                    .showImageForEmptyUri(R.mipmap.default_head).showImageOnFail(R.mipmap.loading_default)
                    .cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();

        File cacheDir = new File(OtherFinals.DIR_CACHE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
