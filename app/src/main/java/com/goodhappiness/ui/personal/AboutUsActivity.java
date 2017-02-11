package com.goodhappiness.ui.personal;

import android.os.Bundle;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.SystemUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity {
    @ViewInject(R.id.about_version)
    private TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.about_puduo));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(getString(R.string.about_puduo));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.about_puduo);
        tv_version.setText(String.format(getString(R.string.format_version_,SystemUtils.getVersionName(this))));//"版本号: "+ SystemUtils.getVersionName(this));
    }

    @Override
    protected void reload() {

    }

}
