package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/11.
 */
public class PeriodRevealed extends BasePeriod implements Serializable {

    private static final long serialVersionUID = 6710243066841447262L;
    private long luckyCode;
    /**
     * period : 3
     * luckyCode : 120346
     * existingTimes : 2000
     * status : 3
     * goods : {"gid":2,"name":"幸福劵 2张","desc":"123213","price":2000,"buyUnit":10,"num":2,"pic":["http://7xsgec.com2.z0.glb.qiniucdn.com/1460797229"]}
     * calcTimestamp : 1460822399000
     * calcTime : 2016-04-16 23:59:59
     * ownerAllCode : []
     * duoquanTimestamp :
     * ownerCost : 5
     * owner : {"nickname":"http://7xsgec.com1","uid":18,"IPAddress":"","IP":"127.0.0.1","avatar":"http://7xsgec.com1.z0.glb.clouddn.com/1459911582","mobile":true}
     */

//    private long period;
//    private int existingTimes;
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
    private long calcTimestamp;
    private String calcTime;
    private String duoquanTimestamp;
    private int ownerCost;
    /**
     * nickname : http://7xsgec.com1
     * uid : 18
     * IPAddress :
     * IP : 127.0.0.1
     * avatar : http://7xsgec.com1.z0.glb.clouddn.com/1459911582
     * mobile : true
     */

    private Owner owner;
    private List<?> ownerAllCode;

    public PeriodRevealed(long period, int luckyCode, int existingTimes, int status, Goods goods, long calcTimestamp, String calcTime, String duoquanTimestamp, int ownerCost, Owner owner, List<?> ownerAllCode) {
        this.period = period;
        this.luckyCode = luckyCode;
        this.existingTimes = existingTimes;
        this.status = status;
        this.goods = goods;
        this.calcTimestamp = calcTimestamp;
        this.calcTime = calcTime;
        this.duoquanTimestamp = duoquanTimestamp;
        this.ownerCost = ownerCost;
        this.owner = owner;
        this.ownerAllCode = ownerAllCode;
    }

//    public long getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(long period) {
//        this.period = period;
//    }

    public long getLuckyCode() {
        return luckyCode;
    }

    public void setLuckyCode(long luckyCode) {
        this.luckyCode = luckyCode;
    }

//    public int getExistingTimes() {
//        return existingTimes;
//    }
//
//    public void setExistingTimes(int existingTimes) {
//        this.existingTimes = existingTimes;
//    }
//
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

    public long getCalcTimestamp() {
        return calcTimestamp;
    }

    public void setCalcTimestamp(long calcTimestamp) {
        this.calcTimestamp = calcTimestamp;
    }

    public String getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(String calcTime) {
        this.calcTime = calcTime;
    }

    public String getDuoquanTimestamp() {
        return duoquanTimestamp;
    }

    public void setDuoquanTimestamp(String duoquanTimestamp) {
        this.duoquanTimestamp = duoquanTimestamp;
    }

    public int getOwnerCost() {
        return ownerCost;
    }

    public void setOwnerCost(int ownerCost) {
        this.ownerCost = ownerCost;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<?> getOwnerAllCode() {
        return ownerAllCode;
    }

    public void setOwnerAllCode(List<?> ownerAllCode) {
        this.ownerAllCode = ownerAllCode;
    }


}
