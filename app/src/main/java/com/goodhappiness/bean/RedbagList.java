package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/8/9.
 */
public class RedbagList {

    /**
     * list : [{"rid":2,"redName":"充值送红包","money":10,"startTime":"2016-08-04 18:04:43","endTime":"2016-08-31 18:04:45","desc":"1","restMoney":10,"useStatus":0},{"rid":2,"redName":"充值送红包","money":10,"startTime":"2016-08-04 18:04:43","endTime":"2016-08-31 18:04:45","desc":"1","restMoney":10,"useStatus":0},{"rid":1,"redName":"注册5元红包活动","money":5,"startTime":"2016-08-04 18:04:34","endTime":"2016-08-31 18:04:38","desc":"1","restMoney":5,"useStatus":0}]
     * more : 2
     * total : 3
     */

    private int more;
    private int total;
    /**
     * rid : 2
     * redName : 充值送红包
     * money : 10
     * startTime : 2016-08-04 18:04:43
     * endTime : 2016-08-31 18:04:45
     * desc : 1
     * restMoney : 10
     * useStatus : 0
     */

    private List<Redbag> list;

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Redbag> getList() {
        return list;
    }

    public void setList(List<Redbag> list) {
        this.list = list;
    }

}
