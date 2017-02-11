package com.goodhappiness.ui.personal.selecthead;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.goodhappiness.R;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.BitmapUtil;
import com.goodhappiness.utils.PictureUtil;
import com.goodhappiness.widget.ClipImageBorderView;
import com.goodhappiness.widget.ClipImageLayout;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @ClassName: ShowImageStep2Activity
 * @Description: 无原型图;
 * 获取到图片后进行剪切制作成矩形头像
 * @date 2015-1-20 上午10:17:22
 */
@ContentView(R.layout.activity_show_ing_step2)
public class ShowImageStep2Activity extends BaseActivity {
    private ClipImageLayout mClipImageLayout;//截取矩形头像的自定义控件
    @ViewInject(R.id.activity_show_img_step2)
    private LinearLayout ll;//本页面的layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


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
    private void initView(Intent intent) {
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("选择头像");
        tv_right.setText("下一步");
        String img_path = "";
        if (intent != null && (intent.getStringExtra(FieldFinals.IMAGE_PATH)) != null && (img_path = intent.getStringExtra(FieldFinals.IMAGE_PATH)).length() > 0) {
            if (!getIntent().getBooleanExtra("is_camera", false)) {
                if(com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD+img_path)!=null){
                    mClipImageLayout = new ClipImageLayout(this, null, new BitmapDrawable(com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD+img_path)),20, ClipImageBorderView.BORDER_STYLE_CIRCLE);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
                    android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                    ll.addView(mClipImageLayout, lp);
                }else{
                    showToast("无法加载图片");
                    finishActivity();
                }
            } else {
                /**
                 * 把图片旋转为正的方向
                 */
                Bitmap newBitmap = BitmapUtil.rotateBitmapByDegree(PictureUtil.getSmallBitmap(img_path), BitmapUtil.readPictureDegree(img_path));
                mClipImageLayout = new ClipImageLayout(this, null, new BitmapDrawable(newBitmap),20,ClipImageBorderView.BORDER_STYLE_CIRCLE);
                android.view.ViewGroup.LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                ll.addView(mClipImageLayout, lp);
            }
            File mTmpFile = new File(img_path);
            if (mTmpFile != null && mTmpFile.exists()) {
                mTmpFile.delete();
            }
        }
    }

    @Event({R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right_text:
                Bitmap bitmap = mClipImageLayout.clip();//截取图片
                //将图片转换成byte数组,传到下一个界面
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();
                Intent intent = new Intent(this, ShowHeadImgActivity.class);
                intent.putExtra(FieldFinals.IMAGE_PATH, data);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setData() {
        initView();
        initView(getIntent());
    }

    @Override
    protected void reload() {

    }
}
