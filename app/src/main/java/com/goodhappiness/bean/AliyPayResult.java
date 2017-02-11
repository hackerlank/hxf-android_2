package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/7/21.
 */
public class AliyPayResult extends BaseBean {


    /**
     * useBank : true
     * params : {"alipay":"partner=\"2088221311334073\"&seller_id=\"2186096277@qq.com\"&out_trade_no=\"6727115771486397\"&subject=\"扑多订单-6727115771486397\"&body=\"扑多订单-6727115771486397\"&total_fee=\"0.01\"&notify_url=\"http://api.hxfapp.com/v1/alipay/callback/attach/Order\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"ODWeIC5sF9IfeaWJ1zymrjiHe%2BUdSMwsDQXRvJ0zbdSMbmXCwdiTD5rZuulw9%2FiEdYw08lDknVYm8jAuM2s8H8NOdtEWhbK55arJ9CwOE3FmAh9tW1ubPjKkqPZHSz6H0yvMgP4hVwqLjJuzdw1%2FZfoG8nMUtiLjUZwkymDCNn0%3D\"&sign_type=\"RSA\""}
     * url : http://wx.hxfapp.com/v1/order/result?sn=6727115771486397
     * browserOpen : 2
     */

    private boolean useBank;
    /**
     * alipay : partner="2088221311334073"&seller_id="2186096277@qq.com"&out_trade_no="6727115771486397"&subject="扑多订单-6727115771486397"&body="扑多订单-6727115771486397"&total_fee="0.01"&notify_url="http://api.hxfapp.com/v1/alipay/callback/attach/Order"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&sign="ODWeIC5sF9IfeaWJ1zymrjiHe%2BUdSMwsDQXRvJ0zbdSMbmXCwdiTD5rZuulw9%2FiEdYw08lDknVYm8jAuM2s8H8NOdtEWhbK55arJ9CwOE3FmAh9tW1ubPjKkqPZHSz6H0yvMgP4hVwqLjJuzdw1%2FZfoG8nMUtiLjUZwkymDCNn0%3D"&sign_type="RSA"
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
        private String alipay;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }
    }
}
