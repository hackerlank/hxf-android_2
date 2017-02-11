package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/8/9.
 */
public class Redbag implements Parcelable,Serializable{


    private static final long serialVersionUID = 342866778625064984L;
    private long rid;
    private String redName;
    private int money;
    private String startTime;
    private String endTime;
    private String desc;
    private int restMoney;
    private int useStatus;
    private int goodsNumStart;
    private int goodsNumEnd;

    public int getGoodsNumStart() {
        return goodsNumStart;
    }

    public void setGoodsNumStart(int goodsNumStart) {
        this.goodsNumStart = goodsNumStart;
    }

    public int getGoodsNumEnd() {
        return goodsNumEnd;
    }

    public void setGoodsNumEnd(int goodsNumEnd) {
        this.goodsNumEnd = goodsNumEnd;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getRedName() {
        return redName;
    }

    public void setRedName(String redName) {
        this.redName = redName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(int restMoney) {
        this.restMoney = restMoney;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.rid);
        dest.writeString(this.redName);
        dest.writeInt(this.money);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.desc);
        dest.writeInt(this.restMoney);
        dest.writeInt(this.useStatus);
        dest.writeInt(this.goodsNumStart);
        dest.writeInt(this.goodsNumEnd);
    }

    public Redbag() {
    }

    protected Redbag(Parcel in) {
        this.rid = in.readLong();
        this.redName = in.readString();
        this.money = in.readInt();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.desc = in.readString();
        this.restMoney = in.readInt();
        this.useStatus = in.readInt();
        this.goodsNumStart = in.readInt();
        this.goodsNumEnd = in.readInt();
    }

    public static final Creator<Redbag> CREATOR = new Creator<Redbag>() {
        @Override
        public Redbag createFromParcel(Parcel source) {
            return new Redbag(source);
        }

        @Override
        public Redbag[] newArray(int size) {
            return new Redbag[size];
        }
    };
}
