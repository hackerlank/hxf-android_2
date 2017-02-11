package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/11/21.
 */
public class UnionPayResult implements Serializable {

    /**
     * useBank : true
     * params : {"html":""}
     * url : http://wx.hxfapp.com/v2/order/result?sn=61121125427708380
     * browserOpen : 2
     */

    private boolean useBank;
    /**
     * html :
     */

    private ParamsBean params;
    private String url;
    private String sn;
    private int browserOpen;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isUseBank() {
        return useBank;
    }

    public void setUseBank(boolean useBank) {
        this.useBank = useBank;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBrowserOpen() {
        return browserOpen;
    }

    public void setBrowserOpen(int browserOpen) {
        this.browserOpen = browserOpen;
    }

    public static class ParamsBean {
        private String html;

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
    }
}
