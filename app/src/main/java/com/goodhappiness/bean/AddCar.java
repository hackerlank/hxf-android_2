package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/5/30.
 */
public class AddCar extends BaseBean {

    /**
     * cart_num : 2
     * num : 87
     * mention : 本期仅剩87人次可参与,已自动为您调整
     */

    private int cart_num;
    private int num;
    private String mention;

    public int getCart_num() {
        return cart_num;
    }

    public void setCart_num(int cart_num) {
        this.cart_num = cart_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }
}
