package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/10/13.
 */
public class PushWinner extends BaseBean{

    private int type;
    private String goodsName;
    private int goodsType;
    private String iosUrl;
    private String androidUrl;
    private String jumpUrl;
    private int pushType;//1中奖 2跳转

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    @Override
    public String toString() {
        return "PushWinner{" +
                "type=" + type +
                ", goodsName='" + goodsName + '\'' +
                ", goodsType=" + goodsType +
                ", iosUrl='" + iosUrl + '\'' +
                ", androidUrl='" + androidUrl + '\'' +
                '}';
    }
}
