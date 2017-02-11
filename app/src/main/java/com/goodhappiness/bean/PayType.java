package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by huangzhifeng on 2017/2/10.
 */

public class PayType implements Serializable {
    private String aliPay;  //支付宝
    private String unionPay; //银联支付
    private String wx;//微信支付

    public String getAliPay() {
        return aliPay;
    }

    public void setAliPay(String aliPay) {
        this.aliPay = aliPay;
    }

    public String getUnionPay() {
        return unionPay;
    }

    public void setUnionPay(String unionPay) {
        this.unionPay = unionPay;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }
}
