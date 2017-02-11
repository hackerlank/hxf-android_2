package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class Info extends BaseBean implements Serializable, Parcelable {

    private static final long serialVersionUID = -6885794870479571246L;
    private int existingTimes;
    /**
     * gid : 1
     * name : 幸福劵 10张
     * desc : 123123
     * price : 1000
     * buyUnit : 1
     * pic : []
     */

    private Goods goods;
    private long period;
    private int status;

    public int getExistingTimes() {
        return existingTimes;
    }

    public void setExistingTimes(int existingTimes) {
        this.existingTimes = existingTimes;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.existingTimes);
        dest.writeParcelable(this.goods, flags);
        dest.writeLong(this.period);
        dest.writeInt(this.status);
    }

    public Info() {
    }

    protected Info(Parcel in) {
        this.existingTimes = in.readInt();
        this.goods = in.readParcelable(Goods.class.getClassLoader());
        this.period = in.readLong();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}
