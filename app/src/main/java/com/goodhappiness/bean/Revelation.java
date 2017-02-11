package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/3/30.
 */
public class Revelation extends BaseBean{
    private long lotteryTimestamp;
    private long remainTime;
    private boolean isRefresh;
    private boolean isSetTimer;

    public Revelation() {
    }

    public Revelation(long lotteryTimestamp, long remainTime, boolean isRefresh) {
        this.lotteryTimestamp = lotteryTimestamp;
        this.remainTime = remainTime;
        this.isRefresh = isRefresh;
    }

    public boolean isSetTimer() {
        return isSetTimer;
    }

    public void setIsSetTimer(boolean isSetTimer) {
        this.isSetTimer = isSetTimer;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public long getLotteryTimestamp() {
        return lotteryTimestamp;
    }

    public void setLotteryTimestamp(long lotteryTimestamp) {
        this.lotteryTimestamp = lotteryTimestamp;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

}
