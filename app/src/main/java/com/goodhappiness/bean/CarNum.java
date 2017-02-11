package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/11.
 */
public class CarNum extends BaseBean{
    public CarNum(int cart_num, boolean isSelect) {
        this.cart_num = cart_num;
        this.isSelect = isSelect;
    }

    private int cart_num;
    private boolean isSelect =  false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCart_num() {
        return cart_num;
    }

    public void setCart_num(int cart_num) {
        this.cart_num = cart_num;
    }
}
