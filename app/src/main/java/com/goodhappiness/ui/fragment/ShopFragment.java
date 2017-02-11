package com.goodhappiness.ui.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * EditPicUpdateActivity simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_shop)
public class ShopFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.webview)
    public WebView webView;
    @ViewInject(R.id.ll_shop)
    private LinearLayout ll_shop;
    @ViewInject(R.id.srl)
    private SwipeRefreshLayout srl;
    @ViewInject(R.id.common_title)
    private TextView tv_title;
    @ViewInject(R.id.common_left)
    private ImageView iv_back;
    @ViewInject(R.id.common_right)
    private ImageView iv_home;

    GestureDetector mGesture;

    public ShopFragment() {
        super(R.layout.fragment_shop);
    }

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }

    @Override
    protected void reload() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (GoodHappinessApplication.isNeedRefresh) {
            GoodHappinessApplication.isNeedRefresh = false;
            onRefresh();
        }
        if (webView != null) {
            webView.resumeTimers();
        }
        MobclickAgent.onPageStart(getResString(R.string.happiness_shop));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.pauseTimers();
        }
        MobclickAgent.onPageEnd(getResString(R.string.happiness_shop));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    protected void setData() {
        srl.setOnRefreshListener(this);
//        srl.setRefreshViewHolder(new BGANormalRefreshViewHolder(GoodHappinessApplication.getContext(), false));
        iv_home.setImageResource(R.mipmap.shop);
        iv_home.setVisibility(View.VISIBLE);
        settingWebView();
        HttpUtils.synCookies(getActivity(), HttpFinal.SHOP_URL);
        webView.loadUrl(HttpFinal.SHOP_URL);
    }

    @Event({R.id.common_left, R.id.common_right})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_left:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                break;
            case R.id.common_right:
                HttpUtils.synCookies(getActivity(), HttpFinal.SHOP_URL);
                webView.loadUrl(HttpFinal.SHOP_URL);
                break;
        }
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
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(false);
        webSettings.supportMultipleWindows();
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setSupportZoom(false); // 支持缩放
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setTextZoom(100);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//        if (mDensity == 240) {
//            webSettings.setDefaultZoom(ZoomDensity.FAR);
//        } else if (mDensity == 160) {
//            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
//        } else if (mDensity == 120) {
//            webSettings.setDefaultZoom(ZoomDensity.CLOSE);
//        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
//            webSettings.setDefaultZoom(ZoomDensity.FAR);
//        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
//            webSettings.setDefaultZoom(ZoomDensity.FAR);
//        } else {
//            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
//        }

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.setWebViewClient(new MyWebViewClient());
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.length() > 8) {
                    tv_title.setText("商城");
                } else {
                    tv_title.setText(title.replace(" ", ""));
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                } else if (newProgress == 100) {
                    srl.setRefreshing(false);
                    if (view.getUrl().contains(HttpFinal.SHOP_URL)) {
                        webView.clearHistory();
                    }
                }
//                srl.setRefreshing(false);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                //构架一个builder来显示网页中的对话框
                DialogFactory.createDefaultDialog(getActivity(), message, new DialogInterface.OnCancelListener() {
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
        };
        webView.setWebChromeClient(wvcc);
    }



    @Override
    public void onRefresh() {
        srl.setRefreshing(true);
        HttpUtils.synCookies(getActivity(), HttpFinal.SHOP_URL);
        webView.reload();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("k_onPageFinished", url);
            if (url.contains(HttpFinal.SHOP_URL)) {
                iv_back.setVisibility(View.GONE);
                iv_home.setVisibility(View.GONE);
            } else {
                iv_back.setVisibility(View.VISIBLE);
                iv_home.setVisibility(View.VISIBLE);
            }
        }

        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null || "".equals(url) || "about:blank".equals(url))
                return true;
            Log.i("mark", "web URL======" + url);
            if (IntentUtils.checkURL(getActivity(), url)) {
                return true;
            }
            HttpUtils.synCookies(getActivity(), url);
            if (url.contains(HttpFinal.SHOP_URL)) {
                view.loadUrl(url);
            } else {
                IntentUtils.startToWebView(getActivity(), url);
            }
//            view.loadUrl(url);
            return true;
        }

    }


}
