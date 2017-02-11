package com.goodhappiness.ui.social.picture;

import android.os.Bundle;

import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.modules.components.TuSdkHelperComponent;

public class EditPicUpdateActivity extends BaseFragmentActivity {
    public static final int INTENT_PUBLISH = 1;
    public static final int INTENT_HEAD = 2;
    public static int intent_type = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        intent_type = getIntent().getIntExtra(FieldFinals.ACTION,1);
        TuSdkHelperComponent componentHelper = new TuSdkHelperComponent(this);
        EditPicFragment tuStickerOnlineFragment = new EditPicFragment();
        componentHelper.pushModalNavigationActivity(tuStickerOnlineFragment, false);
    }

    @Override
    protected void setData() {

    }
}
