package com.goodhappiness.ui.social.picture;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.constant.StringFinal;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;

/**
 * Created by 电脑 on 2016/4/28.
 */
public class CustomCameraFragment extends TuCameraFragment {
    private ImageView ivPic;
    /**
     * 布局ID
     */
    public static int getLayoutId() {
        return com.goodhappiness.R.layout.custom_camera_fragment_layout;
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.custom_camera));
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.custom_camera));
    }
    @Override
    protected void loadView(ViewGroup view) {
        super.loadView(view);
        setDefaultFlashMode(CameraConfigs.CameraFlash.Auto);
        ivPic = getViewById(com.goodhappiness.R.id.camera_iv);
        if(!TextUtils.isEmpty(EditPicFragment.firstImgPath)){
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD+EditPicFragment.firstImgPath,ivPic,new ImageSize((int)GoodHappinessApplication.perHeight*100,(int)GoodHappinessApplication.perHeight*100));
        }
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hubDismissRightNow();
                dismissActivityWithAnim();
            }
        });
    }
}