package com.goodhappiness.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.fragment.StartFragment;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_start)
public class StartActivity extends BaseFragmentActivity {
    @ViewInject(R.id.start_iv_dot)
    private ImageView iv_dot;
    @ViewInject(R.id.start_vp)
    private ViewPager viewPager;
    @ViewInject(R.id.start_rl)
    private RelativeLayout rl;
    @ViewInject(R.id.tl_5)
    private SlidingTabLayout slidingTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"1", "2", "3"};
    private int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FileUtils.saveResourceToFile(this, R.mipmap.ic_launcher, FieldFinals.GOOD_HAPPINESS);
//        FileUtils.saveResourceToFile(this, R.mipmap.ico_list_pd, FieldFinals.SYSTEM_MSG);
//        FileUtils.saveResourceToFile(this, R.mipmap.ico_list_mg, FieldFinals.COMMENT_TO_ME);
//        FileUtils.saveResourceToFile(this,R.mipmap.ico_list_z,FieldFinals.LIKE_TO_ME);
//        File file = new File(FileUtils.getStorageDirectory() + "/" + FieldFinals.GOOD_HAPPINESS + ".jpg");
//        if (!file.exists()) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher).compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] b;
//            b = baos.toByteArray();
//        }

        PreferencesUtil.setPreferences(this, FieldFinals.FIRST_IN,false);
//        RongIMUtils.setRongPic(this);
//        RongIMUtils.refreshRongPic(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.introduce_page));
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.introduce_page));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {

        mFragments.add(StartFragment.newInstance(R.mipmap.start1));
        mFragments.add(StartFragment.newInstance(R.mipmap.start2));
        mFragments.add(StartFragment.newInstance(R.mipmap.start3));
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (length == 0) {
                    length = rl.getWidth() / 2;
                    if (length != 0) {
                        length -= iv_dot.getWidth()/2;
                    }
                }
                if (length != 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_dot.getLayoutParams();
                    params.leftMargin = position * length + (int) (positionOffset * length);
                    iv_dot.setLayoutParams(params);
                }
                rl.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                Log.e("k_", "position:" + position + "*------positionOffset:" + positionOffset + "*-----positionOffsetPixels:" + positionOffsetPixels + "--length:" + length);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slidingTabLayout.setViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
