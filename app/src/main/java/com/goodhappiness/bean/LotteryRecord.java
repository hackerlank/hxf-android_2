package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/8.
 */
public class LotteryRecord extends BaseBean{

    /**
     * list : [{"period":1,"existingTimes":1000,"price":1000,"image_url":"http://7xsgec.com2.z0.glb.qiniucdn.com/1461578612","name":"幸福劵 1张","num":1,"luckyCode":100000193,"ownerCost":1000,"status":3,"calcTime":"2016-04-25 19:34:00","nickname":"skeleton1231","count_buy":60,"currentPeriod":13},{"period":4,"existingTimes":1995,"price":2000,"image_url":"","name":"幸福劵 2张","num":2,"luckyCode":100000407,"ownerCost":1395,"status":3,"calcTime":"2016-04-27 16:49:00","nickname":"马勒戈壁","count_buy":1455,"currentPeriod":12},{"period":5,"existingTimes":1000,"price":1000,"image_url":"http://7xsgec.com2.z0.glb.qiniucdn.com/1461578612","name":"幸福劵 1张","num":1,"luckyCode":100000803,"ownerCost":893,"status":3,"calcTime":"2016-04-26 13:49:00","nickname":"马勒戈壁","count_buy":893,"currentPeriod":13},{"period":6,"existingTimes":1000,"price":1000,"image_url":"http://7xsgec.com2.z0.glb.qiniucdn.com/1461578612","name":"幸福劵 1张","num":1,"luckyCode":100000514,"ownerCost":1000,"status":3,"calcTime":"2016-04-26 16:04:00","nickname":"马勒戈壁","count_buy":1000,"currentPeriod":13},{"period":7,"existingTimes":2000,"price":2000,"image_url":"","name":"幸福劵 2张","num":2,"luckyCode":100001728,"ownerCost":1650,"status":3,"calcTime":"2016-04-28 14:19:00","nickname":"skeleton1231","count_buy":75,"currentPeriod":12}]
     * more : 2
     * total : 5
     */

    private int more;
    private int total;
    /**
     * period : 1
     * existingTimes : 1000
     * price : 1000
     * image_url : http://7xsgec.com2.z0.glb.qiniucdn.com/1461578612
     * name : 幸福劵 1张
     * num : 1
     * luckyCode : 100000193
     * ownerCost : 1000
     * status : 3
     * calcTime : 2016-04-25 19:34:00
     * nickname : skeleton1231
     * count_buy : 60
     * currentPeriod : 13
     */

    private List<ListBean> list;

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
        private long period;
        private int existingTimes;
        private int price;
        private String image_url;
        private String name;
        private int num;
        private int luckyCode;
        private int ownerCost;
        private int status;
        private String calcTime;
        private String nickname;
        private int count_buy;
        private long currentPeriod;
        private int goodsType;
        private List<String> pic;
        private List<String> picNew;
        private List<String> picDetails;


        public List<String> getPic() {
            return pic;
        }

        public void setPic(List<String> pic) {
            this.pic = pic;
        }

        public List<String> getPicNew() {
            return picNew;
        }

        public void setPicNew(List<String> picNew) {
            this.picNew = picNew;
        }

        public List<String> getPicDetails() {
            return picDetails;
        }

        public void setPicDetails(List<String> picDetails) {
            this.picDetails = picDetails;
        }

        public int getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(int goodsType) {
            this.goodsType = goodsType;
        }

        public long getPeriod() {
            return period;
        }

        public void setPeriod(long period) {
            this.period = period;
        }

        public int getExistingTimes() {
            return existingTimes;
        }

        public void setExistingTimes(int existingTimes) {
            this.existingTimes = existingTimes;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCalcTime() {
            return calcTime;
        }

        public void setCalcTime(String calcTime) {
            this.calcTime = calcTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getCount_buy() {
            return count_buy;
        }

        public void setCount_buy(int count_buy) {
            this.count_buy = count_buy;
        }

        public long getCurrentPeriod() {
            return currentPeriod;
        }

        public void setCurrentPeriod(long currentPeriod) {
            this.currentPeriod = currentPeriod;
        }
    }
}
