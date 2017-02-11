package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/23.
 */
public class UserRecharge extends BaseBean {

    /**
     * list : [{"rechargeId":35,"rechargeTime":"2016-04-23 18:23:59","rechargeMoney":100,"status":1,"pay_type":1},{"rechargeId":34,"rechargeTime":"2016-04-23 18:23:51","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":33,"rechargeTime":"2016-04-23 18:22:58","rechargeMoney":500,"status":1,"pay_type":1},{"rechargeId":32,"rechargeTime":"2016-04-23 18:22:18","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":31,"rechargeTime":"2016-04-23 16:27:47","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":29,"rechargeTime":"2016-04-23 16:27:08","rechargeMoney":20,"status":1,"pay_type":1},{"rechargeId":28,"rechargeTime":"2016-04-23 16:19:24","rechargeMoney":100,"status":1,"pay_type":1},{"rechargeId":27,"rechargeTime":"2016-04-23 16:18:21","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":26,"rechargeTime":"2016-04-23 16:17:26","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":25,"rechargeTime":"2016-04-23 16:16:19","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":24,"rechargeTime":"2016-04-23 16:14:20","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":23,"rechargeTime":"2016-04-23 16:14:19","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":22,"rechargeTime":"2016-04-23 16:10:20","rechargeMoney":0,"status":1,"pay_type":1},{"rechargeId":21,"rechargeTime":"2016-04-23 16:03:58","rechargeMoney":2000,"status":1,"pay_type":1}]
     * more : 2
     * total : 14
     */

    private int more;
    private int total;
    /**
     * rechargeId : 35
     * rechargeTime : 2016-04-23 18:23:59
     * rechargeMoney : 100
     * status : 1
     * pay_type : 1
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
        private int rechargeId;
        private String rechargeTime;
        private double rechargeMoney;
        private int status;
        private int pay_type;

        public int getRechargeId() {
            return rechargeId;
        }

        public void setRechargeId(int rechargeId) {
            this.rechargeId = rechargeId;
        }

        public String getRechargeTime() {
            return rechargeTime;
        }

        public void setRechargeTime(String rechargeTime) {
            this.rechargeTime = rechargeTime;
        }

        public double getRechargeMoney() {
            return rechargeMoney;
        }

        public void setRechargeMoney(double rechargeMoney) {
            this.rechargeMoney = rechargeMoney;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }
    }
}
