package com.goodhappiness.ui.social.picture;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.PreferencesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import uk.co.senab.photoview.PhotoView;

@ContentView(R.layout.activity_preview)
public class PreviewActivity extends BaseActivity {
    @ViewInject(R.id.iv)
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        byte[] b = PreferencesUtil.getPreferences(this, FieldFinals.BITMAP);
        if (b != null) {
            PreferencesUtil.setPreferences(this, FieldFinals.BITMAP, null);
            photoView.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
        } else {
            if (getIntent() != null) {
                if (!TextUtils.isEmpty(getIntent().getStringExtra(FieldFinals.URL))) {
                    ImageLoader.getInstance().displayImage(getIntent().getStringExtra(FieldFinals.URL),photoView);
                } else {
                    photoView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.default_head));
                }
            }
        }
    }

    @Override
    protected void reload() {

    }

}
