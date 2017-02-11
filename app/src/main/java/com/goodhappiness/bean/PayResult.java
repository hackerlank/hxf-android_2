package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/7.
 */
public class PayResult extends BaseBean implements Parcelable {

    /**
     * payStatus : 0
     * list : {"success":[{"period":4,"shares":10,"total":2000,"codes":[100001332,100000301,100000620,100000116,100000432,100000104,100001651,100000341,100000271,100001805],"existingTimes":100,"gid":2}],"failure":[]}
     */

    private int payStatus;
    private ListBean list;

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PayResult{" +
                "payStatus=" + payStatus +
                ", list=" + list +
                '}';
    }

    public static class ListBean implements Parcelable {

        /**
         * period : 4
         * shares : 10
         * total : 2000
         * codes : [100001332,100000301,100000620,100000116,100000432,100000104,100001651,100000341,100000271,100001805]
         * existingTimes : 100
         * gid : 2
         */

        private List<SuccessBean> success;
        private List<SuccessBean> failure;

        public List<SuccessBean> getSuccess() {
            return success;
        }

        public void setSuccess(List<SuccessBean> success) {
            this.success = success;
        }

        public List<SuccessBean> getFailure() {
            return failure;
        }

        public void setFailure(List<SuccessBean> failure) {
            this.failure = failure;
        }


        public static class SuccessBean implements Parcelable {

            private long period;
            private int shares;
            private int total;
            private int existingTimes;
            private List<Long> codes;
            private Goods goods;

            public Goods getGoods() {
                return goods;
            }

            public void setGoods(Goods goods) {
                this.goods = goods;
            }

            public long getPeriod() {
                return period;
            }

            public void setPeriod(long period) {
                this.period = period;
            }

            public int getShares() {
                return shares;
            }

            public void setShares(int shares) {
                this.shares = shares;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getExistingTimes() {
                return existingTimes;
            }

            public void setExistingTimes(int existingTimes) {
                this.existingTimes = existingTimes;
            }

            public List<Long> getCodes() {
                return codes;
            }

            public void setCodes(List<Long> codes) {
                this.codes = codes;
            }

            @Override
            public String toString() {
                return "SuccessBean{" +
                        "period=" + period +
                        ", shares=" + shares +
                        ", total=" + total +
                        ", existingTimes=" + existingTimes +
                        ", codes=" + codes +
                        ", goods=" + goods +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.period);
                dest.writeInt(this.shares);
                dest.writeInt(this.total);
                dest.writeInt(this.existingTimes);
                dest.writeList(this.codes);
                dest.writeParcelable(this.goods, flags);
            }

            public SuccessBean() {
            }

            protected SuccessBean(Parcel in) {
                this.period = in.readLong();
                this.shares = in.readInt();
                this.total = in.readInt();
                this.existingTimes = in.readInt();
                this.codes = new ArrayList<Long>();
                in.readList(this.codes, Long.class.getClassLoader());
                this.goods = in.readParcelable(Goods.class.getClassLoader());
            }

            public static final Creator<SuccessBean> CREATOR = new Creator<SuccessBean>() {
                @Override
                public SuccessBean createFromParcel(Parcel source) {
                    return new SuccessBean(source);
                }

                @Override
                public SuccessBean[] newArray(int size) {
                    return new SuccessBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.success);
            dest.writeList(this.failure);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.success = new ArrayList<SuccessBean>();
            in.readList(this.success, SuccessBean.class.getClassLoader());
            this.failure = new ArrayList<SuccessBean>();
            in.readList(this.failure, SuccessBean.class.getClassLoader());
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.payStatus);
        dest.writeParcelable(this.list, flags);
    }

    public PayResult() {
    }

    protected PayResult(Parcel in) {
        this.payStatus = in.readInt();
        this.list = in.readParcelable(ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PayResult> CREATOR = new Parcelable.Creator<PayResult>() {
        @Override
        public PayResult createFromParcel(Parcel source) {
            return new PayResult(source);
        }

        @Override
        public PayResult[] newArray(int size) {
            return new PayResult[size];
        }
    };
}
