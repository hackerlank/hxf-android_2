package com.goodhappiness;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alipay.sdk.app.PayTask;
import com.goodhappiness.bean.AliyPayResult;
import com.goodhappiness.bean.AliyPayResultBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.order.ConfirmOrderActivity;
import com.goodhappiness.ui.order.PayOrderActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.URLParseUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;


/**
 *
 */
@SuppressLint("NewApi")
@ContentView(R.layout.activity_webview)
public class NewWebViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.activity_wv)
    private WebView webView;
    @ViewInject(R.id.common_right)
    private ImageView iv_home;
    @ViewInject(R.id.common_right2)
    private ImageView iv_share;
    @ViewInject(R.id.srl)
    private SwipeRefreshLayout srl;
    public static final String TAG = "WebViewActivity";
    private long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.resumeTimers();
        }
        MobclickAgent.onResume(this);
        if (getIntent().getStringExtra(FieldFinals.HTML) == null) {
            if (getIntent().getStringExtra(FieldFinals.NEW_URL) != null) {
                initURLWebView(getIntent().getStringExtra(FieldFinals.NEW_URL));
                return;
            }
            if (webView != null && !isFinishing()) {
                HttpUtils.synCookies(this, webView.getUrl());
                webView.reload();
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (webView != null) {
            webView.pauseTimers();
        }
    }

    @Override
    protected void setData() {
        srl.setOnRefreshListener(this);
        iv_share.setImageResource(R.mipmap.share);
        iv_home.setImageResource(R.mipmap.shop);
        iv_home.setVisibility(View.VISIBLE);
        if (HomepageActivity.type != HomepageActivity.SHOP) {
            iv_home.setImageResource(R.mipmap.goto_buy);
        }
        initView();
        settingWebView();
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                if (webView != null) {
                    if (webView.getUrl().contains("/v2/order/result") || webView.getUrl().contains("/v2/user/rechargeResult") || webView.getUrl().contains("zh_CN/index")) {
                        finishOrder();
                    } else {
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            finishOrder();
                        }
                    }
                } else {
                    if (webView != null)
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            finishOrder();
                        }
                }
            }
        };
        if (getIntent() != null && getIntent().getStringExtra("m") != null) {// 有方法名

        } else {// 无方法名则直接加载HTML.
            if (getIntent().getStringExtra(FieldFinals.URL) != null) {
                HttpUtils.synCookies(this, getIntent().getStringExtra(FieldFinals.URL));
                initURLWebView(getIntent().getStringExtra(FieldFinals.URL));
            } else if (getIntent().getStringExtra(FieldFinals.HTML) != null) {
                initWebView(getIntent().getStringExtra(FieldFinals.HTML));
            }
        }
    }

    public void finishOrder() {
        AppManager.getAppManager().finishActivities(new Class[]{ConfirmOrderActivity.class
                , PayOrderActivity.class});
        finishActivity();
    }

    @Event({R.id.common_right, R.id.common_right2})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right:
                IntentUtils.startToHomePage(this);
                finishActivity();
                break;
            case R.id.common_right2:
                if (productId != -1) {
                    DialogFactory.createShareDialog(3, NewWebViewActivity.this, null, new String[]{getDid(), getSid(), FieldFinals.SHOP, String.valueOf(productId)});
                }
                break;
        }
    }

    @Override
    protected void reload() {

    }


    private void initWebView(String data) {
        webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    private void initURLWebView(String data) {
        HttpUtils.synCookies(this, data);
        webView.loadUrl(data);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void settingWebView() {
        // // 设置默认缩放方式尺寸是far
        WebSettings webSettings = webView.getSettings();
        webView.requestFocus(View.FOCUS_DOWN);
        webView.requestFocusFromTouch();
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        // User settings
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 关键点
        webSettings.setSaveFormData(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setBlockNetworkImage(false);
        webSettings.supportMultipleWindows();
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setTextZoom(100);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.i("mark", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
        }

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webView.setWebViewClient(new MyWebViewClient());
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.length() > 10) {
                    return;
                }
                tv_title.setText(title.replace(" ", ""));
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                //构架一个builder来显示网页中的对话框
                if (message.contains(getString(R.string.success))) {
                    GoodHappinessApplication.isNeedRefresh = true;
                }
                DialogFactory.createDefaultDialog(NewWebViewActivity.this, message, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.confirm();
                    }
                });
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    if (getIntent().getStringExtra(FieldFinals.HTML) != null) {

                    } else {
                        if (!dialog.isShowing()) {
                            newDialog().show();
                        }
                    }
                } else if (newProgress == 100) {
                    dialog.dismiss();
                    srl.setRefreshing(false);
                    if (view.getUrl().contains(HttpFinal.SHOP_URL)) {
                        webView.clearHistory();
                    }
                    if (view.getUrl().contains(HttpFinal.DETAIL_PRODUCT_ID)) {
                        iv_share.setVisibility(View.VISIBLE);
                        Map<String, String> mapRequest = URLParseUtils.URLRequest(view.getUrl());
                        for (String strRequestKey : mapRequest.keySet()) {
                            if (strRequestKey.equals(FieldFinals.PRODUCT_ID)) {
                                productId = Long.valueOf(mapRequest.get(strRequestKey));
                                break;
                            }
                        }
                    } else {
                        iv_share.setVisibility(View.INVISIBLE);
                        productId = -1;
                    }
                }
            }
        };
        webView.setWebChromeClient(wvcc);
    }

    @Override
    public void onRefresh() {
        if (webView != null) {
            webView.reload();
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.e("e_SslError", error.toString());
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("k_onPageFinished", url);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (url.contains(HttpFinal.SHOP_URL)) {
                HomepageActivity.type = HomepageActivity.SHOP;
                IntentUtils.startToHomePage(NewWebViewActivity.this);
            }
        }

        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            if (url == null || "".equals(url) || "about:blank".equals(url))
                return false;
            Log.i("mark", "web URL======" + url);
//            newDialog(false).show();
            if (url.contains(HttpFinal.GOTO_DONATE_SUCCESS) && getIntent() != null) {
                IntentUtils.sendFlowerSendBroadcastWith(NewWebViewActivity.this,
                        getIntent().getStringExtra(FieldFinals.ACTION),
                        getIntent().getIntExtra(FieldFinals.COUNT, 0),
                        true, getIntent().getStringExtra(FieldFinals.NICKNAME),
                        getIntent().getLongExtra(FieldFinals.UID, 0));
                finishOrder();
                return false;
            }
            if (IntentUtils.checkURL(NewWebViewActivity.this, url)) {
                dialog.dismiss();
                return false;
            }
            if (checkAli(url)) {
                return false;
            }
            dialog.dismiss();
            HttpUtils.synCookies(NewWebViewActivity.this, url);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            view.loadUrl(url);
//                        }
//                    });
//                }
//            }, 500);
//            if(url.contains("v2/order/result")){
//                webView.clearHistory();
//                webView.loadUrl(url);
//            }
            Log.i("mark", "web URL2======" + url);
            return false;
        }

    }

    private boolean checkAli(String url) {
        if (url.contains(HttpFinal.GOTO_ALIYPAY)) {
            int firstIndex = url.indexOf("?");
            final String a = url.substring(firstIndex + 1, url.length());


            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask payTask = new PayTask(NewWebViewActivity.this);
                    String result = null;
                    Log.e("web", new String(Base64.decode(a, 0)).toString());
                    Gson mGson = new Gson();
                    Result res = mGson.fromJson(new String(Base64.decode(a, 0)).toString(), new TypeToken<Result<AliyPayResult>>() {
                    }.getType());
                    AliyPayResult aliyPayResult = (AliyPayResult) res.getData();
                    Log.e("web_pa", aliyPayResult.getParams().getAlipay());
                    result = payTask.pay(aliyPayResult.getParams().getAlipay(), true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
            return true;
        }
        return false;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    AliyPayResultBean payResult = new AliyPayResultBean((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast(R.string.pay_success);
                        IntentUtils.startToExchangeRecord(NewWebViewActivity.this);
                        finishActivity();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast(R.string.pay_result_confirm);
                        } else {
                            showToast(R.string.pay_fail);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
