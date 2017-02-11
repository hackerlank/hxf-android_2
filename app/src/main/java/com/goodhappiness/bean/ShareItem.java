package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/18.
 */
public class ShareItem extends BaseBean    {
    private int iconResources;
    private int sharePlatform;
    private int position;
    public ShareItem(int iconResources, int sharePlatform,int position) {
        this.iconResources = iconResources;
        this.sharePlatform = sharePlatform;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIconResources() {
        return iconResources;
    }

    public void setIconResources(int iconResources) {
        this.iconResources = iconResources;
    }

    public int getSharePlatform() {
        return sharePlatform;
    }

    public void setSharePlatform(int sharePlatform) {
        this.sharePlatform = sharePlatform;
    }


}
