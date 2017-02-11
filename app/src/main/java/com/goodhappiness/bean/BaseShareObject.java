package com.goodhappiness.bean;

import android.graphics.Bitmap;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/8.
 */
public class BaseShareObject extends BaseBean implements Serializable{

    private static final long serialVersionUID = -5817813853194905069L;
    private String shareTitle;
    private String shareTxt;
    private String shareUrl;
    private String shareImg;
    private int shareType;
    private Bitmap bitmap;
    private SHARE_MEDIA share_media;
    private UMImage shareImage;
    private UMShareListener umShareListener;

    public BaseShareObject() {
    }

    public BaseShareObject(String shareTitle, String shareTxt, String shareUrl, SHARE_MEDIA share_media, UMImage shareImage, UMShareListener umShareListener) {
        this.shareTitle = shareTitle;
        this.shareTxt = shareTxt;
        this.shareUrl = shareUrl;
        this.share_media = share_media;
        this.shareImage = shareImage;
        this.umShareListener = umShareListener;
    }

    public BaseShareObject(String shareTitle, String shareTxt, String shareUrl, SHARE_MEDIA share_media, UMImage shareImage) {
        this.shareTitle = shareTitle;
        this.shareTxt = shareTxt;
        this.shareUrl = shareUrl;
        this.share_media = share_media;
        this.shareImage = shareImage;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareTxt() {
        return shareTxt;
    }

    public void setShareTxt(String shareTxt) {
        this.shareTxt = shareTxt;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public SHARE_MEDIA getShare_media() {
        return share_media;
    }

    public void setShare_media(SHARE_MEDIA share_media) {
        this.share_media = share_media;
    }

    public UMImage getShareImage() {
        return shareImage;
    }

    public void setShareImage(UMImage shareImage) {
        this.shareImage = shareImage;
    }

    public UMShareListener getUmShareListener() {
        return umShareListener;
    }

    public void setUmShareListener(UMShareListener umShareListener) {
        this.umShareListener = umShareListener;
    }

    @Override
    public String toString() {
        return "BaseShareObject{" +
                "shareTitle='" + shareTitle + '\'' +
                ", shareTxt='" + shareTxt + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareImg='" + shareImg + '\'' +
                ", bitmap=" + bitmap +
                ", share_media=" + share_media +
                ", shareImage=" + shareImage +
                ", umShareListener=" + umShareListener +
                '}';
    }
}
