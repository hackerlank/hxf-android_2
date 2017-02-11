package com.goodhappiness.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.register.LoginActivity;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.XProgressDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by 电脑 on 2016/3/29.
 */
public abstract class BaseFragment extends Fragment {
    protected int mLayoutId = 0;// 布局Id
    protected View rootView;
    protected Toast mToast;
    protected LinearLayout ll_empty;
    protected TextView tv_reload;
    protected int w, h;
    protected float per;
    private boolean injected = false;
    private Activity activity;
    protected XProgressDialog dialog;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public BaseFragment(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = activity.getLayoutInflater().inflate(mLayoutId, null);
        if(GoodHappinessApplication.w==0){
            WindowManager manage = activity.getWindowManager();
            Display display = manage.getDefaultDisplay();
            GoodHappinessApplication.w = w = display.getWidth();
            GoodHappinessApplication.h =h = display.getHeight();
            GoodHappinessApplication.per = per = (float)w/1080;
        }else{
            w =  GoodHappinessApplication.w;
            h = GoodHappinessApplication.h;
            per = GoodHappinessApplication.per;
        }
        ll_empty = (LinearLayout) rootView.findViewById(R.id.empty_view);

        //initTitle();
        return x.view().inject(this, inflater, container);
    }


    protected abstract void reload();
    protected void showEmptyView(boolean isShow){
        if(ll_empty!=null){
            if(isShow){
                ll_empty.setVisibility(View.VISIBLE);
            }else{
                ll_empty.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
            dialog = new XProgressDialog(getActivity(), getString(R.string.loading_), XProgressDialog.THEME_CIRCLE_PROGRESS);
            tv_reload = (TextView) rootView.findViewById(R.id.empty_view_tv_reload);
            if (tv_reload != null) {
                RxView.clicks(tv_reload).throttleFirst(1000, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                showEmptyView(false);
                                reload();
                            }
                        });
//                tv_reload.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }
            setData();
        }
    }

    public Dialog newDialog() {
        dialog = new XProgressDialog(getActivity(), "正在加载...", XProgressDialog.THEME_CIRCLE_PROGRESS);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected boolean isUserLogined(){
        if(!"".equals(PreferencesUtil.getStringPreferences(getActivity(), FieldFinals.SID))){
            return true;
        }else{
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return false;
        }
    }
    protected int getTheColor(int color){
        return getResources().getColor(color);
    }
    protected String getSid(){
        return PreferencesUtil.getStringPreferences(getActivity(), FieldFinals.SID);
    }
    protected long getUid() {
        if(!TextUtils.isEmpty(getSid())){
            return PreferencesUtil.getLongPreferences(getActivity(), FieldFinals.UID, 10000000000L);
        }else{
            return -1;
        }
    }
    protected String getDid(){
        return PreferencesUtil.getStringPreferences(getActivity(), FieldFinals.DEVICE_IDENTIFIER);
    }

    protected abstract void setData();

    /**
     * 根据string.xml中的id获取字符串
     *
     * @param resId
     * @return
     */
    protected String getResString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 根据id获取View
     *
     * @param id
     * @return View
     */
    public View findViewById(int id) {
        return rootView.findViewById(id);
    }


    public void displayImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, GoodHappinessApplication.options);
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg) {
        try {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            }
            mToast.setText(msg);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 显示提示信息
     *
     * @param stringId
     */
    public void showToast(int stringId) {
        try {
            String msg = getResources().getString(stringId);
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            }
            mToast.setText(msg);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
