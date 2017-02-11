package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/23.
 */
public class BasePeriod extends BaseBean {

    protected long period;
    protected int existingTimes;
    protected int status;
    protected Goods goods;

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public int getExistingTimes() {
        return existingTimes;
    }

    public void setExistingTimes(int existingTimes) {
        this.existingTimes = existingTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
