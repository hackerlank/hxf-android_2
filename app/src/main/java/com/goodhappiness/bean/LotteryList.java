package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/3/29.
 */
public class LotteryList extends BaseBean{

    /**
     * list : [{"existingTimes":0,"status":1,"goods":{"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]},"period":1},{"existingTimes":0,"status":1,"goods":{"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]},"period":2},{"existingTimes":0,"status":1,"goods":{"gid":2,"name":"幸福劵 700张","desc":"123123","price":70000,"buyUnit":1,"pic":["http://7xsgec.com2.z0.glb.qiniucdn.com/1459932032"]},"period":3},{"existingTimes":0,"status":1,"goods":{"gid":2,"name":"幸福劵 700张","desc":"123123","price":70000,"buyUnit":1,"pic":["http://7xsgec.com2.z0.glb.qiniucdn.com/1459932032"]},"period":4},{"existingTimes":0,"status":1,"goods":{"gid":3,"name":"普通劵 9张","desc":"123123","price":900,"buyUnit":2,"pic":[]},"period":5},{"existingTimes":0,"status":1,"goods":{"gid":3,"name":"普通劵 9张","desc":"123123","price":900,"buyUnit":2,"pic":[]},"period":6},{"existingTimes":0,"status":1,"goods":{"gid":4,"name":"幸福劵 500张","desc":"12312","price":50000,"buyUnit":10,"pic":[]},"period":7},{"existingTimes":0,"status":1,"goods":{"gid":4,"name":"幸福劵 500张","desc":"12312","price":50000,"buyUnit":10,"pic":[]},"period":8},{"existingTimes":0,"status":1,"goods":{"gid":5,"name":"幸福劵 50张","desc":"123123","price":50000,"buyUnit":1,"pic":[]},"period":9},{"existingTimes":0,"status":1,"goods":{"gid":5,"name":"幸福劵 50张","desc":"123123","price":50000,"buyUnit":1,"pic":[]},"period":10}]
     * more : 2
     * total : 10
     */

    private int more;
    private int total;
    /**
     * existingTimes : 0
     * status : 1
     * goods : {"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]}
     * period : 1
     */

    private List<PeriodIng> list;

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

    public List<PeriodIng> getList() {
        return list;
    }

    public void setList(List<PeriodIng> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "LotteryList{" +
                "more=" + more +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}
