package com.goodhappiness.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.goodhappiness.R;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseFragment;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_start)
public class StartFragment extends BaseFragment {
    @ViewInject(R.id.iv_start)
    public ImageView iv;
    @ViewInject(R.id.start_to_activity)
    public ImageView iv_ToActivity;
    private static final String ARG_PARAM1 = "param1";
    private int mParam1;

    public StartFragment() {
        super(R.layout.fragment_start);
    }

    public static StartFragment newInstance(int param1) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.start_page)); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.start_page));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void setData() {
        iv.setImageResource(mParam1);
        if (mParam1 == R.mipmap.start3) {
            iv_ToActivity.setVisibility(View.VISIBLE);
            iv_ToActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomepageActivity.type = HomepageActivity.SHOP;
                    startActivity(new Intent(getActivity(), HomepageActivity.class));
                }
            });
        }
    }

}
