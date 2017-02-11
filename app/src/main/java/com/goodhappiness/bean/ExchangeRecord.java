package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/12.
 */
public class ExchangeRecord extends BaseBean{

    /**
     * list : [{"exId":2,"productName":"iphone 6s ","exchangeTime":"2016-04-16 15:17:28","productImage":"http://7xsgec.com2.z0.glb.qiniucdn.com/1460103796","status":4,"happyCoin":129,"generalCoin":0}]
     * more : 2
     * total : 1
     */

    private int more;
    private int total;
    /**
     * exId : 2
     * productName : iphone 6s
     * exchangeTime : 2016-04-16 15:17:28
     * productImage : http://7xsgec.com2.z0.glb.qiniucdn.com/1460103796
     * status : 4
     * happyCoin : 129
     * generalCoin : 0
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

    public static class ListBean extends BaseBean{
        private long exId;
        private String productName;
        private String exchangeTime;
        private String productImage;
        private long productId;
        private int status;
        private int happyCoin;
        private int generalCoin;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public long getExId() {
            return exId;
        }

        public void setExId(long exId) {
            this.exId = exId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getExchangeTime() {
            return exchangeTime;
        }

        public void setExchangeTime(String exchangeTime) {
            this.exchangeTime = exchangeTime;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getHappyCoin() {
            return happyCoin;
        }

        public void setHappyCoin(int happyCoin) {
            this.happyCoin = happyCoin;
        }

        public int getGeneralCoin() {
            return generalCoin;
        }

        public void setGeneralCoin(int generalCoin) {
            this.generalCoin = generalCoin;
        }
    }
}
