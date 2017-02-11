package com.goodhappiness.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.goodhappiness.R;
import com.goodhappiness.bean.BaseShareObject;
import com.goodhappiness.constant.StringFinal;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.utils.Utility;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * 分享
 */
public class ShareUtil {
    private static String shareURL = StringFinal.SHARE_URL;
    private static String shareContent = StringFinal.SHARE_CONTENT;
    private static String shareTitle = StringFinal.SHARE_TITLE;


    public static void shareDefault(Activity activity, SHARE_MEDIA share_media) {
        setDialog(activity);
        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new DefaultShareListener(activity))
                .withText(shareContent).withTitle(shareTitle)
                .withTargetUrl(shareURL)
                .withMedia(new UMImage(activity,
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher)))
                .share();
    }

    private static void setDialog(Activity activity) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle("请稍后");
        dialog.setMessage("加载中...");
        Config.dialog = dialog;
    }

    public static void shareDefaultToSina(final Activity activity) {
        setDialog(activity);
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.SINA)
                .setCallback(new DefaultShareListener(activity))
                .withText(shareContent + "   " + shareURL).withTitle(shareTitle)
                .withMedia(new UMImage(activity,
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher)))
                .share();
    }

    public static void share(Activity activity, BaseShareObject aBaseShareObject, SHARE_MEDIA share_media) {
        setDialog(activity);
        UMImage image = null;
        UMShareListener umShareListener = null;
        if (aBaseShareObject != null) {
            if (!TextUtils.isEmpty(aBaseShareObject.getShareUrl())) {
                shareURL = aBaseShareObject.getShareUrl();
            }
            if (!TextUtils.isEmpty(aBaseShareObject.getShareUrl())) {
                shareContent = aBaseShareObject.getShareTxt();
            }
            if (!TextUtils.isEmpty(aBaseShareObject.getShareUrl())) {
                shareTitle = aBaseShareObject.getShareTitle();
            }
            if(aBaseShareObject.getBitmap()==null) {
                if (!TextUtils.isEmpty(aBaseShareObject.getShareImg())) {
                    image = new UMImage(activity, ImageLoader.getInstance().loadImageSync(aBaseShareObject.getShareImg(), new ImageSize(150, 150)));
                }
                if (image == null) {
                    image = new UMImage(activity,
                            BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
                }
            }else{
                image = new UMImage(activity,aBaseShareObject.getBitmap());
            }
            if (aBaseShareObject.getUmShareListener() == null) {
                umShareListener = new DefaultShareListener(activity);
            } else {
                umShareListener = aBaseShareObject.getUmShareListener();
            }
        }
        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(umShareListener)
                .withText(shareContent).withTitle(shareTitle)
                .withTargetUrl(shareURL)
                .withMedia(image)
                .share();
    }

    public static void shareToSina(Activity activity, BaseShareObject aBaseShareObject) {
        setDialog(activity);
        UMImage image = null;
        UMShareListener umShareListener = null;
        if (aBaseShareObject != null) {
            if (!TextUtils.isEmpty(aBaseShareObject.getShareUrl())) {
                shareContent = aBaseShareObject.getShareTxt();
            }
            if (aBaseShareObject.getShareImage() == null) {
                image = new UMImage(activity,
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));
            } else {
                image = aBaseShareObject.getShareImage();
            }
            if (aBaseShareObject.getUmShareListener() == null) {
                umShareListener = new DefaultShareListener(activity);
            } else {
                umShareListener = aBaseShareObject.getUmShareListener();
            }
        }
        new ShareAction(activity)
                .setPlatform(SHARE_MEDIA.SINA)
                .setCallback(umShareListener)
                .withText(aBaseShareObject.getShareTxt())
                .withMedia(image)
                .share();
    }


    private static class DefaultShareListener implements UMShareListener {
        Activity activity;

        DefaultShareListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(activity, R.string.share_success, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity, "分享失败" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity, R.string.share_cancel, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    public static TextObject getTextObj(BaseShareObject baseShareObject) {
        TextObject textObject = new TextObject();
        textObject.text = baseShareObject.getShareTxt();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    public static ImageObject getImageObj(Context context, BaseShareObject baseShareObject) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = null;
        if (baseShareObject.getBitmap() != null) {
            bitmap = baseShareObject.getBitmap();
        } else {
            if (!TextUtils.isEmpty(baseShareObject.getShareImg())) {
                bitmap = ImageLoader.getInstance().loadImageSync(baseShareObject.getShareImg(), new ImageSize(100, 100));
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        imageObject.setImageObject(BitmapUtil.compressImage(bitmap));
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    public static WebpageObject getWebpageObj(Context context, BaseShareObject baseShareObject) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = baseShareObject.getShareTitle();
        mediaObject.description = baseShareObject.getShareTxt();

        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(baseShareObject.getShareImg(), new ImageSize(100, 100));
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        mediaObject.setThumbImage(BitmapUtil.compressImage(bitmap));
        mediaObject.actionUrl = baseShareObject.getShareUrl();
        mediaObject.defaultText = baseShareObject.getShareTxt();
        return mediaObject;
    }
}
