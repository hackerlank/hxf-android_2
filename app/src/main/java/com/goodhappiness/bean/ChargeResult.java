package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/22.
 */
public class ChargeResult extends BaseBean{
    private int payStatus;
    private int fee;
    private List<Redbag> redBags;
    public ChargeResult() {
    }

    public ChargeResult(int payStatus, int fee) {
        this.payStatus = payStatus;
        this.fee = fee;
    }

    public List<Redbag> getRedBags() {
        return redBags;
    }

    public void setRedBags(List<Redbag> redBags) {
        this.redBags = redBags;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
