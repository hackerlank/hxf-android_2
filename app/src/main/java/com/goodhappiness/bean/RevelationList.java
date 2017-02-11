package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/16.
 */
public class RevelationList extends BaseBean  {

    private int more;
    private int total;
    private boolean isRefresh = false;
    private List<ListBean> list;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private PeriodWillReveal periodWillReveal;
        private PeriodRevealed periodRevealed;
        private boolean isRefresh;
        private int listPosition;

        public ListBean(PeriodWillReveal periodWillReveal, PeriodRevealed periodRevealed,int listPosition) {
            this.periodWillReveal = periodWillReveal;
            this.periodRevealed = periodRevealed;
            this.listPosition = listPosition;
        }

        public int getListPosition() {
            return listPosition;
        }

        public void setListPosition(int listPosition) {
            this.listPosition = listPosition;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setIsRefresh(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        public PeriodRevealed getPeriodRevealed() {
            return periodRevealed;
        }

        public void setPeriodRevealed(PeriodRevealed periodRevealed) {
            this.periodRevealed = periodRevealed;
        }

        public PeriodWillReveal getPeriodWillReveal() {
            return periodWillReveal;
        }

        public void setPeriodWillReveal(PeriodWillReveal periodWillReveal) {
            this.periodWillReveal = periodWillReveal;
        }
    }
}
