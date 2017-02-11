package com.goodhappiness.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.AliyPayResult;
import com.goodhappiness.bean.AliyPayResultBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.order.ConfirmOrderActivity;
import com.goodhappiness.ui.order.InventoryActivity;
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
public class WebViewActivity extends BaseActivity {//implements SwipyRefreshLayout.OnRefreshListener{
    public static final int RESULT_CODE = 1005;
    @ViewInject(R.id.activity_wv)
    private WebView webView;
    //	@ViewInject(R.id.shop_srl)
//	private SwipyRefreshLayout srl;
    @ViewInject(R.id.common_right)
    private ImageView iv_home;
    @ViewInject(R.id.common_right2)
    private ImageView iv_share;
    public static final String TAG = "WebViewActivity";
    private long productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(webView!=null){
            webView.resumeTimers();
        }
        MobclickAgent.onPageStart("WebView");
        MobclickAgent.onResume(this);
        if (getIntent().getStringExtra(FieldFinals.NEW_URL) != null) {
            initURLWebView(getIntent().getStringExtra(FieldFinals.NEW_URL));
            return;
        }
        if(webView!=null&&!isFinishing()){
            HttpUtils.synCookies(this, webView.getUrl());
            webView.reload();
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
        if(webView!=null){
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WebView");
        MobclickAgent.onPause(this);
        if(webView!=null){
            webView.pauseTimers();
        }
    }

    @Override
    protected void setData() {
//		srl.setDirection(SwipyRefreshLayoutDirection.TOP);
//		srl.setOnRefreshListener(this);
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
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    AppManager.getAppManager().finishActivities(new Class[]{InventoryActivity.class, ConfirmOrderActivity.class
                            , PayOrderActivity.class});
                    finishActivity();
//					GoodHappinessApplication.isNeedRefresh = true;
                }
            }
        };
        if (getIntent() != null && getIntent().getStringExtra("m") != null) {// 有方法名

        } else {// 无方法名则直接加载HTML.
            if (getIntent().getStringExtra(FieldFinals.URL) != null) {
                HttpUtils.synCookies(this, getIntent().getStringExtra(FieldFinals.URL));
                initURLWebView(getIntent().getStringExtra(FieldFinals.URL));
            } else if (getIntent().getStringExtra(FieldFinals.HTML) != null){
                initWebView(getIntent().getStringExtra(FieldFinals.HTML));
            }
        }
    }

    @Event({R.id.common_right, R.id.common_right2})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right:
//				HomepageActivity.type = HomepageActivity.SHOP;
//				GoodHappinessApplication.isNeedRefresh = true;
                IntentUtils.startToHomePage(this);
                finishActivity();
                break;
            case R.id.common_right2:
                if (productId != -1) {
                    DialogFactory.createShareDialog(3, WebViewActivity.this, null, new String[]{getDid(), getSid(), FieldFinals.SHOP, String.valueOf(productId)});
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
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
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
                DialogFactory.createDefaultDialog(WebViewActivity.this, message, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.confirm();
                    }
                });
//                Builder builder = new Builder(MainActivity.this);
//                builder.setTitle("提示对话框");
//                builder.setMessage(message);
//                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //点击确定按钮之后，继续执行网页中的操作
//
//                    }
//                });
//                builder.setCancelable(false);
//                builder.create();
//                builder.show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    if (!dialog.isShowing()) {
                        newDialog().show();
                    }
                } else if (newProgress == 100) {
                    dialog.dismiss();
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

//	@Override
//	public void onRefresh(SwipyRefreshLayoutDirection direction) {
//		webView.reload();
//	}

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            Log.e("e_SslError",error.toString());
            handler.proceed();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("k_onPageFinished", url);
//            try {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }catch (Exception e){
//            }
//			if (url.contains(HttpFinal.SHOP_URL)) {
//				iv_home.setVisibility(View.GONE);
//			} else {
//				iv_home.setVisibility(View.VISIBLE);
//			}
            if (url.contains(HttpFinal.SHOP_URL)) {
                HomepageActivity.type = HomepageActivity.SHOP;
                IntentUtils.startToHomePage(WebViewActivity.this);
            }

        }

        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null || "".equals(url) || "about:blank".equals(url))
                return true;
            Log.i("mark", "web URL======" + url);
            newDialog(false).show();
            if (IntentUtils.checkURL(WebViewActivity.this, url)) {
                dialog.dismiss();
                return true;
            }
            if (checkAli(url)) {
                return true;
            }
            dialog.dismiss();
            HttpUtils.synCookies(WebViewActivity.this, url);
            view.loadUrl(url);
            return true;
        }

    }

    private boolean checkAli(String url) {
        if (url.contains(HttpFinal.GOTO_ALIYPAY)) {
            int firstIndex = url.indexOf("?");
            final String a = url.substring(firstIndex + 1, url.length());


            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask payTask = new PayTask(WebViewActivity.this);
                    String result = null;
//					try {
//						String b = URLDecoder.decode(a, "UTF-8");
                    Log.e("web", new String(Base64.decode(a, 0)).toString());
                    Gson mGson = new Gson();
                    Result res = mGson.fromJson(new String(Base64.decode(a, 0)).toString(), new TypeToken<Result<AliyPayResult>>() {
                    }.getType());
                    AliyPayResult aliyPayResult = (AliyPayResult) res.getData();
                    Log.e("web_pa", aliyPayResult.getParams().getAlipay());
                    result = payTask.pay(aliyPayResult.getParams().getAlipay(), true);
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}

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
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast(R.string.pay_success);
                        IntentUtils.startToExchangeRecord(WebViewActivity.this);
                        finishActivity();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast(R.string.pay_result_confirm);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
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
