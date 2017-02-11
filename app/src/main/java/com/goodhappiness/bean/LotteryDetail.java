package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class LotteryDetail extends BaseBean implements Serializable{

    private static final long serialVersionUID = 222720438248176338L;
    /**
     * existingTimes : 0
     * status : 1
     * goods : {"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]}
     * period : 2
     * recordStartTime : null
     */

    private PeriodIng periodIng;
    private PeriodWillReveal periodWillReveal;
    private PeriodRevealed periodRevealed ;
    /**
     * periodIng : {"existingTimes":0,"status":1,"goods":{"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]},"period":2,"recordStartTime":null}
     * periodWillReveal :
     * PeriodRevealed :
     * status : 1
     * detailUrl : http://120.76.148.69/detail/intro/1.html
     * currentPeriod : 2
     * currentPeriodStatus : 1
     * recordStartTime :
     * lotteryHisUrl : http://120.76.148.69/detail/winRecord/1.html
     * calculateUrl : http://120.76.148.69/detail/calc/1-2.html
     * codes : []
     */

    private int status;
    private String detailUrl;
    private long currentPeriod;
    private int currentPeriodStatus;
    private String recordStartTime;
    private String lotteryHisUrl;
    private String calculateUrl;
    private List<Long> codes;
    private List<ProductList> productList;

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public PeriodWillReveal getPeriodWillReveal() {
        return periodWillReveal;
    }

    public void setPeriodWillReveal(PeriodWillReveal periodWillReveal) {
        this.periodWillReveal = periodWillReveal;
    }

    public PeriodRevealed getPeriodRevealed() {
        return periodRevealed;
    }

    public void setPeriodRevealed(PeriodRevealed periodRevealed) {
        this.periodRevealed = periodRevealed;
    }

    public PeriodIng getPeriodIng() {
        return periodIng;
    }

    public void setPeriodIng(PeriodIng periodIng) {
        this.periodIng = periodIng;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public long getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(long currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public int getCurrentPeriodStatus() {
        return currentPeriodStatus;
    }

    public void setCurrentPeriodStatus(int currentPeriodStatus) {
        this.currentPeriodStatus = currentPeriodStatus;
    }

    public String getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public String getLotteryHisUrl() {
        return lotteryHisUrl;
    }

    public void setLotteryHisUrl(String lotteryHisUrl) {
        this.lotteryHisUrl = lotteryHisUrl;
    }

    public String getCalculateUrl() {
        return calculateUrl;
    }

    public void setCalculateUrl(String calculateUrl) {
        this.calculateUrl = calculateUrl;
    }

    public List<Long> getCodes() {
        return codes;
    }

    public void setCodes(List<Long> codes) {
        this.codes = codes;
    }

}
