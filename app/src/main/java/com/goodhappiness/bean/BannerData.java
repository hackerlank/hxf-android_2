package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/6/6.
 */
public class BannerData extends BaseBean{

    /**
     * imgUrl : http://img.hxfapp.com/banner1.png
     * appUrl : ios:gotoRecharge
     * htmlUrl :
     */

    private List<Banner> banners;
    /**
     * lotteryTimestamp : 1465202940
     * uid : 79
     * nickname : 13888888888
     * name : 扑多商城超级礼劵 110张
     * period : 221
     */

    private List<WinnersBean> winners;

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public List<WinnersBean> getWinners() {
        return winners;
    }

    public void setWinners(List<WinnersBean> winners) {
        this.winners = winners;
    }

    public static class WinnersBean {
        private int lotteryTimestamp;
        private long uid;
        private String nickname;
        private String name;
        private long period;

        public int getLotteryTimestamp() {
            return lotteryTimestamp;
        }

        public void setLotteryTimestamp(int lotteryTimestamp) {
            this.lotteryTimestamp = lotteryTimestamp;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPeriod() {
            return period;
        }

        public void setPeriod(long period) {
            this.period = period;
        }
    }
}
