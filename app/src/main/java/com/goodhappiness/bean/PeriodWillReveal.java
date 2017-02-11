package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class PeriodWillReveal extends BasePeriod implements Serializable {

    private static final long serialVersionUID = -7898028193163284094L;
    /**
     * period : 3
     * existingTimes : 2000
     * remainTime : 22669344
     * status : 2
     * goods : {"gid":2,"name":"幸福劵 2张","desc":"123213","price":2000,"buyUnit":10,"num":2,"pic":["http://7xsgec.com2.z0.glb.qiniucdn.com/1460797229"]}
     * lotteryTimestamp : 1460822399000
     */

//    private long period;
//    private int existingTimes;
    private int remainTime;
//    private int status;
    /**
     * gid : 2
     * name : 幸福劵 2张
     * desc : 123213
     * price : 2000
     * buyUnit : 10
     * num : 2
     * pic : ["http://7xsgec.com2.z0.glb.qiniucdn.com/1460797229"]
     */

//    private Goods goods;
    private long lotteryTimestamp;

    public PeriodWillReveal(long period, int existingTimes, int remainTime, int status, Goods goods, long lotteryTimestamp) {
        this.period = period;
        this.existingTimes = existingTimes;
        this.remainTime = remainTime;
        this.status = status;
        this.goods = goods;
        this.lotteryTimestamp = lotteryTimestamp;
    }

//    public long getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(long period) {
//        this.period = period;
//    }

//    public int getExistingTimes() {
//        return existingTimes;
//    }
//
//    public void setExistingTimes(int existingTimes) {
//        this.existingTimes = existingTimes;
//    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public Goods getGoods() {
//        return goods;
//    }
//
//    public void setGoods(Goods goods) {
//        this.goods = goods;
//    }

    public long getLotteryTimestamp() {
        return lotteryTimestamp;
    }

    public void setLotteryTimestamp(long lotteryTimestamp) {
        this.lotteryTimestamp = lotteryTimestamp;
    }

}
