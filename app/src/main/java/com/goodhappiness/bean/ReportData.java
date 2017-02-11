package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/6/20.
 */
public class ReportData {
    private String english;
    private String china;
    private boolean isSelect;
    public ReportData(String english, String china) {
        this.english = english;
        this.china = china;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChina() {
        return china;
    }

    public void setChina(String china) {
        this.china = china;
    }
}
