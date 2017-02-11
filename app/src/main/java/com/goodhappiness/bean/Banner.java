package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/7/26.
 */
public class Banner extends BaseBean {
    private String imgUrl;
    private String appUrl;
    private String htmlUrl;

    public Banner() {
    }

    public Banner(String imgUrl, String appUrl, String htmlUrl) {
        this.imgUrl = imgUrl;
        this.appUrl = appUrl;
        this.htmlUrl = htmlUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
