package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/25.
 */
public class LotteryRecordDetail extends BaseBean implements Serializable {

    private static final long serialVersionUID = -6884193224707140883L;
    /**
     * period : 8
     * status : 1
     * name : 幸福劵 1张
     * luckyCode : 0
     * ownerCost : 0
     * calcTime :
     * awardArr : [{"awardNo":[100000200,100000439,100000459,100000048,100000288,100000474,100000792,100000888,100000971,100000151,100000564,100000467,100000298,100000978,100000516,100000633,100000828,100000553,100000115,100000149,100000331,100000952],"dobkTime":"2016-04-23 17:38:38:8391","count_by":11},{"awardNo":[100000869,100000914,100000397,100000864,100000257,100000814,100000341],"dobkTime":"","count_by":13}]
     * totalCount_by : 24
     */

    private long period;
    private int status;
    private String name;
    private int luckyCode;
    private int ownerCost;
    private String calcTime;
    private int totalCount_by;
    /**
     * awardNo : [100000200,100000439,100000459,100000048,100000288,100000474,100000792,100000888,100000971,100000151,100000564,100000467,100000298,100000978,100000516,100000633,100000828,100000553,100000115,100000149,100000331,100000952]
     * dobkTime : 2016-04-23 17:38:38:8391
     * count_by : 11
     */

    private List<AwardArrBean> awardArr;

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLuckyCode() {
        return luckyCode;
    }

    public void setLuckyCode(int luckyCode) {
        this.luckyCode = luckyCode;
    }

    public int getOwnerCost() {
        return ownerCost;
    }

    public void setOwnerCost(int ownerCost) {
        this.ownerCost = ownerCost;
    }

    public String getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(String calcTime) {
        this.calcTime = calcTime;
    }

    public int getTotalCount_by() {
        return totalCount_by;
    }

    public void setTotalCount_by(int totalCount_by) {
        this.totalCount_by = totalCount_by;
    }

    public List<AwardArrBean> getAwardArr() {
        return awardArr;
    }

    public void setAwardArr(List<AwardArrBean> awardArr) {
        this.awardArr = awardArr;
    }

    public static class AwardArrBean implements Serializable {


        private static final long serialVersionUID = 1911465247862473055L;
        private String dobkTime;
        private int count_by;
        private List<Integer> awardNo;

        public String getDobkTime() {
            return dobkTime;
        }

        public void setDobkTime(String dobkTime) {
            this.dobkTime = dobkTime;
        }

        public int getCount_by() {
            return count_by;
        }

        public void setCount_by(int count_by) {
            this.count_by = count_by;
        }

        public List<Integer> getAwardNo() {
            return awardNo;
        }

        public void setAwardNo(List<Integer> awardNo) {
            this.awardNo = awardNo;
        }

        public AwardArrBean() {
        }

    }

    public LotteryRecordDetail() {
    }

}
