package com.goodhappiness.ui.social;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.MyFragmentPagerAdapter;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragment;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PermissionHelper;
import com.goodhappiness.utils.PreferencesUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.fragment_social)
public class SocialFragment extends BaseFragment {
    @ViewInject(R.id.fragment_social_pager)
    private ViewPager viewPager;
    @ViewInject(R.id.fragment_social_v_line1)
    private ImageView ivBottomLine;
    @ViewInject(R.id.fragment_social_tv_focus)
    private TextView txtPopular;
    @ViewInject(R.id.fragment_social_tv_world)
    private TextView txtSellGood;
    private int currIndex = 0;

    private ArrayList<Fragment> fragmentsList;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    public Fragment focusFragment;
    public Fragment worldFragment;

    public SocialFragment() {
        super(R.layout.fragment_social);
    }

    public static SocialFragment newInstance() {
        SocialFragment fragment = new SocialFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferencesUtil.getBooleanPreferences(getActivity(), FieldFinals.ADD_PUBLISH, false)) {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void setData() {
        InitWidth();
        InitTextView();
        InitViewPager();
        TranslateAnimation animation = new TranslateAnimation(position_one,
                position_one - offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);

    }

    private void InitTextView() {
        txtPopular.setOnClickListener(new MyOnClickListener(0));
        txtSellGood.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        fragmentsList = new ArrayList<>();
        focusFragment = new FocusFragment().newInstance("1", -1);
        worldFragment = new FocusFragment().newInstance("2", -1);
        fragmentsList.add(focusFragment);
        fragmentsList.add(worldFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
                fragmentsList));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(0);
    }

    private void InitWidth() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivBottomLine.getLayoutParams();
        bottomLineWidth = layoutParams.width = (int) ((float) (GoodHappinessApplication.w * 13) / 48);
        ivBottomLine.setLayoutParams(layoutParams);
        offset = bottomLineWidth;
        position_one = GoodHappinessApplication.w / 2;
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                // 指滑动到右边的页面
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_one - offset, 0,
                                0);
                    }
                    break;
                // 滑动到左边的页面
                case 1:

                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0,
                                0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Event({R.id.fragment_social_camera, R.id.fragment_social_search})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_social_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.fragment_social_camera:
                if (IntentUtils.isUserLogined(getActivity())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionHelper helper = new PermissionHelper(getActivity());
                        if(helper.checkPermissions(new String[]{"android.permission.CAMERA"})){
                            DialogFactory.createPublishDialog(getActivity());
                        }else{
                            helper.permissionsCheck("android.permission.CAMERA",2);
                        }
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);//1 can be another integer
                    } else {
                        DialogFactory.createPublishDialog(getActivity());
                    }
//                    PermissionHelper permissionHelper = new PermissionHelper(getActivity());
//                    if (!permissionHelper.checkPermission("android.permission.READ_EXTERNAL_STORAGE")) {
//                        permissionHelper.permissionsCheck("android.permission.READ_EXTERNAL_STORAGE", 12);
//                    }
//                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
//                            == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        //init(barcodeScannerView, getIntent(), null);

//                    } else {

//                    }

                }
                break;
        }
    }


}
