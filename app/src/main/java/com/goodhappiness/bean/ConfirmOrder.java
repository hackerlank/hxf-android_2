package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/11.
 */
public class ConfirmOrder extends BaseBean implements Serializable{

    private static final long serialVersionUID = 8139640731401685794L;
    /**
     * orderSn : F411662502358674
     * total : 0
     * userMoney : 0.0
     */

    private String orderSn;
    private String sn;
    private int fees;
    private long discountPrice;
    private double userMoney;
    private int orderType;
    private int flowerCount;
    private PayType payType;
    private String className;
    private UserInfo toUserInfo;


    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public UserInfo getToUserInfo() {
        return toUserInfo;
    }

    public void setToUserInfo(UserInfo toUserInfo) {
        this.toUserInfo = toUserInfo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public int getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(int flowerCount) {
        this.flowerCount = flowerCount;
    }

    /**
     * 支付类型 0：夺券 1：送花
     * @return
     */
    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getTotal() {
        return fees;
    }

    public void setTotal(int total) {
        this.fees = total;
    }

    public double getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(double userMoney) {
        this.userMoney = userMoney;
    }


    public ConfirmOrder() {
    }


}
